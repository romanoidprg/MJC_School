package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.*;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;


public class CertRepoService implements CommonService<GiftCertificate> {

    private static final String WORDS_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\words.txt";
    private final Logger logger = LogManager.getLogger(CertRepoService.class);

    @Autowired
    @Qualifier("certDao")
    private CommonDao<GiftCertificate, CertCriteria> certDao;

    @Autowired
    @Qualifier("tagDao")
    private CommonDao<Tag, TagCriteria> tagDao;

    @Override
    public long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert = objectMapper.readValue(jsonString, GiftCertificate.class);
        if (!certDao.isExist(cert)) {
            cert.setId(null);
            cert.setCreateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setTags(refreshAndCorrectTags(cert));
            id = certDao.create(cert);
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    private Set<Tag> refreshAndCorrectTags(GiftCertificate cert) {
        Set<Tag> newTags = new HashSet<>();
        TagCriteria tagCriteria;
        for (Tag t : cert.getTags()) {
            if (!tagDao.isExist(t)) {
                tagDao.create(t);
                newTags.add(t);
            } else {
                tagCriteria = new TagCriteria(t.getName(), true, "asc");
                newTags.add(tagDao.readByCriteria(PageRequest.of(1,10), tagCriteria).get(0));
            }
        }
        return newTags;
    }

    @Override
    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
        boolean result = false;
        if (id.matches("[0-9]+")) {
            GiftCertificate oldCert = certDao.readById(Long.parseLong(id));
            if (oldCert != null) {

                ObjectMapper objectMapper = new ObjectMapper();
                GiftCertificate newCert;
                try {
                    newCert = objectMapper.readValue(jsonString, GiftCertificate.class);
                    newCert.setId(oldCert.getId());
                    newCert.setCreateDate(oldCert.getCreateDate());
                    newCert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
                    newCert.setTags(refreshAndCorrectTags(newCert));
                    result = certDao.update(newCert);
                } catch (JsonProcessingException e) {
                    logger.error(e.getMessage());
                }
            } else {
                throw new EntityAlreadyExistException();
            }
        } else {
            throw new NoSuchCertIdException(id);
        }
        return result;
    }

    @Override
    public Long create(String... params) throws LocalAppException {
        return null;
    }

    @Override
    public boolean fillTable() {
        boolean result = false;
        try {
            Tag t;
            Random r1 = new Random();
            Random r2 = new Random();
            File file = new File(WORDS_FILE);
            GiftCertificate cert;
            int size = getWordsAmount(file);
            int id;
            int tagCol;
            for (int i = 0; i < 10000; i++) {
                cert = new GiftCertificate();
                cert.setName(getRandomWord(file, size));
                cert.setDescription(getRandomWord(file, size));
                cert.setPrice(new Random().nextInt(10000));
                cert.setDuration(new Random().nextInt(365));
                cert.setCreateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
                cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
                tagCol = r1.nextInt(4) + 1;
                for (int j = 0; j < tagCol; j++) {
                    id = r2.nextInt(999) + 1;
                    t = tagDao.readById(id);
                    if (t == null) {
                        throw new NoSuchIdException("Tag with id [" + id + "] doesn't exist.");
                    }
                    cert.getTags().add(t);
                }
                certDao.create(cert);
            }
            result = true;
        } catch (FileNotFoundException | NoSuchIdException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public GiftCertificate readById(String id) throws LocalAppException {
        GiftCertificate result;
        if (id.matches("[0-9]+")) {
            result = certDao.readById(Long.parseLong(id));
            if (result == null) {
                throw new NoSuchCertIdException(id);
            }
        } else {
            throw new NoSuchCertIdException(id);
        }
        result.getTags().forEach(t -> t.setCertificates(null));
        return result;
    }

    @Override
    public List<GiftCertificate> readByCriteria(Pageable pageable, String... params) {
        List<GiftCertificate> result = new ArrayList<>();
        if (params.length > 8) {
            String tagName = params[0];
            String name = params[1];
            String description = params[2];
            String sortByName = params[3];
            String sortByCrDate = params[4];
            String sortByUpdDate = params[5];
            String sortNameOrder = params[6];
            String sortCrDateOrder = params[7];
            String sortUpdDateOrder = params[8];

            result = certDao.readByCriteria(pageable,
                    new CertCriteria(tagName, name, description,
                            Boolean.parseBoolean(sortByName),
                            Boolean.parseBoolean(sortByCrDate),
                            Boolean.parseBoolean(sortByUpdDate),
                            sortNameOrder, sortCrDateOrder, sortUpdDateOrder));
            result.forEach(c -> c.getTags().forEach(tag -> tag.setCertificates(null)));
        }
        return result;
    }

    @Override
    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
        if (!id.matches("[0-9]+")) {
            throw new NoSuchCertIdException(id);
        }
        if (params.size() != 1) {
            throw new IncorrectAmountOfCertFieldsException();
        }

        GiftCertificate cert = certDao.readById(Long.parseLong(id));
        if (cert == null) {
            throw new NoSuchCertIdException(id);
        }
        fillCert(cert, params);
        cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        return certDao.update(cert);
    }

    private void fillCert(GiftCertificate cert, Map<String, String> params) {
        String name = params.get("name");
        String description = params.get("description");
        String duration = params.get("duration");
        String price = params.get("price");
        if (duration != null) {
            if (!duration.matches("[0-9]+")) {
                throw new NumberFormatException("Incorrect format of the certificate field");
            } else {
                cert.setDuration(Integer.parseInt(duration));
            }
        }
        if (price != null) {
            if (!price.matches("[0-9]+")) {
                throw new NumberFormatException("Incorrect format of the certificate field");
            } else {
                cert.setPrice(Integer.parseInt(price));
            }
        }
        if (name != null) {
            cert.setName(name);
        }
        if (description != null) {
            cert.setDescription(description);
        }
    }

    @Override
    public void deleteById(String id) throws LocalAppException {
        certDao.delete(readById(id));
    }
}

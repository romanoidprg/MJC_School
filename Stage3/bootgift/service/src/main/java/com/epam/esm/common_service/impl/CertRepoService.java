package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchCertIdException;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.errors.NoSuchOrderIdException;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


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
    public Long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert = objectMapper.readValue(jsonString, GiftCertificate.class);
        if (!certDao.isExist(cert)) {
            cert.setId(null);
            cert.setCreateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            id = certDao.create(cert);
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
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
                tagCol = r1.nextInt(4)+1;
                for (int j = 0; j < tagCol; j++) {
                    id = r2.nextInt(999)+1;
                    t = tagDao.readById(id);
                    if (t==null) {
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
            if (result==null) {
                throw new NoSuchCertIdException(id);
            }
        } else {
            throw new NoSuchCertIdException(id);
        }
        result.getTags().forEach(t -> t.setCertificates(null));
        return result;
    }

    @Override
    public List<GiftCertificate> readByCriteria(String... params) {
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

            result = certDao.readByCriteria(
                    new CertCriteria(tagName, name, description,
                            Boolean.parseBoolean(sortByName),
                            Boolean.parseBoolean(sortByCrDate),
                            Boolean.parseBoolean(sortByUpdDate),
                            sortNameOrder, sortCrDateOrder, sortUpdDateOrder));
        }
        return result;
    }

    @Override
    public boolean updateFromJson(String jsonString) {
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert;
        try {
            cert = objectMapper.readValue(jsonString, GiftCertificate.class);
            result = certDao.update(cert);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            e.getMessage();
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        try {
            result = certDao.deleteById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}

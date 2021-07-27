package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.dao.CertRepo;
import com.epam.esm.dao.TagRepo;
import com.epam.esm.errors.*;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;


public class CertRepoService implements CommonService<GiftCertificate>, CustomCertService {

    private final Logger logger = LogManager.getLogger(CertRepoService.class);

    @Autowired
    CertRepo certRepo;

    @Autowired
    TagRepo tagRepo;

    @Override
    public long createFromJson(String jsonString) throws Exception {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert = objectMapper.readValue(jsonString, GiftCertificate.class);
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name", "description")
                .withIgnorePaths("id", "price", "duration", "createDate", "lastUpdateDate", "tags")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        if (!certRepo.exists(Example.of(cert, em))) {
            cert.setId(null);
            cert.setCreateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setTags(refreshAndCorrectTags(cert));
            id = certRepo.save(cert).getId();
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    private Set<Tag> refreshAndCorrectTags(GiftCertificate cert) {
        Set<Tag> newTags = new HashSet<>();
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name")
                .withIgnorePaths("id", "certificates")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        for (Tag t : cert.getTags()) {
            if (!tagRepo.exists(Example.of(t, em))) {
                tagRepo.save(t);
                newTags.add(t);
            } else {
                newTags.addAll(tagRepo.findAll(Example.of(t, em)));
            }
        }
        return newTags;
    }

    @Override
    public Long create(String... params) throws LocalAppException {
        return null;
    }

    @Override
    public GiftCertificate readById(String id) throws LocalAppException {
        GiftCertificate result;
        if (id.matches("[0-9]+")) {
            result = certRepo.findById(Long.parseLong(id)).orElse(null);
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
    public Page<GiftCertificate> readCertsWithTags(Pageable pageable, String... tagNames) {
        Set<GiftCertificate> certs = new HashSet<>();
        List<String> namesList = Arrays.asList(tagNames);
        for (String n : namesList) {
            certs.addAll(certRepo.findByTagsName(n));
        }
        List<GiftCertificate> result = certs.stream()
                .filter(c -> c.getTags().stream()
                        .filter(t -> namesList.contains(t.getName()))
                        .collect(Collectors.toList()).size() == namesList.size())
                .collect(Collectors.toList());
        PageImpl<GiftCertificate> page = new PageImpl(result, pageable, result.size());

        return page;
    }

    @Override
    public Page<GiftCertificate> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        String tagName = params[0] == null ? "" :  params[0] ;
        String name = params[1] == null ? "" :  params[1] ;
        String description = params[2] == null ? "" :   params[2] ;
        List<GiftCertificate> result = certRepo.findByNameContainingAndDescriptionContainingAndTagsNameContaining(pageable,name, description, tagName);
        result.forEach(c-> c.getTags().forEach(t -> t.setCertificates(null)));
        PageImpl<GiftCertificate> page = new PageImpl(result, pageable, result.size());

        return page;
    }

    @Override
    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
        return false;
    }

    @Override
    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(String id) throws LocalAppException {

    }

    @Override
    public Long getLastQueryCountFromCustom() {
        return null;
    }

}
//
//
//
//    @Override
//    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
//        boolean result = false;
//        if (id.matches("[0-9]+")) {
//            GiftCertificate oldCert = certDao.readById(Long.parseLong(id));
//            if (oldCert != null) {
//
//                ObjectMapper objectMapper = new ObjectMapper();
//                GiftCertificate newCert;
//                try {
//                    newCert = objectMapper.readValue(jsonString, GiftCertificate.class);
//                    newCert.setId(oldCert.getId());
//                    newCert.setCreateDate(oldCert.getCreateDate());
//                    newCert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
//                    newCert.setTags(refreshAndCorrectTags(newCert));
//                    result = certDao.update(newCert);
//                } catch (JsonProcessingException e) {
//                    logger.error(e.getMessage());
//                }
//            } else {
//                throw new EntityAlreadyExistException();
//            }
//        } else {
//            throw new NoSuchCertIdException(id);
//        }
//        return result;
//    }
//
//    @Override
//    public Long create(String... params) throws LocalAppException {
//        return null;
//    }
//
//
//    @Override
//    public List<GiftCertificate> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
//        List<GiftCertificate> result = new ArrayList<>();
//        if (params.length > 8) {
//            String tagName = params[0];
//            String name = params[1];
//            String description = params[2];
//            String sortByName = params[3];
//            String sortByCrDate = params[4];
//            String sortByUpdDate = params[5];
//            String sortNameOrder = params[6];
//            String sortCrDateOrder = params[7];
//            String sortUpdDateOrder = params[8];
//
//            result = certDao.readByCriteria(pageable,
//                    new CertCriteria(tagName, name, description,
//                            Boolean.parseBoolean(sortByName),
//                            Boolean.parseBoolean(sortByCrDate),
//                            Boolean.parseBoolean(sortByUpdDate),
//                            sortNameOrder, sortCrDateOrder, sortUpdDateOrder));
//            result.forEach(c -> c.getTags().forEach(tag -> tag.setCertificates(null)));
//        }
//        return result;
//    }
//
//    @Override
//    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
//        if (!id.matches("[0-9]+")) {
//            throw new NoSuchCertIdException(id);
//        }
//        if (params.size() != 1) {
//            throw new IncorrectAmountOfCertFieldsException();
//        }
//
//        GiftCertificate cert = certDao.readById(Long.parseLong(id));
//        if (cert == null) {
//            throw new NoSuchCertIdException(id);
//        }
//        fillCert(cert, params);
//        cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
//        return certDao.update(cert);
//    }
//
//    private void fillCert(GiftCertificate cert, Map<String, String> params) {
//        String name = params.get("name");
//        String description = params.get("description");
//        String duration = params.get("duration");
//        String price = params.get("price");
//        if (duration != null) {
//            if (!duration.matches("[0-9]+")) {
//                throw new NumberFormatException("Incorrect format of the certificate field");
//            } else {
//                cert.setDuration(Integer.parseInt(duration));
//            }
//        }
//        if (price != null) {
//            if (!price.matches("[0-9]+")) {
//                throw new NumberFormatException("Incorrect format of the certificate field");
//            } else {
//                cert.setPrice(Integer.parseInt(price));
//            }
//        }
//        if (name != null) {
//            cert.setName(name);
//        }
//        if (description != null) {
//            cert.setDescription(description);
//        }
//    }
//
//    @Override
//    public void deleteById(String id) throws LocalAppException {
//        certDao.delete(readById(id));
//    }
//
//    @Override
//    public Long getLastQueryCount() {
//        return certDao.getLastQueryCount();
//    }
//
//    @Override
//    public Long getLastQueryCountFromCustom() {
//        return customCertDao.getLastQueryCount();
//    }
//
//    @Override
//    public List<GiftCertificate> readCertsWithTags(Pageable pageable, String[] tags) {
//        return customCertDao.readWithTags(pageable, tags);
//    }
//

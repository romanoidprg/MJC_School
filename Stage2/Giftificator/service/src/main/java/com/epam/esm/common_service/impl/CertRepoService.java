package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;


public class CertRepoService implements CommonService<GiftCertificate> {

    private final Logger logger = LogManager.getLogger(CertRepoService.class);

    @Autowired
    @Qualifier("certDao")
    private CommonDao<GiftCertificate, CertCriteria> certDao;

    @Override
    public boolean createFromJson(String jsonString) throws JsonProcessingException {
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert;
        try {
            cert = objectMapper.readValue(jsonString, GiftCertificate.class);
            result = certDao.create(cert);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public GiftCertificate readById(String id) throws NoSuchIdException {
        if (id.matches("[0-9]+")) {
            return certDao.readById(Long.parseLong(id));
        } else {
            NoSuchIdException e = new NoSuchIdException("The gift certificate with id ["+id+"] doesn't exist.");
            throw e;
        }
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

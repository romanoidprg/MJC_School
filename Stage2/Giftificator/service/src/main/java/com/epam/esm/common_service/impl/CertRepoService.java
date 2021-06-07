package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.DaoFactory;
import com.epam.esm.dao.impl.CertDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class CertRepoService implements CommonService<GiftCertificate> {

    private ConnectionPool connectionPool = ConnectionPool.REAL_DB;

    private CommonDao<GiftCertificate, CertCriteria> certDao = DaoFactory.getCertDao(connectionPool);

    public void setDataBaseToReal(){
        connectionPool = ConnectionPool.REAL_DB;
        certDao = DaoFactory.getCertDao(connectionPool);
    }

    public void setDataBaseToEmbedded(){
        connectionPool = ConnectionPool.IN_MEMORY_DB;
        certDao = DaoFactory.getCertDao(connectionPool);
    }

    @Override
    public boolean createFromJson(String jsonString) {
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert;
        try {
            cert = objectMapper.readValue(jsonString, GiftCertificate.class);
            result = certDao.create(cert);
        } catch (JsonProcessingException e) {
            //todo: logging error
            e.getMessage();
        }
        return result;
    }

    @Override
    public GiftCertificate readById(String id) {
        return (id.matches("[0-9]+")) ? certDao.readById(Long.parseLong(id)) : null;
    }

    @Override
    public List<GiftCertificate> readByCriteria(String tagName, String name, String description, String sortByName, String sortByDate, String sortOrder) {
        return certDao.readByCriteria(
                new CertCriteria(tagName, name, description,
                        Boolean.parseBoolean(sortByName),
                        Boolean.parseBoolean(sortByDate),
                        sortOrder));
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
            //todo: logging error
            e.getMessage();
        }
        return result;
    }

    @Override
    public boolean deleteById(String id) {
        return certDao.delete(Long.parseLong(id));
    }
}

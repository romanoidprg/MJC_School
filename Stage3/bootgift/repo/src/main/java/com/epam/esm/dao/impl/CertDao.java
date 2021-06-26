package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Repository

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {

    public CertDao(){
    }

    private final Logger logger = LogManager.getLogger(CertDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(GiftCertificate entity) {
        boolean result = false;
        Session session = sessionFactory.openSession();
        session.save(entity);
        session.close();
        return result;
    }

    private long getTagIdIfTagWithNameExist(String name, Connection c) throws SQLException {
        long result = -1;
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
        GiftCertificate result = null;
        return result;
    }

    @Override
    public List<GiftCertificate> readByCriteria(CertCriteria criteria) {
        List<GiftCertificate> result = new ArrayList<>();
        return result;
    }

    private String prepareSQLFromCriteria(CertCriteria cr) {
        StringBuilder result = new StringBuilder();
        return result.toString();
    }

    @Override
    public boolean update(GiftCertificate updCert) {
        boolean result = false;
        return result;
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        return result;
    }
}

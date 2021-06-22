package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component

public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {

    private final Logger logger = LogManager.getLogger(CertDao.class);

    @Override
    public boolean create(GiftCertificate entity) {
        boolean result = false;

        StandardServiceRegistry ssr = new StandardServiceRegistryBuilder().configure("hibernate.config.xml").build();
        Metadata meta = new MetadataSources(ssr).getMetadataBuilder().build();

        SessionFactory factory = meta.getSessionFactoryBuilder().build();
        Session session = factory.openSession();
//        Transaction t = session.beginTransaction();

        session.save(entity);
//        t.commit();
        factory.close();
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

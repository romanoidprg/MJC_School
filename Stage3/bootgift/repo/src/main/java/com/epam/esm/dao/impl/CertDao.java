package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class CertDao implements CommonDao<GiftCertificate, CertCriteria> {

    private static final String IS_EXIST_SQL_QUERY = "SELECT * FROM certificates where name = :name " +
            "AND description = :descr AND price = :price AND duration = :dur";

    public CertDao() {
    }

    private final Logger logger = LogManager.getLogger(CertDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Long create(GiftCertificate entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public boolean isExist(GiftCertificate entity) {
        Session s = sessionFactory.getCurrentSession();
        return s.createSQLQuery(IS_EXIST_SQL_QUERY)
                .setParameter("name", entity.getName())
                .setParameter("descr", entity.getDescription())
                .setParameter("price", entity.getPrice())
                .setParameter("dur", entity.getDuration())
                .addEntity(GiftCertificate.class)
                .stream().findAny().isPresent();

    }

    private long getTagIdIfTagWithNameExist(String name, Connection c) throws SQLException {
        long result = -1;
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(GiftCertificate.class, id);
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

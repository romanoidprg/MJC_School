package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private static final String SQL_READ_CERT_BY_CRITERIA = "SELECT  c.* FROM " +
            "certificates AS c Right JOIN (SELECT * from certs_tags AS ct " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id WHERE t.name LIKE :tName) AS tab ON c.id=tab.cert_id " +
            "WHERE c.name LIKE :cName AND c.description LIKE :cDesc ORDER BY ";
    private static final String SQL_READ_CERT_BY_CRITERIA_ANY_TAG = "SELECT c.* FROM " +
            "certificates AS c WHERE c.name LIKE :cName AND c.description LIKE :cDesc ORDER BY ";

    private static final String SQL_NAME = "c.name ";
    private static final String SQL_CREATE_DATE = "c.create_date ";
    private static final String SQL_UPDATE_DATE = "c.last_update_date ";
    private static final String SQL_ID = "c.id";

    public CertDao() {
    }

    private final Logger logger = LogManager.getLogger(CertDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public long create(GiftCertificate entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public List<GiftCertificate> readAll() {
        return null;
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
    public List<GiftCertificate> readByCriteria(Pageable pageable, CertCriteria criteria) {
        List<GiftCertificate> result = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        if (criteria.getTagName() != "") {
            result = session.createSQLQuery(prepareSQLFromCriteria(criteria))
                    .setParameter("tName", "%" + criteria.getTagName() + "%")
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .addEntity(GiftCertificate.class).list();
        } else {
            result = session.createSQLQuery(prepareSQLFromCriteria(criteria))
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .addEntity(GiftCertificate.class).list();
        }
        return result;

    }

    private String prepareSQLFromCriteria(CertCriteria cr) {
        StringBuilder result = new StringBuilder();
        if (cr.getTagName().equals("")) {
            result.append(SQL_READ_CERT_BY_CRITERIA_ANY_TAG);
        } else {
            result.append(SQL_READ_CERT_BY_CRITERIA);
        }
        boolean sortById = true;

        if (cr.isSortByName()) {
            sortById = false;
            result.append(SQL_NAME);
            result.append(cr.getSortNameOrder());
        }
        if (cr.isSortByCrDate()) {
            if (!sortById) {
                result.append(", ");
            } else {
                sortById = false;
            }
            result.append(SQL_CREATE_DATE);
            result.append(cr.getSortCrDateOrder());
        }

        if (cr.isSortByUpdDate()) {
            if (!sortById) {
                result.append(", ");
            } else {
                sortById = false;
            }
            result.append(SQL_UPDATE_DATE);
            result.append(cr.getSortUpdDateOrder());
        }
        if (sortById) {
            result.append(SQL_ID);
        }

        return result.toString();
    }

    @Override
    public boolean update(GiftCertificate updCert) {
        boolean result = false;
        Session session = sessionFactory.getCurrentSession();
        try {
            session.update(updCert);
            result = true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    @Override
    public void delete(GiftCertificate entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }
}

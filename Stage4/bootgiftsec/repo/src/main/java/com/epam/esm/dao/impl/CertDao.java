package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomCertDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



@Transactional
public class CertDao implements CommonDao<GiftCertificate, CertCriteria>, CustomCertDao {

    private static final String IS_EXIST_SQL_QUERY = "SELECT * FROM certificates where name = :name " +
            "AND description = :descr AND price = :price AND duration = :dur";
    private static final String SQL_READ_CERT_BY_CRITERIA = "SELECT  c.* FROM " +
            "certificates AS c Right JOIN (SELECT * from certs_tags AS ct " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id WHERE t.name LIKE :tName) AS tab ON c.id=tab.cert_id " +
            "WHERE c.name LIKE :cName AND c.description LIKE :cDesc ORDER BY ";
    private static final String SQL_READ_COUNT_CERT_BY_CRITERIA = "SELECT  COUNT(*) FROM " +
            "certificates AS c Right JOIN (SELECT * from certs_tags AS ct " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id WHERE t.name LIKE :tName) AS tab ON c.id=tab.cert_id " +
            "WHERE c.name LIKE :cName AND c.description LIKE :cDesc ";
    private static final String SQL_READ_CERT_BY_CRITERIA_ANY_TAG = "SELECT c.* FROM " +
            "certificates AS c WHERE c.name LIKE :cName AND c.description LIKE :cDesc ORDER BY ";
    private static final String SQL_READ_COUNT_CERT_BY_CRITERIA_ANY_TAG = "SELECT COUNT(*) FROM " +
            "certificates AS c WHERE c.name LIKE :cName AND c.description LIKE :cDesc ";
    public static final String PRE_SQL = "SELECT * ";
    public static final String PRE_SQL_COUNT = "SELECT COUNT(*) ";
    private static final String SQL_READ_CERT_MULTIPLE_TAG_BEGIN = "FROM (" +
            "SELECT c.*, count(c.id) as count FROM " +
            "((certificates AS c RIGHT JOIN certs_tags AS ct on c.id=ct.cert_id) " +
            "LEFT JOIN tags AS t ON ct.tag_id=t.id) " +
            "WHERE ";
    private static final String SQL_READ_CERT_MULTIPLE_TAG_END= " GROUP BY c.id) as tab WHERE tab.count=";

    private static final String SQL_NAME = "c.name ";
    private static final String SQL_CREATE_DATE = "c.create_date ";
    private static final String SQL_UPDATE_DATE = "c.last_update_date ";
    private static final String SQL_ID = "c.id";

    private static final String PAGINATION = " LIMIT :p_size OFFSET :page ";


    public CertDao() {
    }

    private Long lastQueryCount = 0L;

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

    @Override
    public Long getLastQueryCount() {
        return lastQueryCount;
    }

    private long getTagIdIfTagWithNameExist(String name, Connection c) throws SQLException {
        long result = -1;
        return result;
    }

    @Override
    public GiftCertificate readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = 1L;
        return session.get(GiftCertificate.class, id);
    }

    @Override
    public List<GiftCertificate> readByCriteria(Pageable pageable, CertCriteria criteria) {
        List<GiftCertificate> result = new ArrayList<>();
        Session session = sessionFactory.getCurrentSession();
        if (criteria.getTagName() != "") {
            lastQueryCount = ((BigInteger) session.createSQLQuery(SQL_READ_COUNT_CERT_BY_CRITERIA)
                    .setParameter("tName", "%" + criteria.getTagName() + "%")
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .list().get(0)).longValue();
            result = session.createSQLQuery(prepareSQLFromCriteria(criteria))
                    .setParameter("tName", "%" + criteria.getTagName() + "%")
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .setParameter("page", pageable.getOffset())
                    .setParameter("p_size", pageable.getPageSize())
                    .addEntity(GiftCertificate.class).list();
        } else {
            lastQueryCount = ((BigInteger) session.createSQLQuery(SQL_READ_COUNT_CERT_BY_CRITERIA_ANY_TAG)
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .list().get(0)).longValue();
            result = session.createSQLQuery(prepareSQLFromCriteria(criteria))
                    .setParameter("cName", "%" + criteria.getName() + "%")
                    .setParameter("cDesc", "%" + criteria.getDescription() + "%")
                    .setParameter("page", pageable.getOffset())
                    .setParameter("p_size", pageable.getPageSize())
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
        result.append(PAGINATION);
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

    @Override
    public List<GiftCertificate> readWithTags(Pageable pageable, String[] tags) {
        List<GiftCertificate> result;
        Session session = sessionFactory.getCurrentSession();
        StringBuilder sql = new StringBuilder(SQL_READ_CERT_MULTIPLE_TAG_BEGIN);
        for (int i = 0; i < tags.length - 1; i++) {
            sql.append("t.name='" + tags[i] + "'");
            sql.append(" OR ");
        }
        sql.append("t.name='" + tags[tags.length - 1] + "'");
        sql.append(SQL_READ_CERT_MULTIPLE_TAG_END + tags.length);
        lastQueryCount = ((BigInteger) session.createSQLQuery(PRE_SQL_COUNT + sql.toString())
                .list().get(0)).longValue();
        result = session.createSQLQuery(getPageableSQL(PRE_SQL + sql.toString(), pageable))
                .addEntity(GiftCertificate.class).list();
        return result;
    }


    private String getPageableSQL(String sql, Pageable pageable){
        StringBuilder result = new StringBuilder(sql);
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            result.append(" ORDER BY " + sort.stream().findAny().get().getProperty());
            result.append(" " + sort.stream().findAny().get().getDirection().name());
        }
        result.append(" LiMIT " + pageable.getPageSize() + " ");
        result.append(" OFFSET " + pageable.getOffset() + " ");

        return result.toString();
    }
}

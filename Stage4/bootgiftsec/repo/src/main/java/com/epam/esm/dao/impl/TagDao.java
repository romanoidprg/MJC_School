package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomTagDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;


@Transactional
public class TagDao implements CommonDao<Tag, TagCriteria>, CustomTagDao {

    private static final String READ_BY_CRITERIA_SQL = "SELECT * FROM tags WHERE name=:name";
    private static final String WUTUHCO_GET_USER_ID_SQL = "select user_id from " +
            "(select user_id, sum(cost) as summa from orders group by user_id) as t1 " +
            "where t1.summa = " +
            "(select max(t2.summa) from " +
            "(select user_id, sum(cost) as summa from orders group by user_id) as t2) ";
    private static final String WUTUHCO_GET_TAGS_SQL = "SELECT t6.tag_id FROM " +
            "(SELECT t4.*, count(t4.tag_id) as count FROM orders as t3 " +
            "LEFT JOIN certs_tags as t4 on t3.cert_id=t4.cert_id WHERE user_id=:userId GROUP BY t4.tag_id) as t6 " +
            "WHERE t6.count=(SELECT MAX(t5.count) from " +
            "(SELECT t4.*, count(t4.tag_id) as count FROM orders as t3 " +
            "LEFT JOIN certs_tags as t4 on t3.cert_id=t4.cert_id WHERE user_id=:userId GROUP BY t4.tag_id) as t5) ";
    private static final String SQL_READ_TAG_BY_CRITERIA = "SELECT * FROM tags WHERE name LIKE :name ORDER BY ";
    private static final String SQL_READ_COUNT_TAG_BY_CRITERIA = "SELECT COUNT(*) FROM tags WHERE name LIKE :name ";
    private static final String SQL_ID = "id ";
    private static final String SQL_NAME = "name ";
    private static final String SQL_READ_ALL_TAGS = "SELECT * FROM tags ORDER BY id ASC ";
    private static final String SQL_COUNT_READ_ALL_TAGS = "SELECT COUNT(*) FROM tags";
    private static final String PAGINATION = " LIMIT :p_size OFFSET :page ";

    private Long lastQueryCount = 0L;
    private final Logger logger = LogManager.getLogger(TagDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public long create(Tag entity) {
        long id;
        Session session = sessionFactory.getCurrentSession();
        id = (long) session.save(entity);
        return id;
    }

    @Override
    public Tag readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Tag result = session.get(Tag.class, id);
        if (result != null) {
            Hibernate.initialize(result.getCertificates());
            lastQueryCount = 1L;
        }
        return result;
    }

    @Override
    public boolean update(Tag entity) {
        return false;
    }

    @Override
    public List<Tag> readAll() {
        List<Tag> result;
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = ((BigInteger) session.createSQLQuery(SQL_COUNT_READ_ALL_TAGS).list().get(0)).longValue();

        result = (List<Tag>) session.createSQLQuery(SQL_READ_ALL_TAGS).addEntity(Tag.class)
                .list();

        return result;
    }

    @Override
    public List<Tag> readByCriteria(Pageable pageable, TagCriteria criteria) {
        List<Tag> result;
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = ((BigInteger) session.createSQLQuery(SQL_READ_COUNT_TAG_BY_CRITERIA)
                .setParameter("name", "%" + criteria.getName() + "%").list().get(0)).longValue();

        result = (List<Tag>) session.createSQLQuery(prepareSQLFromCriteria(criteria))
                .setParameter("name", "%" + criteria.getName() + "%")
                .setParameter("p_size", pageable.getPageSize())
                .setParameter("page", pageable.getOffset())
                .addEntity(Tag.class).list();

        return result;
    }

    private String prepareSQLFromCriteria(TagCriteria cr) {
        StringBuilder result = new StringBuilder(SQL_READ_TAG_BY_CRITERIA);
        if (cr.isSortByName()) {
            result.append(SQL_NAME);
        } else {
            result.append(SQL_ID);
        }
        result.append(cr.getSortOrder());
        result.append(PAGINATION);
        return result.toString();
    }

    @Override
    public void delete(Tag entity) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(entity);
    }

    @Override
    public boolean isExist(Tag entity) {
        Session session = sessionFactory.getCurrentSession();
        boolean result;
        result = session.createSQLQuery(READ_BY_CRITERIA_SQL)
                .setParameter("name", entity.getName())
                .addEntity(Tag.class).stream().findFirst().isPresent();
        return result;
    }

    @Override
    public List<Tag> getWutuhco() {
        Session session = sessionFactory.getCurrentSession();
        Long userId = ((BigInteger) session.createSQLQuery(WUTUHCO_GET_USER_ID_SQL).list().stream().findFirst().orElse(0L)).longValue();
        List<Long> tagIds = new ArrayList<>();
        session.createSQLQuery(WUTUHCO_GET_TAGS_SQL).setParameter("userId", userId)
                .list().forEach(id -> tagIds.add(((BigInteger) id).longValue()));
        List<Tag> tags = new ArrayList<>();
        for (Long id : tagIds) {
            tags.add(readById(id));
        }
        return tags;
    }

    @Override
    public Long getLastQueryCount() {
        return lastQueryCount;
    }

}

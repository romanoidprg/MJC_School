package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TagDao implements CommonDao<Tag, TagCriteria> {

    private static final String READ_BY_CRITERIA_SQL_QUERY = "SELECT * FROM tags WHERE name=:name";
    private final Logger logger = LogManager.getLogger(TagDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Long create(Tag entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public Tag readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        Tag result = session.get(Tag.class, id);
        if (result != null) {
            Hibernate.initialize(result.getCertificates());
        }
        return result;
    }

    @Override
    public boolean update(Tag entity) {
        return false;
    }

    @Override
    public List<Tag> readByCriteria(TagCriteria criteria) {
        Session session = sessionFactory.getCurrentSession();
        List<Tag> result;
        result = session.createSQLQuery(READ_BY_CRITERIA_SQL_QUERY)
                .setParameter("name", criteria.getName())
                .addEntity(Tag.class).list();
        return result;
    }

    @Override
    public void delete(Tag entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public boolean isExist(Tag entity) {
        Session session = sessionFactory.getCurrentSession();
        boolean result;
        result = session.createSQLQuery(READ_BY_CRITERIA_SQL_QUERY)
                .setParameter("name", entity.getName())
                .addEntity(Tag.class).stream().findFirst().isPresent();
        return result;
    }


}

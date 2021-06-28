package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional
public class TagDao implements CommonDao<Tag, TagCriteria> {

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
        return session.get(Tag.class, id);
    }

    @Override
    public boolean update(Tag entity) {
        return false;
    }

    @Override
    public List<Tag> readByCriteria(TagCriteria criteria) {
        List<Tag> result = new ArrayList<>();
        return result;
    }

    @Override
    public boolean deleteById(long id) {
        boolean result = false;
        return result;
    }

    @Override
    public boolean isExist(Tag entity) {
        return false;
    }


}

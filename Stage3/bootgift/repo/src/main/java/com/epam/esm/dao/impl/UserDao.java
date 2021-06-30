package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.User;
import com.epam.esm.model.UserCriteria;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class UserDao implements CommonDao<User, UserCriteria> {

    private static final String IS_EXIST_SQL_QUERY = "SELECT * FROM users where name = :name";

    public UserDao() {
    }

    private final Logger logger = LogManager.getLogger(UserDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Long create(User entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }


    @Override
    public User readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        return session.get(User.class, id);
    }

    @Override
    public List<User> readByCriteria(UserCriteria criteria) {
        return null;
    }


    @Override
    public boolean update(User entity) {
        return false;
    }

    @Override
    public void delete(User entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public boolean isExist(User entity) {
        Session s = sessionFactory.getCurrentSession();
        return s.createSQLQuery(IS_EXIST_SQL_QUERY)
                .setParameter("name", entity.getName())
                .addEntity(User.class)
                .stream().findAny().isPresent();
    }
}

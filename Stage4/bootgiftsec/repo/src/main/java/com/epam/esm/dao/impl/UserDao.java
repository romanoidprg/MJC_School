package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomCertDao;
import com.epam.esm.dao.CustomUserDao;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.model.UserCriteria;
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
import java.util.List;

@Transactional
public class UserDao implements CommonDao<User, UserCriteria>, CustomUserDao {

    private static final String IS_EXIST_SQL_QUERY = "SELECT * FROM users where name = :name";
    private static final String SQL_READ_COUNT_ALL = "SELECT COUNT(*) FROM users ";
    private static final String SQL_READ_ALL = "SELECT * FROM users";

    public UserDao() {
    }

    private Long lastQueryCount = 0L;

    private final Logger logger = LogManager.getLogger(UserDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public long create(User entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public List<User> readAll() {
        return null;
    }


    @Override
    public User readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = 1L;
        return session.get(User.class, id);
    }

    @Override
    public List<User> readByCriteria(Pageable pageable, UserCriteria criteria) {
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = ((BigInteger) session.createSQLQuery(SQL_READ_COUNT_ALL).list().get(0)).longValue();
        List<User> result = (List<User>) session.createSQLQuery(getPageableSQL(SQL_READ_ALL, pageable))
                .addEntity(User.class).list();
        return result;
    }

    private String getPageableSQL(String sql, Pageable pageable) {
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

    @Override
    public Long getLastQueryCount() {
        return lastQueryCount;
    }

    @Override
    public User readByName(String userName) {
        Session s = sessionFactory.getCurrentSession();
        User user = (User)s.createSQLQuery(IS_EXIST_SQL_QUERY)
                .setParameter("name", userName)
                .addEntity(User.class).stream().findAny().orElse(null);
        return user;
    }
}

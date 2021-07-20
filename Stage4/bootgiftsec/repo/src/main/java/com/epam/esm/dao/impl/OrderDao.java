package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCriteria;
import com.epam.esm.model.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Hibernate;
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
public class OrderDao implements CommonDao<Order, OrderCriteria> {

    private static final String SQL_READ_ALL = "SELECT * FROM orders ";
    private static final String SQL_READ_COUNT_ALL = "SELECT COUNT(*) FROM orders  ";

    public OrderDao() {
    }

    private Long lastQueryCount = 0L;

    private final Logger logger = LogManager.getLogger(OrderDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public long create(Order entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public List<Order> readAll() {
        return null;
    }

    @Override
    public Order readById(long id) {
        Session session = sessionFactory.getCurrentSession();
        lastQueryCount = 1L;
        return session.get(Order.class, id);
    }

    @Override
    public List<Order> readByCriteria(Pageable pageable, OrderCriteria criteria) {
        Session session = sessionFactory.getCurrentSession();
        String sqlCount = SQL_READ_COUNT_ALL;
        String sql = SQL_READ_ALL;
        if (criteria.getUserId()!=null) {
            sql = sql +" WHERE user_id=" + criteria.getUserId().toString()+" ";
            sqlCount = sqlCount +" WHERE user_id=" + criteria.getUserId().toString()+" ";
        }
        lastQueryCount = ((BigInteger) session.createSQLQuery(sqlCount).list().get(0)).longValue();
        return  (List<Order>) session.createSQLQuery(getPageableSQL(sql, pageable))
                .addEntity(Order.class).list();
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

    @Override
    public boolean update(Order entity) {
        return false;
    }

    @Override
    public void delete(Order entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }

    @Override
    public boolean isExist(Order entity) {
        return false;
    }

    @Override
    public Long getLastQueryCount() {
        return lastQueryCount;
    }
}

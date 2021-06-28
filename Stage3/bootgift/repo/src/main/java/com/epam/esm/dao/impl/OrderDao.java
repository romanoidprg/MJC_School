package com.epam.esm.dao.impl;

import com.epam.esm.dao.CommonDao;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCriteria;
import com.epam.esm.model.User;
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
public class OrderDao implements CommonDao<Order, OrderCriteria> {

    public OrderDao() {
    }

    private final Logger logger = LogManager.getLogger(OrderDao.class);

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public Long create(Order entity) {
        Long id;
        Session session = sessionFactory.getCurrentSession();
        id = (Long) session.save(entity);
        return id;
    }

    @Override
    public Order readById(long id) {
        return null;
    }

    @Override
    public List<Order> readByCriteria(OrderCriteria criteria) {
        return null;
    }

    @Override
    public boolean update(Order entity) {
        return false;
    }

    @Override
    public boolean deleteById(long id) {
        return false;
    }

    @Override
    public boolean isExist(Order entity) {
        return false;
    }
}
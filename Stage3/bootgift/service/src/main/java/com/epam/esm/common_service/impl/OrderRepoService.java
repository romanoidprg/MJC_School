package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;


public class OrderRepoService implements CommonService<Order> {

    private final Logger logger = LogManager.getLogger(OrderRepoService.class);

    @Autowired
    @Qualifier("orderDao")
    private CommonDao<Order, OrderCriteria> orderDao;

    @Override
    public Long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        Order order = objectMapper.readValue(jsonString, Order.class);
        if (!orderDao.isExist(order)) {
            order.setId(null);
            order.setTimeStamp(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            order.setCost(order.getCert().getPrice());
            id = orderDao.create(order);
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    @Override
    public Order readById(String id) throws NoSuchIdException {
        return null;
    }

    @Override
    public List<Order> readByCriteria(String... params) {
        return null;
    }

    @Override
    public boolean updateFromJson(String jsonString) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean fillTable() {
        return false;
    }
}

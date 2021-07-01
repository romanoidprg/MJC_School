package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.*;
import com.epam.esm.model.*;
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
import java.util.Map;


public class OrderRepoService implements CommonService<Order> {

    private final Logger logger = LogManager.getLogger(OrderRepoService.class);

    @Autowired
    @Qualifier("orderDao")
    private CommonDao<Order, OrderCriteria> orderDao;

    @Autowired
    @Qualifier("userDao")
    private CommonDao<User, UserCriteria> userDao;

    @Autowired
    @Qualifier("certDao")
    private CommonDao<GiftCertificate, CertCriteria> certDao;

    @Override
    public long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
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
    public Long create(String... params) throws LocalAppException {
        String userId = params[0];
        String certId = params[1];
        if (userId.matches("[0-9]+")) {
            if (certId.matches("[0-9]+")) {
                Order order = new Order();
                User user = userDao.readById(Long.parseLong(userId));
                if (user == null) {
                    throw new NoSuchUserIdException(userId);
                }
                order.setUser(user);
                GiftCertificate cert = certDao.readById(Long.parseLong(certId));
                if (cert == null) {
                    throw new NoSuchCertIdException(certId);
                }
                order.setCert(cert);
                order.setCost(cert.getPrice());
                order.setTimeStamp(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
                return orderDao.create(order);
            } else {
                throw new NoSuchCertIdException(certId);
            }
        } else {
            throw new NoSuchUserIdException(userId);
        }
    }

    @Override
    public Order readById(String id) throws LocalAppException {
        Order result;
        if (id.matches("[0-9]+")) {
            result = orderDao.readById(Long.parseLong(id));
            if (result == null) {
                throw new NoSuchOrderIdException(id);
            }
        } else {
            throw new NoSuchOrderIdException(id);
        }
        result.getCert().getTags().forEach(t -> t.setCertificates(null));
        return result;
    }

    @Override
    public List<Order> readByCriteria(String... params) {
        return null;
    }

    @Override
    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
        return false;
    }

    @Override
    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(String id) throws LocalAppException {
                orderDao.delete(readById(id));
    }

    @Override
    public boolean fillTable() {
        return false;
    }
}

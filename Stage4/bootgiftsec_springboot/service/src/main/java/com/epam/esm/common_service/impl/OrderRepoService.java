package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomOrderService;
import com.epam.esm.dao.CertRepo;
import com.epam.esm.dao.OrderRepo;
import com.epam.esm.dao.UserRepo;
import com.epam.esm.errors.*;
import com.epam.esm.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Map;


public class OrderRepoService implements CommonService<Order>, CustomOrderService {

    @Autowired
    OrderRepo orderRepo;
    @Autowired
    UserRepo userRepo;
    @Autowired
    CertRepo certRepo;

    private final Logger logger = LogManager.getLogger(OrderRepoService.class);

    @Override
    public long createFromJson(String jsonString) throws Exception {
        return 0;
    }

    @Override
    public Long createFromUserIdAndCertId(Long userId, Long certId) throws Exception{
        User user = userRepo.findById(userId).orElse(null);
        GiftCertificate cert = certRepo.findById(certId).orElse(null);
        if(user==null) {
            throw new NoSuchUserIdException(String.valueOf(userId));
        } else if (cert==null) {
            throw new NoSuchCertIdException(String.valueOf(certId));
        } else {
            Order order = new Order();
            order.setUser(user);
            order.setCert(cert);
            order.setCost(cert.getPrice());
            order.setTimeStamp(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            return orderRepo.save(order).getId();
        }
    }

    @Override
    public Order readById(Long id) throws LocalAppException {
        return null;
    }

    @Override
    public Page<Order> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        return null;
    }

    @Override
    public boolean updateField(Long id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(Long id) throws LocalAppException {

    }

}
//
//    @Override
//    public long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
//        return 0L;
//    }
//
//
//    @Override
//    public Long create(String... params) throws LocalAppException {
//        String userId = params[0];
//        String certId = params[1];
//        if (userId.matches("[0-9]+")) {
//            if (certId.matches("[0-9]+")) {`
//                Order order = new Order();
//                User user = userDao.readById(Long.parseLong(userId));
//                if (user == null) {
//                    throw new NoSuchUserIdException(userId);
//                }
//                order.setUser(user);
//                GiftCertificate cert = certDao.readById(Long.parseLong(certId));
//                if (cert == null) {
//                    throw new NoSuchCertIdException(certId);
//                }
//                order.setCert(cert);
//                order.setCost(cert.getPrice());
//                order.setTimeStamp(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
//                return orderDao.create(order);
//            } else {
//                throw new NoSuchCertIdException(certId);
//            }
//        } else {
//            throw new NoSuchUserIdException(userId);
//        }
//    }
//
//    @Override
//    public Order readById(String id) throws LocalAppException {
//        Order result;
//        if (id.matches("[0-9]+")) {
//            result = orderDao.readById(Long.parseLong(id));
//            if (result == null) {
//                throw new NoSuchOrderIdException(id);
//            }
//        } else {
//            throw new NoSuchOrderIdException(id);
//        }
//        result.getCert().getTags().forEach(t -> t.setCertificates(null));
//        return result;
//    }
//
//    @Override
//    public List<Order> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
//        OrderCriteria criteria = new OrderCriteria();
//        if (params.length>0) {
//            if (params[0].matches("[0-9]+")) {
//                criteria.setUserId(Long.parseLong(params[0]));
//            } else {
//                throw new NoSuchUserIdException(params[0]);
//            }
//        }
//        List<Order> result = orderDao.readByCriteria(pageable, criteria);
//        result.forEach(o -> o.getCert().getTags().forEach(t->t.setCertificates(null)));
//        return  result;
//    }
//
//    @Override
//    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
//        return false;
//    }
//
//    @Override
//    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
//        return false;
//    }
//
//    @Override
//    public void deleteById(String id) throws LocalAppException {
//                orderDao.delete(readById(id));
//    }
//
//    @Override
//    public Long getLastQueryCount() {
//        return orderDao.getLastQueryCount();
//    }
//
//    @Override
//    public boolean fillTable() {
//        return false;
//    }

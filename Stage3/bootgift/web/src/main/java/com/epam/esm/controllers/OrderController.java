package com.epam.esm.controllers;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

//    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return certRepoService.fillTable();
//    }


    @GetMapping(value = "/{id}")
    public Order readOrderById(@PathVariable String id) throws LocalAppException {
            return orderRepoService.readById(id);
    }
    @DeleteMapping(value = "/{id}")
    public void deleteOrder(@PathVariable String id) throws LocalAppException{
        orderRepoService.deleteById(id);
    }


}
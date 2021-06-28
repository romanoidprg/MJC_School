package com.epam.esm.controllers;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

    @PostMapping
    public Long createUser(@RequestBody String jsonString) throws Exception {
        return orderRepoService.createFromJson(jsonString);
    }

//    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return certRepoService.fillTable();
//    }

//
//    @GetMapping(value = "/{id}")
//    public User readUserById(@PathVariable String id) throws NoSuchIdException {
//        return userRepoService.readById(id);
//    }
//
//    @GetMapping
//    public List<GiftCertificate> readCertificatesByParams(
//            @RequestParam(value = "tag_name", required = false) String tagName,
//            @RequestParam(value = "name", required = false) String name,
//            @RequestParam(value = "description", required = false) String description,
//            @RequestParam(value = "sort_by_name", required = false) String sortByName,
//            @RequestParam(value = "sort_by_cr_date", required = false) String sortByCrDate,
//            @RequestParam(value = "sort_by_upd_date", required = false) String sortByUpdDate,
//            @RequestParam(value = "sort_name_order", required = false) String sortNameOrder,
//            @RequestParam(value = "sort_cr_date_order", required = false) String sortCrDateOrder,
//            @RequestParam(value = "sort_upd_date_order", required = false) String sortUpdDateOrder) {
//
//        return userRepoService.readByCriteria(tagName, name, description,
//                sortByName, sortByCrDate, sortByUpdDate,
//                sortNameOrder, sortCrDateOrder, sortUpdDateOrder);
//    }
//
//    @PutMapping
//    public boolean updateCertificate(@RequestBody String jsonString) {
//        return userRepoService.updateFromJson(jsonString);
//    }
//
//    @DeleteMapping(value = "/{id}")
//    public boolean deleteCertificate(@PathVariable String id) {
//        return userRepoService.deleteById(id);
//    }


}
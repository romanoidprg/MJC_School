package com.epam.esm.controllers;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {

    @Autowired
    @Qualifier("userRepoService")
    CommonService<User> userRepoService;

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

    @PostMapping
    public Long createUser(@RequestBody String jsonString) throws Exception {
        return userRepoService.createFromJson(jsonString);
    }

    @PostMapping(value = "/filltable")
    public boolean fillCertificateTable() throws Exception {
        return userRepoService.fillTable();
    }


    @PostMapping(value = "/{userId}/orderforcertificate/{certId}")
    public Long createOrder(@PathVariable String userId, @PathVariable String certId) throws LocalAppException {
        return orderRepoService.create(userId, certId);
    }

    @GetMapping(value = "/{id}")
    public User readUserById(@PathVariable String id) throws LocalAppException {
        return userRepoService.readById(id);
    }


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

    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable String id) throws LocalAppException {
        userRepoService.deleteById(id);
    }


}
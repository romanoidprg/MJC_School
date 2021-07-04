package com.epam.esm.controllers;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/v1/certificates")
public class CertificatesController {

    private static final String DEF_PAGE = "1";
    private static final String DEF_P_SIZE = "5";
    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

    @PostMapping
    public Long createCertificate(@RequestBody String jsonString) throws Exception {
        return certRepoService.createFromJson(jsonString);
    }



    @PutMapping(value = "/{id}")
    public boolean changeCertificateField(@PathVariable String id,
            @RequestParam Map<String,String> params) throws LocalAppException {
            return certRepoService.updateField(id, params);
    }

    @GetMapping
    public List<GiftCertificate> readCertificatesByParams(
            @RequestParam(value = "tag_name", required = false) String tagName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "sort_by_name", required = false) String sortByName,
            @RequestParam(value = "sort_by_cr_date", required = false) String sortByCrDate,
            @RequestParam(value = "sort_by_upd_date", required = false) String sortByUpdDate,
            @RequestParam(value = "sort_name_order", required = false) String sortNameOrder,
            @RequestParam(value = "sort_cr_date_order", required = false) String sortCrDateOrder,
            @RequestParam(value = "sort_upd_date_order", required = false) String sortUpdDateOrder,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "page_size", required = false) String pSize
            ) {
        page = page == null ? DEF_PAGE : page;
        pSize = pSize == null ? DEF_P_SIZE : pSize;
        Pageable pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(pSize));
        return certRepoService.readByCriteria(pageable, tagName, name,
                description, sortByName, sortByCrDate,
                sortByUpdDate, sortNameOrder, sortCrDateOrder, sortUpdDateOrder);
    }

//    @PutMapping(value = "/{id}")
//    public boolean updateCertificate(@PathVariable String id, @RequestBody String jsonString) throws LocalAppException {
//            return certRepoService.updateFromJson(id, jsonString);
//    }

    @DeleteMapping(value = "/{id}")
    public void deleteCertificate(@PathVariable String id) throws LocalAppException {
            certRepoService.deleteById(id);
    }

    @GetMapping(value = "/{id}")
    public GiftCertificate readCertificateById(@PathVariable String id) throws LocalAppException {
        GiftCertificate result = certRepoService.readById(id);
        return result;
    }
}


    //    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return certRepoService.fillTable();
//    }

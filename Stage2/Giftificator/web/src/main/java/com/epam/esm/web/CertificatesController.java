package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/v1/certificates")
public class CertificatesController {

    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

    @PostMapping
    public boolean createCertificate(@RequestBody String jsonString) throws JsonProcessingException {
        return certRepoService.createFromJson(jsonString);
    }


    @GetMapping(value = "/{id}")
    public GiftCertificate readCertificateById(@PathVariable String id) throws NoSuchIdException {
        return certRepoService.readById(id);
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
            @RequestParam(value = "sort_upd_date_order", required = false) String sortUpdDateOrder) {

        return certRepoService.readByCriteria(tagName, name, description,
                sortByName, sortByCrDate, sortByUpdDate,
                sortNameOrder, sortCrDateOrder, sortUpdDateOrder);
    }

    @PutMapping
    public boolean updateCertificate(@RequestBody String jsonString) {
        return certRepoService.updateFromJson(jsonString);
    }

    @DeleteMapping(value = "/{id}")
    public boolean deleteCertificate(@PathVariable String id) {
        return certRepoService.deleteById(id);
    }


}
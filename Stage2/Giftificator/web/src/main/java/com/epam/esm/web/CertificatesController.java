package com.epam.esm.web;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/v1/certificates")
public class CertificatesController {

    CommonService<GiftCertificate> certRepoService = new CertRepoService();

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public boolean createCertificate(@RequestBody String jsonString) {
        return certRepoService.createFromJson(jsonString);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public GiftCertificate readCertificateById(@PathVariable String id) {
        return certRepoService.readById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<GiftCertificate> readCertificatesByParams(
            @RequestParam(value = "tag_name", required = false) String tagName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "sort_by_name", required = false) String sortByName,
            @RequestParam(value = "sort_by_date", required = false) String sortByDate,
            @RequestParam(value = "sort_order", required = false) String sortOrder) {

        return certRepoService.readByCriteria(tagName, name, description, sortByName, sortByDate, sortOrder);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateCertificate(@RequestBody String jsonString) {
        return certRepoService.updateFromJson(jsonString);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteCertificate(@PathVariable String id) {
        //todo: must return result of deleting
        return certRepoService.deleteById(id);
    }


}

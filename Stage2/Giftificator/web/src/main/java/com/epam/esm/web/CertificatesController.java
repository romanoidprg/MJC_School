package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.model.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping(value = "/v1/certificates")
public class CertificatesController {

    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

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
            @RequestParam(value = "sort_by_cr_date", required = false) String sortByCrDate,
            @RequestParam(value = "sort_by_upd_date", required = false) String sortByUpdDate,
            @RequestParam(value = "sort_name_order", required = false) String sortNameOrder,
            @RequestParam(value = "sort_cr_date_order", required = false) String sortCrDateOrder,
            @RequestParam(value = "sort_upd_date_order", required = false) String sortUpdDateOrder) {

        return certRepoService.readByCriteria(tagName, name, description,
                sortByName, sortByCrDate, sortByUpdDate,
                sortNameOrder, sortCrDateOrder, sortUpdDateOrder);
    }

    @RequestMapping(method = RequestMethod.PUT)
    @ResponseBody
    public boolean updateCertificate(@RequestBody String jsonString) {
        return certRepoService.updateFromJson(jsonString);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteCertificate(@PathVariable String id) {
        return certRepoService.deleteById(id);
    }


}
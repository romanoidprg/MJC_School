package com.epam.esm.controllers;

import com.epam.esm.Errors.NoAnyTagException;
import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.BoolWrapper;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.IdWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.epam.esm.CommonWeb.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/certificates")
public class CertificatesController {


    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

    @Autowired
    CustomCertService customCertRepoService;

    @PostMapping
    public EntityModel<IdWrapper> createCertificate(@RequestBody String jsonString) throws Exception {
        Long id = certRepoService.createFromJson(jsonString);
        return EntityModel.of(IdWrapper.of(id),
                getLinkRead(id),
                getLinkReadParams("tag_name", "name", "descr",
                        "id", "asc",
                        DEF_PAGE, DEF_P_SIZE),
                getLinkDel(id));
    }

    @GetMapping(value = "/{id}")
    public EntityModel<GiftCertificate> readCertificateById(@PathVariable String id) throws Exception {
        return EntityModel.of(certRepoService.readById(id),
                getLinkRead(Long.parseLong(id)).withSelfRel(),
                getLinkDel(Long.parseLong(id)));
    }

    @GetMapping(value = "/withtags")
    public CollectionModel<EntityModel<GiftCertificate>> readCertsWithTags(
            @RequestParam(value = "tag", required = false) String[] tags,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize
    ) throws Exception {
        if (tags == null) {
            throw new NoAnyTagException();
        }

        Pageable pageable = getPagebale(sortBy, sortOrder, page, pSize);
        Page<GiftCertificate> resultPage = customCertRepoService.readCertsWithTags(pageable, tags);

        if (resultPage.getNumber() > resultPage.getTotalPages()) {
            throw new NoSuchPageException();
        }

        List<EntityModel<GiftCertificate>> entCerts = entityCertListFromPage(resultPage);
        CollectionModel<EntityModel<GiftCertificate>> collEntCerts = CollectionModel.of(
                entCerts, getLinkReadParams(tags, sortBy, sortOrder, page, pSize).withSelfRel().expand()
        );
        collEntCerts.add(getLinksPaginationForGetWithTags(tags, resultPage));

        return collEntCerts;
    }

    private List<EntityModel<GiftCertificate>> entityCertListFromPage(Page<GiftCertificate> resultPage) throws Exception {
        List<GiftCertificate> certList = resultPage.toList();

        List<EntityModel<GiftCertificate>> entCerts = new ArrayList<>();
        for (GiftCertificate c : certList) {
            c.getTags().forEach(t -> t.setCertificates(null));
            entCerts.add(EntityModel.of(c,
                    getLinkRead(c.getId()),
                    getLinkDel(c.getId())));
        }
        return entCerts;
    }

    private List<Link> getLinksPaginationForGetWithTags(String[] tags, Page<GiftCertificate> page)
            throws Exception {
        List<Link> links = new ArrayList<>();
        String sortBy = null;
        String order = null;
        if (!page.getPageable().getSort().toList().isEmpty()) {
            sortBy = page.getPageable().getSort().toList().get(0).getProperty();
            order = page.getPageable().getSort().toList().get(0).getDirection().name();
        }
        if (page.hasPrevious()) {
            int firstPage = page.getPageable().first().getPageNumber();
            int prevPage = page.previousOrFirstPageable().getPageNumber();
            links.add(getLinkReadParams(tags, sortBy, order, firstPage, page.getSize()).expand().withRel(REL_FIRST_PAGE));
            links.add(getLinkReadParams(tags, sortBy, order, prevPage, page.getSize()).expand().withRel(REL_PREVIOUS_PAGE));
        }
        if (page.hasNext()) {
            int nextPage = page.nextPageable().getPageNumber();
            int lastPage = page.getTotalPages() - 1;
            links.add(getLinkReadParams(tags, sortBy, order, nextPage, page.getSize()).expand().withRel(REL_NEXT_PAGE));
            links.add(getLinkReadParams(tags, sortBy, order, lastPage, page.getSize()).expand().withRel(REL_LAST_PAGE));
        }
        return links;
    }

    private Link getLinkReadParams(String[] tags, String sortBy, String order, Integer page, Integer pSize) throws Exception {
        return linkTo(methodOn(CertificatesController.class)
                .readCertsWithTags(tags, sortBy, order, page, pSize)).withRel(REL_READ_CERT_TAGS);
    }

    @GetMapping
    public CollectionModel<EntityModel<GiftCertificate>> readCertificatesByParams(
            @RequestParam(value = "tag_name", required = false) String tagName,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize
    ) throws Exception {
        Pageable pageable = getPagebale(sortBy, sortOrder, page, pSize);

        Page<GiftCertificate> resultPage = certRepoService.readByCriteria(pageable, tagName, name, description);

        if (resultPage.getNumber() > resultPage.getTotalPages()) {
            throw new NoSuchPageException();
        }

        List<EntityModel<GiftCertificate>> entCerts = entityCertListFromPage(resultPage);
        CollectionModel<EntityModel<GiftCertificate>> collEntCerts = CollectionModel.of(
                entCerts, getLinkReadParams(tagName, name, description, sortBy, sortOrder, page, pSize).withSelfRel().expand()
        );
        collEntCerts.add(getLinksPagination(tagName, name, description, resultPage));

        return collEntCerts;

    }


    private List<Link> getLinksPagination(String tagName, String name, String description,
                                          Page<GiftCertificate> page) throws Exception {
        List<Link> links = new ArrayList<>();
        String sortBy = null;
        String order = null;
        if (!page.getPageable().getSort().toList().isEmpty()) {
            sortBy = page.getPageable().getSort().toList().get(0).getProperty();
            order = page.getPageable().getSort().toList().get(0).getDirection().name();
        }
        if (page.hasPrevious()) {
            int firstPage = page.getPageable().first().getPageNumber();
            int prevPage = page.previousOrFirstPageable().getPageNumber();
            links.add(getLinkReadParams(tagName, name, description, sortBy, order, firstPage, page.getSize()).expand().withRel(REL_FIRST_PAGE));
            links.add(getLinkReadParams(tagName, name, description, sortBy, order, prevPage, page.getSize()).expand().withRel(REL_PREVIOUS_PAGE));
        }
        if (page.hasNext()) {
            int nextPage = page.nextPageable().getPageNumber();
            int lastPage = page.getTotalPages() - 1;
            links.add(getLinkReadParams(tagName, name, description, sortBy, order, nextPage, page.getSize()).expand().withRel(REL_NEXT_PAGE));
            links.add(getLinkReadParams(tagName, name, description, sortBy, order, lastPage, page.getSize()).expand().withRel(REL_LAST_PAGE));
        }
        return links;
    }

    @PutMapping(value = "/{id}")
    public EntityModel<BoolWrapper> changeCertificateField(@PathVariable String id,
                                                           @RequestParam Map<String, String> params) throws Exception {
        return EntityModel.of(BoolWrapper.of(certRepoService.updateField(id, params)),
                getLinkRead(Long.parseLong(id)),
                getLinkDel(Long.parseLong(id)));
    }


    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteCertificate(@PathVariable String id) throws LocalAppException {
        certRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    private Link getLinkDel(Long id) throws LocalAppException {
        return linkTo(methodOn(CertificatesController.class)
                .deleteCertificate(String.valueOf(id))).withRel(REL_DELETE_CERT);
    }

    private Link getLinkReadParams(String tagName, String name, String description, String sortBy, String sortOrder,
                                   Integer page, Integer pSize) throws Exception {
        return linkTo(methodOn(CertificatesController.class).readCertificatesByParams(tagName, name, description,
                sortBy, sortOrder, page, pSize)).withRel(REL_READ_CERT_PARAMS);
    }

    private Link getLinkRead(Long id) throws Exception {
        return linkTo(methodOn(CertificatesController.class)
                .readCertificateById(String.valueOf(id))).withRel(REL_READ_CERT_BY_ID);
    }


}

//    @PutMapping(value = "/{id}")
//    public boolean updateCertificate(@PathVariable String id, @RequestBody String jsonString) throws LocalAppException {
//            return certRepoService.updateFromJson(id, jsonString);
//    }

//    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return certRepoService.fillTable();
//    }

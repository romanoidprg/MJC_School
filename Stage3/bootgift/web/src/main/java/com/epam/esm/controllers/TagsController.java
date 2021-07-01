package com.epam.esm.controllers;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/tags")
public class TagsController {

    private static final String REL_READ_BY_ID = "Read tag by id.";
    private static final String REL_DELETE_TAG = "Delete tag";
    private static final String TAG_ID = "tag_id";
    public static final String TAG_IN_JSON_FORMAT = "tag_in_json_format";
    public static final String REL_CREATE_TAG_FROM_JSON_FORMAT = "Create tag from json format";
    public static final String WUTUHCO = "Get the most widely used tag of a user with the highest cost of all orders.";
    public static final String READ_1_ST_TAG = "Read 1-st tag.";


    @Autowired
    @Qualifier("tagRepoService")
    CommonService<Tag> tagRepoService;

    @PostMapping
    public Long createTag(@RequestBody String jsonString) throws Exception {
        return tagRepoService.createFromJson(jsonString);
    }


    @GetMapping
    public List<Tag> readTagByParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort_by_name", required = false) String sortByName,
            @RequestParam(value = "sort_order", required = false) String sortOrder) {

        return tagRepoService.readByCriteria(name, sortByName, sortOrder);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable String id) throws LocalAppException {
        tagRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/wutuhco")
    public CollectionModel<Tag> getWutuhco() throws Exception {
        List<Tag> tagList = ((TagRepoService) tagRepoService).getMostUsedTagsOfUserWithMostExpensiveOrdersCost();
        CollectionModel<Tag> tags = CollectionModel.empty();
        for(Tag t: tagList){
            tags.ad
        }
        CollectionModel<Tag> tags = CollectionModel.of(tagList,
                linkTo(methodOn(TagsController.class).readTagById("1")).withRel(READ_1_ST_TAG),
                linkTo(methodOn(TagsController.class).getWutuhco()).withSelfRel(),
                linkTo(methodOn(TagsController.class).deleteTag(TAG_ID)).withRel(REL_DELETE_TAG));
        return tags;
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Tag> readTagById(@PathVariable String id) throws Exception {
        Tag tag = tagRepoService.readById(id);
        tag.setCertificates(null);
        EntityModel<Tag> tagEM = EntityModel.of(tag,
                linkTo(methodOn(TagsController.class).readTagById(id)).withSelfRel(),
                linkTo(methodOn(TagsController.class).getWutuhco()).withRel(WUTUHCO),
                linkTo(methodOn(TagsController.class).deleteTag(id)).withRel(REL_DELETE_TAG));
        return tagEM;
    }

}


//    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return tagRepoService.fillTable();
//    }

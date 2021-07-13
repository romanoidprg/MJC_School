package com.epam.esm.controllers;

import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.CommonWeb.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/tags")
public class TagsController {


    @Autowired
    @Qualifier("tagRepoService")
    private CommonService<Tag> tagRepoService;

    @Autowired
    @Qualifier("customTagRepoService")
    private CustomTagServise<Tag> customTagRepoService;

    @PostMapping
    public EntityModel<Long> createTag(@RequestBody String jsonString) throws Exception {
        Long id = tagRepoService.createFromJson(jsonString);
        return EntityModel.of(id,
                getLinkRead(id),
                getLinkWutuhco(),
                getLinkReadParams("", "true", "asc", DEF_PAGE, DEF_P_SIZE),
                getLinkDel(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<Tag>> readTagByParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort_by_name", required = false) String sortByName,
            @RequestParam(value = "sort_order", required = false) String sortOrder,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "page_size", required = false) String pSize
    ) throws Exception {
        Pageable pageable = getPagebale(page, pSize, Sort.unsorted());
        List<Tag> tagList = tagRepoService.readByCriteria(pageable, name, sortByName, sortOrder);
        List<EntityModel<Tag>> entTags = new ArrayList<>();
        Long totalCount = tagRepoService.getLastQueryCount();
        PageImpl<EntityModel<Tag>> pager = new PageImpl(entTags, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entTags = new ArrayList<>();
        for (Tag t : tagList) {
            t.setCertificates(null);
            entTags.add(EntityModel.of(t,
                    getLinkRead(t.getId()),
                    getLinkDel(t.getId())));
        }
        pager = new PageImpl(entTags, pageable, totalCount);
        CollectionModel<EntityModel<Tag>> collEntTags = CollectionModel.of(pager,
                getLinkReadParams(name, sortByName, sortOrder, page, pSize).expand().withSelfRel(),
                getLinkWutuhco());
        collEntTags.add(getLinksPagination(name, sortByName, sortOrder, pageable, pager));

        return collEntTags;
    }

    private List<Link> getLinksPagination(String name,
                                          String sortByName,
                                          String sortOrder,
                                          Pageable pageable,
                                          PageImpl<EntityModel<Tag>> pager) throws Exception {
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() != 0) {
            links.add(getLinkReadParams(name, sortByName, sortOrder,
                    String.valueOf(pageable.first().getPageNumber()),
                    String.valueOf(pageable.getPageSize()))
                    .expand().withRel(REL_FIRST_PAGE));
            links.add(getLinkReadParams(name, sortByName, sortOrder,
                    String.valueOf(pageable.previousOrFirst().getPageNumber()),
                    String.valueOf(pageable.getPageSize()))
                    .expand().withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(getLinkReadParams(name, sortByName, sortOrder,
                    String.valueOf(pageable.next().getPageNumber()),
                    String.valueOf(pageable.getPageSize()))
                    .expand().withRel(REL_NEXT_PAGE));
            links.add(getLinkReadParams(name, sortByName, sortOrder,
                    String.valueOf(pager.getTotalPages() - 1),
                    String.valueOf(pageable.getPageSize()))
                    .expand().withRel(REL_LAST_PAGE));
        }
        return links;
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable String id) throws LocalAppException {
        tagRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping(value = "/wutuhco")
    public CollectionModel<EntityModel<Tag>> getWutuhco() throws Exception {
        List<Tag> tagList = customTagRepoService.getMostUsedTagsOfUserWithMostExpensiveOrdersCost();
        List<EntityModel<Tag>> entTags = new ArrayList<>();
        for (Tag t : tagList) {
            t.setCertificates(null);
            entTags.add(EntityModel.of(t,
                    getLinkRead(t.getId()),
                    getLinkDel(t.getId())));
        }
        CollectionModel<EntityModel<Tag>> collEntTags = CollectionModel.of(entTags,
                getLinkWutuhco().withSelfRel(),
                getLinkReadParams("", "true", "asc", DEF_PAGE, DEF_P_SIZE)
        );
        return collEntTags;
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Tag> readTagById(@PathVariable String id) throws Exception {
        Tag tag = tagRepoService.readById(id);
        tag.setCertificates(null);
        EntityModel<Tag> tagEM = EntityModel.of(tag,
                getLinkRead(Long.parseLong(id)).withSelfRel(),
                getLinkWutuhco(),
                getLinkReadParams("", "true", "asc", DEF_PAGE, DEF_P_SIZE),
                getLinkDel(Long.parseLong(id)));
        return tagEM;
    }

    private Link getLinkWutuhco() throws Exception {
        return linkTo(methodOn(TagsController.class).getWutuhco()).withRel(REL_WUTUHCO);
    }

    private Link getLinkDel(Long id) throws LocalAppException {
        return linkTo(methodOn(TagsController.class).deleteTag(String.valueOf(id))).withRel(REL_DELETE_TAG);
    }

    private Link getLinkReadParams(String name, String sortByName, String sortOrder, String page, String pSize) throws Exception {
        return linkTo(methodOn(TagsController.class).readTagByParams(name, sortByName, sortOrder, page, pSize)).withRel(REL_READ_TAG_PARAMS);
    }

    private Link getLinkRead(Long id) throws Exception {
        return linkTo(methodOn(TagsController.class).readTagById(String.valueOf(id))).withRel(REL_READ_BY_ID);
    }


}


//    @PostMapping(value = "/filltable")
//    public boolean fillCertificateTable() throws Exception {
//        return tagRepoService.fillTable();
//    }

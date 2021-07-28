package com.epam.esm.controllers;

import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.IdWrapper;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
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
    public EntityModel<IdWrapper> createTag(@RequestBody String jsonString) throws Exception {
        Long id = tagRepoService.createFromJson(jsonString);
        return EntityModel.of(IdWrapper.of(id),
                getLinkRead(id),
                getLinkWutuhco(),
//                getLinkReadParams("", "name", "asc", DEF_PAGE, DEF_P_SIZE),
                getLinkDel(id));
    }

    @GetMapping
    public CollectionModel<EntityModel<Tag>> readTagByParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize
    ) throws Exception {
        Pageable pageable = getPagebale(page, pSize,
                Sort.by(Sort.Direction.DESC.name().equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC,
                        sortBy));
        Page<Tag> resultPage = tagRepoService.readByCriteria(pageable, name);
        if (resultPage.getNumber() > resultPage.getTotalPages()) {
            throw new NoSuchPageException();
        }
        List<EntityModel<Tag>> entTags = entityTagListFromPage(resultPage);
        CollectionModel<EntityModel<Tag>> collEntTags = CollectionModel.of(entTags,
                getLinkReadParams(name, sortBy, sortOrder, page, pSize).expand().withSelfRel(),
                getLinkWutuhco());
        collEntTags.add(getLinksPagination(name, sortBy, sortOrder, resultPage));

        return collEntTags;
    }

    private List<EntityModel<Tag>> entityTagListFromPage(Page<Tag> resultPage) throws Exception {
        List<Tag> tagList = resultPage.toList();

        List<EntityModel<Tag>> entTags = new ArrayList<>();
        for (Tag t : tagList) {
            t.setCertificates(null);
            entTags.add(EntityModel.of(t,
                    getLinkRead(t.getId()),
                    getLinkDel(t.getId())));
        }
        return entTags;
    }

    private List<Link> getLinksPagination(String name, String sortBy, String sortOrder, Page page) throws Exception {
        List<Link> links = new ArrayList<>();
        if (page.hasPrevious()) {
            int firstPage = page.getPageable().first().getPageNumber();
            int prevPage = page.previousOrFirstPageable().getPageNumber();
            links.add(getLinkReadParams(name, sortBy, sortOrder, firstPage, page.getSize()).expand().withRel(REL_FIRST_PAGE));
            links.add(getLinkReadParams(name, sortBy, sortOrder, prevPage, page.getSize()).expand().withRel(REL_PREVIOUS_PAGE));
        }
        if (page.hasNext()) {
            int nextPage = page.nextPageable().getPageNumber();
            int lastPage = page.getTotalPages() - 1;
            links.add(getLinkReadParams(name, sortBy, sortOrder, nextPage, page.getSize()).expand().withRel(REL_NEXT_PAGE));
            links.add(getLinkReadParams(name, sortBy, sortOrder, lastPage, page.getSize()).expand().withRel(REL_LAST_PAGE));
        }
        return links;
    }


    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) throws LocalAppException {
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
                getLinkReadParams("", "name", "asc", DEF_PAGE, DEF_P_SIZE)
        );
        return collEntTags;
    }

    @GetMapping(value = "/{id}")
    public EntityModel<Tag> readTagById(@PathVariable Long id) throws Exception {
        Tag tag = tagRepoService.readById(id);
        tag.setCertificates(null);
        EntityModel<Tag> tagEM = EntityModel.of(tag,
                getLinkRead(id).withSelfRel(),
                getLinkWutuhco(),
                getLinkReadParams("", "name", "asc", DEF_PAGE, DEF_P_SIZE),
                getLinkDel(id));
        return tagEM;
    }

    private Link getLinkWutuhco() throws Exception {
        return linkTo(methodOn(TagsController.class).getWutuhco()).withRel(REL_WUTUHCO);
    }

    private Link getLinkDel(Long id) throws LocalAppException {
        return linkTo(methodOn(TagsController.class).deleteTag(id)).withRel(REL_DELETE_TAG);
    }

    private Link getLinkReadParams(String name, String sortBy, String sortOrder, Integer page, Integer pSize) throws Exception {
        return linkTo(methodOn(TagsController.class).readTagByParams(name, sortBy, sortOrder, page, pSize)).withRel(REL_READ_TAG_PARAMS);
    }

    private Link getLinkRead(Long id) throws Exception {
        return linkTo(methodOn(TagsController.class).readTagById(id)).withRel(REL_READ_BY_ID);
    }


}


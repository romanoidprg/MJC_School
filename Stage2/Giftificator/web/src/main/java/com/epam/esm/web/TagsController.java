package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/v1/tags")
public class TagsController {

    CommonService<Tag> tagRepoService = new TagRepoService();

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public boolean createTag(@RequestBody String jsonString) {
        return tagRepoService.createFromJson(jsonString);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Tag readTagById(@PathVariable String id) {
        return tagRepoService.readById(id);
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> readTagByParams(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "sort_by_name", required = false) String sortByName,
            @RequestParam(value = "sort_order", required = false) String sortOrder) {

        return tagRepoService.readByCriteria(name, sortByName, sortOrder);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTag(@PathVariable String id) {
        //todo: must return result of deleting
        return tagRepoService.deleteById(id);
    }


}

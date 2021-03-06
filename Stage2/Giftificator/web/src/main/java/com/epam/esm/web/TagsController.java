package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/v1/tags")
public class TagsController {

    @Autowired
    @Qualifier("tagRepoService")
    CommonService<Tag> tagRepoService;

    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public boolean createTag(@RequestBody String jsonString) throws JsonProcessingException {
        return tagRepoService.createFromJson(jsonString);
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Tag readTagById(@PathVariable String id) throws NoSuchIdException {
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
        return tagRepoService.deleteById(id);
    }

}

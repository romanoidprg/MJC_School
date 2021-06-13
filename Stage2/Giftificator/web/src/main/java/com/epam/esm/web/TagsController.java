package com.epam.esm.web;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.model.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Controller
@RequestMapping(value = "/v1/tags")
public class TagsController {

    @Autowired
    @Qualifier("tagRepoService")
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
        return tagRepoService.deleteById(id);
    }

    @GetMapping(value = "/testExceptionHandler", produces = APPLICATION_JSON_VALUE)
    public boolean testExceptionHandler() throws FileNotFoundException {
        if (true) {
            throw new FileNotFoundException("BusinessException in testExceptionHandler");
        }
        return true;
    }

}

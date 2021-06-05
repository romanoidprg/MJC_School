package com.epam.esm.web;

import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping(value = "/v1/tags")
public class TagsController {
    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public boolean createTag() {
        //todo: must return result of creating
        return true;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> readAllTags() {
        //todo: must return result of creating
        List<Tag> tagList = new ArrayList<>();
        tagList.add(new Tag(12, "12 tag"));
        tagList.add(new Tag(13, "13 tag"));
        tagList.add(new Tag(14, "14 tag"));
        tagList.add(new Tag(15, "15 tag"));
        return tagList;
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Tag readTagWithId(@PathVariable String id) {
        //todo: must return certificate with this id
        return new Tag(Long.parseLong(id), id + " Tag");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public boolean deleteTag(@PathVariable String id) {
        //todo: must return result of deleting
        return true;
    }


}

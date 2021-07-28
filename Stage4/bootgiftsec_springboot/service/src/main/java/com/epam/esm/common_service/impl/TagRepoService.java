package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.dao.TagRepo;
import com.epam.esm.dao.UserRepo;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchTagIdException;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TagRepoService implements CommonService<Tag>, CustomTagServise<Tag> {

    private final Logger logger = LogManager.getLogger(TagRepoService.class);

    @Autowired
    private TagRepo tagRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public long createFromJson(String jsonString) throws EntityAlreadyExistException, JsonProcessingException {
        long id;
        ObjectMapper objectMapper = new ObjectMapper();
        Tag tag = objectMapper.readValue(jsonString, Tag.class);
        tag.setCertificates(new HashSet<>());
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name")
                .withIgnorePaths("id", "certificates")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        if (!tagRepo.exists(Example.of(tag, em))) {
            id = tagRepo.save(tag).getId();
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }


    @Override
    public Tag readById(Long id) throws LocalAppException {
        Optional<Tag> tag = tagRepo.findById(id);
        if (!tag.isPresent()) {
            throw new NoSuchTagIdException(String.valueOf(id));
        }
        return tag.get();
    }

    @Override
    public Page<Tag> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        Page<Tag> page = Page.empty(pageable);
        if (params.length > 0) {
            Tag tag = new Tag();
            tag.setName(params[0]);
            ExampleMatcher em = ExampleMatcher.matchingAll()
                    .withIgnoreCase("name")
                    .withIgnorePaths("id", "certificates")
                    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
            page = tagRepo.findAll(Example.of(tag, em), pageable);
        }
        return page;
    }

    @Override
    public boolean updateField(Long id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(Long id) throws LocalAppException {
        tagRepo.deleteById(id);
    }

    @Override
    public List<Tag> getMostUsedTagsOfUserWithMostExpensiveOrdersCost() {
        List<Tag> tags = new ArrayList<>();
        List<Long> tagIds = tagRepo.getIdOfMostUsefullTagByUserId(
                userRepo.getIdOfUserWhichHaveHigestCostOfOrders());
        for (Long id : tagIds) {
            if (id != null) {
                tags.add(tagRepo.findById(id).get());
            }
        }
        return tags;
    }
}

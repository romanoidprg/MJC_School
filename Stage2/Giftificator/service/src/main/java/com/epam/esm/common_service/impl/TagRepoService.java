package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.DaoFactory;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public class TagRepoService implements CommonService<Tag> {

    private final Logger logger = LogManager.getLogger(TagRepoService.class);

    @Autowired
    @Qualifier("tagDao")
    private CommonDao<Tag, TagCriteria> tagDao;

    @Override
    public boolean createFromJson(String jsonString) throws JsonProcessingException{
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
        Tag tag;
        try {
            tag = objectMapper.readValue(jsonString, Tag.class);
            result = tagDao.create(tag);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return result;
    }

    @Override
    public Tag readById(String id) throws NoSuchIdException {
        if (id.matches("[0-9]+")) {
            return tagDao.readById(Long.parseLong(id));
        } else {
            throw new NoSuchIdException("The gift certificate with id ["+id+"] doesn't exist.");
        }
    }

    @Override
    public List<Tag> readByCriteria(String... params) {
        List<Tag> tagList = new ArrayList<>();
        if (params.length > 2) {
            String name = params[0];
            String sortByName = params[1];
            String sortOrder = params[2];

            tagList = tagDao.readByCriteria(
                    new TagCriteria(name, Boolean.parseBoolean(sortByName), sortOrder));
        }
        return tagList;
    }

    @Override
    public boolean updateFromJson(String jsonString) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        boolean result = false;
        try {
            result = tagDao.deleteById(Long.parseLong(id));
        } catch (NumberFormatException e) {
            logger.error(e.getMessage());
        }
        return  result;
    }
}

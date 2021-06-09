package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.cpool.ConnectionPool;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.DaoFactory;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class TagRepoService implements CommonService<Tag> {

    private ConnectionPool connectionPool = ConnectionPool.REAL_DB;

    private CommonDao<Tag, TagCriteria> tagDao = DaoFactory.getTagDao(connectionPool);

    public void setDataBaseToReal(){
        connectionPool = ConnectionPool.REAL_DB;
        tagDao = DaoFactory.getTagDao(connectionPool);
    }

    public void setDataBaseToEmbedded(){
        connectionPool = ConnectionPool.IN_MEMORY_DB;
        tagDao = DaoFactory.getTagDao(connectionPool);
    }

    @Override
    public boolean createFromJson(String jsonString) {
        boolean result = false;
        ObjectMapper objectMapper = new ObjectMapper();
        Tag tag;
        try {
            tag = objectMapper.readValue(jsonString, Tag.class);
            result = tagDao.create(tag);
        } catch (JsonProcessingException e) {
            //todo: logging error
            e.getMessage();
        }
        return result;
    }

    @Override
    public Tag readById(String id) {
        return (id.matches("[0-9]+")) ? tagDao.readById(Long.parseLong(id)) : null;
    }

    @Override
    public List<Tag> readByCriteria(String... params) {
        String name = params[0];
        String sortByName = params[1];
        String sortOrder = params[2];

        return tagDao.readByCriteria(
                new TagCriteria(name, Boolean.parseBoolean(sortByName), sortOrder));
    }

    @Override
    public boolean updateFromJson(String jsonString) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return tagDao.deleteById(Long.parseLong(id));
    }
}

package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class TagRepoService implements CommonService<Tag> {

    private static final String WORDS_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\words.txt";

    private final Logger logger = LogManager.getLogger(TagRepoService.class);

    @Autowired
    @Qualifier("tagDao")
    private CommonDao<Tag, TagCriteria> tagDao;

    @Override
    public Long createFromJson(String jsonString) throws JsonProcessingException {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Tag tag = objectMapper.readValue(jsonString, Tag.class);
            id = tagDao.create(tag);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            throw e;
        }
        return id;
    }

    @Override
    public Tag readById(String id) throws NoSuchIdException {
        if (id.matches("[0-9]+")) {
            return tagDao.readById(Long.parseLong(id));
        } else {
            NoSuchIdException e = new NoSuchIdException("The gift certificate with id [" + id + "] doesn't exist.");
            logger.error(e.getMessage());
            throw e;
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
        return result;
    }

    @Override
    public boolean fillTable() {
        boolean result = false;
        try {
            File file = new File(WORDS_FILE);
            Tag tag = new Tag();
            int size = getWordsAmount(file);
            for (int i = 0; i < 1000; i++) {
                tag.setName(getRandomWord(file, size));
                tagDao.create(tag);
            }
            result = true;
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        return result;
    }


}

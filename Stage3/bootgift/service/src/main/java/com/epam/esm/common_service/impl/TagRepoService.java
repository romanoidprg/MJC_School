package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomTagServise;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomTagDao;
import com.epam.esm.dao.impl.TagDao;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchTagIdException;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Pageable;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class TagRepoService implements CommonService<Tag>, CustomTagServise<Tag> {

    private static final String WORDS_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\words.txt";

    private final Logger logger = LogManager.getLogger(TagRepoService.class);

    @Autowired
    @Qualifier("tagDao")
    private CommonDao<Tag, TagCriteria> tagDao;

    @Autowired
    @Qualifier("customTagDao")
    private CustomTagDao<Tag> customTagDao;

    @Override
    public long createFromJson(String jsonString) throws EntityAlreadyExistException, JsonProcessingException {
        long id;
        ObjectMapper objectMapper = new ObjectMapper();
        Tag tag = objectMapper.readValue(jsonString, Tag.class);
        tag.setCertificates(null);
        if (!tagDao.isExist(tag)) {
            id = tagDao.create(tag);
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    @Override
    public Long create(String... params) throws LocalAppException {
        return null;
    }

    @Override
    public Tag readById(String id) throws LocalAppException {
        Tag result;
        if (id.matches("[0-9]+")) {
            result = tagDao.readById(Long.parseLong(id));
            if (result == null) {
                throw new NoSuchTagIdException(id);
            }
        } else {
            throw new NoSuchTagIdException(id);
        }
        result.getCertificates().forEach(c -> c.setTags(null));
        return result;
    }

    @Override
    public List<Tag> readByCriteria(Pageable pageable, String... params) {
        List<Tag> tagList = new ArrayList<>();
        if (params.length > 2) {
            String name = params[0];
            String sortByName = params[1];
            String sortOrder = params[2];
//            if (Arrays.stream(params).filter(p->p!=null).findAny().orElse(null)!=null) {
                tagList = tagDao.readByCriteria(pageable,
                        new TagCriteria(name, Boolean.parseBoolean(sortByName), sortOrder));
//            } else {
//                tagList = tagDao.readAll();
//            }
        }
        return tagList;
    }

    @Override
    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
        return false;
    }

    @Override
    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(String id) throws LocalAppException {
        Tag t = readById(id);
        tagDao.delete(t);
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

    @Override
    public List<Tag> getMostUsedTagsOfUserWithMostExpensiveOrdersCost() {
        return customTagDao.getWutuhco();
    }


}

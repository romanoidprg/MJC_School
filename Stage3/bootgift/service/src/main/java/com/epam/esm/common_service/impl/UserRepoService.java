package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.impl.UserDao;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.epam.esm.model.User;
import com.epam.esm.model.UserCriteria;
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


public class UserRepoService implements CommonService<User> {

    private static final String WORDS_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\words.txt";
    private final Logger logger = LogManager.getLogger(UserRepoService.class);

    @Autowired
    @Qualifier("userDao")
    private CommonDao<User, UserCriteria> userDao;

    @Override
    public Long createFromJson(String jsonString) throws JsonProcessingException, EntityAlreadyExistException {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        if (!userDao.isExist(user)) {
            user.setId(null);
            id = userDao.create(user);
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    @Override
    public User readById(String id) throws NoSuchIdException {
        return null;
    }

    @Override
    public List<User> readByCriteria(String... params) {
        return null;
    }

    @Override
    public boolean updateFromJson(String jsonString) {
        return false;
    }

    @Override
    public boolean deleteById(String id) {
        return false;
    }

    @Override
    public boolean fillTable() {
        return false;
    }
}

package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.*;
import com.epam.esm.model.Tag;
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
import java.util.List;


public class UserRepoService implements CommonService<User> {

    private static final String NAMES_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\names.txt";
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
    public Long create(String... params) throws LocalAppException {
        return null;
    }


    @Override
    public User readById(String id) throws LocalAppException {
        User result;
        if (id.matches("[0-9]+")) {
            result = userDao.readById(Long.parseLong(id));
            if (result == null) {
                throw new NoSuchUserIdException(id);
            }
        } else {
            throw new NoSuchUserIdException(id);
        }
//        result.getCertificates().forEach(c -> c.setTags(null));
        return result;
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
        boolean result = false;
        try {
            File file = new File(NAMES_FILE);
            User user = new User();
            int size = getWordsAmount(file);
            for (int i = 0; i < 1000; i++) {
                user.setName(getRandomWord(file, size));
                if (!userDao.isExist(user)) {
                    userDao.create(user);
                } else {
                    i--;
                }
            }
            result = true;
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage());
        }
        return result;
    }

}

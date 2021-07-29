package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.dao.UserRepo;
import com.epam.esm.errors.*;
import com.epam.esm.model.Authority;
import com.epam.esm.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class UserRepoService implements CommonService<User>, CustomUserService {

    private static final String NAMES_FILE = "D:\\JWD\\Lab\\Stage3\\bootgift\\web\\src\\main\\resources\\names.txt";
    private final Logger logger = LogManager.getLogger(UserRepoService.class);

    @Autowired
    private UserRepo userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public long createFromJson(String jsonString) throws Exception {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonString, User.class);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        Set<Authority> authoritySet = new HashSet<>();
        authoritySet.add(new Authority("user"));
        user.setAuthorities(authoritySet);
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name")
                .withIgnorePaths("password", "enabled", "authorities")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        if (!userRepo.exists(Example.of(user, em))) {
            id = userRepo.save(user).getId();
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    @Override
    public User readById(Long id) throws LocalAppException {
        return null;
    }

    @Override
    public Page<User> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        return null;
    }

    @Override
    public boolean updateField(Long id, Map<String, String> params) throws LocalAppException {
        return false;
    }

    @Override
    public void deleteById(Long id) throws LocalAppException {

    }

    @Override
    public User readByName(String name) {
        return userRepo.findByNameIgnoreCase(name);
    }
//
//    @Autowired
//    CustomUserDao customUserDao;


}
//    @Override
//    public Long create(String... params) throws LocalAppException {
//        return null;
//    }
//
//
//    @Override
//    public User readById(String id) throws LocalAppException {
//        User result;
//        if (id.matches("[0-9]+")) {
//            result = userDao.readById(Long.parseLong(id));
//            if (result == null) {
//                throw new NoSuchUserIdException(id);
//            }
//        } else {
//            throw new NoSuchUserIdException(id);
//        }
//        return result;
//    }
//
//    @Override
//    public List<User> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
//        UserCriteria criteria = new UserCriteria();
//        return userDao.readByCriteria(pageable, criteria);
//    }
//
//    @Override
//    public boolean updateFromJson(String id, String jsonString) throws LocalAppException {
//        return false;
//    }
//
//    @Override
//    public boolean updateField(String id, Map<String, String> params) throws LocalAppException {
//        return false;
//    }
//
//    @Override
//    public void deleteById(String id) throws LocalAppException {
//        userDao.delete(readById(id));
//    }
//
//    @Override
//    public Long getLastQueryCount() {
//        return userDao.getLastQueryCount();
//    }
//
//    @Override
//    public boolean fillTable() {
//        boolean result = false;
//        try {
//            File file = new File(NAMES_FILE);
//            User user = new User();
//            int size = getWordsAmount(file);
//            for (int i = 0; i < 1000; i++) {
//                user.setName(getRandomWord(file, size));
//                if (!userDao.isExist(user)) {
//                    userDao.create(user);
//                } else {
//                    i--;
//                }
//            }
//            result = true;
//        } catch (FileNotFoundException e) {
//            logger.error(e.getMessage());
//        }
//        return result;
//    }
//
//    @Override
//    public User readByName(String name) {
//        return customUserDao.readByName(name);
//    }
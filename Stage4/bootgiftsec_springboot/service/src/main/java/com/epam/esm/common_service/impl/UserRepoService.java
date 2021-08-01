package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.dao.UserRepo;
import com.epam.esm.errors.EntityAlreadyExistException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchUserIdException;
import com.epam.esm.model.Authority;
import com.epam.esm.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
public class UserRepoService implements CommonService<User>, CustomUserService {

    @Autowired
    UserRepo userRepo;

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
        User result;
        result = userRepo.findById(id).orElse(null);
        if (result == null) {
            throw new NoSuchUserIdException(String.valueOf(id));
        }
        return result;
    }

    @Override
    public Page<User> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        String userName = params.length == 0 ?
                "" :
                params[0] == null ? "" : params[0];
        return userRepo.findByNameContainingIgnoreCase(userName, pageable);
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
}

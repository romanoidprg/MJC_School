package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.TestConfig;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchUserIdException;
import com.epam.esm.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@SpringBootTest
class UserRepoServiceTest {

    @Autowired
    @Qualifier("userRepoService")
    CommonService<User> userRepoService;

    private final String jsonStringTag = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"User\"\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void createFromJson() throws Exception {
        assertThrows(JsonProcessingException.class, () -> userRepoService.createFromJson(notJsonString));
        assertEquals(1L, userRepoService.createFromJson(jsonStringTag));
    }

    @Test
    void readById() throws LocalAppException {
        assertThrows(NoSuchUserIdException.class, () -> userRepoService.readById("aa"));
        assertEquals(new User(), userRepoService.readById("1"));
    }


}
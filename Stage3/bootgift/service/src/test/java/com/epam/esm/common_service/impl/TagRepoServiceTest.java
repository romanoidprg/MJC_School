package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.TestConfig;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchTagIdException;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;


@Import(TestConfig.class)
@SpringBootTest
class TagRepoServiceTest {

    @Autowired
    @Qualifier("tagRepoService")
    CommonService<Tag> tagRepoService;

    private final String jsonStringTag = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"Tag\"\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void createFromJson() throws Exception {
        assertThrows(JsonProcessingException.class, () -> tagRepoService.createFromJson(notJsonString));
        assertEquals(1L, tagRepoService.createFromJson(jsonStringTag));
    }

    @Test
    void readById() throws LocalAppException {
        assertThrows(NoSuchTagIdException.class, () -> tagRepoService.readById("aa"));
        assertEquals(new Tag(), tagRepoService.readById("1"));
    }

    @Test
    void readByCriteria() throws LocalAppException {
        assertEquals(0, tagRepoService.readByCriteria(Pageable.unpaged(), "", "").size());
        assertEquals(2L, tagRepoService.readByCriteria(Pageable.unpaged(), "", "","").size());
    }
}
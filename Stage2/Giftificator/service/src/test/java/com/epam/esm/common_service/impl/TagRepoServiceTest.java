package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.config.TestConfig;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
class TagRepoServiceTest {

    @Autowired
    @Qualifier("tagRepoService")
    CommonService<Tag> tagRepoService;

    private final String jsonStringTag = "{\n" +
            "    \"id\": 1,\n" +
            "    \"name\": \"Aurora\"\n" +
            "}";
    private final String jsonStringNotTag = "{\n" +
            "    \"family\": 1,\n" +
            "    \"author\": \"Aurora\"\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void WhenPassJsonStringThenSuccess() {
        assertTrue(tagRepoService.createFromJson(jsonStringTag));
        assertFalse(tagRepoService.createFromJson(jsonStringNotTag));
        assertFalse(tagRepoService.createFromJson(notJsonString));
    }

    @Test
    void whenPassNumericStringThenSuccess() {
        assertEquals(new Tag(), tagRepoService.readById("12"));
        assertNull(tagRepoService.readById("qwerty"));
    }

    @Test
    void whenPassRequiredAmountOfStringParamsThenSuccess() {
        List<Tag> listTag = Arrays.asList(new Tag(), new Tag());
        assertEquals(listTag, tagRepoService.readByCriteria("1", "2", "3"));
        assertEquals(new ArrayList<>(), tagRepoService.readByCriteria("1"));
    }

    @Test
    void whenPassIdStringThenSuccess() {
        assertTrue(tagRepoService.deleteById("12"));
        assertFalse(tagRepoService.deleteById("sdf"));
    }
}
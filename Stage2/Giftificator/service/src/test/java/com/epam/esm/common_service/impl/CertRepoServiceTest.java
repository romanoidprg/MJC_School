package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.config.TestConfig;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
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
class CertRepoServiceTest {

    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

    private final String jsonStringCert = "{\n" +
            "    \"id\": 1,\n" +
            "    \"description\": \"This is Black2 Cert\",\n" +
            "    \"price\": 3229,\n" +
            "    \"duration\": 430,\n" +
            "    \"createDate\": \"2021-05-05T18:32:22.597\",\n" +
            "    \"lastUpdateDate\": \"2021-05-05T18:32:22.597\",\n" +
            "    \"tags\": [\n" +
            "        {\n" +
            "            \"name\": \"Cinema\"\n" +
            "        },\n" +
            "         {\n" +
            "            \"name\": \"Men\"\n" +
            "        }\n" +
            "    ]\n" +
            "}";
    private  final String jsonStringNoCert = "{\n" +
            "    \"field\": 1,\n" +
            "    \"field2\": \"This is Black2 Cert\",\n" +
            "    \"field3\": 3229,\n" +
            "    \"field4\": 430\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void WhenPassJsonStringThenSuccess()  {
        try {
            assertTrue(certRepoService.createFromJson(jsonStringCert));
            assertFalse(certRepoService.createFromJson(jsonStringNoCert));
            assertFalse(certRepoService.createFromJson(notJsonString));
        } catch (JsonProcessingException e) {
        }

    }

    @Test
    void whenPassNumericStringThenSuccess() {
        try {
            assertEquals(new GiftCertificate(), certRepoService.readById("12"));
            assertNull(certRepoService.readById("qwerty"));
        } catch (NoSuchIdException e) {
        }
    }

    @Test
    void whenPassRequiredAmountOfStringParamsThenSuccess() {
        List<GiftCertificate> listCert = Arrays.asList(new GiftCertificate(), new GiftCertificate());
        assertEquals(listCert, certRepoService.readByCriteria("1", "2","3","4","5","6","7","8","9"));
        assertEquals(new ArrayList<>(), certRepoService.readByCriteria("1"));
    }

    @Test
    void whenPassJsonStringThenSuccessUpdate() {
        assertTrue(certRepoService.updateFromJson(jsonStringCert));
        assertFalse(certRepoService.updateFromJson(jsonStringNoCert));
        assertFalse(certRepoService.updateFromJson(notJsonString));
    }

    @Test
    void whenPassIdStringThenSuccess() {
        assertTrue(certRepoService.deleteById("12"));
        assertFalse(certRepoService.deleteById("sdf"));
    }


}
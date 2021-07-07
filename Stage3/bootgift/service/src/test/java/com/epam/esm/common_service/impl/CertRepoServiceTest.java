package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.TestConfig;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchCertIdException;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@SpringBootTest
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
    void WhenPassJsonStringThen_CreateFromJson_Success() throws Exception {
        assertEquals(1L, certRepoService.createFromJson(jsonStringCert));
        assertThrows(JsonProcessingException.class,() -> certRepoService.createFromJson(jsonStringNoCert));
        assertThrows(JsonProcessingException.class,() -> certRepoService.createFromJson(notJsonString));

    }



    @Test
    void readById() throws LocalAppException {
        assertEquals(new GiftCertificate(), certRepoService.readById("1"));
        assertThrows(NoSuchCertIdException.class, () -> certRepoService.readById("aa"));
    }

    @Test
    void readByCriteria() {
    }

    @Test
    void updateField() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void getLastQueryCount() {
    }

    @Test
    void readCertsWithTags() {
    }
}
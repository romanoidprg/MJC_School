package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.config.TestConfig;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfig.class, loader = AnnotationConfigContextLoader.class)
class CertRepoServiceTest {

    @Autowired
    @Qualifier("certRepoService")
    CommonService<GiftCertificate> certRepoService;

    @Test
    void successIf_createFromJson_GetJsonString() {
        String jsonStringCert = "{\n" +
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
        String jsonStringNoCert = "{\n" +
                "    \"field\": 1,\n" +
                "    \"field2\": \"This is Black2 Cert\",\n" +
                "    \"field3\": 3229,\n" +
                "    \"field4\": 430\n" +
                "}";
        String notJsonString = "qwerty";

        assertTrue(certRepoService.createFromJson(jsonStringCert));
        assertFalse(certRepoService.createFromJson(jsonStringNoCert));
        assertFalse(certRepoService.createFromJson(notJsonString));

    }

    @Test
    void readById() {
    }

    @Test
    void readByCriteria() {
    }

    @Test
    void updateFromJson() {
    }

    @Test
    void deleteById() {
    }


}
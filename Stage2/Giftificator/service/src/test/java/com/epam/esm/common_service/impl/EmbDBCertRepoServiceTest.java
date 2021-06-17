package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.config.EmbededDbTestConfig;
import com.epam.esm.common_service.config.TestConfig;
import com.epam.esm.errors.NoSuchIdException;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = EmbededDbTestConfig.class, loader = AnnotationConfigContextLoader.class)
class EmbDBCertRepoServiceTest {

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
    private final String jsonStringNoCert = "{\n" +
            "    \"field\": 1,\n" +
            "    \"field2\": \"This is Black2 Cert\",\n" +
            "    \"field3\": 3229,\n" +
            "    \"field4\": 430\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void WhenPassJsonStringThenSuccess() {
        try {
            certRepoService.createFromJson(jsonStringCert);
            certRepoService.createFromJson(jsonStringCert);
            GiftCertificate cert1 = certRepoService.readByCriteria("", "", "", "", "", "", "", "", "").get(0);
            GiftCertificate cert2 = certRepoService.readByCriteria("", "", "", "", "", "", "", "", "").get(1);
            assertEquals("This is Black2 Cert", cert1.getDescription());
            assertEquals("This is Black2 Cert", cert2.getDescription());
            certRepoService.deleteById(String.valueOf(cert1.getId()));
            certRepoService.deleteById(String.valueOf(cert2.getId()));
            assertEquals(0, certRepoService.readByCriteria("", "", "", "", "", "", "", "", "").size());

        } catch (JsonProcessingException e) {
            Assertions.fail();
        }
    }

}
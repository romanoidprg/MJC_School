package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.TestConfig;
import com.epam.esm.errors.IncorrectAmountOfCertFieldsException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchCertIdException;
import com.epam.esm.model.GiftCertificate;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.Map;

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
    private final String jsonStringNoCert = "{\n" +
            "    \"field\": 1,\n" +
            "    \"field2\": \"This is Black2 Cert\",\n" +
            "    \"field3\": 3229,\n" +
            "    \"field4\": 430\n" +
            "}";
    private final String notJsonString = "qwerty";

    @Test
    void WhenPassJsonStringThen_CreateFromJson_Success() throws Exception {
        assertEquals(1L, certRepoService.createFromJson(jsonStringCert));
        assertThrows(JsonProcessingException.class, () -> certRepoService.createFromJson(jsonStringNoCert));
        assertThrows(JsonProcessingException.class, () -> certRepoService.createFromJson(notJsonString));

    }

    @Test
    void WhenPassJsonStringThen_UpdateFromJson_Success() throws Exception {
        assertTrue(certRepoService.updateFromJson("1", jsonStringCert));
        assertThrows(NoSuchCertIdException.class, () -> certRepoService.updateFromJson("aa", jsonStringCert));
        assertFalse(certRepoService.updateFromJson("1", jsonStringNoCert));
        assertThrows(NoSuchCertIdException.class, () -> certRepoService.updateFromJson("aa", jsonStringNoCert));

    }

    @Test
    void WhenPassRCorrectIdThen_readById_Success() throws LocalAppException {
        assertEquals(new GiftCertificate(), certRepoService.readById("1"));
        assertThrows(NoSuchCertIdException.class, () -> certRepoService.readById("aa"));
    }

    @Test
    void readByCriteria() throws LocalAppException {
        String[] params = {"", "", "", "", "", "", "", "", ""};
        assertEquals(2, certRepoService.readByCriteria(Pageable.unpaged(), params).size());
        assertEquals(0, certRepoService.readByCriteria(Pageable.unpaged(), "").size());
    }

    @Test
    void updateField() {
        Map<String, String> map = new HashMap<>();
        assertThrows(NoSuchCertIdException.class, () -> certRepoService.updateField("aa", new HashMap<>()));
        assertThrows(IncorrectAmountOfCertFieldsException.class, () -> certRepoService.updateField("1", map));
        map.put("duration", "aa");
        assertThrows(NumberFormatException.class, () -> certRepoService.updateField("1", map));
        map.clear();
        map.put("price", "aa");
        assertThrows(NumberFormatException.class, () -> certRepoService.updateField("1", map));
    }

    @Test
    void readById() {
        assertThrows(NoSuchCertIdException.class,() -> certRepoService.readById("aa"));
    }
}
package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.TestConfig;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.errors.NoSuchCertIdException;
import com.epam.esm.errors.NoSuchOrderIdException;
import com.epam.esm.errors.NoSuchUserIdException;
import com.epam.esm.model.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.*;

@Import(TestConfig.class)
@SpringBootTest
class OrderRepoServiceTest {

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

    @Test
    void create() throws LocalAppException {
        assertThrows(NoSuchCertIdException.class, () -> orderRepoService.create("1", "aa"));
        assertThrows(NoSuchUserIdException.class, () -> orderRepoService.create("aa", "1"));
        assertEquals(1L, orderRepoService.create("1", "1"));
    }

    @Test
    void readById() throws LocalAppException {
        assertThrows(NoSuchOrderIdException.class, () -> orderRepoService.readById("aa"));
        assertEquals(1, orderRepoService.readById("1").getCert().getTags().size());

    }

    @Test
    void readByCriteria() throws LocalAppException {
        assertThrows(NoSuchUserIdException.class, () -> orderRepoService.readByCriteria(Pageable.unpaged(), "aa"));
        assertEquals(2, orderRepoService.readByCriteria(Pageable.unpaged(), "1").size());
    }

}
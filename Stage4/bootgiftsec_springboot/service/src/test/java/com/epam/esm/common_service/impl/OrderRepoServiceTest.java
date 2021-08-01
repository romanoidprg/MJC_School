package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomOrderService;
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
    CustomOrderService orderRepoService;
    @Autowired
    CommonService<Order> commonOrderRepoService;

    @Test
    void create() throws Exception {
        assertThrows(NoSuchCertIdException.class, () -> orderRepoService.createFromUserIdAndCertId(1L, 82937429374L));
        assertThrows(NoSuchUserIdException.class, () -> orderRepoService.createFromUserIdAndCertId(89237423423L, 1L));
        assertEquals(1L, orderRepoService.createFromUserIdAndCertId(1L, 1L));
    }

    @Test
    void readById() throws LocalAppException {
        assertThrows(NoSuchOrderIdException.class, () -> commonOrderRepoService.readById(2389423894L));
        assertEquals(1, commonOrderRepoService.readById(1L).getCert().getTags().size());

    }

    @Test
    void readByCriteria() throws LocalAppException {
        assertThrows(NoSuchUserIdException.class, () -> commonOrderRepoService.readByCriteria(Pageable.unpaged(), "aa"));
        assertEquals(2, commonOrderRepoService.readByCriteria(Pageable.unpaged(), "1").getContent().size());
    }

}
package com.epam.esm.common_service;

import com.epam.esm.common_service.impl.CertRepoService;
import com.epam.esm.common_service.impl.OrderRepoService;
import com.epam.esm.common_service.impl.TagRepoService;
import com.epam.esm.common_service.impl.UserRepoService;
import com.epam.esm.dao.CertRepo;
import com.epam.esm.dao.CommonDao;
import com.epam.esm.dao.CustomCertDao;
import com.epam.esm.dao.CustomTagDao;
import com.epam.esm.dao.OrderRepo;
import com.epam.esm.dao.TagRepo;
import com.epam.esm.dao.UserRepo;
import com.epam.esm.model.CertCriteria;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.Order;
import com.epam.esm.model.OrderCriteria;
import com.epam.esm.model.Tag;
import com.epam.esm.model.TagCriteria;
import com.epam.esm.model.User;
import com.epam.esm.model.UserCriteria;
import org.aspectj.weaver.ast.Or;
import org.hibernate.SessionFactory;
import org.hibernate.internal.SessionFactoryImpl;
import org.mockito.Mockito;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.jws.soap.SOAPBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;


@SpringBootConfiguration
public class TestConfig {

//    @Bean
//    public SessionFactory sessionFactory() {
//        SessionFactory sf = Mockito.mock(SessionFactoryImpl.class);
//        return sf;
//    }

    @Bean
    public CommonService<GiftCertificate> certRepoService() {
        return new CertRepoService();
    }

    @Bean
    public CustomCertService customCertRepoService() {
        return new CertRepoService();
    }

    @Bean
    public CommonService<User> userRepoService() {
        return new UserRepoService();
    }

    @Bean
    public CommonService<Order> orderRepoService() {
        return new OrderRepoService();
    }

    @Bean
    public CommonService<Tag> tagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CustomTagService customTagRepoService() {
        return new TagRepoService();
    }

    @Bean
    public CertRepo certRepo() {
        GiftCertificate cert = new GiftCertificate();
        GiftCertificate cert1 = new GiftCertificate();
        cert.setId(1L);
        cert1.setId(1L);
        CertRepo mockCertRepo = Mockito.mock(CertRepo.class);
        Mockito.when(mockCertRepo.save(any())).thenReturn(cert);
        Mockito.when(mockCertRepo.exists(any())).thenReturn(false);
        Mockito.when(mockCertRepo.findById(anyLong())).thenReturn(Optional.of(cert));
        Mockito.when(mockCertRepo.findByNameContainingAndDescriptionContainingAndTagsNameContaining(any(), any(), any(), any()))
                .thenReturn(new PageImpl<>(Arrays.asList(cert, cert1)));
        return mockCertRepo;

    }


    @Bean
    public TagRepo tagRepo() {
        Tag tag = new Tag();
        tag.setId(1L);
        Tag tag1 = new Tag();
        tag1.setId(1L);
        TagRepo mockTagRepo = Mockito.mock(TagRepo.class);
        Mockito.when(mockTagRepo.save(any())).thenReturn(tag);
        Mockito.when(mockTagRepo.exists(any())).thenReturn(false);
        Mockito.when(mockTagRepo.findById(anyLong())).thenReturn(Optional.of(new Tag()));
        Mockito.when(mockTagRepo.findAll(any(),any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(tag,tag1)));
        Mockito.when(mockTagRepo.getIdOfMostUsefullTagByUserId(anyLong())).thenReturn(Arrays.asList(1L,2L));
        return mockTagRepo;
    }

    @Bean
    public OrderRepo orderRepo() {
        Order order = new Order();
        Order order1 = new Order();
        order.setId(1L);
        order1.setId(1L);
        OrderRepo mockOrderRepo = Mockito.mock(OrderRepo.class);
        Mockito.when(mockOrderRepo.save(any())).thenReturn(order);
        Mockito.when(mockOrderRepo.exists(any())).thenReturn(false);
        Mockito.when(mockOrderRepo.findById(anyLong())).thenReturn(Optional.of(new Order()));
        Mockito.when(mockOrderRepo.findAll(any(),any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(order,order1)));

        return mockOrderRepo;
    }

    @Bean
    public UserRepo userRepo () {
        User user = new User();
        User user1 = new User();
        user.setId(1L);
        user1.setId(1L);
        UserRepo mockUserRepo = Mockito.mock(UserRepo.class);
        Mockito.when(mockUserRepo.save(any())).thenReturn(user);
        Mockito.when(mockUserRepo.exists(any())).thenReturn(false);
        Mockito.when(mockUserRepo.findById(anyLong())).thenReturn(Optional.of(new User()));
        Mockito.when(mockUserRepo.findAll(any(),any(Pageable.class)))
                .thenReturn(new PageImpl<>(Arrays.asList(user,user1)));

        return mockUserRepo;
    }

}
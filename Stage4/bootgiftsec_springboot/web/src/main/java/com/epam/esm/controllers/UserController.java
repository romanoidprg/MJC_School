package com.epam.esm.controllers;

import com.epam.esm.Errors.NoPermissionsForResourceException;
import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.Errors.UnknownUserOrWrongPasswordException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomOrderService;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.dao.CustomUserDao;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.Authority;
import com.epam.esm.model.IdWrapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.utils.JwtTokenUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.CommonWeb.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/users")
public class UserController {

    private static final String REL_LOGIN = "Log_in";
    private static final String REL_SIGNIN = "Sign_in";
    private static final String MSG_LOGOUT = "Logouted";
    @Autowired
    @Qualifier("userRepoService")
    CommonService<User> userRepoService;

    @Autowired
    @Qualifier("orderRepoService")
    CustomOrderService customOrderService;

    @Autowired
    CustomUserService customUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody String jsonUser) throws Exception {
        try {
            User reqwestUser = (new ObjectMapper()).readValue(jsonUser, User.class);
            User user = customUserService.readByName(reqwestUser.getName());

            if (user != null && bCryptPasswordEncoder.matches(reqwestUser.getPassword(), user.getPassword())) {
                String swt = jwtTokenUtil.generateAccessToken(user);

                return ResponseEntity.ok()
                        .header(
                                HttpHeaders.AUTHORIZATION,
                                swt
                        )
                        .body(swt);
            } else {
                throw new UnknownUserOrWrongPasswordException();
            }
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping(value = "/signup")
    public EntityModel<IdWrapper> createUser(@RequestBody String jsonString) throws Exception {
        Long userId = userRepoService.createFromJson(jsonString);
        return EntityModel.of(IdWrapper.of(userId),
                linkTo(methodOn(UserController.class).createUser(jsonString)).withSelfRel());
    }


    @PostMapping(value = "/{userId}/order/for/certificate/{certId}")
    public EntityModel<IdWrapper> createOrder(@PathVariable Long userId, @PathVariable Long certId) throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = customUserService.readByName(userName);
        if (!userId.equals(user.getId())) {
            throw new NoPermissionsForResourceException();
        }
        Long orderId = customOrderService.createFromUserIdAndCertId(userId, certId);
        return EntityModel.of(IdWrapper.of(orderId),
                linkTo(methodOn(UserController.class).createOrder(userId, certId)).withSelfRel(),
                linkTo(methodOn(UserController.class).readUserById(userId)).withRel(REL_READ_USER_BY_ID),
                linkTo(methodOn(CertificatesController.class).readCertificateById(certId)).withRel(REL_READ_CERT_BY_ID),
                linkTo(methodOn(OrderController.class).readOrderById(orderId)).withRel(REL_READ_ORDER_BY_ID)
        );
    }


    @GetMapping(value = "/{id}")
    public EntityModel<User> readUserById(@PathVariable Long id) throws Exception {
        return EntityModel.of(userRepoService.readById(id),
                linkTo(methodOn(UserController.class).readUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).deleteUser(id)).withRel(REL_DEL_USER_BY_ID)
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) throws LocalAppException {
        userRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping
    public CollectionModel<EntityModel<User>> readAllUsers(
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder) throws Exception {

        Pageable pageable = getPagebale(page, pSize, getSort(sortBy, sortOrder));
        List<User> userList = userRepoService.readByCriteria(pageable).toList();
        List<EntityModel<User>> entUsers = new ArrayList<>();
        Long totalCount = 0L;//userRepoService.getLastQueryCount();
        PageImpl<EntityModel<User>> pager = new PageImpl(entUsers, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entUsers = new ArrayList<>();
        for (User u : userList) {
            entUsers.add(EntityModel.of(u,
                    linkTo(methodOn(UserController.class).readUserById(u.getId())).withRel(REL_READ_USER_BY_ID),
                    linkTo(methodOn(UserController.class).deleteUser(u.getId())).withRel(REL_DEL_USER_BY_ID))
            );
        }
        pager = new PageImpl(entUsers, pageable, totalCount);
        CollectionModel<EntityModel<User>> collEntUsers = CollectionModel.of(pager,
                linkTo(methodOn(UserController.class).readAllUsers(page, pSize, sortBy, sortOrder)).withSelfRel().expand()
        );
        collEntUsers.add(getLinksPagination(pageable, pager));

        return collEntUsers;
    }


    private List<Link> getLinksPagination(Pageable pageable, PageImpl<EntityModel<User>> pager) throws Exception {
        List<Link> links = new ArrayList<>();
        String sortBy = null;
        String sortOrder = null;
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            sortBy = sort.stream().findFirst().get().getProperty();
            sortOrder = sort.stream().findFirst().get().getDirection().name();
        }
        if (pageable.getPageNumber() != 0) {
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    (pageable.first().getPageNumber()),
                    (pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    (pageable.previousOrFirst().getPageNumber()),
                    (pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    (pageable.next().getPageNumber()),
                    (pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    (pager.getTotalPages() - 1),
                    (pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

}
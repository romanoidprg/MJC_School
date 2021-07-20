package com.epam.esm.controllers;

import com.epam.esm.Errors.NoPermissionsForResourceException;
import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.dao.CustomUserDao;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.IdWrapper;
import com.epam.esm.model.Order;
import com.epam.esm.model.User;
import com.epam.esm.utils.JwtTokenUtil;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @Autowired
    @Qualifier("userRepoService")
    CommonService<User> userRepoService;

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

    @Autowired
    CustomUserService customUserService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @PostMapping(value = "login")
    public ResponseEntity<String> login(@RequestBody User user) {
        try {
            org.springframework.security.core.userdetails.User userUD
                    = new org.springframework.security.core.userdetails.User(user.getName(),
                    user.getPassword(),
                    user.isEnabled(),
                    true,
                    true,
                    true,
                    user.getAuthorities());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(userUD)
                    )
                    .body(userUD.getUsername());
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @PostMapping
    public EntityModel<IdWrapper> createUser(@RequestBody String jsonString) throws Exception {
        Long userId = userRepoService.createFromJson(jsonString);
        return EntityModel.of(IdWrapper.of(userId),
                linkTo(methodOn(UserController.class).createUser(jsonString)).withSelfRel());
    }

    @PostMapping(value = "/{userId}/order/for/certificate/{certId}")
    public EntityModel<IdWrapper> createOrder(@PathVariable String userId, @PathVariable String certId) throws Exception {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = customUserService.readByName(userName);
        if (Long.parseLong(userId) != user.getId()) {
            throw new NoPermissionsForResourceException();
        }
        Long orderId = orderRepoService.create(userId, certId);
        return EntityModel.of(IdWrapper.of(orderId),
                linkTo(methodOn(UserController.class).createOrder(userId, certId)).withSelfRel(),
                linkTo(methodOn(UserController.class).readUserById(userId)).withRel(REL_READ_USER_BY_ID),
                linkTo(methodOn(CertificatesController.class).readCertificateById(certId)).withRel(REL_READ_CERT_BY_ID),
                linkTo(methodOn(OrderController.class).readOrderById(String.valueOf(orderId))).withRel(REL_READ_ORDER_BY_ID)
        );
    }


    @GetMapping(value = "/{id}")
    public EntityModel<User> readUserById(@PathVariable String id) throws Exception {
        return EntityModel.of(userRepoService.readById(id),
                linkTo(methodOn(UserController.class).readUserById(id)).withSelfRel(),
                linkTo(methodOn(UserController.class).createOrder(id, "cert_id")).withRel(REL_ORDER_WITH_USER)
        );
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) throws LocalAppException {
        userRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping
    public CollectionModel<EntityModel<User>> readAllUsers(
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "page_size", required = false) String pSize,
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder) throws Exception {

        Pageable pageable = getPagebale(page, pSize, getSort(sortBy, sortOrder));
        List<User> userList = userRepoService.readByCriteria(pageable);
        List<EntityModel<User>> entUsers = new ArrayList<>();
        Long totalCount = userRepoService.getLastQueryCount();
        PageImpl<EntityModel<User>> pager = new PageImpl(entUsers, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entUsers = new ArrayList<>();
        for (User u : userList) {
            entUsers.add(EntityModel.of(u,
                    linkTo(methodOn(UserController.class).readUserById(String.valueOf(u.getId()))).withRel(REL_READ_USER_BY_ID),
                    linkTo(methodOn(UserController.class).deleteUser(String.valueOf(u.getId()))).withRel(REL_DEL_USER_BY_ID))
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
                    String.valueOf(pageable.first().getPageNumber()),
                    String.valueOf(pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    String.valueOf(pageable.previousOrFirst().getPageNumber()),
                    String.valueOf(pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    String.valueOf(pageable.next().getPageNumber()),
                    String.valueOf(pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(UserController.class).readAllUsers(
                    String.valueOf(pager.getTotalPages() - 1),
                    String.valueOf(pageable.getPageSize()),
                    sortBy, sortOrder))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

}
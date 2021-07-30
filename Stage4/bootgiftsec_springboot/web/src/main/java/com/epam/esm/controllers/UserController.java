package com.epam.esm.controllers;

import com.epam.esm.Errors.NoPermissionsForResourceException;
import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.Errors.UnknownUserOrWrongPasswordException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomOrderService;
import com.epam.esm.common_service.CustomUserService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.IdWrapper;
import com.epam.esm.model.User;
import com.epam.esm.utils.JwtTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import org.springframework.web.bind.annotation.*;

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
            @RequestParam(value = "sort_by", required = false) String sortBy,
            @RequestParam(value = "sort_order", required = false) String sortOrder,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize) throws Exception {

        Pageable pageable = getPagebale(page, pSize, getSort(sortBy, sortOrder));
        Page<User> resultPage = userRepoService.readByCriteria(pageable);


        if (resultPage.getNumber() > resultPage.getTotalPages()) {
            throw new NoSuchPageException();
        }

        List<EntityModel<User>> entUsers = entityUserListFromPage(resultPage);
        CollectionModel<EntityModel<User>> collEntUsers = CollectionModel.of(
                entUsers, getLinkReadAll(sortBy, sortOrder, page, pSize).expand());
        collEntUsers.add(getLinksPagination(resultPage));

        return collEntUsers;

    }


    private List<EntityModel<User>> entityUserListFromPage(Page<User> resultPage) throws Exception {
        List<User> userList = resultPage.toList();

        List<EntityModel<User>> entCerts = new ArrayList<>();
        for (User u : userList) {
            entCerts.add(EntityModel.of(u,
                    getLinkRead(u.getId()),
                    getLinkDel(u.getId())));
        }
        return entCerts;
    }

    private Link getLinkRead(Long id) throws Exception {
        return linkTo(methodOn(UserController.class)
                .readUserById(id)).withRel(REL_READ_USER_BY_ID);
    }

    private Link getLinkDel(Long id) throws Exception {
        return linkTo(methodOn(UserController.class)
                .deleteUser(id)).withRel(REL_DEL_USER_BY_ID);
    }

    private Link getLinkReadAll(String sortBy, String sortOrder, Integer page, Integer pSize) throws Exception {
        return linkTo(methodOn(UserController.class)
                .readAllUsers(sortBy, sortOrder, page, pSize)).withSelfRel();
    }

    private List<Link> getLinksPagination(Page<User> page) throws Exception {
        List<Link> links = new ArrayList<>();
        String sortBy = null;
        String order = null;
        if (!page.getPageable().getSort().toList().isEmpty()) {
            sortBy = page.getPageable().getSort().toList().get(0).getProperty();
            order = page.getPageable().getSort().toList().get(0).getDirection().name();
        }
        if (page.hasPrevious()) {
            int firstPage = page.getPageable().first().getPageNumber();
            int prevPage = page.previousOrFirstPageable().getPageNumber();
            links.add(getLinkReadAll(sortBy, order, firstPage, page.getSize()).expand().withRel(REL_FIRST_PAGE));
            links.add(getLinkReadAll(sortBy, order, prevPage, page.getSize()).expand().withRel(REL_PREVIOUS_PAGE));
        }
        if (page.hasNext()) {
            int nextPage = page.nextPageable().getPageNumber();
            int lastPage = page.getTotalPages() - 1;
            links.add(getLinkReadAll(sortBy, order, nextPage, page.getSize()).expand().withRel(REL_NEXT_PAGE));
            links.add(getLinkReadAll(sortBy, order, lastPage, page.getSize()).expand().withRel(REL_LAST_PAGE));
        }
        return links;
    }

}
package com.epam.esm.controllers;

import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.common_service.CommonService;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.epam.esm.CommonWeb.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/v1/orders")
public class OrderController {

    @Autowired
    @Qualifier("orderRepoService")
    CommonService<Order> orderRepoService;

    @GetMapping
    public CollectionModel<EntityModel<Order>> readAllOrders(@RequestParam(value = "page", required = false) String page,
                                                             @RequestParam(value = "page_size", required = false) String pSize) throws LocalAppException {
        Pageable pageable = getPagebale(page, pSize, Sort.unsorted());
        List<Order> orderList = orderRepoService.readByCriteria(pageable);
        List<EntityModel<Order>> entOrders = new ArrayList<>();
        Long totalCount = orderRepoService.getLastQueryCount();
        PageImpl<EntityModel<Order>> pager = new PageImpl(entOrders, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entOrders = new ArrayList<>();
        for (Order o : orderList) {
            entOrders.add(EntityModel.of(o,
                    linkTo(methodOn(OrderController.class).readOrderById(String.valueOf(o.getId()))).withRel(REL_READ_ORDER_BY_ID),
                    linkTo(methodOn(OrderController.class).deleteOrder(String.valueOf(o.getId()))).withRel(REL_DEL_ORDER_BY_ID))
            );
        }
        pager = new PageImpl(entOrders, pageable, totalCount);
        CollectionModel<EntityModel<Order>> collEntOrders = CollectionModel.of(pager,
                linkTo(methodOn(OrderController.class).readAllOrders(page, pSize)).withSelfRel().expand()
        );
        collEntOrders.add(getLinksPagination(pageable, pager));

        return collEntOrders;
    }

    @GetMapping(value = "/foruser/{userId}")
    public CollectionModel<EntityModel<Order>> readAllOrdersForUser(
            @PathVariable String userId,
            @RequestParam(value = "page", required = false) String page,
            @RequestParam(value = "page_size", required = false) String pSize) throws LocalAppException {
        Pageable pageable = getPagebale(page, pSize, Sort.unsorted());
        List<Order> orderList = orderRepoService.readByCriteria(pageable, userId);
        List<EntityModel<Order>> entOrders = new ArrayList<>();
        Long totalCount = orderRepoService.getLastQueryCount();
        PageImpl<EntityModel<Order>> pager = new PageImpl(entOrders, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entOrders = new ArrayList<>();
        for (Order o : orderList) {
            entOrders.add(EntityModel.of(o,
                    linkTo(methodOn(OrderController.class).readOrderById(String.valueOf(o.getId()))).withRel(REL_READ_ORDER_BY_ID),
                    linkTo(methodOn(OrderController.class).deleteOrder(String.valueOf(o.getId()))).withRel(REL_DEL_ORDER_BY_ID))
            );
        }
        pager = new PageImpl(entOrders, pageable, totalCount);
        CollectionModel<EntityModel<Order>> collEntOrders = CollectionModel.of(pager,
                linkTo(methodOn(OrderController.class).readAllOrdersForUser(userId, page, pSize)).withSelfRel().expand()
        );
        collEntOrders.add(getLinksPaginationForUser(userId, pageable, pager));

        return collEntOrders;
    }


    @GetMapping(value = "/{id}")
    public EntityModel<Order> readOrderById(@PathVariable String id) throws LocalAppException {
        return EntityModel.of(orderRepoService.readById(id),
                linkTo(methodOn(OrderController.class).readOrderById(id)).withSelfRel(),
                linkTo(methodOn(OrderController.class).deleteOrder(id)).withRel(REL_DEL_ORDER_BY_ID));
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteOrder(@PathVariable String id) throws LocalAppException {
        orderRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    private List<Link> getLinksPagination(Pageable pageable, PageImpl<EntityModel<Order>> pager) throws LocalAppException {
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() != 0) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    String.valueOf(pageable.first().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    String.valueOf(pageable.previousOrFirst().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    String.valueOf(pageable.next().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    String.valueOf(pager.getTotalPages() - 1),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

    private List<Link> getLinksPaginationForUser(String id, Pageable pageable, PageImpl<EntityModel<Order>> pager) throws LocalAppException {
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() != 0) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    String.valueOf(pageable.first().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    String.valueOf(pageable.previousOrFirst().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    String.valueOf(pageable.next().getPageNumber()),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    String.valueOf(pager.getTotalPages() - 1),
                    String.valueOf(pageable.getPageSize())))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

}
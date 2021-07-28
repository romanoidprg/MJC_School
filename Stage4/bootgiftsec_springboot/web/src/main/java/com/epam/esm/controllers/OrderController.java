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
    public CollectionModel<EntityModel<Order>> readAllOrders(@RequestParam(value = "page", required = false) Integer page,
                                                             @RequestParam(value = "page_size", required = false) Integer pSize) throws LocalAppException {
        Pageable pageable = getPagebale(page, pSize, Sort.unsorted());
        List<Order> orderList = orderRepoService.readByCriteria(pageable).toList();
        List<EntityModel<Order>> entOrders = new ArrayList<>();
        Long totalCount = 0L;//orderRepoService.getLastQueryCount();
        PageImpl<EntityModel<Order>> pager = new PageImpl(entOrders, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entOrders = new ArrayList<>();
        for (Order o : orderList) {
            entOrders.add(EntityModel.of(o,
                    linkTo(methodOn(OrderController.class).readOrderById(o.getId())).withRel(REL_READ_ORDER_BY_ID),
                    linkTo(methodOn(OrderController.class).deleteOrder(o.getId())).withRel(REL_DEL_ORDER_BY_ID))
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
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "page_size", required = false) Integer pSize) throws LocalAppException {
        Pageable pageable = getPagebale(page, pSize, Sort.unsorted());
        List<Order> orderList = orderRepoService.readByCriteria(pageable, userId).toList();
        List<EntityModel<Order>> entOrders = new ArrayList<>();
        Long totalCount = 0l;//orderRepoService.getLastQueryCount();
        PageImpl<EntityModel<Order>> pager = new PageImpl(entOrders, pageable, totalCount);

        if (pageable.getPageNumber() >= pager.getTotalPages()) {
            throw new NoSuchPageException();
        }

        entOrders = new ArrayList<>();
        for (Order o : orderList) {
            entOrders.add(EntityModel.of(o,
                    linkTo(methodOn(OrderController.class).readOrderById(o.getId())).withRel(REL_READ_ORDER_BY_ID),
                    linkTo(methodOn(OrderController.class).deleteOrder(o.getId())).withRel(REL_DEL_ORDER_BY_ID))
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
    public EntityModel<Order> readOrderById(@PathVariable Long id) throws LocalAppException {
        return EntityModel.of(orderRepoService.readById(id),
                linkTo(methodOn(OrderController.class).readOrderById(id)).withSelfRel(),
                linkTo(methodOn(OrderController.class).deleteOrder(id)).withRel(REL_DEL_ORDER_BY_ID));
    }

    @DeleteMapping(value = "/{id}/delete")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) throws LocalAppException {
        orderRepoService.deleteById(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    private List<Link> getLinksPagination(Pageable pageable, PageImpl<EntityModel<Order>> pager) throws LocalAppException {
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() != 0) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    (pageable.first().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    (pageable.previousOrFirst().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    (pageable.next().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrders(
                    (pager.getTotalPages() - 1),
                    (pageable.getPageSize())))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

    private List<Link> getLinksPaginationForUser(String id, Pageable pageable, PageImpl<EntityModel<Order>> pager) throws LocalAppException {
        List<Link> links = new ArrayList<>();
        if (pageable.getPageNumber() != 0) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    (pageable.first().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_FIRST_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    (pageable.previousOrFirst().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_PREVIOUS_PAGE));
        }
        if (pageable.next().getPageNumber() < pager.getTotalPages()) {
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    (pageable.next().getPageNumber()),
                    (pageable.getPageSize())))
                    .withRel(REL_NEXT_PAGE));
            links.add(linkTo(methodOn(OrderController.class).readAllOrdersForUser(
                    id,
                    (pager.getTotalPages() - 1),
                    (pageable.getPageSize())))
                    .withRel(REL_LAST_PAGE));
        }
        return links;
    }

}
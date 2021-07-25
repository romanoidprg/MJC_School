package com.epam.esm;

import com.epam.esm.Errors.NoSuchPageException;
import com.epam.esm.Errors.UnknownSortParametrException;
import com.epam.esm.errors.LocalAppException;
import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;

public class CommonWeb {

    public static final String REL_READ_BY_ID = "Read this tag.";
    public static final String REL_DELETE_TAG = "Delete this tag";
    public static final String TAG_ID = "tag_id";
    public static final String TAG_IN_JSON_FORMAT = "tag_in_json_format";
    public static final String REL_CREATE_TAG_FROM_JSON_FORMAT = "Create tag from json format";
    public static final String REL_WUTUHCO = "Get the most widely used tag of a user with the highest cost of all orders.";
    public static final String READ_1_ST_TAG = "Read 1-st tag.";
    public static final String REL_READ_TAG_PARAMS = "Read tag with params";
    public static final String REL_NEXT_PAGE = "Next page";
    public static final String REL_PREVIOUS_PAGE = "Previous page";
    public static final String REL_FIRST_PAGE = "First page";
    public static final String REL_LAST_PAGE = "Last page";

    public static final String REL_DELETE_CERT = "Delete this certificate";
    public static final String REL_READ_CERT_PARAMS = "Read certificate with params";
    public static final String REL_READ_CERT_TAGS = "Read certificate with tags";
    public static final String REL_READ_CERT_BY_ID = "Read this certificate.";

    public static final String REL_READ_USER_BY_ID = "Read this user.";
    public static final String REL_DEL_USER_BY_ID = "Delete this user.";
    public static final String REL_ORDER_WITH_USER = "Create order with user.";

    public static final String REL_READ_ORDER_BY_ID = "Read this order.";
    public static final String REL_DEL_ORDER_BY_ID = "Delete this order.";

    public static final int DEF_PAGE = 0;
    public static final int DEF_P_SIZE = 5;

    public static Pageable getPagebale(Integer page, Integer pSize, Sort sort) throws LocalAppException {
        if (page == null) {
            page = DEF_PAGE;
        }
        if (pSize == null) {
            pSize = DEF_P_SIZE;
        }
        return PageRequest.of(page, pSize, sort);
    }

    public static Pageable getPagebale(String sortBy, String sortOrder, Integer page, Integer pSize) throws LocalAppException {
        Sort sort;
        if (sortBy == null) {
            sort = Sort.unsorted();
        } else {
            sort = Sort.by(Sort.Direction.DESC.name().equalsIgnoreCase(sortOrder) ? Sort.Direction.DESC : Sort.Direction.ASC, sortBy);
        }
        if (page == null) {
            page = DEF_PAGE;
        }
        if (pSize == null) {
            pSize = DEF_P_SIZE;
        }
        return PageRequest.of(page, pSize, sort);
    }


    public static Sort getSort(String sortBy, String sortOrder) throws UnknownSortParametrException {
        Sort sort = Sort.by("id").ascending();
        if (sortBy != null) {
            if (sortBy.equalsIgnoreCase("id") || sortBy.equalsIgnoreCase("name")) {
                sort = Sort.by(sortBy);
                if (sortOrder != null && sortOrder.equalsIgnoreCase("desc")) {
                    sort = sort.descending();
                }
            } else {
                throw new UnknownSortParametrException(sortBy);
            }
        } else if (sortOrder != null) {
            throw new UnknownSortParametrException();
        }
        return sort;
    }
}

package com.epam.esm.common_service;

import java.util.List;

public interface CommonService<T> {

    boolean createFromJson(String jsonString);

    T readById(String id);

    List<T> readByCriteria(String... params);

//    List<T> readByCriteria(String tagName, String name, String description,
//                           String sortByName, String sortByCrDate, String sortByUpdDate,
//                           String sortNameOrder, String sortCrDateOrder, String sortUpdDateOrder);

    boolean updateFromJson(String jsonString);

    boolean deleteById(String id);

}

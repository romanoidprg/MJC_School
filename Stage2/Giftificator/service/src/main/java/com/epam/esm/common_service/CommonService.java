package com.epam.esm.common_service;

import java.util.List;

public interface CommonService<T> {

    boolean createFromJson(String jsonString);

    T readById(String id);

    List<T> readByCriteria(String tagName, String name, String description, String sortByName, String sortByDate, String sortOrder);

    boolean updateFromJson(String jsonString);

    boolean deleteById(String id);

}

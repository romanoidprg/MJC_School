package com.epam.esm.common_service;

import java.util.List;

public interface CommonService<T> {

    boolean createFromJson(String jsonString);

    T readById(String id);

    List<T> readByCriteria(String... params);

    boolean updateFromJson(String jsonString);

    boolean deleteById(String id);

}

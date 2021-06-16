package com.epam.esm.common_service;

import com.epam.esm.errors.NoSuchIdException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface CommonService<T> {

    boolean createFromJson(String jsonString) throws JsonProcessingException;

    T readById(String id) throws NoSuchIdException;

    List<T> readByCriteria(String... params);

    boolean updateFromJson(String jsonString);

    boolean deleteById(String id);

}

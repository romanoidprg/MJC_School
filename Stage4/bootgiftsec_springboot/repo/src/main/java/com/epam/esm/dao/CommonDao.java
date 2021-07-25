package com.epam.esm.dao;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommonDao<T, U> {

    long create(T entity);

    List<T> readAll();

    T readById(long id);

    List<T> readByCriteria(Pageable pageable, U criteria);

    boolean update(T entity);

    void delete(T entity);

    boolean isExist(T entity);

    Long getLastQueryCount();

}

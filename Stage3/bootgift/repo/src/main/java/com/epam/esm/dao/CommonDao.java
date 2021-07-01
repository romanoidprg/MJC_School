package com.epam.esm.dao;

import java.util.List;

public interface CommonDao<T, U> {

    long create(T entity);

    T readById(long id);

    List<T> readByCriteria(U criteria);

    boolean update(T entity);

    void delete(T entity);

    boolean isExist(T entity);

}

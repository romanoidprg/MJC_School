package com.epam.esm.dao;

import java.util.List;

public interface CommonDao<T, U> {

    boolean create(T entity);

    T readById(long id);

    List<T> readByCriteria(U criteria);

    boolean update(T entity);

    boolean deleteById(long id);

}

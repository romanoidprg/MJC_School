package com.epam.esm.dao;

import com.epam.esm.model.Criteria;

import java.util.List;

public interface CommonDao<T, U extends Criteria> {

    boolean create(T entity);

    T readById(long id);

    List<T> readByCriteria(U criteria);

    boolean update(T entity);

    boolean delete(long id);

}

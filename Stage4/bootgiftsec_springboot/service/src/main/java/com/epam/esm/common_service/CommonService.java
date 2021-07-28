package com.epam.esm.common_service;

import com.epam.esm.errors.LocalAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommonService<T> {

    long createFromJson(String jsonString) throws Exception;

//    Long create (String... params) throws LocalAppException;

    T readById(Long id) throws LocalAppException;

    Page<T> readByCriteria(Pageable pageable, String... params) throws LocalAppException;

//    boolean updateFromJson(String id, String jsonString) throws LocalAppException;

    boolean updateField(Long id, Map<String, String> params) throws LocalAppException;

    void deleteById(Long id) throws LocalAppException;

}

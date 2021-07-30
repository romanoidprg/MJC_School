package com.epam.esm.common_service;

import com.epam.esm.errors.LocalAppException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommonService<T> {

    long createFromJson(String jsonString) throws Exception;

    T readById(Long id) throws LocalAppException;

    Page<T> readByCriteria(Pageable pageable, String... params) throws LocalAppException;

    boolean updateField(Long id, Map<String, String> params) throws LocalAppException;

    void deleteById(Long id) throws LocalAppException;

}

package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCertDao {

    List<GiftCertificate> readWithTags(Pageable pageable, String[] tags);
    Long getLastQueryCount();
}

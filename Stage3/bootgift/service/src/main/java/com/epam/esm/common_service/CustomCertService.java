package com.epam.esm.common_service;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CustomCertService {

    List<GiftCertificate> readCertsWithTags(Pageable pageable, String[] tags);
    Long getLastQueryCountFromCustom();

}

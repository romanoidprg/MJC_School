package com.epam.esm.common_service;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCertService {

    Page<GiftCertificate> readCertsWithTags(Pageable pageable, String... tags) throws Exception;
}

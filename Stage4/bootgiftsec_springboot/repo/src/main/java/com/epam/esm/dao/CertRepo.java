package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CertRepo extends JpaRepository<GiftCertificate, Long> {
    List<GiftCertificate> findByTagsName(String name);

    Page<GiftCertificate> findByNameContainingAndDescriptionContainingAndTagsNameContaining(
            String name, String description, String tagName, Pageable pageable);

}

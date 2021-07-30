package com.epam.esm.dao;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CertRepo extends JpaRepository<GiftCertificate, Long> {
//    List<GiftCertificate> findByTagsName(String name);
@Query(value = "SELECT c.* FROM certificates as c, certs-tags as ct, tags as t WHERE c.id=ct.cert_id AND ct.tag_id=t.id AND t.name=:tags", nativeQuery = true)
    Page<GiftCertificate> findByTagsName(String tags, Pageable pageable);

    Page<GiftCertificate> findByNameContainingAndDescriptionContainingAndTagsNameContaining(
            String name, String description, String tagName, Pageable pageable);

}

package com.epam.esm.common_service.impl;

import com.epam.esm.common_service.CommonService;
import com.epam.esm.common_service.CustomCertService;
import com.epam.esm.dao.CertRepo;
import com.epam.esm.dao.TagRepo;
import com.epam.esm.errors.*;
import com.epam.esm.model.GiftCertificate;
import com.epam.esm.model.GiftCertificate_;
import com.epam.esm.model.Tag;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

@Service
public class CertRepoService implements CommonService<GiftCertificate>, CustomCertService {

    @Autowired
    CertRepo certRepo;

    @Autowired
    TagRepo tagRepo;

    @Override
    public long createFromJson(String jsonString) throws Exception {
        Long id;
        ObjectMapper objectMapper = new ObjectMapper();
        GiftCertificate cert = objectMapper.readValue(jsonString, GiftCertificate.class);
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name", "description")
                .withIgnorePaths("id", "price", "duration", "createDate", "lastUpdateDate", "tags")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        if (!certRepo.exists(Example.of(cert, em))) {
            cert.setId(null);
            cert.setCreateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            cert.setTags(refreshAndCorrectTags(cert));
            id = certRepo.save(cert).getId();
        } else {
            throw new EntityAlreadyExistException();
        }
        return id;
    }

    private Set<Tag> refreshAndCorrectTags(GiftCertificate cert) {
        Set<Tag> newTags = new HashSet<>();
        ExampleMatcher em = ExampleMatcher.matchingAll()
                .withIgnoreCase("name")
                .withIgnorePaths("id", "certificates")
                .withStringMatcher(ExampleMatcher.StringMatcher.EXACT);
        for (Tag t : cert.getTags()) {
            if (!tagRepo.exists(Example.of(t, em))) {
                t.setId(null);
                tagRepo.save(t);
                newTags.add(t);
            } else {
                newTags.addAll(tagRepo.findAll(Example.of(t, em)));
            }
        }
        return newTags;
    }


    @Override
    public GiftCertificate readById(Long id) throws LocalAppException {
        GiftCertificate result;
        result = certRepo.findById(id).orElse(null);
        if (result == null) {
            throw new NoSuchCertIdException(String.valueOf(id));
        }
        result.getTags().forEach(t -> t.setCertificates(null));
        return result;
    }

    @Override
    public Page<GiftCertificate> readCertsWithTags(Pageable pageable, String... tagNames) throws Exception {
        List<String> namesList = Arrays.asList(tagNames);
        Tag tag;
        List<Tag> tags = new ArrayList<>();
        for (String n : namesList) {
            tag = tagRepo.findByName(n);
            if (tag == null) {
                throw new NoTagWithSuchNameException(n);
            }
            tags.add(tag);
        }
        Specification<GiftCertificate> spec = getSpecificationByTag(tags.get(0));
        for (int i = 1; i < tags.size(); i++) {
            spec = spec.and(getSpecificationByTag(tags.get(i)));
        }
        Page<GiftCertificate> resultPage = certRepo.findAll(spec, pageable);
        resultPage.getContent().forEach(c -> c.getTags().forEach(t -> t.setCertificates(null)));
        return resultPage;
    }

    private Specification<GiftCertificate> getSpecificationByTag(Tag tag) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isMember(tag, root.get(GiftCertificate_.TAGS));
    }

    @Override
    public Page<GiftCertificate> readByCriteria(Pageable pageable, String... params) throws LocalAppException {
        String tagName = params.length == 0 ? "" : params[0] == null ? "" : params[0];
        String name = params.length == 0 ? "" : params[1] == null ? "" : params[1];
        String description = params.length == 0 ? "" : params[2] == null ? "" : params[2];
        Page<GiftCertificate> result = certRepo.findByNameContainingAndDescriptionContainingAndTagsNameContaining(name, description, tagName, pageable);
        result.forEach(c -> c.getTags().forEach(t -> t.setCertificates(null)));

        return result;
    }

    @Override
    public boolean updateField(Long id, Map<String, String> params) throws LocalAppException {

        if (params.size() != 1) {
            throw new IncorrectAmountOfCertFieldsException();
        }

        GiftCertificate cert = certRepo.findById(id).orElse(null);
        if (cert == null) {
            throw new NoSuchCertIdException(String.valueOf(id));
        }
        fillCert(cert, params);
        cert.setLastUpdateDate(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
        return certRepo.save(cert).getId().equals(id);
    }

    private void fillCert(GiftCertificate cert, Map<String, String> params) {
        String name = params.get("name");
        String description = params.get("description");
        String duration = params.get("duration");
        String price = params.get("price");
        if (duration != null) {
            if (!duration.matches("[0-9]+")) {
                throw new NumberFormatException("Incorrect format of the certificate field");
            } else {
                cert.setDuration(Integer.parseInt(duration));
            }
        }
        if (price != null) {
            if (!price.matches("[0-9]+")) {
                throw new NumberFormatException("Incorrect format of the certificate field");
            } else {
                cert.setPrice(Integer.parseInt(price));
            }
        }
        if (name != null) {
            cert.setName(name);
        }
        if (description != null) {
            cert.setDescription(description);
        }
    }


    @Override
    public void deleteById(Long id) throws LocalAppException {
        certRepo.deleteById(id);
    }
}
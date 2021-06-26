package com.epam.esm.model;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    @ManyToMany(mappedBy = "tags", cascade = {CascadeType.ALL})
    private List<GiftCertificate> certificates = new ArrayList<>();

    public Tag() {
    }

    public Tag(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public List<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<GiftCertificate> certificates) {
        this.certificates = certificates;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


}



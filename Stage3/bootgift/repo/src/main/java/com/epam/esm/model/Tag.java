package com.epam.esm.model;

import org.springframework.lang.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

//@Entity
//@Table(name = "tags")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    private long id;

    private String name;

    @ManyToMany(mappedBy = "tags")
    private Set<GiftCertificate> certificates;

    public Tag() {
    }

    public Tag(long id, String name, Set<GiftCertificate> certificates) {
        this.id = id;
        this.name = name;
        this.certificates = certificates;
    }

    public Set<GiftCertificate> getCertificates() {
        return certificates;
    }

    public void setCertificates(Set<GiftCertificate> certificates) {
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



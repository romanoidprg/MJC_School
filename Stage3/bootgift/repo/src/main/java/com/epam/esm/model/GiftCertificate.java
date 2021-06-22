package com.epam.esm.model;

import com.sun.xml.fastinfoset.util.StringArray;
import org.apache.commons.lang3.time.DateFormatUtils;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "certificates")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String description;
    private int price;
    private int duration;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "last_update_date")
    private Date lastUpdateDate;

    @ManyToMany(targetEntity = Tag.class, cascade = CascadeType.ALL)
    @JoinTable(name = "certs_tags", joinColumns = @JoinColumn(name = "cert_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags;


    public GiftCertificate() {
    }

    public GiftCertificate(long id,
                           String name,
                           String description,
                           int price,
                           int duration,
                           Date createDate,
                           Date lastUpdateDate,
                           Set<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = createDate;
        this.lastUpdateDate = lastUpdateDate;
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }
//
//    public void resetNullFieldsToDefaults() {
//        if (name == null) {
//            name = "";
//        }
//        if (description == null) {
//            description = "";
//        }
//        if (price == -1) {
//            price = 0;
//        }
//        if (duration == -1) {
//            duration = 0;
//        }
//        if (createDate == null) {
//            createDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
//        }
//        if (lastUpdateDate == null) {
//            lastUpdateDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
//        }
//        if (tags == null) {
//            tags = new ArrayList<>();
//        }
//
//    }

//    public void update(GiftCertificate newCert) {
//        if (newCert.name != null) {
//            this.name = newCert.name;
//        }
//        if (newCert.description != null) {
//            this.description = newCert.description;
//        }
//        if (newCert.price != -1) {
//            this.price = newCert.price;
//        }
//        if (newCert.duration != -1) {
//            this.duration = newCert.duration;
//        }
//        if (newCert.createDate != null) {
//            this.createDate = newCert.createDate;
//        }
//        if (newCert.lastUpdateDate != null) {
//            this.lastUpdateDate = newCert.lastUpdateDate;
//        }
//        if (newCert.tags != null) {
//            this.tags = newCert.tags;
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id == that.id &&
                price == that.price &&
                duration == that.duration &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                Objects.equals(createDate, that.createDate) &&
                Objects.equals(lastUpdateDate, that.lastUpdateDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, price, duration, createDate, lastUpdateDate);
    }
}


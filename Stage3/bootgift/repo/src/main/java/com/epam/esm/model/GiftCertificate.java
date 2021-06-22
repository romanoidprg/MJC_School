package com.epam.esm.model;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;


public class GiftCertificate {

    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    private long id = -1;
    private String name = null;
    private String description = null;
    private int price = -1;
    private int duration = -1;
    private String createDate = null;
    private String lastUpdateDate = null;
    private List<Tag> tags = null;


    public GiftCertificate() {
    }

    public GiftCertificate(long id,
                           String name,
                           String description,
                           int price,
                           int duration,
                           Date createDate,
                           Date lastUpdateDate,
                           List<Tag> tags) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createDate = DateFormatUtils.format(createDate, DATE_FORMAT);
        this.lastUpdateDate = DateFormatUtils.format(lastUpdateDate, DATE_FORMAT);
        this.tags = tags;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getDuration() {
        return duration;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void resetNullFieldsToDefaults() {
        if (name == null) {
            name = "";
        }
        if (description == null) {
            description = "";
        }
        if (price == -1) {
            price = 0;
        }
        if (duration == -1) {
            duration = 0;
        }
        if (createDate == null) {
            createDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
        }
        if (lastUpdateDate == null) {
            lastUpdateDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
        }
        if (tags == null) {
            tags = new ArrayList<>();
        }

    }

    public void update(GiftCertificate newCert) {
        if (newCert.name != null) {
            this.name = newCert.name;
        }
        if (newCert.description != null) {
            this.description = newCert.description;
        }
        if (newCert.price != -1) {
            this.price = newCert.price;
        }
        if (newCert.duration != -1) {
            this.duration = newCert.duration;
        }
        if (newCert.createDate != null) {
            this.createDate = newCert.createDate;
        }
        if (newCert.lastUpdateDate != null) {
            this.lastUpdateDate = newCert.lastUpdateDate;
        }
        if (newCert.tags != null) {
            this.tags = newCert.tags;
        }
    }

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


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

    private long id = 0;
    private String name = "";
    private String description = "";
    private int price = 0;
    private int duration = 0;
    private String createDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
    private String lastUpdateDate = DateFormatUtils.format(Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)), DATE_FORMAT);
    private List<Tag> tags = new ArrayList<>();


    public GiftCertificate(){
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


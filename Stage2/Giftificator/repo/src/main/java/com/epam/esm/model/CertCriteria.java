package com.epam.esm.model;

public class CertCriteria extends Criteria {
    String tagName = "";
    String name = "";
    String description = "";
    boolean sortByName = false;
    boolean sortByDate = false;
    String sortOrder = "";

    public CertCriteria(String tagName, String name, String description, boolean sortByName, boolean sortByDate, String sortOrder) {
        this.tagName = tagName != null ? tagName : "";
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.sortByName = sortByName;
        this.sortByDate = sortByDate;
        this.sortOrder = "asc".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder) ? sortOrder : "";
    }
}

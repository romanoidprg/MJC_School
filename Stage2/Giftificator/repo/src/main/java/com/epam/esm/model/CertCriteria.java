package com.epam.esm.model;

public class CertCriteria {
    private final String tagName;
    private final String name;
    private final String description;
    private final boolean sortByName;
    private final boolean sortByCrDate;
    private final boolean sortByUpdDate;
    private final String sortNameOrder;
    private final String sortCrDateOrder;
    private final String sortUpdDateOrder;


    public CertCriteria(String tagName, String name, String description,
                        boolean sortByName, boolean sortByCrDate, boolean sortByUpdDate,
                        String sortNameOrder, String sortCrDateOrder, String sortUpdDateOrder) {
        this.tagName = tagName != null ? tagName : "";
        this.name = name != null ? name : "";
        this.description = description != null ? description : "";
        this.sortByName = sortByName;
        this.sortByCrDate = sortByCrDate;
        this.sortByUpdDate = sortByUpdDate;
        this.sortNameOrder = "asc".equalsIgnoreCase(sortNameOrder) || "desc".equalsIgnoreCase(sortNameOrder) ? sortNameOrder : "";
        this.sortCrDateOrder = "asc".equalsIgnoreCase(sortCrDateOrder) || "desc".equalsIgnoreCase(sortCrDateOrder) ? sortCrDateOrder : "";
        this.sortUpdDateOrder = "asc".equalsIgnoreCase(sortUpdDateOrder) || "desc".equalsIgnoreCase(sortUpdDateOrder) ? sortUpdDateOrder : "";
    }

    public String getTagName() {
        return tagName;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSortByName() {
        return sortByName;
    }

    public boolean isSortByCrDate() {
        return sortByCrDate;
    }

    public boolean isSortByUpdDate() {
        return sortByUpdDate;
    }

    public String getSortNameOrder() {
        return sortNameOrder;
    }

    public String getSortCrDateOrder() {
        return sortCrDateOrder;
    }

    public String getSortUpdDateOrder() {
        return sortUpdDateOrder;
    }
}

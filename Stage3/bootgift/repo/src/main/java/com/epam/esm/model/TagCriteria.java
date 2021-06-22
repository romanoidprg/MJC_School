package com.epam.esm.model;

public class TagCriteria {
    private final String name;
    private final boolean sortByName;
    private final String sortOrder;

    public TagCriteria(String name, boolean sortByName, String sortOrder) {
        this.name = name != null ? name : "%";
        this.sortByName = sortByName;
        this.sortOrder = "asc".equalsIgnoreCase(sortOrder) || "desc".equalsIgnoreCase(sortOrder) ? sortOrder : "";
    }


    public String getName() {
        return name;
    }

    public boolean isSortByName() {
        return sortByName;
    }

    public String getSortOrder() {
        return sortOrder;
    }
}

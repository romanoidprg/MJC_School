package com.epam.esm.model;

public class IdWrapper {
    private Long id;
    public IdWrapper(){}
    private IdWrapper(Long id){
        this.id = id;
    }
    public static IdWrapper of(Long id){
        return  new IdWrapper(id);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

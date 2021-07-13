package com.epam.esm.model;

public class BoolWrapper {
    private Boolean result;
    public BoolWrapper(){}
    private BoolWrapper(Boolean result){
        this.result = result;
    }
    public static BoolWrapper of(Boolean result){
        return  new BoolWrapper(result);
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }
}

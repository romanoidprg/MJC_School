package com.epam.esm.errors;

public class NoSuchIdException extends Exception{
    public NoSuchIdException(){
        super();
    }
    public NoSuchIdException(String s){
        super(s);
    }
}

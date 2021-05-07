package com.enableets.edu.pakage.book.bo;

import lombok.Data;

@Data
public class IdNameMapBO {

    private String id;

    private String name;

    public IdNameMapBO(){}

    public IdNameMapBO(String id, String name){
        this.id = id;
        this.name = name;
    }
}

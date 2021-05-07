package com.enableets.edu.pakage.book.bo;

import lombok.Data;

@Data
public class CodeNameMapBO {

    private String code;

    private String name;

    public CodeNameMapBO() {}

    public CodeNameMapBO(String code, String name){
        this.code = code;
        this.name = name;
    }
}

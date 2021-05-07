package com.enableets.edu.pakage.framework.book.bo;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BookCodeNameMapBO {

    public BookCodeNameMapBO(String code, String name){
        this.code = code;
        this.name = name;
    }

    public BookCodeNameMapBO(String code, String name, String... relations) {
        this.code = code;
        this.name = name;
        this.relationCode = join(relations);
    }

    private String code;

    private String name;

    private String relationCode;

    private static String join(String...relations) {
        if (relations != null && relations.length > 0) {
            String result = "";
            for (String s : relations) {
                result += "_" + s;
            }
            return result.substring(1);
        } else {
            return null;
        }
    }
}

package com.enableets.edu.sdk.pakage.book.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/28
 **/
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CodeNameMapDTO {

    public CodeNameMapDTO(String code, String name){
        this.code = code;
        this.name = name;
    }

    public CodeNameMapDTO(String code, String name, String... relations) {
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

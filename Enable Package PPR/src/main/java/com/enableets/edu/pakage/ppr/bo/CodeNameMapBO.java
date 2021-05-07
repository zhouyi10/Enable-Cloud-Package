package com.enableets.edu.pakage.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
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

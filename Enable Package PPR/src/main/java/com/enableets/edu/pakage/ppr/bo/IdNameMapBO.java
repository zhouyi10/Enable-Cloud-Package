package com.enableets.edu.pakage.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
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

package com.enableets.edu.pakage.framework.actionflow.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IdNameMapBO implements Serializable {

    private static final long serialVersionUID = 3384766860240022988L;
    private String id;

    private String name;
}

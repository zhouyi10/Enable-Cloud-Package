package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWIdNameBO {

    private String id;

    private String name;
    
}

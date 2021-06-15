package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import io.swagger.models.auth.In;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWRequestListBO<T> {

    private List<T> list;

    private Integer total;

}

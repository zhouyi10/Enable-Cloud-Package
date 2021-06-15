package com.enableets.edu.pakage.framework.ppr.bo.xkw;

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
public class XKWChildQuestionBO {

    private String childAnswer;

    private String childBody;

    private Integer number;

    private List<XKWIdNameBO> childOptions;

}

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

    /* 小题答案 */
    private String childAnswer;

    /* 小题题文 */
    private String childBody;

    /* 小题序号 */
    private Integer number;

    /* 小题选项集合 */
    private List<XKWIdNameBO> childOptions;

}

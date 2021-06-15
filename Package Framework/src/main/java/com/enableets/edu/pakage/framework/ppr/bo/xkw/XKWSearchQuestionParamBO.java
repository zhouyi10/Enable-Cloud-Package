package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * 学科网试题搜索对象
 *
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWSearchQuestionParamBO {

    private String blankId;
}

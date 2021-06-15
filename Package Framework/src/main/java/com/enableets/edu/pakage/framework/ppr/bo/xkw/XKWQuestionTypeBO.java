package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 题目类型
 *
 * @author caleb_liu@enable-ets.com
 * @since 2020/09/01 14:09
 **/

@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWQuestionTypeBO {

    private Integer canSplit;

    private String id;

    private String name;

    private String parentId;

    private Integer selectType;

}

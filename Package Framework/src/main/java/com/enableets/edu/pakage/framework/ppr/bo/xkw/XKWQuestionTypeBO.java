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

    /* 是否可以拆分小题，0-否，1-是 */
    private Integer canSplit;

    private String id;

    /* 名称 */
    private String name;

    /* 父题型id */
    private String parentId;

    /* 是否是选择题，0-否，1-是 */
    private Integer selectType;

}

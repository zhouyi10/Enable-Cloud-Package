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

    /* 基础数据-题库id */
    private Integer blankId;

    /* 高亮关键字：0不显示，1显示 */
    private Integer highlight;

    /* 基础数据-年级id */
    private Integer learnGrades;

    /* 页码（1~10） */
    private Integer pageIndex;

    /* 每页记录数（1~10） */
    private Integer pageSize;

    /* 基础数据-地区id */
    private Integer province;

    /* 基础数据-试题难度id */
    private Integer quesDiff;

    /* 基础数据-试题类型id */
    private Integer quesType;

    /* 年份 */
    private Integer year;

    /* 搜索关键字(2~50个字符之间) */
    private String searchKeyword;
}

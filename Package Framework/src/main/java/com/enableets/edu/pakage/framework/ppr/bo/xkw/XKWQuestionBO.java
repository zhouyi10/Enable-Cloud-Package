package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWQuestionBO {

    /* 地区 */
    private List<XKWIdNameBO> areas;

    /* 更新日期 */
    private Date auditTime;

    /* 知识点 */
    private List<List<XKWIdNameBO>> categories;

    /* 章节 */
    private List<List<XKWIdNameBO>> chapters;

    /* 拆分后的小题列表 */
    private List<XKWChildQuestionBO> childQues;

    /* 试题来源 */
    private String from;

    /* 学段 */
    private List<XKWGradeBO> grades;

    /* 试题id */
    private String id;

    /* knowledge */
    private String knowledge;

    /* 拆分的选项 */
    private List<XKWIdNameBO> options;

    /* 试卷类型 */
    private List<XKWIdNameBO> paperTypes;

    /* 试题平台id */
    private String qbmId;

    /* 试题答案 */
    private String quesAnswer;

    /* 试题属性 */
    private XKWIdNameBO quesAttribute;

    /* 试题题干 */
    private String quesBody;

    /* 包含小题数 */
    private Integer quesChildNum;

    /* 试题难度 */
    private List<XKWIdNameBO> quesDiff;

    /* 试题难度值 */
    private Integer quesDiffValue;

    /* 试题Guid */
    private String quesGuid;

    /* 试题解析 */
    private String quesParse;

    /* 试题分数 */
    private Integer quesScore;

    /* 试题星级 */
    private Integer quesStar;

    /* 试题类型 */
    private XKWQuestionTypeBO quesType;

    /* 添加日期 */
    private Date time;

    /* 试题名称 */
    private String title;

    /* 使用次数 */
    private Integer useSum;

}

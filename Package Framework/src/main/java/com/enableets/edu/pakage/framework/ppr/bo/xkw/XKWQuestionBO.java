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

    private List<XKWIdNameBO> areas;

    private Date auditTime;

    private List<XKWIdNameBO> categories;

    private List<XKWIdNameBO> chapters;

    private List<XKWChildQuestionBO> childQues;

    private String from;

    private List<XKWGradeBO> grades;

    private String id;

    private String knowledge;

    private List<XKWIdNameBO> options;

    private List<XKWIdNameBO> paperTypes;

    private String qbmId;

    private String quesAnswer;

    private XKWIdNameBO quesAttribute;

    private String quesBody;

    private Integer quesChildNum;

    private List<XKWIdNameBO> quesDiff;

    private Integer quesDiffValue;

    private String quesGuid;

    private String quesParse;

    private Integer quesScore;

    private Integer quesStar;

    private XKWQuestionTypeBO quesType;

    private Date time;

    private String title;

    private Integer useSum;

}

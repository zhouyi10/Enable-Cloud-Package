package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @author tony_liu@enable-ets.com
 * @since 2020/5/25 14:15
 */

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestRecipientGroupBO {

    /** 考试标识*/
    private Long testId;

    //参与考试班级id
    private String classId;

    //参与考试班级名称
    private String className;

    //参与考试群组id
    private String groupId;

    //参与考试群组名称
    private String groupName;

    //参与考试班级批阅状态（'0'未批阅,'1'正在批阅,'2'批阅完成）
    private String markStatus;
}

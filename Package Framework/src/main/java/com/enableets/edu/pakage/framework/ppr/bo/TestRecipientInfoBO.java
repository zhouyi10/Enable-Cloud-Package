package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/31
 **/
@Data
@Accessors(chain = true)
public class TestRecipientInfoBO implements java.io.Serializable {

    private String testId;

    private String userId;

    private String userName;

    private String schoolId;

    private String schoolName;

    private String termId;

    private String termName;

    private String gradeCode;

    private String gradeName;

    private String classId;

    private String className;

    private String groupId;

    private String groupName;
}

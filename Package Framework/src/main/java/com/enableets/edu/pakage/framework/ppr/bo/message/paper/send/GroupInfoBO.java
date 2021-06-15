package com.enableets.edu.pakage.framework.ppr.bo.message.paper.send;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/17
 **/
@Data
@Accessors(chain = true)
public class GroupInfoBO {

    private String stepId;

    private String gradeCode;

    private String gradeName;

    private String groupId;

    private String groupName;

    private String label;

    private Integer total;

    private String type;

    private List<MemberInfo> members;

    @Data
    @Accessors(chain = true)
    public static class MemberInfo{

        private String stepId;

        private String userId;

        private String fullname;
    }
}


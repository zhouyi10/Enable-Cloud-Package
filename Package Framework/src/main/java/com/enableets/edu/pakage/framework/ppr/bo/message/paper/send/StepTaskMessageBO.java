package com.enableets.edu.pakage.framework.ppr.bo.message.paper.send;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/17
 **/
@Data
@Accessors(chain = true)
public class StepTaskMessageBO {
    private String taskId;

    private String schoolId;

    private String schoolName;

    private String termId;

    private String termName;

    private String businessId;

    private String title;

    private String type;

    private String appId;

    private Date startTime;

    private Date endTime;

    private Date publishTime;

    private String ownerIdentityId;

    private String ownerIdentityType;

    private String publisherIdentityId;

    private String publisherIdentityType;

    private String description;

    private String status;

    private List<StepInfoBO> steps;
}

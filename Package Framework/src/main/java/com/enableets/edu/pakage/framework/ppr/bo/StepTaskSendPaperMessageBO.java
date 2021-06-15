package com.enableets.edu.pakage.framework.ppr.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * Step Task RabbitMq Message Entity
 * @author walle_yu@enable-ets.com
 * @since 2021/05/17
 **/
@Data
@Accessors(chain = true)
public class StepTaskSendPaperMessageBO {

    private String taskId;

    private String courseId;

    private String courseName;

    private String schoolId;

    private String schoolName;

    private String termId;

    private String termName;

    private String subjectCode;

    private String subjectName;

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

    private List<StepInfo> steps;

}

@Data
@Accessors(chain = true)
class StepInfo{
    private String stepId;

    private String businessId;

    private String name;

    private Date publishTime;

    private Date startTime;

    private Date endTime;

    private String description;

    private Integer sequence;

    private List<ContentInfo> contents;

    private List<GroupInfo> groups;
}

@Data
@Accessors(chain = true)
class ContentInfo{

    private String contentId;

    private String description;

    private String extendAttrs;

    private String name;

    private Integer sequence;

    private String typeCode;

    private String typeName;

    private List<ContentFileInfo> files;
}

@Data
@Accessors(chain = true)
class ContentFileInfo{

    private String contentId;

    private String fileId;

    private String md5;

    private Long size;

    private String sizeDisplay;

    private String businessId;

    private String type;

    private String Description;

    private List<UrlInfo> urls;
}

@Data
@Accessors(chain = true)
class UrlInfo{
    private String url;
}

@Data
@Accessors(chain = true)
class GroupInfo{

    private String stepId;

    private String groupId;

    private String groupName;

    private String label;

    private Integer total;

    private String type;

    private List<MemberInfo> members;
}

@Data
@Accessors(chain = true)
class MemberInfo{

    private String stepId;

    private String userId;

    private String fullName;
}

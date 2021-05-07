package com.enableets.edu.pakage.framework.ppr.test.service.submit.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

/**
 * @author duffy_ding
 * @since 2020/03/16
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubmitAttributeBO {

    /** answer type  _STEP 子任务  _ACTIVITY 任务 */
    private String type;

    /** test id, param which required */
    private String testId;

    /** parent activity id, the id of activity that test is in */
    private String parentActivityId;

    /** activity id, the id of activity that user reply  */
    private String activityId;

    /** user id, param which required */
    private String userId;

    /** step id, the child id of activity, which test is in, only use in type @{_STEP}  */
    private String stepId;

    /** step instance id, the reply id of step, which test is in, only use in type @{_STEP}  */
    private String stepInstanceId;

    /** content id, only use in type @{_STEP}  */
    private String contentId;

    /** file id, only use in type @{_STEP}  */
    private String fileId;

    /** result:  test user id */
    private String testUserId;

    /** */
    private String paperId;

    /** */
    private String version;

    public SubmitAttributeBO clone() {
        SubmitAttributeBO attribute = new SubmitAttributeBO();
        attribute.setType(type);
        attribute.setTestId(testId);
        attribute.setParentActivityId(parentActivityId);
        attribute.setActivityId(activityId);
        attribute.setUserId(userId);
        attribute.setStepId(stepId);
        attribute.setStepInstanceId(stepInstanceId);
        attribute.setContentId(contentId);
        attribute.setFileId(fileId);
        attribute.setTestUserId(testUserId);
        attribute.setPaperId(paperId);
        attribute.setVersion(version);
        return attribute;
    }
}

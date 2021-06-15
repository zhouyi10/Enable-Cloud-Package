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

    /** test id, param which required */
    private String testId;

    private String stepId;

    /** file id, only use in type @{_STEP}  */
    private String fileId;

    /** result:  test user id */
    private String testUserId;

    /** */
    private String paperId;

}

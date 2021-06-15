package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/22
 **/
@Data
public class SubmitResultBO {

    private final static String SUCCESS = "success";

    public final static String ERROR = "error";

    public static SubmitResultBO success(String businessId) {
        SubmitResultBO resultBO = new SubmitResultBO();
        resultBO.setBusinessId(businessId);
        resultBO.setSubmitStatus(SUCCESS);
        return resultBO;
    }

    public static SubmitResultBO error(String errorCode, String message) {
        SubmitResultBO resultBO = new SubmitResultBO();
        resultBO.setErrorCode(errorCode);
        resultBO.setSubmitStatus(ERROR);
        resultBO.setMessage(message);
        return resultBO;
    }

    private String businessId;

    private String submitStatus;

    private String errorCode;

    private String message;
}

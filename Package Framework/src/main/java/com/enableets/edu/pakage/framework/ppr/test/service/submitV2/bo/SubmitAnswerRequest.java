package com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo;

import lombok.Data;

import java.util.concurrent.CompletableFuture;

/**
 * @author tony_liu@enable-ets.com
 * @since 2021/1/28
 **/

@Data
public class SubmitAnswerRequest {

    private String id;

    private String userId;

    private String data;

    private CompletableFuture<AnswerRequestDataBO> future = new CompletableFuture<>();

    public SubmitAnswerRequest(String id, String userId, String data) {
        this.id = id;
        this.userId = userId;
        this.data = data;
    }
}

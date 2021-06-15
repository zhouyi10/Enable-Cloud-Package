package com.enableets.edu.sdk.pakage.ppr.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/26
 **/
@Data
@Accessors(chain = true)
public class TestMarkResultInfoDTO {

    private String stepId;

    private String activityId;

    private String fileId;

    private List<UserScoreDTO> users;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserScoreDTO{

        private String testUserId;

        private String userId;

        private Float score;
    }
}

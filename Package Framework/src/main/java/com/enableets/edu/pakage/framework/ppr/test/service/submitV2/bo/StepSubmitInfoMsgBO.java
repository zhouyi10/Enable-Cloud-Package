package com.enableets.edu.pakage.framework.ppr.test.service.submitV2.bo;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/05/12
 **/

@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class StepSubmitInfoMsgBO {

    private String stepInstanceId;

    private String stepId;

    private String userId;

    private String businessId;

    private Long createTime;
}

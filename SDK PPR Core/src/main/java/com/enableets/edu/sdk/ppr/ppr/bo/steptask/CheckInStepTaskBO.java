package com.enableets.edu.sdk.ppr.ppr.bo.steptask;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CheckInStepTaskBO {

    private String activityId;

    private String userId;

    private List<StepInstanceBO> stepInstances;
}

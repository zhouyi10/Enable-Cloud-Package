package com.enableets.edu.pakage.framework.ppr.bo.xkw;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2021/06/10
 **/

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
public class XKWGradeBO extends XKWIdNameBO{
    private String stageId;
}

package com.enableets.edu.sdk.ppr.ppr.bo;

import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/10/28
 * @Author walle_yu@enable-ets.com
 **/

@Data
@NoArgsConstructor
public class PPRInfoBO {

    private PPRBO pprBO;

    private CardBO cardBO;

    public PPRInfoBO(PPRBO pprBO, CardBO cardBO) {
        this.pprBO = pprBO;
        this.cardBO = cardBO;
    }

}

package com.enableets.edu.sdk.ppr.ppr.action.invoker;

import com.enableets.edu.sdk.ppr.adapter.ActivityServiceAdapter;
import com.enableets.edu.sdk.ppr.adapter.PaperServiceAdapter;
import com.enableets.edu.sdk.ppr.adapter.StepInfoBO;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.ppr.action.PPRActionHandlerException;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.steptask.CheckInStepTaskBO;
import com.enableets.edu.sdk.ppr.ppr.bo.steptask.StepInstanceBO;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.PaperCardBuilderHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
public class PaperCardBOInvoker{

    private final CardBO cardBO;

    private final Configuration configuration;

    private final PaperCardBuilderHandler paperCardBuilderHandler;

    public PaperCardBOInvoker(CardBO cardBO, Configuration configuration, PaperCardBuilderHandler paperCardBuilderHandler) {
        this.cardBO = cardBO;
        this.configuration = configuration;
        this.paperCardBuilderHandler = paperCardBuilderHandler;
    }

    /**
     * Temporary storage
     * @return
     */
    public boolean save(){
        boolean saveStatus = PaperServiceAdapter.save(paperCardBuilderHandler.build(cardBO), configuration);
       // checkIn(paperCardBO.getActions());
        return saveStatus;
    }

    /**
     * step check in
     * @param actions
     */
    private void checkIn(List<StepActionBO> actions){
        StepInfoBO stepInfo = ActivityServiceAdapter.getStepInfo(actions.get(0).getId(), configuration);
        if (stepInfo == null){
            throw new PPRActionHandlerException("Activity info not found");
        }
        List<StepInstanceBO> stepInstances = new ArrayList<>();
        for (StepActionBO action : actions) {
            stepInstances.add(new StepInstanceBO(action.getId(), null));
        }
        CheckInStepTaskBO checkInStepTaskBO = new CheckInStepTaskBO(stepInfo.getActivityId(), actions.get(0).getUserId(), stepInstances);
        ActivityServiceAdapter.checkIn(checkInStepTaskBO, configuration);
    }
}

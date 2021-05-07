package com.enableets.edu.sdk.ppr.ppr.bo.card;

import com.enableets.edu.sdk.ppr.ppr.bo.CodeNameMapBO;
import com.enableets.edu.sdk.ppr.ppr.bo.IdNameMapBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;

import java.util.Date;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/30
 **/
@Data
@NoArgsConstructor
public class CardBO {

    private String paperId;

    /** Paper Name */
    private String name;

    /** Stage Info */
    private CodeNameMapBO stage;

    /** Grade Info */
    private CodeNameMapBO grade;

    /** Subject Info */
    private CodeNameMapBO subject;

    /** Paper Score */
    private Float totalPoints;

    /** Recommended answer time  */
    private Long answerCostTime;

    /** User Info */
    private IdNameMapBO user;

    /** Create Time*/
    private Date createTime;

    public CardBO(AnswerCardBO answerCard){
        this.answerCard = answerCard;
    }

    private AnswerCardBO answerCard;

    private List<StepActionBO> actions;

    private List<AnswerBO> answers;

}

package com.enableets.edu.pakage.manager.ppr.vo;

import java.util.List;
import lombok.Data;

/**
 * Answer Card Info
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
public class AddAnswerCardInfoVO {

    /** Primary key*/
    private String answerCardId;

    /** Paper ID*/
    private String examId;

    /** Answer sheet layout(1：One Column，2：Two Columns，3：Three Columns)*/
    private Integer columnType;

    /** Candidate number edition 1:qCode，2:Admission ticket No ，3:Short exam number)*/
    private Integer candidateNumberEdition;

    /** Page Type(A3, A4)*/
    private String pageType;

    /** True or false Question Style(0: Default T/F, 1:Right/Wrong √/× )*/
    private String judgeStyle;

    /** Sealing line(0:Default hide， 1：show)*/
    private String sealingLineStatus;

    /** Is show Question Stem(0：Default hide, 1:show)*/
    private String questionContentStatus;

    /** Answer Card Page Count*/
    private Integer pageCount;

    /** Axis Info*/
    private List<AddAnswerCardAxisVO> axises;

    /** timeline Info*/
    private List<AddAnswerCardTimelineVO> timelines;

    /** */
    private String creator;

}

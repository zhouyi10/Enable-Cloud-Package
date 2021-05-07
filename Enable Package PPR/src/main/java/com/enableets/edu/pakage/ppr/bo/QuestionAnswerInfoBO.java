package com.enableets.edu.pakage.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
public class QuestionAnswerInfoBO {

    /** Question ID  */
    private Long questionId;

    /** Answer display text  */
    private String label;

    /** Answer matching strategy  */
    private String strategy;

    /** Answer analysis  */
    private String analysis;
}

package com.enableets.edu.pakage.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
public class QuestionOptionInfoBO {

    /** Option ID*/
    private String optionId;

    /** Question ID*/
    private String questionId;

    /** Option title*/
    private String alias;

    /** Option content*/
    private String label;

    /** Option order*/
    private Integer sequencing;
}

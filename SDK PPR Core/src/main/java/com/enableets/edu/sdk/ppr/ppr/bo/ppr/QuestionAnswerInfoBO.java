package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAnswerInfoBO {

    private String label;

    /** 答案匹配策略  */
    private String strategy;

    /** 答案解析  */
    private String analysis;
}

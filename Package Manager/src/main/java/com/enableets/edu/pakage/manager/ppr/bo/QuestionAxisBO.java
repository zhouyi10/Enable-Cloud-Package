package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 **/
@Data
public class QuestionAxisBO {

    private String questionId;

    private String fileId;

    private Float xAxis;

    private Float yAxis;

    private Float width;

    private Float height;
}

package com.enableets.edu.pakage.manager.ppr.vo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 **/
@Data
public class QuestionAxisVO {

    private String questionId;

    private String fileId;

    private Float xAxis;

    private Float yAxis;

    private Float width;

    private Float height;
}

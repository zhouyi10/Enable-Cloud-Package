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

    public Float getxAxis() {
        return xAxis;
    }

    public void setxAxis(Float xAxis) {
        this.xAxis = xAxis;
    }

    public Float getyAxis() {
        return yAxis;
    }

    public void setyAxis(Float yAxis) {
        this.yAxis = yAxis;
    }
}

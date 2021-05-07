package com.enableets.edu.sdk.ppr.ppr.core.paper;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/27
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@XStreamAlias("href")
public class QuestionHref {

    public QuestionHref(String ref) {
        this.ref = ref;
    }

    public QuestionHref(String ref, String questionNo){
        this.ref = ref;
        this.questionNo = questionNo;
    }

    public QuestionHref(String ref, Double width, Double height, Double xAxis, Double yAxis){
        this.ref = ref;
        this.width = width;
        this.height = height;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
    }

    @XStreamAsAttribute
    private String ref;

    @XStreamAsAttribute
    private String questionNo;

    @XStreamAsAttribute
    private Double width;

    @XStreamAsAttribute
    private Double height;

    @XStreamAsAttribute
    private Double xAxis;

    @XStreamAsAttribute
    private Double yAxis;
}

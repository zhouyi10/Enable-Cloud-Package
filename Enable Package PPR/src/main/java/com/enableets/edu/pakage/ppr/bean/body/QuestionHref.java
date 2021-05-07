package com.enableets.edu.pakage.ppr.bean.body;

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

    public QuestionHref(String fileName) {
        this.ref = getHref(fileName);
    }

    public QuestionHref(String fileName, String questionNo){
        this.ref = getHref(fileName);
        this.questionNo = questionNo;
    }

    public QuestionHref(String fileName, Double width, Double height, Double xAxis, Double yAxis){
        this.ref = getHref(fileName);
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

    private String getHref(String fileName){
        return new StringBuilder("./files/").append(fileName).toString();
    }
}

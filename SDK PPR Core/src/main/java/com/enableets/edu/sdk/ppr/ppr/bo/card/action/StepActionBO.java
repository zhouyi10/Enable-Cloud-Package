package com.enableets.edu.sdk.ppr.ppr.bo.card.action;

import com.enableets.edu.sdk.ppr.annotation.PaperProperties;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
@Data
public class StepActionBO {

    public StepActionBO(){}

    public StepActionBO(String id, String type, String description, String userId, String fullname){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullname = fullname;
    }

    public StepActionBO(String id, String type, String description, String userId, String fullname, Long timestamp){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullname = fullname;
        this.timestamp = timestamp;
    }

    public StepActionBO(String id, String type, String description, String userId, String fullname, Long startTimeStamp, Long endTimeStamp){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullname = fullname;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    private String id;

   // private String name;
    private String type;
    
    private String description;

    @PaperProperties
    private String userId;

    @PaperProperties
    private String fullname;

    @PaperProperties(isNullNotShow = true)
    private Long timestamp;

    @PaperProperties(isNullNotShow = true)
    private Long startTimeStamp;

    @PaperProperties(isNullNotShow = true)
    private Long endTimeStamp;


}

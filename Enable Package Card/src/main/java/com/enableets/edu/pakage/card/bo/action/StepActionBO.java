package com.enableets.edu.pakage.card.bo.action;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/14
 **/
@Data
public class StepActionBO {

    public StepActionBO(){}

    public StepActionBO(String id, String type, String description, String userId, String fullName){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullName = fullName;
    }

    public StepActionBO(String id, String type, String description, String userId, String fullName, Long timestamp){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullName = fullName;
        this.timestamp = timestamp;
    }

    public StepActionBO(String id, String type, String description, String userId, String fullName, Long startTimeStamp, Long endTimeStamp){
        this.id = id;
        this.type = type;
        this.description = description;
        this.userId = userId;
        this.fullName = fullName;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    private String id;

    private String type;
    
    private String description;

    private String userId;

    private String fullName;

    private Long timestamp;

    private Long startTimeStamp;

    private Long endTimeStamp;

}

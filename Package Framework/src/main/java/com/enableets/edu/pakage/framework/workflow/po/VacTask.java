package com.enableets.edu.pakage.framework.workflow.po;

import lombok.Data;

import java.util.Date;

/**
 * @author tony_liu@enable-ets.com
 * @since 2021/2/26
 */
@Data
public class VacTask {

    private String id;
    private String name;
    private VacationPO vac;
    private Date createTime;

}

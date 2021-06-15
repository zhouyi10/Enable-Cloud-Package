package com.enableets.edu.pakage.microservice.coursepackage.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author sumbean
 * @ClassName GroupVO
 * @Description TODO
 * @date 2021/5/26 19:30
 * @Version 1.0
 */
@Data
public class GroupVO {

    @ApiModelProperty(value = "group Id")
    private String groupId;

    @ApiModelProperty(value = "group name")
    private String groupName;
}

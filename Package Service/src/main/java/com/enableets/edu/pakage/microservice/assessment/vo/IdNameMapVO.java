package com.enableets.edu.pakage.microservice.assessment.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/09/25
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "Id Name Map VO", description = "Id Name Map VO")
public class IdNameMapVO{

    @ApiParam(value = "id")
    private String id;

    @ApiParam(value = "name")
    private String name;
}

package com.enableets.edu.pakage.microservice.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.enableets.edu.pakage.framework.core.Constants;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "QuestionKnowledgeInfoVO", description = "Knowledge Info")
public class QuestionKnowledgeInfoVO {

  /** Knowledge ID*/
  @ApiModelProperty(value = "Knowledge ID", required = false)
  private String knowledgeId;

  /** */
  @ApiModelProperty(value = "Knowledge Name", required = false)
  private String knowledgeName;

  /** */
  @ApiModelProperty(value = "TextBook Version", required = false)
  private String materialVersion;

  /** */
  @ApiModelProperty(value = "TextBook Version Name", required = false)
  private String materialVersionName;

  /** */
  @ApiModelProperty(value = "Search Code", required = false)
  private String searchCode;

  @ApiModelProperty(value = "Knowledge No.", required = false)
  private String knowledgeNo;

}

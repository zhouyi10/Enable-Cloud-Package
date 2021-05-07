package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionKnowledgeInfoDTO {

  /** Knowledge ID*/
  private String knowledgeId;

  /** */
  private String knowledgeName;

  /** */
  private String materialVersion;

  /** */
  private String materialVersionName;

  /** */
  private String searchCode;

  private String knowledgeNo;

}

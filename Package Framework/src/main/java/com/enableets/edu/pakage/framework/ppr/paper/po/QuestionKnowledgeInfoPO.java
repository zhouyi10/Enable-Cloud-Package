package com.enableets.edu.pakage.framework.ppr.paper.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "question_knowledge_info")
@Data
public class QuestionKnowledgeInfoPO {

  @Column(name = "question_id")
  private String questionId;

  @Column(name = "knowledge_id")
  private String knowledgeId;

  @Column(name = "knowledge_name")
  private String knowledgeName;

  @Column(name = "material_version")
  private String materialVersion;

  @Column(name = "material_version_name")
  private String materialVersionName;

  @Column(name = "search_code")
  private String searchCode;

  @Column(name = "creator")
  private String creator;

  @Column(name = "create_time")
  private java.util.Date createTime;

  @Column(name = "updator")
  private String updator;

  @Column(name = "update_time")
  private java.util.Date updateTime;

}

package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * Knowledge Info DTO
 * @author walle_yu@enable-ets.com
 * @since 2020/10/22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeInfoDTO {

	private String knowledgeId;

	private String knowledgeName;

	private String materialVersion;

	private String materialVersionName;

	private String searchCode;

	private String knowledgeNo;

	private String outlineId;

}

package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Option Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionOptionDTO {

	/**
	 * Option ID
	 */
	private Long optionId;

	/**
	 * Question ID
	 */
	private Long questionId;

	/**
	 * Option title
	 */
	private String alias;

	/**
	 * Option content
	 */
	private String label;

	/**
	 * 选项排序
	 */
	private Integer sequencing;
}

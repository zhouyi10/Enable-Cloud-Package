package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Answer Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionAnswerDTO {

	/** Question ID  */
	private Long questionId;
	
	/** Answer display text  */
	private String label;
	
	/** Answer matching strategy  */
	private String strategy;
	
	/** Answer analysis  */
	private String analysis;

}

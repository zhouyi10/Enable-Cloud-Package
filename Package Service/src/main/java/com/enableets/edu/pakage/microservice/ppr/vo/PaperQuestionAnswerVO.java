package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Answer Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperQuestionAnswerVO {

	/** Question ID  */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long questionId;
	
	/** Answer display text  */
	private String label;
	
	/** Answer matching strategy  */
	private String strategy;
	
	/** Answer analysis  */
	private String analysis;

}

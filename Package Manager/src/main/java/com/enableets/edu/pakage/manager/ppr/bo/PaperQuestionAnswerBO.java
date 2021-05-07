package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Answer Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperQuestionAnswerBO {

	/** Question ID  */
	private Long questionId;
	
	/** Answer display text  */
	private String label;
	
	/** Answer matching strategy  */
	private String strategy;
	
	/** Answer analysis  */
	private String analysis;

}

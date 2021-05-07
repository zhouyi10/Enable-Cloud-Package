package com.enableets.edu.pakage.manager.ppr.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Option Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperQuestionOptionBO {

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

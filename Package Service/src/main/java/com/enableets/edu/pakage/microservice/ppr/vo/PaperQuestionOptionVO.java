package com.enableets.edu.pakage.microservice.ppr.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Test Paper Structure-Question Option Information
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaperQuestionOptionVO {

	/**
	 * Option ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
	private Long optionId;

	/**
	 * Question ID
	 */
	@JsonSerialize(using = ToStringSerializer.class)
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

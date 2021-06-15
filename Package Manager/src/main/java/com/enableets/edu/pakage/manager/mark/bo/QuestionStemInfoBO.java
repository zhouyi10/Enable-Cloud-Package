package com.enableets.edu.pakage.manager.mark.bo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * 题干信息DTO
 * @author walle_yu@enable-ets.com
 * @since 2018年3月14日
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuestionStemInfoBO {
	
	/** 带html标签题干信息  */
	public String richText;
	
	/** 无html标签题干信息  */
	public String plaintext;

	public QuestionStemInfoBO(){

	}

	public QuestionStemInfoBO(String richText, String plaintext) {
		this.richText = richText;
		this.plaintext = plaintext;
	}

	public String getRichText() {
		return richText;
	}

	public void setRichText(String richText) {
		this.richText = richText;
	}

	public String getPlaintext() {
		return plaintext;
	}

	public void setPlaintext(String plaintext) {
		this.plaintext = plaintext;
	}
}
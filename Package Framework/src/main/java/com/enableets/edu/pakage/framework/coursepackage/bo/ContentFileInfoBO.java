package com.enableets.edu.pakage.framework.coursepackage.bo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: AddContentFileInfoVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
public class ContentFileInfoBO {
	
	/**
	 * file Id
	 */
	
	private String fileId;

	/**
	 * file Name
	 */
	
	private String fileName;

	/**
	 * File extension
	 */
	
	private String fileExt;
	
	/**
	 * File address
	 */
	
	private String url;

	/**
	 * md5
	 */
	private String md5;

	/**
	 * file size
	 */
	private Long size;

	/**
	 * file size display
	 */
	private String sizeDisplay;
	
	/**
	 * description
	 */
	
	private String description;
	
	/**
	 * file Order
	 */
	
	private Integer fileOrder;
	
	/**
	 * extend
	 */
	
	private String extendAttrs;

	/**
	 * uploadDate
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date uploadDate;

}

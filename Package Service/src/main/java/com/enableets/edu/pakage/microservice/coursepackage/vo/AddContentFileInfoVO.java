package com.enableets.edu.pakage.microservice.coursepackage.vo;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Description: AddContentFileInfoVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value="AddContentFileInfoVO", description="AddContentFileInfoVO")
public class AddContentFileInfoVO {
	

	@ApiModelProperty(value="file id")
	private String fileId;


	@ApiModelProperty(value="file name")
	private String fileName;

	@ApiModelProperty(value="file ext")
	private String fileExt;

	@ApiModelProperty(value="File address")
	private String url;
	
	@ApiModelProperty(value="file md5")
	private String md5;
	
	@ApiModelProperty(value="file size")
	private Long size;
	
	@ApiModelProperty(value="file sizeDisplay", hidden = true)
	private String sizeDisplay;
	

	@ApiModelProperty(value="description")
	private String description;
	

	@ApiModelProperty(value="file Order")
	private Integer fileOrder;
	

	@ApiModelProperty(value="extend")
	private String extendAttrs;

	@ApiModelProperty(value="Upload time")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
	private Date uploadDate;

}

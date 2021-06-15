package com.enableets.edu.pakage.framework.coursepackage.bo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @Description: AddCoursePackageVO
 * @Author: chris_cai@enable-ets.com
 * @Since: 2021-05-22
 */
@Data
public class KnowledgeInfoBO {

	/**
	  * knowledge Id
	  */
	 
	 private String knowledgeId;
	 
	/**
	  * knowledge Name
	  */
	 
	 private String knowledgeName;
	 /**
	  * searchCode
	  */
	 
     private String searchCode;
	
	/**
	  * Version
	  */
	 private String materialVersion;


	/**
	 * Version Name
	 */
	private String materialVersionName;

}

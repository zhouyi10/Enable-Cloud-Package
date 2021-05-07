package com.enableets.edu.pakage.manager.ppr.vo;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import lombok.Data;

/**
 *
 */
@Data
public class QueryPaperVO implements java.io.Serializable {

	private String paperId;

	private String stageCode;

	private String gradeCode;

	private String subjectCode;

	private String providerCode;

	private String materialVersion;

	private String searchCode;

	private String name;

	private String userId;

	private String schoolId;

	private Integer offset;

	private Integer rows;

	/** 用户信息 */
	private IdNameMapVO user;

	/** 年级信息 */
	private CodeNameMapVO grade;

	/** 学科信息 */
	private CodeNameMapVO subject;

	/** 创建时间 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createTime;

	/** 在何处使用标识 */
	private String usageCode;

	/** 极值试卷年份 **/
	private String year;

	/** 极值省份id **/
	private String provinceId;

	/** 极值城市id **/
	private String cityId;

	/** 极值试卷类型id **/
	private String examTypeId;

}

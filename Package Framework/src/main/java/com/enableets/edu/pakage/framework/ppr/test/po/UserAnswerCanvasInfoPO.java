package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * user_answer_canvas_info
 */
@Entity
@Table(name = "user_answer_canvas_info")
@Data
@Accessors(chain = true)
public class UserAnswerCanvasInfoPO {

	@Column(name = "canvas_id")
	private String canvasId;

	@Column(name = "answer_id")
	private String answerId;

	@Column(name = "canvas_type")
	private String canvasType;

	@Column(name = "canvas_order")
	private Integer canvasOrder;

	@Column(name = "content")
	private String content;

	@Column(name = "creator")
	private String creator;

	@Column(name = "create_time")
	private Date createTime;

	@Column(name = "updator")
	private String updator;

	@Column(name = "update_time")
	private Date updateTime;

	@Column(name = "canvas_answer_type")
	private String canvasAnswerType;

	@Column(name = "file_id")
	private String fileId;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "content_id")
	private String contentId;

	@Column(name = "url")
	private String url;

	@Transient
	private String sourceFileId;

}

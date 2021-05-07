package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "test_info")
@Data
public class TestInfoPO {

	@Id
	@Column(name="test_id")
	private String testId;

	@Column(name="activity_id")
	private String activityId;

	@Column(name="activity_type")
	private String activityType;

	@Column(name="school_id")
	private String schoolId;

	@Column(name="term_id")
	private String termId;

	@Column(name="grade_code")
	private String gradeCode;

	@Column(name="grade_name")
	private String gradeName;

	@Column(name="subject_code")
	private String subjectCode;

	@Column(name="subject_name")
	private String subjectName;

	@Column(name="test_name")
	private String testName;

	@Column(name="exam_id")
	private String examId;

	@Column(name="file_id")
	private String fileId;

	@Column(name="mark_type")
	private String markType;

	@Column(name="test_type")
	private String testType;

	@Column(name="test_publish_type")
	private String testPublishType;

	@Column(name="test_publish_time")
	private java.util.Date testPublishTime;

	@Column(name="test_time")
	private java.util.Date testTime;

	@Column(name="start_time")
	private java.util.Date startTime;

	@Column(name="end_time")
	private java.util.Date endTime;

	@Column(name="start_submit_time")
	private java.util.Date startSubmitTime;

	@Column(name="end_submit_time")
	private java.util.Date endSubmitTime;

	@Column(name="sender")
	private String sender;

	@Column(name="sender_name")
	private String senderName;

	@Column(name="creator")
	private String creator;

	@Column(name="create_time")
	private java.util.Date createTime;

	@Column(name="updator")
	private String updator;

	@Column(name="update_time")
	private java.util.Date updateTime;

	@Column(name="del_status")
	private String delStatus;

	@Column(name="test_cost_time")
	private Float testCostTime;

	@Column(name="app_id")
	private String appId;

	@Column(name="delay_submit")
	private Integer delaySubmit;

	@Column(name="resubmit")
	private Integer resubmit;

	@Column(name="from")
	private String from;

	@Column(name = "exam_name")
	private String examName;
}

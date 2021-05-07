package com.enableets.edu.pakage.framework.ppr.test.po;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.util.Date;
import lombok.Data;

@Entity
@Table(name = "test_recipient_info")
@Data
public class TestRecipientInfoPO {

	@Column(name="test_id")
	private Long testId;

	@Column(name="user_id")
	private String userId;

	@Column(name="user_name")
	private String userName;

	@Column(name="school_id")
	private String schoolId;

	@Column(name="school_name")
	private String schoolName;

	@Column(name="term_id")
	private String termId;

	@Column(name="term_name")
	private String termName;

	@Column(name="grade_code")
	private String gradeCode;

	@Column(name="grade_name")
	private String gradeName;

	@Column(name="class_id")
	private String classId;

	@Column(name="class_name")
	private String className;

	@Column(name="group_id")
	private String groupId;

	@Column(name="group_name")
	private String groupName;

	@Column(name="creator")
	private String creator;

	@Column(name="create_time")
	private Date createTime;

	@Column(name="updator")
	private String updator;

	@Column(name="update_time")
	private Date updateTime;
}

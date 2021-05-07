package com.enableets.edu.pakage.framework.ppr.test.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @Author: gary_zhang@enable-ets.com
 * @Date: 2021/1/19 15:16
 * @Description: Answer card timeline information
 */

@Data
@Entity
@Table(name = "answer_card_timeline")
public class CardTimeAxisPO {
    @Column(name = "timeline_id")
    private Long timelineId;

    @Column(name = "answer_card_id")
    private Long answerCardId;

    @Column(name = "trigger_time")
    private Integer triggerTime;

    @Column(name = "node_id")
    private Long nodeId;

    @Column(name = "question_id")
    private Long questionId;

    @Column(name = "parent_node_id")
    private Long parentNodeId;

    @Column(name = "parent_id")
    private Long parentId;

    @Column(name = "sequencing")
    private Long sequencing;

    @Column(name = "page_no")
    private Integer pageNo;

    @Column(name = "type_code")
    private String typeCode;

    @Column(name = "type_name")
    private String typeName;

    @Column(name = "option_count")
    private Long optionCount;

    @Column(name = "row_count")
    private Long rowCount;

    @Column(name = "creator")
    private String creator;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "updator")
    private String updator;

    @Column(name = "update_time")
    private Date updateTime;
}

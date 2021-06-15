package com.enableets.edu.pakage.framework.ppr.paper.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.sql.Timestamp;

/**
 * @author tony_liu@enable-ets.com
 * @since 2021/5/17
 **/
@Entity
@Table(name = "exam_stype_info")
@Data
public class ExamStypeInfoPO {
    @Id()
    @Column(name = "exam_id")
    private String examId;
    //副标题
    @Column(name = "subtitle")
    private String subtitle;
    // 0：无 1：有 保密标记'
    @Column(name = "secrecy_symbol")
    private Integer secrecySymbol;
    //0：无 1：有  考生输入栏',
    @Column(name = "stu_info_column")
    private Integer stuInfoColumn;
    //注意事项
    @Column(name = "matters_attention")
    private String mattersAttention;
    //试卷信息
    @Column(name = "test_Info")
    private String testInfo;
    //誉分栏，0：无誉分栏，1：有誉分栏
    @Column(name = "record_score_column")
    private Integer recordScoreColumn;
    @Column(name = "binding_line")
    private Integer bindingLine;
    @Column(name = "creator")
    private String creator;
    @Column(name = "updator")
    private String updator;
    @Column(name = "create_time")
    private Timestamp createTime;
    @Column(name = "update_time")
    private Timestamp updateTime;
    @Column(name = "sum_bigtopic")
    private  String sumBigtopic;


}

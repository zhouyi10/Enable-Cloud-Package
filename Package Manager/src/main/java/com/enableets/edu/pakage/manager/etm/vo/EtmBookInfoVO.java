package com.enableets.edu.pakage.manager.etm.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.dozer.Mapping;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
public class EtmBookInfoVO {

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

    @Mapping(value = "stage.code")
    private String stageCode;

    @Mapping(value = "stage.name")
    private String stageName;

    /** Grade Info  */
    @Mapping(value = "grade.code")
    private String gradeCode;

    @Mapping(value = "grade.name")
    private String gradeName;

    /** Subject Info  */
    @Mapping(value = "subject.code")
    private String subjectCode;

    @Mapping(value = "subject.name")
    private String subjectName;

    @Mapping(value = "textBookVersion.code")
    private  String textBookVersionCode;

    @Mapping(value = "textBookVersion.name")
    private  String textBookVersionName;

    @Mapping(value = "term.code")
    private  String termCode;

    @Mapping(value = "term.name")
    private  String termName;

    @Mapping(value = "user.id")
    private String userId;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private String contentId;

    private String providerCode;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** ETM page node*/
    private List<PageVO> pageList;

}

package com.enableets.edu.pakage.manager.ppr.vo;

import org.dozer.Mapping;

import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/03
 **/
@Data
public class PaperInfoVO {

    /** Paper ID  */
    private String paperId;

    /** Paper Name */
    private String name;

    /***/
    private String paperType;

    /** Stage Info  */
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

    @Mapping(value = "materialVersion.id")
    private String materialVersion;

    private List<String> searchCodes;

    /** Paper Total Score */
    private Float totalPoints;

    /** Paper Nodes  */
    private List<PaperNodeInfoVO> nodes;

    /**Knowledge */
    private List<KnowledgeInfoVO> knowledges;

    /** User ID*/
    private String userId;

    /** */
    private List<FileInfoVO> files;

    /** */
    private AddAnswerCardInfoVO answerCard;

    /***/
    private String opener;

    /***/
    private String subtitle;

    private  String examStypeinfoPO;

    private  String sumBigtopic;
}

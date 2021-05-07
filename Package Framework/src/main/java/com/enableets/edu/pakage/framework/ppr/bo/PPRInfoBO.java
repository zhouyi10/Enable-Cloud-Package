package com.enableets.edu.pakage.framework.ppr.bo;

import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.bo.IdNameMapBO;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
public class PPRInfoBO {
    
    /** Paper ID */
    private Long paperId;

    /** Paper Name */
    private String name;

    /** Stage Info  */
    private CodeNameMapBO stage;

    /** Grade Info  */
    private CodeNameMapBO grade;

    /** Subject Info  */
    private CodeNameMapBO subject;

    /** Total Score */
    private Float totalPoints;

    /** Test paper node information  */
    private List<PPRNodeInfoBO> nodes;

    /** User Info  */
    private IdNameMapBO user;

    /** School Info  */
    private IdNameMapBO school;

    /** Provider Code */
    private String providerCode;

    /** Textbook version*/
    private IdNameMapBO materialVersion;

    /** Resource ID */
    private Long contentId;

    /***/
    private Long answerCostTime;

    /** Zone Info  */
    private CodeNameMapBO zone;

    /** Knowledge Info  */
    private List<QuestionKnowledgeInfoBO> knowledges;

    /** Files */
    private List<FileInfoBO> files;

    /** Answer Card*/
    private AnswerCardInfoBO answerCard;

    /***/
    private String paperType;

    /***/
    private Date createTime;
}

package com.enableets.edu.sdk.ppr.ppr.core.question;

import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;

import java.util.List;

/**
 * question.xml XStream description
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
@Data
@XStreamAlias("question")
public class Question extends EntityToXml{

    public Question(){
        this.affixId = "";
        this.answer = "";
        this.answerContent = "";
        this.description = "";
        this.listen = "";
        this.questionContent = "";
        this.questionContentNoHtml = "";
        this.questionDifficulty = "";
        this.questionType = "";
        this.questionTypeId = "";
        this.questionTypeName = "";
        this.knowledgeId = "";
        this.knowledgeName = "";
    }

    @XStreamAsAttribute
    private String questionId;

    @XStreamAsAttribute
    private String answer;

    @XStreamAsAttribute
    private String answerContent;

    @XStreamAsAttribute
    private String estimateTime;

    @XStreamAsAttribute
    private String description;

    @XStreamAsAttribute
    private String listen;

    @XStreamAsAttribute
    private String questionContent;

    @XStreamAsAttribute
    private String questionContentNoHtml;

    @XStreamAsAttribute
    private String questionDifficulty;

    @XStreamAsAttribute
    private String questionType;

    @XStreamAsAttribute
    private String questionTypeId;

    @XStreamAsAttribute
    private String questionTypeName;

    @XStreamAsAttribute
    private Float score;

    @XStreamAsAttribute
    private String knowledgeId;

    @XStreamAsAttribute
    private String knowledgeName;

    @XStreamAsAttribute
    private String affixId;

    @XStreamImplicit
    private List<QuestionOption> options;
}

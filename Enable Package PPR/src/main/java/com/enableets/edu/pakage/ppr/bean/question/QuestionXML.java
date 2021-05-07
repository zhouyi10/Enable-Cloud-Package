package com.enableets.edu.pakage.ppr.bean.question;

import com.enableets.edu.pakage.core.bean.IEnableXmlPackage;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;
import lombok.Data;

/**
 * question.xml XStream description
 * @author walle_yu@enable-ets.com
 * @since 2020/06/18
 **/
@Data
@XStreamAlias("question")
public class QuestionXML implements IEnableXmlPackage {

    public QuestionXML(){
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

    @Override
    public Class[] buildXmlClasses() {
        return new Class[]{
                QuestionXML.class,
                QuestionOption.class,
                Option.class
        };
    }
}

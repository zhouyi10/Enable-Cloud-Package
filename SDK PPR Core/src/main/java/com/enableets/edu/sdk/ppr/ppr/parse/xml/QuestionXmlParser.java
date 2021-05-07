package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.core.question.Option;
import com.enableets.edu.sdk.ppr.ppr.core.question.Question;
import com.enableets.edu.sdk.ppr.ppr.core.question.QuestionOption;
import com.enableets.edu.sdk.ppr.xml.xpath.XNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class QuestionXmlParser extends AbstractXmlParser<Question, Question> {

    public QuestionXmlParser(String questionXml){
        super(questionXml);
        setIgnoreVersion(Boolean.FALSE);
    }

    public QuestionXmlParser(File file){
        super(file);
        setIgnoreVersion(Boolean.FALSE);
    }

    @Override
    public Question parse() throws PPRVersionMismatchException {
        if (xPathParser == null) return null;
        XNode questionXNode = xPathParser.evalNode("/question");
        if (questionXNode == null) return null;
        Question question = new Question();
        question.setQuestionId(questionXNode.getAttribute("questionId"));
        question.setAnswer(questionXNode.getAttribute("answer"));
        question.setAnswerContent(questionXNode.getAttribute("answerContent"));
        question.setDescription(questionXNode.getAttribute("description"));
        question.setListen(questionXNode.getAttribute("listen"));
        question.setQuestionContent(questionXNode.getAttribute("questionContent"));
        question.setQuestionContentNoHtml(questionXNode.getAttribute("questionContentNoHtml"));
        question.setQuestionDifficulty(questionXNode.getAttribute("questionDifficulty"));
        question.setQuestionType(questionXNode.getAttribute("questionType"));
        question.setQuestionTypeId(questionXNode.getAttribute("questionTypeId"));
        question.setQuestionTypeName(questionXNode.getAttribute("questionTypeName"));
        question.setScore(Float.valueOf(questionXNode.getAttribute("score")));
        question.setKnowledgeId(questionXNode.getAttribute("knowledgeId"));
        question.setKnowledgeName(questionXNode.getAttribute("knowledgeName"));
        question.setAffixId(questionXNode.getAttribute("affixId"));
        List<XNode> optionXNodes = questionXNode.evalNodes("./questionOption");
        if (optionXNodes != null && optionXNodes.size() > 0){
            List<QuestionOption> options = new ArrayList<>();
            for (XNode optionXNode : optionXNodes) {
                QuestionOption questionOption = new QuestionOption();
                questionOption.setOptionId(optionXNode.getAttribute("optionId"));
                questionOption.setQuestionId(optionXNode.getAttribute("questionId"));
                XNode xNode = optionXNode.evalNode("./options");
                if (xNode != null){
                    questionOption.setOption(new Option(xNode.getAttribute("optionContent"), xNode.getAttribute("optionId"), xNode.getAttribute("optionOrder"), xNode.getAttribute("optionTitle")));
                }
                options.add(questionOption);
            }
            question.setOptions(options);
        }
        return question;
    }

    @Override
    public Question parseBO() throws PPRVersionMismatchException {
        return parse();
    }
}

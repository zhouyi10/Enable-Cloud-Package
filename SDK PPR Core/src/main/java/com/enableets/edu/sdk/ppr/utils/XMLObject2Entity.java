package com.enableets.edu.sdk.ppr.utils;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.sdk.ppr.core.BeanUtils;
import com.enableets.edu.sdk.ppr.ppr.bo.CodeNameMapBO;
import com.enableets.edu.sdk.ppr.ppr.bo.IdNameMapBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.AnswerCardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardAxisBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.ActionMapper;
import com.enableets.edu.sdk.ppr.ppr.bo.card.action.StepActionBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.AnswerTrailBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.answer.CanvasBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.FileInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.KnowledgeInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.NodeInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionAnswerInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionOptionInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionStemInfoBO;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.PaperFiles;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Question;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Section;
import com.enableets.edu.sdk.ppr.ppr.core.paper.TestPart;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.ActionItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Answer;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Axis;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Layout;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.LayoutQuestion;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.enableets.edu.sdk.ppr.ppr.core.question.QuestionOption;
import com.enableets.edu.sdk.ppr.ppr.parse.exceptions.PPRParserException;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.val;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/22
 **/
public class XMLObject2Entity {

    public static PPRBO tranPaperXML(PaperXML paperXML) {
        if (paperXML == null || paperXML.getHeader() == null || paperXML.getHeader().getProperty() == null || ObjectUtil.isNull(paperXML.getHeader().getProperty().getItems()) ||
                ObjectUtil.isNull(paperXML.getBody().getTestParts())) {
            return null;
        }
        List<Item> items = paperXML.getHeader().getProperty().getItems();
        PPRBO paper = new PPRBO();
        paper.setStage(new CodeNameMapBO());
        paper.setGrade(new CodeNameMapBO());
        paper.setSubject(new CodeNameMapBO());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Item item : items) {
            if (item.getKey().equals("id")) paper.setPaperId(Long.valueOf(item.getValue()));
            else if (item.getKey().equals("title")) paper.setName(item.getValue());
            else if (item.getKey().equals("stageCode")) paper.getStage().setCode(item.getValue());
            else if (item.getKey().equals("stageName")) paper.getStage().setName(item.getValue());
            else if (item.getKey().equals("gradeCode")) paper.getGrade().setCode(item.getValue());
            else if (item.getKey().equals("gradeName")) paper.getGrade().setName(item.getValue());
            else if (item.getKey().equals("subjectCode")) paper.getSubject().setCode(item.getValue());
            else if (item.getKey().equals("subjectName")) paper.getSubject().setName(item.getValue());
            else if (item.getKey().equals("author")) paper.setUser(new IdNameMapBO("", item.getValue()));
            else if (item.getKey().equals("createTime")) {
                try {
                    paper.setCreateTime(sdf.parse(item.getValue()));
                } catch (ParseException e) {

                }
            }
        }
        List<TestPart> testParts = paperXML.getBody().getTestParts();
        for (TestPart testPart : testParts) {
            tranPaperTestPartXml(testPart, paper, paperXML);
        }
        paper.setFiles(tranPaperFile(paperXML.getFiles()));
        return paper;
    }

    private static List<FileInfoBO> tranPaperFile(PaperFiles files){
        if (files == null || CollectionUtils.isEmpty(files.getItems())) return null;
        else {
            List<FileInfoBO> paperFiles = new ArrayList<>();
            for (FileItem item : files.getItems()) {
                if (item.getFormat().equals("xml")) continue;
                else{
                    item.getHrefs().forEach(e -> {
                        paperFiles.add(new FileInfoBO(e.getHref()));
                    });
                }
            }
            return paperFiles;
        }
    }

    public static CardBO tranPaperCardXML(PaperCardXML paperCardXML) {
        if (paperCardXML == null || paperCardXML.getHeader() == null || paperCardXML.getHeader().getProperty() == null || CollectionUtil.isEmpty(paperCardXML.getHeader().getProperty().getItems())
                ) {  // || CollectionUtil.isEmpty(paperCardXML.getBody().getLayout().getQuestions())
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Item> items = paperCardXML.getHeader().getProperty().getItems();
        CardBO cardBO = new CardBO();
        cardBO.setStage(new CodeNameMapBO());
        cardBO.setGrade(new CodeNameMapBO());
        cardBO.setSubject(new CodeNameMapBO());
        cardBO.setUser(new IdNameMapBO());
        for (Item item : items) {
            if (item.getKey().equals("id")){
                cardBO.setPaperId(item.getValue());
            } else if (item.getKey().equals("title")) cardBO.setName(item.getValue());
            else if (item.getKey().equals("stageCode")) cardBO.getStage().setCode(item.getValue());
            else if (item.getKey().equals("stageName")) cardBO.getStage().setName(item.getValue());
            else if (item.getKey().equals("gradeCode")) cardBO.getGrade().setCode(item.getValue());
            else if (item.getKey().equals("gradeName")) cardBO.getGrade().setName(item.getValue());
            else if (item.getKey().equals("subjectCode")) cardBO.getSubject().setCode(item.getValue());
            else if (item.getKey().equals("subjectName")) cardBO.getSubject().setName(item.getValue());
            else if (item.getKey().equals("author")) cardBO.setUser(new IdNameMapBO("", item.getValue()));
            else if (item.getKey().equals("createTime")) {
                try {
                    cardBO.setCreateTime(sdf.parse(item.getValue()));
                } catch (ParseException e) {

                }
            }
        }
        if (paperCardXML.getBody() == null) return cardBO;
        Layout layout = paperCardXML.getBody().getLayout();
        tranPaperCardLayoutXml(layout, cardBO);
        if (ObjectUtil.isNotNull(paperCardXML.getBody().getAction()) && CollectionUtil.isNotEmpty(paperCardXML.getBody().getAction().getItems())) {
            tranPaperCardAction(paperCardXML.getBody().getAction().getItems(), cardBO);
        }
        if (ObjectUtil.isNotNull(paperCardXML.getBody().getAnswerCard()) && CollectionUtil.isNotEmpty(paperCardXML.getBody().getAnswerCard().getAnswerItems())) {
            tranPaperCardAnswers(paperCardXML.getBody().getAnswerCard().getAnswerItems(), cardBO);
        }
        return cardBO;
    }

    private static void tranPaperTestPartXml(TestPart testPart, PPRBO pprBo, PaperXML paperXML) {
        if (CollectionUtils.isEmpty(pprBo.getNodes())) pprBo.setNodes(new ArrayList<NodeInfoBO>());
        NodeInfoBO node = new NodeInfoBO();
        node.setNodeId(testPart.getId());
        node.setName(testPart.getTitle());
        node.setLevel(Constants.TEST_PART_LEVEL);
        for (Section section : testPart.getSections()) {
            tranPaperSectionXml(section, node.getNodeId(), pprBo, paperXML);
        }
        pprBo.getNodes().add(node);
    }

    private static void tranPaperSectionXml(Section section, String parentId, PPRBO pprBo, PaperXML paperXML) {
        NodeInfoBO node = new NodeInfoBO();
        pprBo.getNodes().add(node);
        node.setNodeId(section.getId());
        node.setParentNodeId(parentId);
        node.setName(section.getTitle());
        node.setLevel(Constants.SECTION_LEVEL);
        if (CollectionUtil.isEmpty(section.getQuestions())) return;
        for (Question question : section.getQuestions()) {
            tranPaperQuestionXml(question, node.getNodeId(), Constants.QUESTION_LEVEL, pprBo, paperXML);
        }
    }

    private static void tranPaperQuestionXml(Question question, String parentId, int level, PPRBO pprBo, PaperXML paperXML) {
        if (question == null) return ;
        NodeInfoBO node = new NodeInfoBO();
        pprBo.getNodes().add(node);
        node.setNodeId(question.getId());
        node.setParentNodeId(parentId);
        node.setLevel(level);
        if (StringUtils.isNotBlank(question.getId())) node.setQuestion(tranQuestion(question.getValue(), pprBo, paperXML));
        if (CollectionUtil.isEmpty(question.getChildren())) return;
        for (Question childQuestion : question.getChildren()) {
            tranPaperQuestionXml(childQuestion, node.getNodeId(), Constants.QUESTION_CHILD_LEVEL, pprBo, paperXML);
        }
    }

    private static QuestionInfoBO tranQuestion(String questionId, PPRBO pprBo, PaperXML paperXML) {
        Map<String, com.enableets.edu.sdk.ppr.ppr.core.question.Question> questionMap = paperXML.getQuestionMap();
        if (StringUtils.isBlank(questionId) || CollectionUtil.isEmpty(questionMap)) return null;
        com.enableets.edu.sdk.ppr.ppr.core.question.Question question = questionMap.get(questionId);
        if (question == null) return null;
        QuestionInfoBO questionBO = new QuestionInfoBO();
        questionBO.setQuestionId(question.getQuestionId());
        questionBO.setType(new CodeNameMapBO(question.getQuestionTypeId(), question.getQuestionTypeName()));
        questionBO.setDifficulty(new CodeNameMapBO(question.getQuestionDifficulty(), ""));
        val stem = new QuestionStemInfoBO();
        stem.setPlaintext(question.getQuestionContentNoHtml());
        stem.setRichText(question.getQuestionContent());
        questionBO.setStem(stem);
        val answer = new QuestionAnswerInfoBO();
        answer.setLabel(question.getAnswer());
        answer.setAnalysis(question.getAnswerContent());
        questionBO.setAnswer(answer);
        questionBO.setAffixId(question.getAffixId());
        questionBO.setPoints(question.getScore());

        questionBO.setOptions(tranOption(question));
        questionBO.setKnowledges(tranKnowledge(question));
        return questionBO;
    }

    private static List<KnowledgeInfoBO> tranKnowledge(com.enableets.edu.sdk.ppr.ppr.core.question.Question parse) {
        if (StringUtils.isBlank(parse.getKnowledgeId())) return null;
        List<KnowledgeInfoBO> knowledgeList = new ArrayList<>();
        String[] knowledgeIds = parse.getKnowledgeId().split("@@@");
        String[] knowledgeNames = parse.getKnowledgeName().split("@@@");
        for (int i = 0; i < knowledgeIds.length; i++) {
            String knowledgeId = knowledgeIds[i];
            String knowledgeName = knowledgeNames[i];
            val knowledgeInfoBO = new KnowledgeInfoBO();
            knowledgeList.add(knowledgeInfoBO);
            knowledgeInfoBO.setKnowledgeId(knowledgeId);
            knowledgeInfoBO.setKnowledgeName(knowledgeName);
        }
        return knowledgeList;
    }

    private static List<QuestionOptionInfoBO> tranOption(com.enableets.edu.sdk.ppr.ppr.core.question.Question parse) {
        if (CollectionUtil.isEmpty(parse.getOptions())) return null;

        List<QuestionOptionInfoBO> options = new ArrayList<>();
        for (QuestionOption option : parse.getOptions()) {
            val o = option.getOption();
            if (o == null) continue;
            val optionBO = new QuestionOptionInfoBO();
            optionBO.setOptionId(o.getOptionId());
            optionBO.setOptionContent(o.getOptionContent());
            optionBO.setOptionTitle(o.getOptionTitle());
            optionBO.setSequencing(o.getOptionOrder());
            options.add(optionBO);
        }
        return options;
    }

    private static void tranPaperCardLayoutXml(Layout layout, CardBO cardBO) {
        AnswerCardBO answerCard = null;
        if (layout != null && CollectionUtils.isNotEmpty(layout.getQuestions())){
            answerCard = new AnswerCardBO();
            List<CardAxisBO> axises = new ArrayList<>();
            answerCard.setPageType(layout.getPageType());
            for (LayoutQuestion question : layout.getQuestions()) {
                List<Axis> axisXmls = question.getAxises();
                for (Axis axisXml : axisXmls) {
                    CardAxisBO axis = new CardAxisBO();
                    axis.setQuestionId(axisXml.getId());
                    axis.setParentId(question.getId());
                    axis.setXAxis(axisXml.getXAxis());
                    axis.setYAxis(axisXml.getYAxis());
                    axis.setWidth(axisXml.getWidth());
                    axis.setHeight(axisXml.getHeight());
                    axis.setPageNo(axisXml.getPageNo());
                    axises.add(axis);
                }
            }
            answerCard.setAxises(axises);
        }
        cardBO.setAnswerCard(answerCard);
    }

    private static void tranPaperCardAction(List<ActionItem> ActionItems, CardBO cardBO) {
        cardBO.setActions(new ArrayList<>());
        for (ActionItem actionItem : ActionItems) {
            if (StringUtils.isBlank(actionItem.getName())) throw new PPRParserException("actionItem name cannot null!");
            if (CollectionUtil.isEmpty(actionItem.getProperty().getItems())) throw new PPRParserException("actionItem action Items cannot null!");
            StepActionBO stepAction = Utils.create(actionItem.getProperty().getItems(), StepActionBO.class);;
            if (stepAction == null) return;
            stepAction.setType(ActionMapper.getActionType(actionItem.getName()));
            stepAction.setId(actionItem.getId());
            stepAction.setDescription(actionItem.getDescription());
            cardBO.getActions().add(stepAction);
        }
    }

    private static void tranPaperCardAnswers(List<AnswerItem> answerItems, CardBO cardBO) {
        cardBO.setAnswers(new ArrayList<>());
        for (AnswerItem answerItemXml : answerItems) {
            for (Answer xml : answerItemXml.getAnswers()) {
                AnswerBO answerBO = new AnswerBO();
                cardBO.getAnswers().add(answerBO);
                answerBO.setQuestionId(xml.getId());
                if (!xml.getId().equals(answerItemXml.getId())) {
                    answerBO.setParentId(answerItemXml.getId());
                }
                answerBO.setAnswer(xml.getText());
                if (xml.getTrail() != null && CollectionUtil.isNotEmpty(xml.getTrail().getTimestamps())) {
                    List<AnswerTrailBO> trailS = BeanUtils.convert(xml.getTrail().getTimestamps(), AnswerTrailBO.class);
                    answerBO.setTrails(trailS);
                }
                if (CollectionUtil.isNotEmpty(xml.getFiles())) {
                    answerBO.setCanvases(BeanUtils.convert(xml.getFiles(), CanvasBO.class));
                }
            }
        }
    }
}

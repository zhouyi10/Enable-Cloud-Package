package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Action;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.ActionItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Answer;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerCard;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.AnswerItem;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Axis;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Body;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Layout;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.LayoutQuestion;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Timestamp;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Trail;
import com.enableets.edu.sdk.ppr.utils.XMLObject2Entity;
import com.enableets.edu.sdk.ppr.xml.xpath.XNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class PaperCardXmlParser extends AbstractXmlParser<PaperCardXML, CardBO> {

    public PaperCardXmlParser(String paperCardXmlStr){
       super(paperCardXmlStr);
       setIgnoreVersion(Boolean.FALSE);
    }

    public PaperCardXmlParser(File file){
        super(file);
        setIgnoreVersion(Boolean.FALSE);
    }

    @Override
    public PaperCardXML parse() throws PPRVersionMismatchException {
        if (xPathParser == null) return null;
        PaperCardXML paperCardXML = new PaperCardXML();
        paperCardXML.setHeader(commonNodeParse.parseHeader(xPathParser.evalNode("/paperCard/header")));
        paperCardXML.setBody(parseBody(xPathParser.evalNode("/paperCard/body")));
        return paperCardXML;
    }

    @Override
    public CardBO parseBO() throws PPRVersionMismatchException {
        return XMLObject2Entity.tranPaperCardXML(parse());
    }

    private Body parseBody(XNode xNode){
        if (xNode == null) return null;
        Body body = new Body();
        body.setLayout(parseLayout(xNode.evalNode("./layout")));
        body.setAction(parseAction(xNode.evalNode("./action")));
        body.setAnswerCard(parseAnswerCard(xNode.evalNode("./answerCard")));
        return body;
    }

    private Layout parseLayout(XNode xNode) {
        Layout layout = new Layout();
        if (xNode == null) return layout;
        layout.setPageType(xNode.getAttribute("pageType"));
        List<XNode> xNodes = xNode.evalNodes("./question");
        if (xNodes == null || xNodes.size() == 0) return layout;
        List<LayoutQuestion> questions = new ArrayList<>();
        for (XNode questionXNode : xNodes) {
            questions.add(parseLayoutQuestion(questionXNode));
        }
        layout.setQuestions(questions);
        return layout;
    }

    private LayoutQuestion parseLayoutQuestion(XNode xNode){
        LayoutQuestion question = new LayoutQuestion();
        question.setId(xNode.getAttribute("id"));
        List<XNode> xNodes = xNode.evalNodes("./axis");
        if (xNodes == null || xNodes.size() == 0) return question;
        List<Axis> axises = new ArrayList<>();
        for (XNode node : xNodes) {
            axises.add(new Axis(node.getAttribute("id"), Double.valueOf(node.getAttribute("xAxis")), Double.valueOf(node.getAttribute("yAxis")), Double.valueOf(node.getAttribute("width")), Double.valueOf(node.getAttribute("height")), Long.valueOf(node.getAttribute("pageNo"))));
        }
        question.setAxises(axises);
        return question;
    }

    private Action parseAction(XNode xNode) {
        Action action = new Action();
        if (xNode == null) return action;
        List<XNode> xNodes = xNode.evalNodes("./item");
        if (xNodes == null || xNodes.size() == 0) return action;
        List<ActionItem> items = new ArrayList<>();
        for (XNode node : xNodes) {
            items.add(parseActionItem(node));
        }
        action.setItems(items);
        return action;
    }

    private ActionItem parseActionItem(XNode node) {
        ActionItem item = new ActionItem(node.getAttribute("id"), node.getAttribute("name"), node.getAttribute("description"));
        List<XNode> xNodes = node.evalNodes("./property/item");
        List<Item> items = new ArrayList<>();
        for (XNode xNode : xNodes) {
            items.add(new Item(xNode.getAttribute("key"), xNode.getBody()));
        }
        item.setProperty(new Property(items));
        return item;
    }

    private AnswerCard parseAnswerCard(XNode xNode) {
        if (xNode == null) return null;
        AnswerCard answerCard = new AnswerCard();
        List<XNode> xNodes = xNode.evalNodes("./answerItem");
        if (xNodes == null || xNodes.size() == 0) return answerCard;
        List<AnswerItem> answerItems = new ArrayList<>();
        for (XNode node : xNodes) {
            answerItems.add(parseAnswerItem(node));
        }
        answerCard.setAnswerItems(answerItems);
        return answerCard;
    }

    private AnswerItem parseAnswerItem(XNode node) {
        AnswerItem item = new AnswerItem();
        item.setId(Long.valueOf(node.getAttribute("id")));
        List<Answer> answers = new ArrayList<>();
        List<XNode> xNodes = node.evalNodes("./answer");
        for (XNode xNode : xNodes) {
            answers.add(parseAnswer(xNode));
        }
        item.setAnswers(answers);
        return item;
    }

    private Answer parseAnswer(XNode xNode) {
        Answer answer = new Answer();
        answer.setId(Long.valueOf(xNode.getAttribute("id")));
        XNode textXNode = xNode.evalNode("./text");
        if (textXNode != null) answer.setText(textXNode.getBody());
        XNode filesXNode = xNode.evalNode("./files");
        if (filesXNode != null) {
            List<FileItem> files = new ArrayList<>();
            List<XNode> itemsXNode = filesXNode.evalNodes("./item");
            for (XNode node : itemsXNode) {
                files.add(new FileItem(node.getAttribute("format"), node.getAttribute("fileId"),
                        node.getAttribute("fileName"), node.getAttribute("md5"), node.getAttribute("url")));
            }
            answer.setFiles(files);
        }
        XNode trailXNode = xNode.evalNode("./trail");
        if (trailXNode != null){
            List<Timestamp> timestamps = new ArrayList<>();
            List<XNode> xNodes = trailXNode.evalNodes("./timestamp");
            for (XNode node : xNodes) {
                timestamps.add(new Timestamp(Long.valueOf(node.getAttribute("start")), Long.valueOf(node.getAttribute("end"))));
            }
            answer.setTrail(new Trail(timestamps));
        }
        return answer;
    }
}

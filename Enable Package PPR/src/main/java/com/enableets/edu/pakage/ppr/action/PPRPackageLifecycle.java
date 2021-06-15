package com.enableets.edu.pakage.ppr.action;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.adapter.FileStorageAdapter;
import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.card.bean.EnableCardBeanDefinition;
import com.enableets.edu.pakage.card.bean.EnableCardPackage;
import com.enableets.edu.pakage.card.bean.body.CardBody;
import com.enableets.edu.pakage.card.bean.body.action.Action;
import com.enableets.edu.pakage.card.bean.body.action.ActionItem;
import com.enableets.edu.pakage.card.bean.body.answer.Answer;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerItem;
import com.enableets.edu.pakage.card.bean.body.answer.AnswerQuestion;
import com.enableets.edu.pakage.card.bean.body.answer.Timestamp;
import com.enableets.edu.pakage.card.bean.body.answer.Trail;
import com.enableets.edu.pakage.card.bean.body.layout.Axis;
import com.enableets.edu.pakage.card.bean.body.layout.Layout;
import com.enableets.edu.pakage.card.bean.body.layout.LayoutQuestion;
import com.enableets.edu.pakage.card.bean.body.layout.TimeAxis;
import com.enableets.edu.pakage.card.bean.body.mark.Mark;
import com.enableets.edu.pakage.card.bean.body.mark.MarkItem;
import com.enableets.edu.pakage.card.bean.body.mark.MarkResult;
import com.enableets.edu.pakage.card.bo.CardAxisBO;
import com.enableets.edu.pakage.card.bo.CardTimeAxisBO;
import com.enableets.edu.pakage.card.bo.action.ActionMapper;
import com.enableets.edu.pakage.card.bo.action.StepActionBO;
import com.enableets.edu.pakage.card.bo.answer.AnswerBO;
import com.enableets.edu.pakage.card.bo.answer.CanvasBO;
import com.enableets.edu.pakage.core.action.DefaultPackageLifecycle;
import com.enableets.edu.pakage.core.bean.FileItem;
import com.enableets.edu.pakage.core.bean.Files;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.Href;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.core.core.Constants;
import com.enableets.edu.pakage.core.core.htmlparser.handler.AttachmentDownloadHandler;
import com.enableets.edu.pakage.core.core.htmlparser.handler.IAttachmentHandler;
import com.enableets.edu.pakage.core.core.htmlparser.parser.AttachmentHtmlParser;
import com.enableets.edu.pakage.core.core.xpath.XNode;
import com.enableets.edu.pakage.core.core.xpath.XPathParser;
import com.enableets.edu.pakage.core.core.xpath.XPathParserFactory;
import com.enableets.edu.pakage.core.core.xstream.EntityToXmlUtils;
import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.core.utils.JsonUtils;
import com.enableets.edu.pakage.core.utils.Utils;
import com.enableets.edu.pakage.ppr.bean.EnablePPRBeanDefinition;
import com.enableets.edu.pakage.ppr.bean.EnablePPRPackage;
import com.enableets.edu.pakage.ppr.bean.PPRPackageWrapper;
import com.enableets.edu.pakage.ppr.bean.body.PPRBody;
import com.enableets.edu.pakage.ppr.bean.body.Question;
import com.enableets.edu.pakage.ppr.bean.body.QuestionHref;
import com.enableets.edu.pakage.ppr.bean.body.Section;
import com.enableets.edu.pakage.ppr.bean.body.TestPart;
import com.enableets.edu.pakage.ppr.bean.question.Option;
import com.enableets.edu.pakage.ppr.bean.question.QuestionOption;
import com.enableets.edu.pakage.ppr.bean.question.QuestionXML;
import com.enableets.edu.pakage.ppr.bo.CodeNameMapBO;
import com.enableets.edu.pakage.ppr.bo.FileInfoBO;
import com.enableets.edu.pakage.ppr.bo.IdNameMapBO;
import com.enableets.edu.pakage.ppr.bo.NodeInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionAnswerInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionAxisInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionOptionInfoBO;
import com.enableets.edu.pakage.ppr.bo.QuestionStemInfoBO;
import com.enableets.edu.pakage.ppr.core.PPRConstants;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PPRPackageLifecycle extends DefaultPackageLifecycle {

    private PPRPackageWrapper pprPackageWrapper;
    private EnablePPRBeanDefinition pprBeanDefinition;
    private EnableCardBeanDefinition cardBeanDefinition;

    public PPRPackageLifecycle(){

    }

    public PPRPackageLifecycle(PPRPackageWrapper pprPackageWrapper){
        super(pprPackageWrapper, pprPackageWrapper.getConfiguration());
        this.pprPackageWrapper = pprPackageWrapper;
        this.pprBeanDefinition = pprPackageWrapper.getPprBeanDefinition();
        if (pprPackageWrapper.getEnableCard() != null) {
            this.cardBeanDefinition = pprPackageWrapper.getEnableCard().getCardBeanDefinition();
        }
    }

    @Override
    public void parse() {
        if (pprPackageWrapper == null) return;
        String unZipDestDir = null;
        try {
            //1. Get .PPR File Local Path
            this.downLoad();
            //2. UnZip .PPR
            String localZipPath = pprPackageWrapper.getPackageFileInfo().getLocalZipPath();
            if (StringUtils.isBlank(localZipPath)) return;
            unZipDestDir = this.unZip(localZipPath);
            //3. Parse Header Body Files ...
            this.parsePPRConstruction(unZipDestDir);
            //4. convert package object to ppr BO define
            this.convert2BeanDefinition(unZipDestDir);
            //5.Parse Card Header Body Files
            this.parseCardConstruction(unZipDestDir);
            pprPackageWrapper.setPprBeanDefinition(pprBeanDefinition);
        } finally {
            if (StringUtils.isBlank(unZipDestDir)){
                new File(unZipDestDir).delete();
            }
        }
    }

    public EnableCardPackage parse(String cardXml) {
        XPathParser xPathParser = XPathParserFactory.buildToString(cardXml);
        EnableCardPackage enableCardPackage = new EnableCardPackage();
        enableCardPackage.setHeader(Utils.parseHeader(xPathParser.evalNode("/package/header")));
        enableCardPackage.setBody(new CardBody(this.parseCardLayout(xPathParser.evalNode("/package/body/layout")), this.parseCardAnswer(xPathParser.evalNode("/package/body/answer")), this.parseCardAction(xPathParser.evalNode("/package/body/action")), this.parseCardMark(xPathParser.evalNode("/package/body/mark"))));
        return enableCardPackage;
    }

    private void parseCardConstruction(String unZipDestDir) {
        XPathParser xPathParser = XPathParserFactory.buildToFile(unZipDestDir + "/card/card.xml");
        EnableCardPackage enableCardPackage = new EnableCardPackage();
        enableCardPackage.setHeader(Utils.parseHeader(xPathParser.evalNode("/package/header")));
        enableCardPackage.setBody(new CardBody(this.parseCardLayout(xPathParser.evalNode("/package/body/layout")), this.parseCardAnswer(xPathParser.evalNode("/package/body/answer")), this.parseCardAction(xPathParser.evalNode("/package/body/action")), this.parseCardMark(xPathParser.evalNode("/package/body/mark"))));
        pprPackageWrapper.setEnableCard(new EnableCard(enableCardPackage, pprPackageWrapper.getConfiguration()));
    }

    private Mark parseCardMark(XNode xNode) {
        if (xNode == null) return null;
        List<MarkItem> items = new ArrayList<>();
        List<XNode> xNodes = xNode.evalNodes("./item");
        for (XNode node : xNodes) {
            MarkItem item = new MarkItem();
            item.setId(node.getAttribute("id"));
            MarkResult markResult = new MarkResult();
            XNode markResultXNode = node.evalNode("./markResult");
            markResult.setId(markResultXNode.getAttribute("id"));
            markResult.setScore(Float.valueOf(markResultXNode.getAttribute("score")));
            markResult.setStatus(markResultXNode.getAttribute("status"));
            markResult.setUserId(markResultXNode.getAttribute("userId"));
            markResult.setFullName(markResultXNode.getAttribute("fullName"));
            markResult.setTimestamp(Long.valueOf(markResultXNode.getAttribute("timestamp")));
            markResult.setComment(markResultXNode.evalString("comment"));
            List<XNode> fileItemXNodes = markResultXNode.evalNodes("./files/item");
            if (CollectionUtils.isNotEmpty(fileItemXNodes)){
                markResult.setFiles(this.parseCardFilesItem(fileItemXNodes));
            }
        }
        return new Mark(items);
    }

    private List<com.enableets.edu.pakage.card.bean.body.FileItem> parseCardFilesItem(List<XNode> fileItemXNodes){
        List<com.enableets.edu.pakage.card.bean.body.FileItem> files = new ArrayList<>();
        for (XNode itemXNode : fileItemXNodes) {
            com.enableets.edu.pakage.card.bean.body.FileItem fileItem = new com.enableets.edu.pakage.card.bean.body.FileItem();
            fileItem.setFormat(itemXNode.getAttribute("format"));
            fileItem.setFileId(itemXNode.getAttribute("fileId"));
            fileItem.setFileName(itemXNode.getAttribute("fileName"));
            fileItem.setUrl(itemXNode.getAttribute("url"));
            fileItem.setHref(itemXNode.evalString("href"));
            files.add(fileItem);
        }
        return files;
    }

    private Action parseCardAction(XNode xNode) {
        if (xNode == null) return null;
        List<ActionItem> items = new ArrayList<>();
        List<XNode> itemXNodes = xNode.evalNodes("./item");
        for (XNode itemXNode : itemXNodes) {
            ActionItem item = new ActionItem();
            item.setId(itemXNode.getAttribute("id"));
            item.setType(itemXNode.getAttribute("type"));
            item.setName(itemXNode.getAttribute("name"));
            item.setDescription(itemXNode.getAttribute("description"));
            item.setProperty(Utils.parseProperty(itemXNode.evalNode("./property")));
            items.add(item);
        }
        return new Action(items);
    }

    private Answer parseCardAnswer(XNode xNode) {
        if (xNode == null) return null;
        List<AnswerItem> items = new ArrayList<>();
        List<XNode> itemXNodes = xNode.evalNodes("./answerItem");
        for (XNode itemXNode : itemXNodes) {
            AnswerItem item = new AnswerItem();
            item.setId(itemXNode.getAttribute("id"));
            List<AnswerQuestion> answers = new ArrayList<>();
            List<XNode> questionNodes = itemXNode.evalNodes("./question");
            for (XNode questionNode : questionNodes) {
                AnswerQuestion question = new AnswerQuestion();
                question.setId(questionNode.getAttribute("id"));
                question.setText(questionNode.evalString("./text"));
                List<XNode> filesItems = questionNode.evalNodes("./files/item");
                if (CollectionUtils.isNotEmpty(filesItems)){
                    question.setFiles(this.parseCardFilesItem(filesItems));
                }
                XNode trailXNode = questionNode.evalNode("./trail");
                if (trailXNode != null) {
                    List<XNode> timestampXNodes = trailXNode.evalNodes("timestamp");
                    List<Timestamp> timestamps = new ArrayList<>();
                    for (XNode timestampXNode : timestampXNodes) {
                       timestamps.add(new Timestamp(Long.valueOf(timestampXNode.getAttribute("start")), Long.valueOf(timestampXNode.getAttribute("end"))));
                    }
                    question.setTrail(new Trail(timestamps));
                }
                answers.add(question);
            }
            item.setAnswers(answers);
            items.add(item);
        }
        return new Answer(items);
    }

    private Layout parseCardLayout(XNode xNode) {
        if (xNode == null) return null;
        Layout layout = new Layout();
        layout.setId(xNode.getAttribute("id"));
        layout.setPageType(xNode.getAttribute("pageType"));
        layout.setEdition(xNode.getAttribute("edition"));
        layout.setColumn(xNode.getAttribute("column"));
        List<XNode> questionXNodes = xNode.evalNodes("./question");
        if (CollectionUtils.isNotEmpty(questionXNodes)){
            List<LayoutQuestion> questions = new ArrayList<>();
            for (XNode questionXNode : questionXNodes) {
                LayoutQuestion question = new LayoutQuestion();
                question.setId(questionXNode.getAttribute("id"));
                List<XNode> axisXNodes = questionXNode.evalNodes("./axis");
                List<Axis> axises = new ArrayList<>();
                for (XNode axisXNode : axisXNodes) {
                    Axis axis = new Axis();
                    axis.setId(axisXNode.getAttribute("id"));
                    axis.setType(axisXNode.getAttribute("type"));
                    axis.setName(axisXNode.getAttribute("name"));
                    axis.setXAxis(Double.valueOf(axisXNode.getAttribute("xAxis")));
                    axis.setYAxis(Double.valueOf(axisXNode.getAttribute("yAxis")));
                    axis.setWidth(Double.valueOf(axisXNode.getAttribute("width")));
                    axis.setHeight(Double.valueOf(axisXNode.getAttribute("height")));
                    axis.setPageNo(Long.valueOf(axisXNode.getAttribute("pageNo")));
                    axis.setOptionCount(Long.valueOf(axisXNode.getAttribute("optionCount") == null ? "0" : axisXNode.getAttribute("optionCount")));
                    axis.setRowCount(Long.valueOf(axisXNode.getAttribute("rowCount")));
                    axises.add(axis);
                }
                question.setAxises(axises);
                questions.add(question);
            }
            layout.setQuestions(questions);
        }
        return layout;
    }

    private void convert2BeanDefinition(String unZipDestDir) {
        if (pprBeanDefinition == null) pprBeanDefinition = new EnablePPRBeanDefinition();
        this.convertHeader2ToBeanDefinition();
        this.convertBody2BeanDefinition(unZipDestDir);
        this.convertFiles2BeanDefinition();
    }

    private void convertFiles2BeanDefinition() {
        Files files = pprPackageWrapper.getFiles();
        if (files == null || CollectionUtils.isEmpty(files.getItems())) return;
        List<FileInfoBO> fileInfos = new ArrayList<>();
        for (FileItem item : files.getItems()) {
            if (item.getFormat().equals("xml")) continue;
            for (Href href : item.getHrefs()) {
                FileInfoBO file = new FileInfoBO();
                file.setUrl(href.getHref());
                fileInfos.add(file);
            }
        }
        pprBeanDefinition.setFiles(fileInfos);
    }


    private void convertBody2BeanDefinition(String unZipDestDir) {
        PPRBody body = pprPackageWrapper.getBody();
        if (body == null || CollectionUtils.isEmpty(body.getTestParts())) return;
        List<NodeInfoBO> nodes = new ArrayList<>();
        for (TestPart testPart : body.getTestParts()) {
            NodeInfoBO node = new NodeInfoBO();
            node.setNodeId(testPart.getId());
            node.setName(testPart.getTitle());
            node.setLevel(PPRConstants.TEST_PART_LEVEL);
            nodes.add(node);
            if (CollectionUtils.isNotEmpty(testPart.getSections())) {
                nodes.addAll(this.convertSection2NodeDefinition(testPart.getSections(), node.getNodeId(), unZipDestDir));
            }
        }
        pprBeanDefinition.setNodes(nodes);
    }

    private List<NodeInfoBO> convertSection2NodeDefinition(List<Section> sections, String parentNodeId, String unZipDestDir) {
        List<NodeInfoBO> nodes = new ArrayList<>();
        for (Section section : sections) {
            NodeInfoBO node = new NodeInfoBO();
            node.setNodeId(section.getId());
            node.setName(section.getTitle());
            node.setParentNodeId(parentNodeId);
            node.setLevel(PPRConstants.SECTION_LEVEL);
            nodes.add(node);
            if (CollectionUtils.isNotEmpty(section.getQuestions())){
                nodes.addAll(this.convertQuestion2NodeDefinition(section.getQuestions(), node.getNodeId(), unZipDestDir));
            }
        }
        return nodes;
    }

    private List<NodeInfoBO> convertQuestion2NodeDefinition(List<Question> questions, String parentNodeId, String unZipDestDir) {
        List<NodeInfoBO> nodes = new ArrayList<>();
        for (Question question : questions) {
            NodeInfoBO node = new NodeInfoBO();
            node.setNodeId(question.getId());
            node.setParentNodeId(parentNodeId);
            node.setLevel(PPRConstants.QUESTION_LEVEL);
            node.setExternalNo(question.getTitle());
            node.setQuestion(this.convertQuestion2QuestionDefinition(question.getValue(), question.getHrefs(), unZipDestDir));
            if (CollectionUtils.isNotEmpty(question.getChildren())){
                nodes.addAll(this.convertChildQuestion2NodeDefinition(question.getChildren(), node.getNodeId(), unZipDestDir));
            }
        }
        return nodes;
    }

    private List<NodeInfoBO> convertChildQuestion2NodeDefinition(List<Question> children, String parentNodeId, String unZipDestDir) {
        List<NodeInfoBO> nodes = new ArrayList<>();
        for (Question question : children) {
            NodeInfoBO node = new NodeInfoBO();
            node.setNodeId(question.getId());
            node.setParentNodeId(parentNodeId);
            node.setLevel(PPRConstants.QUESTION_CHILD_LEVEL);
            node.setQuestion(this.convertQuestion2QuestionDefinition(question.getValue(), question.getHrefs(), unZipDestDir));
        }
        return nodes;
    }

    private QuestionInfoBO convertQuestion2QuestionDefinition(String questionId, List<QuestionHref> hrefs, String unZipDestDir) {
        QuestionInfoBO question = new QuestionInfoBO();
        question.setQuestionId(questionId);
        List<QuestionAxisInfoBO> axises = null;
        for (QuestionHref href : hrefs) {
            if (href.getRef().equals(questionId + ".xml")) {
                this.parseQuestionXML(question, unZipDestDir);
            }else{
                if (href.getXAxis() != null){
                    if (axises == null) axises = new ArrayList<>();
                    QuestionAxisInfoBO axis = new QuestionAxisInfoBO();
                    axis.setHeight(href.getHeight());
                    axis.setWidth(href.getWidth());
                    axis.setXAxis(href.getXAxis());
                    axis.setYAxis(href.getYAxis());
                    axis.setUrl(href.getRef());
                    axises.add(axis);
                }
            }
        }
        return question;
    }

    private void parseQuestionXML(QuestionInfoBO question, String unZipDestDir) {
        XPathParser xPathParser = XPathParserFactory.buildToFile(unZipDestDir + "/files/" + question.getQuestionId()  +".xml");
        XNode questionXNode = xPathParser.evalNode("/question");
        if (questionXNode == null) return;
        question.setQuestionId(questionXNode.getAttribute("questionId"));
        QuestionAnswerInfoBO answer = new QuestionAnswerInfoBO();
        answer.setStrategy(questionXNode.getAttribute("answer"));
        answer.setLabel(questionXNode.getAttribute("answerContent"));
        question.setAnswer(answer);
        question.setListen(questionXNode.getAttribute("listen"));
        QuestionStemInfoBO stemInfoBO = new QuestionStemInfoBO();
        stemInfoBO.setRichText(questionXNode.getAttribute("questionContent"));
        stemInfoBO.setPlaintext(questionXNode.getAttribute("questionContentNoHtml"));
        question.setStem(stemInfoBO);
        question.setDifficulty(new CodeNameMapBO(questionXNode.getAttribute("questionDifficulty"), null));
        question.setType(new CodeNameMapBO(questionXNode.getAttribute("questionTypeId"), questionXNode.getAttribute("questionTypeName")));
        question.setPoints(Float.valueOf(questionXNode.getAttribute("score")));
        List<QuestionKnowledgeInfoBO> knowledges = new ArrayList<>();
        String knowledgeId = questionXNode.getAttribute("knowledgeId");
        if (StringUtils.isNotBlank(knowledgeId)) {
            String[] ids = knowledgeId.split("@@@");
            String[] names = questionXNode.getAttribute("knowledgeName").split("@@@");
            for (int i = 0; i < ids.length; i++) {
                knowledges.add(new QuestionKnowledgeInfoBO(ids[i], names[i]));
            }
            question.setKnowledges(knowledges);
        }
        question.setAffixId(questionXNode.getAttribute("affixId"));
        List<XNode> optionXNodes = questionXNode.evalNodes("./questionOption");
        if (optionXNodes != null && optionXNodes.size() > 0){
            List<QuestionOptionInfoBO> options = new ArrayList<>();
            for (XNode optionXNode : optionXNodes) {
                XNode xNode = optionXNode.evalNode("./options");
                if (xNode != null){
                    QuestionOptionInfoBO questionOption = new QuestionOptionInfoBO();
                    questionOption.setOptionId(optionXNode.getAttribute("optionId"));
                    questionOption.setQuestionId(optionXNode.getAttribute("questionId"));
                    questionOption.setLabel(xNode.getAttribute("optionContent"));
                    questionOption.setSequencing(Integer.valueOf(xNode.getAttribute("optionOrder")));
                    questionOption.setAlias(xNode.getAttribute("optionTitle"));
                    options.add(questionOption);
                }
            }
            question.setOptions(options);
        }
    }

    private void convertHeader2ToBeanDefinition() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Property property = pprPackageWrapper.getHeader().getProperty();
        List<Item> items = property.getItems();
        pprBeanDefinition.setStage(new CodeNameMapBO());
        pprBeanDefinition.setGrade(new CodeNameMapBO());
        pprBeanDefinition.setSubject(new CodeNameMapBO());
        pprBeanDefinition.setUser(new IdNameMapBO());
        for (Item item : items) {
            if (item.getKey().equals("id")) pprBeanDefinition.setPaperId(item.getValue());
            else if (item.getKey().equals("title")) pprBeanDefinition.setName(item.getValue());
            else if (item.getKey().equals("stageCode")) pprBeanDefinition.getStage().setCode(item.getValue());
            else if (item.getKey().equals("stageName")) pprBeanDefinition.getStage().setName(item.getValue());
            else if (item.getKey().equals("gradeCode")) pprBeanDefinition.getGrade().setCode(item.getValue());
            else if (item.getKey().equals("gradeName")) pprBeanDefinition.getGrade().setName(item.getValue());
            else if (item.getKey().equals("subjectCode")) pprBeanDefinition.getSubject().setCode(item.getValue());
            else if (item.getKey().equals("subjectName")) pprBeanDefinition.getSubject().setName(item.getValue());
            else if (item.getKey().equals("author")) pprBeanDefinition.setUser(new IdNameMapBO("", item.getValue()));
            else if (item.getKey().equals("createTime")) {
                try {
                    pprBeanDefinition.setCreateTime(sdf.parse(item.getValue()));
                } catch (ParseException e) {

                }
            }
        }
    }

    private void parsePPRConstruction(String unZipDestDir) {
        XPathParser xPathParser = XPathParserFactory.buildToFile(unZipDestDir + "/ppr/" + "ppr.xml");
        pprPackageWrapper.setHeader(Utils.parseHeader(xPathParser.evalNode("/package/header")));
        pprPackageWrapper.setBody(parsePaperBody(xPathParser.evalNode("/package/body")));
        pprPackageWrapper.setFiles(parsePprFiles(xPathParser.evalNode("/package/files")));
    }

    private Files parsePprFiles(XNode xNode) {
        if (xNode == null) return null;
        List<FileItem> items = new ArrayList<>();
        List<XNode> xNodes = xNode.evalNodes("./item");
        for (XNode node : xNodes) {
            FileItem item = new FileItem();
            item.setFormat(node.getAttribute("format"));
            List<Href> hrefs = new ArrayList<>();
            List<XNode> xNodes1 = node.evalNodes("./href");
            for (XNode xNode1 : xNodes1) {
                hrefs.add(new Href(xNode1.getBody()));
            }
            item.setHrefs(hrefs);
            items.add(item);
        }
        return new Files(items);
    }

    private PPRBody parsePaperBody(XNode evalNode) {
        PPRBody pprBody = new PPRBody();
        List<XNode> testPartXNodes = evalNode.evalNodes("./testPart");
        if (testPartXNodes == null || testPartXNodes.size() == 0) return pprBody;
        List<TestPart> testParts = new ArrayList<>();
        for (XNode testPartXNode : testPartXNodes) {
            testParts.add(parseTestPart(testPartXNode));
        }
        pprBody.setTestParts(testParts);
        return pprBody;
    }

    private TestPart parseTestPart(XNode testPartXNode) {
        TestPart testPart = new TestPart();
        testPart.setId(testPartXNode.getAttribute("id"));
        testPart.setTitle(testPartXNode.getAttribute("title"));
        testPart.setScore(Float.valueOf(testPartXNode.getAttribute("score")));
        List<XNode> sectionXNodes = testPartXNode.evalNodes("./section");
        List<Section> sections = new ArrayList<>();
        for (XNode sectionXNode : sectionXNodes) {
            sections.add(parseSection(sectionXNode));
        }
        testPart.setSections(sections);
        return testPart;
    }

    private Section parseSection(XNode sectionXNode) {
        Section section = new Section();
        section.setId(sectionXNode.getAttribute("id"));
        section.setTitle(sectionXNode.getAttribute("title"));
        section.setScore(Float.valueOf(sectionXNode.getAttribute("score")));
        List<Question> questions = new ArrayList<>();
        List<XNode> questionXNodes = sectionXNode.evalNodes("./question");
        for (XNode questionXNode : questionXNodes) {
            questions.add(parseQuestion(questionXNode));
        }
        section.setQuestions(questions);
        return section;
    }

    private Question parseQuestion(XNode questionXNode) {
        Question question = new Question();
        question.setId(questionXNode.getAttribute("id"));
        question.setValue(questionXNode.getAttribute("value"));
        question.setTitle(questionXNode.getAttribute("title"));
        question.setScore(Float.valueOf(questionXNode.getAttribute("score")));
        XNode hrefsXNode = questionXNode.evalNode("hrefs");
        if (hrefsXNode != null){
            List<XNode> hrefXNodes = hrefsXNode.evalNodes("href");
            List<QuestionHref> hrefs = new ArrayList<>();
            for (XNode hrefXNode : hrefXNodes) {
                if (StringUtils.isNotBlank(hrefXNode.getAttribute("questionNo"))) {
                    hrefs.add(new QuestionHref(hrefXNode.getAttribute("ref"), hrefXNode.getAttribute("questionNo")));
                }else if(StringUtils.isNotBlank(hrefXNode.getAttribute("xAxis"))){
                    hrefs.add(new QuestionHref(hrefXNode.getAttribute("ref"), Double.valueOf(hrefXNode.getAttribute("width")), Double.valueOf(hrefXNode.getAttribute("height")), Double.valueOf(hrefXNode.getAttribute("xAxis")), Double.valueOf(hrefXNode.getAttribute("yAxis"))));
                }else{
                    hrefs.add(new QuestionHref(hrefXNode.getAttribute("ref")));
                }
            }
            question.setHrefs(hrefs);
        }
        XNode childrenXNode = questionXNode.evalNode("children");
        if (childrenXNode != null){
            List<XNode> childQuestionXNode = childrenXNode.evalNodes("question");
            List<Question> children = new ArrayList<>();
            for (XNode xNode : childQuestionXNode) {
                children.add(parseQuestion(xNode));
            }
            question.setChildren(children);
        }
        return question;
    }

    private String unZip(String localZipPath){
        String unZipDestDir = new StringBuilder(pprPackageWrapper.getConfiguration().getPPRTempPath()).append("/").append(FileUtil.mainName(localZipPath)).toString();
        ZipUtil.unzip(pprPackageWrapper.getPackageFileInfo().getLocalZipPath(), unZipDestDir);
        return unZipDestDir;
    }

    @Override
    public void build(){
        String pprPathDir = new File(pprPackageWrapper.getConfiguration().getPPRTempPath() + "/" + pprBeanDefinition.getPaperId() + "/").toString();
        try {
            //1.ppr.xml
            pprPackageWrapper.setHeader(new Header(Constants.VERSION, Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, this.getProperties()));
            pprPackageWrapper.setBody(new PPRBody(this.buildBodyTestPart(pprBeanDefinition.getNodes())));
            pprPackageWrapper.setFiles(new Files(this.buildFilesItem()));
            EnablePPRPackage pprPackage = new EnablePPRPackage(pprPackageWrapper.getHeader(), (PPRBody) pprPackageWrapper.getBody(), pprPackageWrapper.getFiles());
            EntityToXmlUtils.toFile(pprPackage, pprPathDir + "/ppr/ppr.xml");
            //2.card.xml
            EnableCardPackage enableCardPackage = new EnableCardPackage();
            enableCardPackage.setHeader(this.buildCardHeader());
            if (cardBeanDefinition != null) {
                enableCardPackage.setBody(this.buildCardBody());
            }
            EntityToXmlUtils.toFile(enableCardPackage, pprPathDir + "/card/card.xml");
            //3.Zip PPR
            this.handlePhysicalFiles();
            this.zipFileAndUpload();
        }finally {
            new File(pprPathDir).delete();
        }
    }

    private Header buildCardHeader() {
        List<Item> items = new ArrayList<>();
        if (cardBeanDefinition != null) {
            items.add(new Item("id", cardBeanDefinition.getAnswerCardId()));
            items.add(new Item("paperId", cardBeanDefinition.getExamId()));
        } else {
            items.add(new Item("paperId", pprBeanDefinition.getPaperId()));
        }
        return new Header(Constants.VERSION, Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, new Property(items));
    }

    private CardBody buildCardBody() {
        CardBody body = new CardBody();
        body.setLayout(this.buildCardBodyLayout());
        body.setAnswer(this.buildCardBodyAnswer());
        body.setAction(this.buildCardBodyAction());
        return body;
    }

    private Action buildCardBodyAction() {
        if (CollectionUtils.isEmpty(cardBeanDefinition.getActions())) return null;
        List<ActionItem> actionItems = new ArrayList<>();
        for (StepActionBO action : cardBeanDefinition.getActions()) {
            ActionItem item = new ActionItem();
            item.setId(action.getId());
            item.setType(action.getType());
            item.setName(ActionMapper.getActionName(action.getType()));
            item.setDescription(action.getDescription());
            List<Item> items = new ArrayList<>();
            items.add(new Item("userId", action.getUserId()));
            items.add(new Item("fullName", action.getFullName()));
            if (action.getTimestamp() != null) items.add(new Item("timestamp", action.getTimestamp() + ""));
            if (action.getStartTimeStamp() != null) items.add(new Item("startTimeStamp", action.getStartTimeStamp() + ""));
            if (action.getEndTimeStamp() != null) items.add(new Item("endTimeStamp", action.getEndTimeStamp() + ""));
            item.setProperty(new Property(items));
            actionItems.add(item);
        }
        return new Action(actionItems);
    }

    private Answer buildCardBodyAnswer() {
        if (CollectionUtils.isEmpty(cardBeanDefinition.getAnswers())) return null;
        List<AnswerBO> answers = cardBeanDefinition.getAnswers();
        List<AnswerItem> answerItems = new ArrayList<>();
        Map<Long, List<AnswerBO>> answerMap = answers.stream().collect(Collectors.groupingBy(AnswerBO::getParentId));
        for (Map.Entry<Long, List<AnswerBO>> entry : answerMap.entrySet()) {
            List<AnswerQuestion> answerQuestions = new ArrayList<>();
            for (AnswerBO answerBO : entry.getValue()) {
                AnswerQuestion aq = new AnswerQuestion();
                aq.setId(answerBO.getQuestionId() + "");
                aq.setText(answerBO.getAnswer());
                if (CollectionUtils.isNotEmpty(answerBO.getCanvases())){
                    List<com.enableets.edu.pakage.card.bean.body.FileItem> fileItems = new ArrayList<>();
                    for (CanvasBO canvas : answerBO.getCanvases()) {
                        com.enableets.edu.pakage.card.bean.body.FileItem fileItem = new com.enableets.edu.pakage.card.bean.body.FileItem();
                        fileItem.setFormat(canvas.getFileExt());
                        fileItem.setFileId(canvas.getFileId());
                        fileItem.setFileName(canvas.getFileName());
                        fileItem.setUrl(canvas.getUrl());
                        fileItem.setHref("./files/" + getFileName(canvas.getFileName(), canvas.getFileExt()));
                        fileItems.add(fileItem);
                    }
                    aq.setFiles(fileItems);
                }
                aq.setTrail(new Trail(BeanUtils.convert(answerBO.getTrails(), Timestamp.class)));
            }
            answerItems.add(new AnswerItem(entry.getKey() + "", answerQuestions));
        }
        return new Answer(answerItems);
    }

    private Layout buildCardBodyLayout() {
        Layout layout = new Layout();
        layout.setId(cardBeanDefinition.getAnswerCardId());
        layout.setColumn(cardBeanDefinition.getColumnType()+"");
        layout.setEdition(cardBeanDefinition.getCandidateNumberEdition()+"");
        layout.setPageType(cardBeanDefinition.getPageType());
        if (CollectionUtils.isNotEmpty(cardBeanDefinition.getAxises())){
            layout.setQuestions(this.buildCardLayoutQuestion(cardBeanDefinition.getAxises()));
        }
        if(CollectionUtils.isNotEmpty(cardBeanDefinition.getTimelines())){
            layout.setQuestions(this.buildCardLayoutQuestionV2(cardBeanDefinition.getTimelines()));
        }
        return layout;
    }

    private List<LayoutQuestion> buildCardLayoutQuestion(List<CardAxisBO> axises) {
        List<LayoutQuestion> questions = new ArrayList<>();
        axises.forEach(e -> {
            if (StringUtils.isBlank(e.getParentId())) e.setParentId(e.getQuestionId());
        });
        Map<String, List<CardAxisBO>> map = axises.stream().collect(Collectors.groupingBy(CardAxisBO::getParentId));
        for (Map.Entry<String, List<CardAxisBO>> entry : map.entrySet()) {
            LayoutQuestion question = new LayoutQuestion();
            question.setId(entry.getKey());
            List<Axis> as = new ArrayList<>();
            for (CardAxisBO cardAxisBO : entry.getValue()) {
                as.add(new Axis(cardAxisBO.getQuestionId(), cardAxisBO.getTypeCode(), cardAxisBO.getTypeName(), cardAxisBO.getxAxis(), cardAxisBO.getyAxis(), cardAxisBO.getWidth(), cardAxisBO.getHeight(), cardAxisBO.getPageNo(), cardAxisBO.getOptionCount(), cardAxisBO.getRowCount()));
            }
            question.setAxises(as);
            questions.add(question);
        }
        return questions;
    }

    private List<LayoutQuestion> buildCardLayoutQuestionV2(List<CardTimeAxisBO> timelines){
        List<LayoutQuestion> questions = new ArrayList<>();
        timelines.forEach(e->{
          if (StringUtils.isBlank(e.getParentId())) e.setParentId(e.getQuestionId());
        });
        Map<String, List<CardTimeAxisBO>> map = timelines.stream().collect(Collectors.groupingBy(CardTimeAxisBO::getParentId));
        for (Map.Entry<String, List<CardTimeAxisBO>> entry : map.entrySet()) {
            LayoutQuestion question = new LayoutQuestion();
            question.setId(entry.getKey());
            List<TimeAxis> as = new ArrayList<>();
            for (CardTimeAxisBO cardTimeAxisBO : entry.getValue()) {
                as.add(new TimeAxis(cardTimeAxisBO.getQuestionId(), cardTimeAxisBO.getTypeCode(), cardTimeAxisBO.getTypeName(), cardTimeAxisBO.getTriggerTime(), cardTimeAxisBO.getPageNo(), cardTimeAxisBO.getOptionCount()));
            }
            question.setTimeAxes(as);
            questions.add(question);
        }
        return questions;
    }

    private void zipFileAndUpload(){
        String pprPathDir = new File(pprPackageWrapper.getConfiguration().getPPRTempPath() + "/" + pprBeanDefinition.getPaperId() + "/").toString();
        ZipUtil.zip(pprPathDir, pprPathDir + ".ppr" );
        PackageFileInfo file = FileStorageAdapter.upload(new File(pprPathDir + ".ppr"), pprPackageWrapper.getConfiguration());
        file.setExt("ppr");
        pprPackageWrapper.setPackageFileInfo(file);
    }

    private void handlePhysicalFiles(){
        String pprPathDir = new File(pprPackageWrapper.getConfiguration().getPPRTempPath() + "/" + pprBeanDefinition.getPaperId() + "/").toString();
        AttachmentDownloadHandler handler = new AttachmentDownloadHandler();
        if (CollectionUtils.isNotEmpty(pprBeanDefinition.getFiles())){
            try {
                for (FileInfoBO file : pprBeanDefinition.getFiles()) {
                    handler.downloadFileRetry(file.getUrl(), new File(pprPathDir + "/ppr/files/" + getFileName(file.getFileName(), file.getFileExt()))).execute();
                }
            } catch (InterruptedException e) {
                throw new PPRPackageLifecycleException("Download PPR Paper File Failure!");
            }
        }
        if (CollectionUtils.isNotEmpty(pprBeanDefinition.getNodes())){
            List<NodeInfoBO> questionNodes = pprBeanDefinition.getNodes().stream().filter(e -> (e.getLevel().intValue() == PPRConstants.QUESTION_LEVEL || e.getLevel().intValue() == PPRConstants.QUESTION_CHILD_LEVEL
            ) && e.getQuestion() != null).collect(Collectors.toList());
            if (questionNodes != null && questionNodes.size() > 0){
                questionNodes.forEach(e -> {
                    QuestionXML question = replaceFixedPathToAbsolute(buildQuestionXml(e), pprPathDir + "/ppr/files/");
                    EntityToXmlUtils.toFile(question, pprPathDir + "/ppr/files" + File.separator +  e.getQuestion().getQuestionId() + ".xml");
                });
            }
        }
        if (cardBeanDefinition != null && CollectionUtils.isNotEmpty(cardBeanDefinition.getAnswers())){
            try {
                for (AnswerBO answer : cardBeanDefinition.getAnswers()) {
                    if (CollectionUtils.isEmpty(answer.getCanvases())) continue;
                    for (CanvasBO canvas : answer.getCanvases()) {
                        handler.downloadFileRetry(canvas.getUrl(), new File(pprPathDir + "/card/files/" + getFileName(canvas.getFileName(), canvas.getFileExt()))).execute();
                    }
                }
            } catch (InterruptedException e) {
                throw new PPRPackageLifecycleException("Download PPR Paper File Failure!");
            }
        }
    }

    private QuestionXML replaceFixedPathToAbsolute(QuestionXML question, String basePath){
        IAttachmentHandler handler = new AttachmentDownloadHandler(basePath);
        String parse = null;
        try {
            parse = AttachmentHtmlParser.parse(JsonUtils.convert(question), handler);
        }catch (Exception e){
            e.printStackTrace();
        }
        return JsonUtils.convert(parse, QuestionXML.class);
    }

    private QuestionXML buildQuestionXml(NodeInfoBO node) {
        QuestionInfoBO questionBO = node.getQuestion();
        QuestionXML question = new QuestionXML();
        question.setQuestionId(questionBO.getQuestionId());
        if (questionBO.getAnswer() != null) {  //Parent Question no answer attribute
            question.setAnswer(questionBO.getAnswer().getLabel());
            question.setAnswerContent(questionBO.getAnswer().getStrategy());
        }
        question.setEstimateTime(questionBO.getEstimateTime() == null ? "" : questionBO.getEstimateTime().toString());
        question.setDescription("");
        question.setListen("");
        question.setQuestionContent(questionBO.getStem().getRichText());
        question.setQuestionContentNoHtml(questionBO.getStem().getPlaintext());
        question.setQuestionDifficulty(questionBO.getDifficulty().getCode());
        question.setQuestionType(questionBO.getType().getCode());
        question.setQuestionTypeId(questionBO.getType().getCode());
        question.setQuestionTypeName(questionBO.getType().getName());
        question.setScore(node.getPoints());
        question.setAffixId(questionBO.getAffixId());
        if (CollectionUtils.isNotEmpty(questionBO.getKnowledges())) {
            question.setKnowledgeId(questionBO.getKnowledges().stream().map(e -> e.getKnowledgeId()).reduce((x, y) -> x + "@@@" + y).get());
            question.setKnowledgeName(questionBO.getKnowledges().stream().map(e -> e.getKnowledgeName()).reduce((x, y) -> x + "@@@" + y).get());
        }
        if (questionBO.getOptions() != null && questionBO.getOptions().size() > 0) {
            List<QuestionOption> options = new ArrayList<>();
            for (QuestionOptionInfoBO option : questionBO.getOptions()) {
                options.add(new QuestionOption(option.getOptionId(), questionBO.getQuestionId(), new Option(option.getLabel(), option.getOptionId(), option.getSequencing() + "", option.getAlias())));
            }
            question.setOptions(options);
        }
        return question;
    }

    public String getFileName(String name, String ext){
        if (name.endsWith("." + ext)) return name;
        return name + "." + ext;
    }

    private List<FileItem> buildFilesItem(){
        List<FileItem> items = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pprBeanDefinition.getFiles())){
            Map<String, List<FileInfoBO>> map = pprBeanDefinition.getFiles().stream().collect(Collectors.groupingBy(FileInfoBO::getFileExt));
            for (Map.Entry<String, List<FileInfoBO>> entry : map.entrySet()) {
                List<Href> hrefs = new ArrayList<>();
                for (FileInfoBO file : entry.getValue()) {
                    hrefs.add(new Href(file.getFileName().endsWith("." + file.getFileExt()) ? file.getFileName() : file.getFileName() + "." + file.getFileExt()));
                }
                items.add(new FileItem(entry.getKey(), hrefs));
            }
        }
        if (CollectionUtils.isNotEmpty(pprBeanDefinition.getNodes())){
            List<Href> hrefs = new ArrayList<>();
            for (NodeInfoBO node : pprBeanDefinition.getNodes()) {
                if (node.getLevel().intValue() != 3) continue;
                hrefs.add(new Href(node.getQuestion().getQuestionId() + ".xml"));
            }
            items.add(new FileItem("xml", hrefs));
        }
        return items;
    }


    private List<TestPart> buildBodyTestPart(List<NodeInfoBO> nodes){
        List<TestPart> testParts = new ArrayList<>();
        if (CollectionUtils.isEmpty(nodes)) return testParts;
        for (NodeInfoBO node : nodes) {
            if (node.getLevel().intValue() == PPRConstants.TEST_PART_LEVEL){
                testParts.add(new TestPart(node.getNodeId(), node.getName(), node.getPoints(),buildSection(this.getChildNode(nodes, node.getNodeId()))));
            }
        }
        if (testParts.size() == 0) {// No testPart, Not a standard structure, new a part
            testParts.add(new TestPart("", "", 0.0f, buildSection(nodes)));
        }
        return testParts;
    }

    private List<Section> buildSection(List<NodeInfoBO> nodes){
        List<Section> sections = new ArrayList<>();
        if (CollectionUtils.isEmpty(nodes)) return null;
        for (NodeInfoBO node : nodes) {
            if (node.getLevel().intValue() == PPRConstants.SECTION_LEVEL){
                sections.add(new Section(node.getNodeId(), node.getName(), node.getPoints(), buildQuestion(this.getChildNode(nodes, node.getNodeId()))));
            }
        }
        if (sections.size() == 0){
            sections.add(new Section("", "", 0.0f, buildQuestion(nodes)));
        }
        return sections;
    }

    private List<Question> buildQuestion(List<NodeInfoBO> nodes){
        List<Question> questions = new ArrayList<>();
        if (CollectionUtils.isEmpty(nodes)) return questions;
        for (NodeInfoBO node : nodes) {
            if (node.getLevel().intValue() == PPRConstants.QUESTION_LEVEL){
                questions.add(new Question(node.getNodeId(), node.getQuestion().getQuestionId(), node.getExternalNo(), node.getPoints(), this.getQuestionHref(node.getQuestion()), this.buildChildQuestion(getChildNode(nodes, node.getNodeId()))));
            }
        }
        return questions;
    }


    private Map<String, FileInfoBO> paperFileMap;

    private void initPaperFileMap(){
        if (paperFileMap == null){
            paperFileMap = new HashMap<>();
            if (CollectionUtils.isNotEmpty(pprBeanDefinition.getFiles())){
                pprBeanDefinition.getFiles().forEach(file -> paperFileMap.put(file.getFileId(), file));
            }
        }
    }

    private List<QuestionHref> getQuestionHref(QuestionInfoBO question){
        List<QuestionHref> hrefs = new ArrayList<>();
        hrefs.add(new QuestionHref(question.getQuestionId() + ".xml"));
        if (CollectionUtils.isNotEmpty(question.getAxises())){
            this.initPaperFileMap();
            question.getAxises().forEach(e -> {
                FileInfoBO file = paperFileMap.get(e.getFileId());
                if (file != null){
                    hrefs.add(new QuestionHref(file.getFileName().endsWith("." + file.getFileExt()) ? file.getFileName() : file.getFileName() + "." + file.getFileExt(), e.getWidth(), e.getHeight(), e.getXAxis(), e.getYAxis()));
                }
            });
        }
        return hrefs;
    }

    private List<Question> buildChildQuestion(List<NodeInfoBO> nodes){
        if (CollectionUtils.isEmpty(nodes)) return null;
        List<Question> questions = new ArrayList<>();
        for (NodeInfoBO node : nodes) {
            if (node.getLevel().intValue() == PPRConstants.QUESTION_CHILD_LEVEL) {
                questions.add(new Question(node.getNodeId(), node.getQuestion().getQuestionId(), node.getExternalNo(), node.getPoints(),getQuestionHref(node.getQuestion()), null));
            }
        }
        return questions;
    }

    /**
     * Get children of this node, Include all descendants
     * @param nodes
     * @param nodeId
     * @return
     */
    public List<NodeInfoBO> getChildNode(List<NodeInfoBO> nodes, String nodeId){
        List<NodeInfoBO> children = new ArrayList<>();
        for (NodeInfoBO node : nodes) {
            if (StringUtils.isNotBlank(node.getParentNodeId()) && node.getParentNodeId().equals(nodeId)){
                children.add(node);
                children.addAll(getChildNode(nodes, node.getNodeId()));
            }
        }
        return children;
    }

    private Property getProperties() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Item> items = new ArrayList<>();
        items.add(new Item("id", pprBeanDefinition.getPaperId()));
        items.add(new Item("title", pprBeanDefinition.getName()));
        items.add(new Item("description", pprBeanDefinition.getName()));
        if (pprBeanDefinition.getStage() != null) {
            items.add(new Item("stageCode", pprBeanDefinition.getStage().getCode()));
            items.add(new Item("stageName", pprBeanDefinition.getStage().getName()));
        }
        if (pprBeanDefinition.getGrade() != null){
            items.add(new Item("gradeCode", pprBeanDefinition.getGrade().getCode()));
            items.add(new Item("gradeName", pprBeanDefinition.getGrade().getName()));
        }
        if (pprBeanDefinition.getSubject() != null){
            items.add(new Item("subjectCode", pprBeanDefinition.getSubject().getCode()));
            items.add(new Item("subjectName", pprBeanDefinition.getSubject().getName()));
        }
        items.add(new Item("author", pprBeanDefinition.getUser().getName()));
        items.add(new Item("createTime", sdf.format(pprBeanDefinition.getCreateTime())));
        return new Property(items);
    }
}

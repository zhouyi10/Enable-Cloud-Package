package com.enableets.edu.sdk.ppr.ppr.parse.xml;

import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Href;
import com.enableets.edu.sdk.ppr.ppr.core.PaperFiles;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Body;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Question;
import com.enableets.edu.sdk.ppr.ppr.core.paper.QuestionHref;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Section;
import com.enableets.edu.sdk.ppr.ppr.core.paper.TestPart;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.utils.XMLObject2Entity;
import com.enableets.edu.sdk.ppr.xml.xpath.XNode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/17
 **/
public class PaperXmlParser extends AbstractXmlParser<PaperXML, PPRBO> {

    public PaperXmlParser(String paperXml){
       super(paperXml);
       setIgnoreVersion(Boolean.FALSE);
    }

    public PaperXmlParser(File file){
        super(file);
        setIgnoreVersion(Boolean.FALSE);
    }

    @Override
    public PaperXML parse() throws PPRVersionMismatchException {
        PaperXML paperXML = new PaperXML();
        paperXML.setHeader(commonNodeParse.parseHeader(xPathParser.evalNode("/paper/header")));
        paperXML.setBody(parsePaperBody(xPathParser.evalNode("/paper/body")));
        paperXML.setFiles(parsePaperFiles(xPathParser.evalNode("/paper/files")));
        return paperXML;
    }

    @Override
    public PPRBO parseBO() throws PPRVersionMismatchException {
        return XMLObject2Entity.tranPaperXML(parse());
    }

    private Body parsePaperBody(XNode evalNode) {
        Body body = new Body();
        if (evalNode == null) return body;
        List<XNode> testPartXNodes = evalNode.evalNodes("./testPart");
        if (testPartXNodes == null || testPartXNodes.size() == 0) return body;
        List<TestPart> testParts = new ArrayList<>();
        for (XNode testPartXNode : testPartXNodes) {
            testParts.add(parseTestPart(testPartXNode));
        }
        body.setTestParts(testParts);
        return body;
    }

    private TestPart parseTestPart(XNode testPartXNode) {
        TestPart testPart = new TestPart();
        testPart.setId(testPartXNode.getAttribute("id"));
        testPart.setTitle(testPartXNode.getAttribute("title"));
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

    private PaperFiles parsePaperFiles(XNode xNode) {
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
        return new PaperFiles(items);
    }
}

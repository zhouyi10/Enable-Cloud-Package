package com.enableets.edu.sdk.ppr.ppr.builder.content;

import org.apache.commons.collections.CollectionUtils;

import com.enableets.edu.sdk.ppr.adapter.FileStorageAdapter;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.core.BeanUtils;
import com.enableets.edu.sdk.ppr.core.JsonUtils;
import com.enableets.edu.sdk.ppr.core.PPRBaseException;
import com.enableets.edu.sdk.ppr.exceptions.PPRException;
import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.FileInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.NodeInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.QuestionOptionInfoBO;
import com.enableets.edu.sdk.ppr.ppr.builder.handler.PaperCardBuilder;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.core.FileItem;
import com.enableets.edu.sdk.ppr.ppr.core.Header;
import com.enableets.edu.sdk.ppr.ppr.core.Href;
import com.enableets.edu.sdk.ppr.ppr.core.Item;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;
import com.enableets.edu.sdk.ppr.ppr.core.PaperFiles;
import com.enableets.edu.sdk.ppr.ppr.core.Property;
import com.enableets.edu.sdk.ppr.ppr.core.paper.PaperXML;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Question;
import com.enableets.edu.sdk.ppr.ppr.core.paper.QuestionHref;
import com.enableets.edu.sdk.ppr.ppr.core.paper.Section;
import com.enableets.edu.sdk.ppr.ppr.core.paper.TestPart;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Body;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.Layout;
import com.enableets.edu.sdk.ppr.ppr.core.paperCard.PaperCardXML;
import com.enableets.edu.sdk.ppr.ppr.core.question.Option;
import com.enableets.edu.sdk.ppr.ppr.core.question.QuestionOption;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.handler.AttachmentDownloadHandler;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.handler.IAttachmentHandler;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.parser.AttachmentHtmlParser;
import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.utils.Utils;
import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXml;
import com.enableets.edu.sdk.ppr.xml.xstream.EntityToXmlUtils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author walle_yu@enable-ets.com
 * @date 2020/06/24 17:37
 */

public abstract class AbstractContent implements IContent{

    public PPRInfoBO pprInfoBO;

    public Configuration configuration;

    private Map<String, FileInfoBO> paperFileMap = new HashMap<>();

    public AbstractContent(PPRInfoBO pprInfoBO){
        this.pprInfoBO = pprInfoBO;
    }

    private PPRBO pprBO;

    private CardBO cardBO;

    /**
     * 将ppr对象转xstream 转换对象
     */
    private void beforeGeneratePPRDoc(){
        if (!validData()) return;
        pprBO = pprInfoBO.getPprBO();
        cardBO = pprInfoBO.getCardBO();
        if (CollectionUtils.isNotEmpty(pprBO.getFiles())){
            pprBO.getFiles().forEach(e -> paperFileMap.put(e.getFileId(), e));
        }
    }

    /**
     * 参数校验
     * @return
     */
    private boolean validData(){
        if (pprInfoBO == null || pprInfoBO.getPprBO() == null || CollectionUtils.isEmpty(pprInfoBO.getPprBO().getNodes())) return false;
        return true;
    }

    @Override
    public PPR build(){
        beforeGeneratePPRDoc();
        PPR ppr = null;
        String rootPath = configuration.getPPRTempPath() + File.separator + Utils.getRandom();
        try {
            PaperXML paperXML = buildPaper();
            if (paperXML == null) return null;
            writeXmlToFile(paperXML, rootPath + "/ppr/ppr.xml");
            downloadPaperFiles(rootPath + "/ppr/files");
            createQuestionXmlFile(rootPath + "/ppr/files");
            PaperCardXML paperCard = getPaperCard(paperXML);
            writeXmlToFile(paperCard, rootPath + "/ppr/card.xml");
            zipPPR(rootPath);
            ppr = uploadToStorage(rootPath);
        }catch (PPRException e){
            File file = new File(rootPath);
            if (file.exists()){
                FileUtil.del(file);
            }
            throw new PPRBaseException(e);
        }finally {
            File file = new File(rootPath);
            if (file.exists()){
                FileUtil.del(file);
            }
        }
        return ppr;
    }

    private void createQuestionXmlFile(String dirPath){
        if (CollectionUtils.isNotEmpty(pprBO.getNodes())){
            List<NodeInfoBO> questionNodes = pprBO.getNodes().stream().filter(e -> (e.getLevel().intValue() == Constants.QUESTION_LEVEL || e.getLevel().intValue() == Constants.QUESTION_CHILD_LEVEL
            ) && e.getQuestion() != null).collect(Collectors.toList());
            if (questionNodes != null && questionNodes.size() > 0){
                questionNodes.forEach(e -> {
                    com.enableets.edu.sdk.ppr.ppr.core.question.Question question = replaceFixedPathToAbsolute(buildQuestionXml(e), dirPath);
                    EntityToXmlUtils.toFile(question, dirPath + File.separator +  e.getQuestion().getQuestionId() + ".xml");
                });
            }
        }
    };

    private com.enableets.edu.sdk.ppr.ppr.core.question.Question buildQuestionXml(NodeInfoBO node){
        QuestionInfoBO questionBO = node.getQuestion();
        com.enableets.edu.sdk.ppr.ppr.core.question.Question question = new com.enableets.edu.sdk.ppr.ppr.core.question.Question();
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
        if (questionBO.getOptions() != null && questionBO.getOptions().size() > 0){
            List<QuestionOption> options = new ArrayList<>();
            for (QuestionOptionInfoBO option : questionBO.getOptions()) {
                options.add(new QuestionOption(option.getOptionId(), questionBO.getQuestionId(), new Option(option.getOptionContent(), option.getOptionId(), option.getSequencing(), option.getOptionTitle())));
            }
            question.setOptions(options);
        }
        return question;
    }

    private com.enableets.edu.sdk.ppr.ppr.core.question.Question replaceFixedPathToAbsolute(com.enableets.edu.sdk.ppr.ppr.core.question.Question question, String basePath){
        IAttachmentHandler handler = new AttachmentDownloadHandler(basePath);
        String parse = null;
        try {
            parse = AttachmentHtmlParser.parse(JsonUtils.convert(question), handler);
        }catch (Exception e){
            e.printStackTrace();
        }
        return JsonUtils.convert(parse, com.enableets.edu.sdk.ppr.ppr.core.question.Question.class);
    }

    public abstract void downloadPaperFiles(String dirPath);

    public PaperXML buildPaper(){
        if (pprBO == null) return null;
        return new PaperXML(buildHeader(), buildBody(), buildFiles());
    }

    private Header buildHeader(){
        return new Header(Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, this.getHeadProperty());
    }

    private Property getHeadProperty(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        List<Item> items = new ArrayList<>();
        items.add(new Item("id", pprBO.getPaperId().toString()));
        items.add(new Item("title", pprBO.getName()));
        items.add(new Item("description", pprBO.getName()));
        if (pprBO.getStage() != null) {
            items.add(new Item("stageCode", pprBO.getStage().getCode()));
            items.add(new Item("stageName", pprBO.getStage().getName()));
        }
        if (pprBO.getGrade() != null){
            items.add(new Item("gradeCode", pprBO.getGrade().getCode()));
            items.add(new Item("gradeName", pprBO.getGrade().getName()));
        }
        if (pprBO.getSubject() != null){
            items.add(new Item("subjectCode", pprBO.getSubject().getCode()));
            items.add(new Item("subjectName", pprBO.getSubject().getName()));
        }
        items.add(new Item("author", pprBO.getUser().getName()));
        items.add(new Item("createTime", sdf.format(pprBO.getCreateTime())));
        return new Property(items);
    }

    private com.enableets.edu.sdk.ppr.ppr.core.paper.Body buildBody(){
        return new com.enableets.edu.sdk.ppr.ppr.core.paper.Body(buildBodyTestPart(pprBO.getNodes()));
    }

    private PaperFiles buildFiles(){
        List<NodeInfoBO> questionNodes = pprBO.getNodes().stream().filter(e -> e.getLevel().intValue() == Constants.QUESTION_LEVEL || e.getLevel().intValue() == Constants.QUESTION_CHILD_LEVEL).collect(Collectors.toList());
        if (questionNodes == null || questionNodes.size() == 0) return null;
        List<Href> hrefs = questionNodes.stream().map(e -> new Href("./files/" + e.getQuestion().getQuestionId() + ".xml")).collect(Collectors.toList());
        List<FileItem> items = new ArrayList<>();
        items.add(new FileItem("xml", hrefs));
        List<FileItem> fileItems = this.buildBodyFilesWithOutQuestionXml();
        if (CollectionUtils.isNotEmpty(fileItems)) items.addAll(fileItems);
        return new PaperFiles(items);
    }

    public abstract List<FileItem> buildBodyFilesWithOutQuestionXml();

    private List<TestPart> buildBodyTestPart(List<NodeInfoBO> nodes) {
        if (nodes == null || nodes.size() == 0) return null;
        List<TestPart> testParts = new ArrayList<>();
        for (NodeInfoBO node : nodes) {
            if (node.getLevel().intValue() == Constants.TEST_PART_LEVEL){
                testParts.add(new TestPart(node.getNodeId(), node.getName(), buildSection(getChildNode(nodes, node.getNodeId()))));
            }
        }
        if (testParts.size() == 0) {// No testPart, Not a standard structure, new a part
            testParts.add(new TestPart("", "", buildSection(nodes)));
        }
        return testParts;
    }

    /**
     * build section
     * @param childNodes
     * @return
     */
    private List<Section> buildSection(List<NodeInfoBO> childNodes) {
        if (childNodes == null || childNodes.size() == 0) return null;
        List<Section> sections = new ArrayList<>();
        for (NodeInfoBO node : childNodes) {
            if (node.getLevel().intValue() == Constants.SECTION_LEVEL)
                sections.add(new Section(node.getNodeId(), node.getName(), buildQuestion(getChildNode(childNodes, node.getNodeId()))));
        }
        if (sections.size() == 0) {  // No section, Not a standard structure, new a part
            sections.add(new Section("", "", buildQuestion(childNodes)));
        }
        return sections;
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


    private PPR uploadToStorage(String rootPath){
        File file = new File(rootPath + File.separator + pprBO.getPaperId() + ".ppr");
        if (file.exists()){
            PPR ppr = BeanUtils.convert(FileStorageAdapter.upload(file ,configuration), PPR.class);
            if (ppr != null && StringUtils.isBlank(ppr.getExt())) ppr.setExt("ppr");
            return ppr;
        }
        return null;
    }

    private List<Question> buildQuestion(List<NodeInfoBO> questionNodes){
        if (questionNodes == null || questionNodes.size() == 0) return null;
        List<Question> questions = new ArrayList<>();
        for (NodeInfoBO questionNode : questionNodes) {
            if (questionNode.getLevel().intValue() == Constants.QUESTION_LEVEL){
                questions.add(new Question(questionNode.getNodeId(), questionNode.getQuestion().getQuestionId(), getQuestionHref(questionNode.getQuestion()), buildChildQuestion(getChildNode(questionNodes, questionNode.getNodeId()))));
            }
        }
        return questions;
    }

    private List<QuestionHref> getQuestionHref(QuestionInfoBO question){
        List<QuestionHref> hrefs = new ArrayList<>();
        hrefs.add(new QuestionHref("./files/" + question.getQuestionId() + ".xml"));
        if (CollectionUtils.isNotEmpty(question.getAxises())){
            question.getAxises().forEach(e -> {
                FileInfoBO fileInfoBO = paperFileMap.get(e.getFileId());
                if (fileInfoBO != null){
                    hrefs.add(new QuestionHref("./files/" + getFileName(fileInfoBO.getFileName(), fileInfoBO.getFileExt()), e.getWidth(), e.getHeight(), e.getXAxis(), e.getYAxis()));
                }
            });
        }
        return hrefs;
    }

    public String getFileName(String name, String ext){
        if (name.endsWith("." + ext)) return name;
        return name + "." + ext;
    }

    private List<Question> buildChildQuestion(List<NodeInfoBO> children) {
        if (children == null || children.size() == 0) return null;
        List<Question> questions = new ArrayList<>();
        for (NodeInfoBO questionNode : children) {
            if (questionNode.getLevel().intValue() == Constants.QUESTION_CHILD_LEVEL) {
                questions.add(new Question(questionNode.getNodeId(), questionNode.getQuestion().getQuestionId(), getQuestionHref(questionNode.getQuestion()), null));
            }
        }
        return questions;
    }

    /**
     * compress ppr
     * @param rootPath
     */
    private void zipPPR(String rootPath){
        String srcFolder = rootPath + File.separator + "ppr";
        String zipPath = rootPath + File.separator + pprBO.getPaperId() + ".ppr";
        ZipUtil.zip(srcFolder, zipPath);
    }


    /**
     *  create paper.xml
     * @param xml
     * @param path
     */
    private void writeXmlToFile(EntityToXml xml, String path){
        EntityToXmlUtils.toFile(xml, path);
    }

    /**
     * create paperCardXml Obj
     * @param paperXML
     * @return
     */
    private PaperCardXML getPaperCard(PaperXML paperXML){
        PaperCardXML paperCardXML = new PaperCardXML();
        paperCardXML.setHeader(paperXML.getHeader());
        if (cardBO == null) {
            paperCardXML.setBody(new Body());
            return paperCardXML;
        }
        paperCardXML.setBody(new Body(getLayout(), null, null));
        return paperCardXML;
    }

    /**
     * create answer axis
     * @return
     */
    private Layout getLayout(){
        PaperCardBuilder builder = new PaperCardBuilder();
        return builder.createLayout(pprInfoBO.getCardBO().getAnswerCard());
    }

    @Override
    public void setConfiguration(Configuration configuration){
        this.configuration = configuration;
    }
}

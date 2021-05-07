package com.enableets.edu.pakage.framework.ppr.paper.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.card.bean.EnableCard;
import com.enableets.edu.pakage.card.bean.EnableCardBeanDefinition;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.framework.bo.CodeNameMapBO;
import com.enableets.edu.pakage.framework.bo.IdNameMapBO;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.test.po.QuestionAxisInfoPO;
import com.enableets.edu.pakage.framework.ppr.test.service.AnswerCardInfoService;
import com.enableets.edu.pakage.framework.ppr.bo.AnswerCardInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.ExamInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.FileInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRQuestionAxisBO;
import com.enableets.edu.pakage.framework.ppr.bo.PPRQuestionBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperQuestionOptionBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamKindInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamQuestionInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamQuestionTypeInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.PaperQuestionAxisDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.PaperQuestionInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.PaperQuestionKnowledgeInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.PaperQuestionOptionInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamKindInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamQuestionTypeInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionKnowledgeInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionOptionInfoPO;
import com.enableets.edu.pakage.framework.ppr.utils.PaperInfoUtils;
import com.enableets.edu.pakage.framework.ppr.utils.PprInfoUtils;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import com.enableets.edu.pakage.ppr.bean.EnablePPRBeanDefinition;
import com.enableets.edu.pakage.ppr.bean.PPRPackageWrapper;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.dto.KnowledgeInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Service
public class PPRInfoService {

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private AnswerCardInfoService answerCardInfoService;

    @Autowired
    private PaperQuestionInfoDAO paperQuestionInfoDAO;

    @Autowired
    private PaperQuestionKnowledgeInfoDAO paperQuestionKnowledgeInfoDAO;

    @Autowired
    private PaperQuestionOptionInfoDAO paperQuestionOptionInfoDAO;

    @Autowired
    private PaperQuestionAxisDAO paperQuestionAxisDAO;

    @Autowired
    private ExamInfoDAO examInfoDAO;

    @Autowired
    private ExamKindInfoDAO examKindInfoDAO;

    @Autowired
    private ExamQuestionTypeInfoDAO examQuestionTypeInfoDAO;

    @Autowired
    private ExamQuestionInfoDAO examQuestionInfoDAO;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ITokenGenerator tokenGenerator;


    /**
     * Add New PPR
     * @param ppr
     * @return
     */
    @Transactional
    public PPRInfoBO add(PPRInfoBO ppr){
        if (ppr.getAnswerCard() == null) {
            throw new MicroServiceException("70-", "Answer Card Info Is Null!");
        }
        if (CollectionUtils.isEmpty(ppr.getNodes())){
            throw new MicroServiceException("70-", "PPR Missing structure(Node)");
        }
        ContentInfoDTO content = contentInfoServiceSDK.add(this.convertPPR2Content(ppr)).getData();
        //Set PPR basic information through Content
        this.setBasicInfo(ppr, content);
        //Generate primary key
        this.generatePrimaryKey(ppr);
        //1、Save Question Structure
        Map<String, FileInfoBO> fileMap = ppr.getFiles().stream().collect(Collectors.toMap(FileInfoBO::getFileId, file -> file));
        this.saveQuestionInfo(ppr, fileMap);
        //2、PPR Data persistence
        this.savePPRData(ppr);
        //3、Save Answer Card
        ppr.getAnswerCard().setCreator(ppr.getUser().getId());
        this.saveAnswerCard(ppr.getAnswerCard());
        //4、Generate PPR File & Add it To PPR Content
        this.generatePPRFile(ppr);
        return ppr;
    }

    private void generatePPRFile(PPRInfoBO ppr) {
//        IPPRBuilderHandler pprBuilderHandler = pprHandlerFactory.getPprBuilderHandler();
//        com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO sdkPPRBO = new com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO();
//        sdkPPRBO.setPprBO(BeanUtils.convert(ppr, PPRBO.class));
//        sdkPPRBO.setCardBO(new CardBO(BeanUtils.convert(ppr.getAnswerCard(), AnswerCardBO.class)));
//        PPR pprFile = pprBuilderHandler.build(new PPRFileContent(BeanUtils.convert(ppr, sdkPPRBO)));
        // --------------core code-------------------
        // 1. context
        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        // 2. ppr model
        EnablePPRBeanDefinition pprBeanDefinition = BeanUtils.convert(ppr, EnablePPRBeanDefinition.class);
        // 4. card Decoration
        EnableCard enableCard = new EnableCard(BeanUtils.convert(ppr.getAnswerCard(), EnableCardBeanDefinition.class), configuration);
        // 3. PPR wrapper
        PPRPackageWrapper pprPackageWrapper = new PPRPackageWrapper(configuration, pprBeanDefinition, enableCard);
        // 4.PPR Lifecycle
        PPRPackageLifecycle packageLifecycle = new PPRPackageLifecycle(pprPackageWrapper);
        // 5.build .ppr
        packageLifecycle.build();
        // 6. Get .ppr document
        PackageFileInfo packageFileInfo = pprPackageWrapper.getPackageFileInfo();
        // ------------------------ ----------------------------------
        ContentFileInfoDTO file = new ContentFileInfoDTO();
        file.setFileId(packageFileInfo.getFileId());
        file.setFileName(packageFileInfo.getName());
        file.setFileExt(packageFileInfo.getExt());
        file.setUrl(packageFileInfo.getDownloadUrl());
        file.setMd5(packageFileInfo.getMd5());
        file.setSize(packageFileInfo.getSize());
        file.setSizeDisplay(packageFileInfo.getSizeDisplay());
        file.setContentId(ppr.getContentId().toString());
        file.setFileOrder(0);
        contentInfoServiceSDK.addFileToContent(file);
    }

    /**
     * Save Answer Card
     * @param answerCard
     */
    private void saveAnswerCard(AnswerCardInfoBO answerCard) {
        answerCardInfoService.add(answerCard);
    }

    /**
     * Save PPR Structure
     * @param ppr
     */
    private void savePPRData(PPRInfoBO ppr) {
        ExamInfoPO exam = new ExamInfoPO();
        List<ExamKindInfoPO> kinds = new ArrayList<>();
        List<ExamQuestionTypeInfoPO> types = new ArrayList<>();
        List<ExamQuestionInfoPO> questions = new ArrayList<>();
        exam.setExamId(ppr.getPaperId().toString());
        exam.setExamName(ppr.getName());
        exam.setExamType("4"); //Default "The PPR(Picture) Exam Type"
        if (ppr.getMaterialVersion() != null)
            exam.setMaterialVersionId(ppr.getMaterialVersion().getId());
        exam.setScore(ppr.getTotalPoints());
        exam.setAnswerCostTime(100L); //Default
        exam.setDelStatus("0");
        exam.setContentId(ppr.getContentId().toString());
        exam.setCreator(ppr.getUser().getId());
        exam.setCreateTime(ppr.getCreateTime());
        exam.setUpdator(exam.getCreator());
        exam.setUpdateTime(exam.getCreateTime());
        exam.setGradeCode(ppr.getGrade().getCode());
        exam.setGradeName(ppr.getGrade().getName());
        exam.setSubjectCode(ppr.getSubject().getCode());
        exam.setSubjectName(ppr.getSubject().getName());
        //exam.setDiffcult();
        ppr.getNodes().forEach(node -> {
            switch (node.getLevel().intValue()){
                case PPRConstants.LEVEL_EXAM_KIND :
                    ExamKindInfoPO kind = new ExamKindInfoPO();
                    kind.setExamKindId(node.getNodeId().toString());
                    kind.setExamId(ppr.getPaperId().toString());
                    kind.setKindNo(node.getSequencing());
                    kind.setScore(node.getPoints());
                    kind.setDescription(node.getName());
                    kind.setCreator(exam.getCreator());
                    kind.setUpdator(exam.getUpdator());
                    kind.setCreateTime(exam.getCreateTime());
                    kind.setUpdateTime(exam.getUpdateTime());
                    kinds.add(kind);
                    break;
                case PPRConstants.LEVEL_EXAM_QUES_TYPE :
                    ExamQuestionTypeInfoPO type = new ExamQuestionTypeInfoPO();
                    type.setExamQuestionTypeId(node.getNodeId().toString());
                    type.setExamKindId(node.getParentNodeId().toString());
                    type.setQuestionTypeNo(node.getSequencing());
                    type.setDescription(node.getName());
                    type.setScore(node.getPoints());
                    type.setCreator(exam.getCreator());
                    type.setUpdator(exam.getUpdator());
                    type.setCreateTime(exam.getCreateTime());
                    type.setUpdateTime(exam.getUpdateTime());
                    types.add(type);
                    break;
                case PPRConstants.LEVEL_EXAM_QUES :
                    ExamQuestionInfoPO question = new ExamQuestionInfoPO();
                    question.setExamQuestionId(node.getNodeId().toString());
                    question.setExamQuestionTypeId(node.getParentNodeId().toString());
                    question.setQuestionNo(node.getExternalNo());
                    question.setQuestionOrder(node.getInternalNo());
                    question.setScore(node.getPoints());
                    question.setQuestionId(node.getQuestion().getQuestionId());
                    question.setCreator(exam.getCreator());
                    question.setUpdator(exam.getUpdator());
                    question.setCreateTime(exam.getCreateTime());
                    question.setUpdateTime(exam.getUpdateTime());
                    questions.add(question);
                    break;
                default:
                    break;
            }
        });
        if (CollectionUtils.isNotEmpty(questions)) examQuestionInfoDAO.insertList(questions);
        if (CollectionUtils.isNotEmpty(types)) examQuestionTypeInfoDAO.insertList(types);
        if (CollectionUtils.isNotEmpty(kinds)) examKindInfoDAO.insertList(kinds);
        examInfoDAO.insertSelective(exam);
    }

    /**
     * 1、Save question structure
     * @param ppr
     */
    private void saveQuestionInfo(PPRInfoBO ppr, Map<String, FileInfoBO> fileMap) {
        List<QuestionInfoPO> questions = new ArrayList<>();
        List<QuestionKnowledgeInfoPO> knowledges = new ArrayList<>();
        List<QuestionOptionInfoPO> options = new ArrayList<>();
        List<QuestionAxisInfoPO> axises = new ArrayList<>();
        ppr.getNodes().forEach(node -> {
            if (node.getQuestion() != null){
                PPRQuestionBO question = node.getQuestion();
                QuestionInfoPO questionPO = new QuestionInfoPO();
                questionPO.setQuestionId(question.getQuestionId());
                questionPO.setParentId(question.getParentId());
                if (question.getSubject() != null){
                    questionPO.setSubjectId(question.getSubject().getCode());
                    questionPO.setSubjectName(question.getSubject().getName());
                }
                questionPO.setQuestionTypeId(getCode(question.getType()));
                questionPO.setListen(question.getListen());
                questionPO.setQuestionDifficulty(getCode(question.getDifficulty()));
                questionPO.setAbilityId(getCode(question.getAbility()));
                questionPO.setScore(question.getPoints() == null ? 0f : question.getPoints());
                if (question.getStem() != null) {
                    questionPO.setQuestionContent(question.getStem().getRichText());
                    questionPO.setQuestionContentNoHtml(question.getStem().getPlaintext());
                }
                if (question.getAnswer() != null){
                    questionPO.setAnswer(question.getAnswer().getLabel());
                    questionPO.setAnswerContent(question.getAnswer().getStrategy());
                    questionPO.setAnswerAnalyse(question.getAnswer().getAnalysis());
                }
                questionPO.setChildCount(0);
                questionPO.setEstimateTime(1f);
                questionPO.setPublisher(ppr.getUser().getId());
                questionPO.setQuestionChildOrder(0);
                questionPO.setAffixId(question.getAffixId());
                questionPO.setCreator(ppr.getUser().getId());
                questionPO.setCreateTime(ppr.getCreateTime());
                questionPO.setUpdator(ppr.getUser().getId());
                questionPO.setUpdateTime(ppr.getCreateTime());
                questionPO.setQuestionNo(question.getQuestionNo());
                questionPO.setQuestionKind(PPRConstants.PPR_QUESTION_KIND);  //4、PPR Question/Not persistent
                questionPO.setContentId(null);
                questionPO.setQuestionSource("_ppr_mark");
                if (CollectionUtils.isNotEmpty(question.getKnowledges())){
                    question.getKnowledges().forEach(knowledge -> {
                        knowledge.setQuestionId(questionPO.getQuestionId());
                        knowledge.setCreator(ppr.getUser().getId());
                        knowledge.setUpdator(ppr.getUser().getId());
                        knowledge.setCreateTime(ppr.getCreateTime());
                        knowledge.setUpdateTime(ppr.getCreateTime());
                    });
                    knowledges.addAll(BeanUtils.convert(question.getKnowledges(), QuestionKnowledgeInfoPO.class));
                }
                if (CollectionUtils.isNotEmpty(question.getOptions())){
                    questionPO.setOptionCount(question.getOptions().size());
                    for (PaperQuestionOptionBO option : question.getOptions()) {
                        QuestionOptionInfoPO optionOp = new QuestionOptionInfoPO();
                        optionOp.setOptionId(tokenGenerator.getToken().toString());
                        optionOp.setQuestionId(questionPO.getQuestionId());
                        optionOp.setOptionTitle(option.getAlias());
                        optionOp.setOptionContent(option.getLabel());
                        optionOp.setOptionOrder(option.getSequencing());
                        optionOp.setCreator(ppr.getUser().getId());
                        optionOp.setCreateTime(ppr.getCreateTime());
                        optionOp.setUpdator(ppr.getUser().getId());
                        optionOp.setUpdateTime(ppr.getCreateTime());
                        options.add(optionOp);
                    }
                }
                if (CollectionUtils.isNotEmpty(question.getAxises())){
                    question.getAxises().forEach(axis -> {
                        QuestionAxisInfoPO axisPO = BeanUtils.convert(axis, QuestionAxisInfoPO.class);
                        FileInfoBO file = fileMap.get(axisPO.getFileId());
                        if (file != null){
                            axisPO.setFileName(file.getFileName());
                            axisPO.setFileExt(file.getFileExt());
                            axisPO.setUrl(file.getUrl());
                        }
                        axisPO.setCreator(ppr.getUser().getId());
                        axisPO.setUpdator(ppr.getUser().getId());
                        axisPO.setCreateTime(ppr.getCreateTime());
                        axisPO.setUpdateTime(ppr.getCreateTime());
                        axises.add(axisPO);
                    });
                }
                questions.add(questionPO);
            }
        });
        if (CollectionUtils.isNotEmpty(questions)){
            paperQuestionInfoDAO.insertList(questions);
        }
        if (CollectionUtils.isNotEmpty(knowledges)){
            paperQuestionKnowledgeInfoDAO.insertList(knowledges);
        }
        if (CollectionUtils.isNotEmpty(options)){
            paperQuestionOptionInfoDAO.insertList(options);
        }
        if (CollectionUtils.isNotEmpty(axises)){
            paperQuestionAxisDAO.insertList(axises);
        }
    }

    private String getCode(CodeNameMapBO bo){
        return bo == null ? null : bo.getCode();
    }

    private String getId(IdNameMapBO bo){
        return bo == null ? null : bo.getId();
    }

    /**
     * Set PPR basic information through Content
     * @param ppr
     * @param content
     */
    private void setBasicInfo(PPRInfoBO ppr, ContentInfoDTO content) {
        ppr.setPaperId(content.getContentId());
        ppr.setContentId(content.getContentId());
        ppr.setUser(new IdNameMapBO(content.getCreator(), content.getCreatorName()));
        ppr.getAnswerCard().setExamId(content.getContentId());
        ppr.setCreateTime(new Date());
    }

    /**
     * Generate Primary Key
     * @param ppr
     */
    private void generatePrimaryKey(PPRInfoBO ppr) {
        Map<Long, Long> primaryKeyMap = new HashMap<>(); //Node:<Original, New>
        Map<String, String> questionIdMap = new HashMap<>(); //Question:<Original, New>
        ppr.getNodes().forEach(node -> {
            primaryKeyMap.put(node.getNodeId(), (Long)tokenGenerator.getToken());
            if (node.getQuestion() != null){
                String id = tokenGenerator.getToken().toString();
                questionIdMap.put(node.getQuestion().getQuestionId(), id);
                node.getQuestion().setQuestionId(id);
                node.getQuestion().setQuestionNo("ppr_" + id);
                if (CollectionUtils.isNotEmpty(node.getQuestion().getAxises())) {
                    for (int i = 0; i < node.getQuestion().getAxises().size(); i++) {
                        PPRQuestionAxisBO axis = node.getQuestion().getAxises().get(i);
                        axis.setAxisId(tokenGenerator.getToken().toString());
                        axis.setQuestionId(id);
                        axis.setSequence(i + 1);
                    }
                }
            }
        });
        Map<Long, Long> parentNodeMap = new HashMap<>();
        ppr.getNodes().forEach(node -> {
            node.setNodeId(primaryKeyMap.get(node.getNodeId()));
            node.setParentNodeId(primaryKeyMap.get(node.getParentNodeId()));
            parentNodeMap.put(node.getNodeId(), node.getParentNodeId());
        });
        ppr.getAnswerCard().setAnswerCardId((Long)tokenGenerator.getToken());
        if(ppr.getAnswerCard().getAxises() != null){
            ppr.getAnswerCard().getAxises().forEach(axis -> {
                axis.setAxisId((Long)tokenGenerator.getToken());
                axis.setNodeId(primaryKeyMap.get(axis.getNodeId()));
                axis.setParentNodeId(parentNodeMap.get(axis.getNodeId()));
                axis.setQuestionId(StringUtils.isBlank(questionIdMap.get(axis.getQuestionId().toString())) ? null : Long.valueOf(questionIdMap.get(axis.getQuestionId().toString())));
                if (axis.getParentId() == null){
                    axis.setParentId(axis.getQuestionId());
                }else {
                    axis.setParentId(StringUtils.isBlank(questionIdMap.get(axis.getParentId().toString())) ? null : Long.valueOf(questionIdMap.get(axis.getParentId().toString())));
                }
            });
        }
        if(ppr.getAnswerCard().getTimelines() != null){
            ppr.getAnswerCard().getTimelines().forEach(timeline -> {
                timeline.setTimelineId((Long)tokenGenerator.getToken());
                timeline.setNodeId(primaryKeyMap.get(timeline.getNodeId()));
                timeline.setParentNodeId(parentNodeMap.get(timeline.getParentNodeId()));
                timeline.setQuestionId(StringUtils.isBlank(questionIdMap.get(timeline.getQuestionId().toString())) ? null : Long.valueOf(questionIdMap.get(timeline.getQuestionId().toString())));
                if (timeline.getParentId() == null){
                    timeline.setParentId(timeline.getQuestionId());
                }else {
                    timeline.setParentId(StringUtils.isBlank(questionIdMap.get(timeline.getParentId().toString())) ? null : Long.valueOf(questionIdMap.get(timeline.getParentId().toString())));
                }
            });
        }
    }

    private ContentInfoDTO convertPPR2Content(PPRInfoBO pprInfoBO) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        Date today = Calendar.getInstance().getTime();
        ContentInfoDTO content = new ContentInfoDTO();
        content.setContentName(pprInfoBO.getName());
        content.setContentDescription(pprInfoBO.getName());
        if (pprInfoBO.getContentId() != null)
            content.setProviderContentId(pprInfoBO.getContentId().toString());
        content.setProviderCode(Constants.CONTENT_PRIVATE_TYPE);
        content.setTypeCode(Constants.CONTENT_TYPE_EXAM);
        content.setCreator(pprInfoBO.getUser().getId());
        content.setCreatorName(pprInfoBO.getUser().getName());
        content.setCreateTime(sdf.format(today));
        content.setUpdateTime(sdf.format(today));
        content.setSchoolId(pprInfoBO.getSchool().getId());
        content.setSchoolName(pprInfoBO.getSchool().getName());
        content.setStageCode(pprInfoBO.getStage().getCode());
        content.setStageName(pprInfoBO.getStage().getName());
        content.setGradeCode(pprInfoBO.getGrade().getCode());
        content.setGradeName(pprInfoBO.getGrade().getName());
        content.setSubjectCode(pprInfoBO.getSubject().getCode());
        content.setSubjectName(pprInfoBO.getSubject().getName());
        content.setHtmlContext(pprInfoBO.getName());
        content.setPlaintextContext(pprInfoBO.getName());
        if (CollectionUtils.isNotEmpty(pprInfoBO.getKnowledges())) {
            content.setKnowledgeList(BeanUtils.convert(pprInfoBO.getKnowledges(), KnowledgeInfoDTO.class));
        }
        content.setUsageCode(PPRConstants.DEFAULT_PPR_USE_CODE);
        if (CollectionUtils.isNotEmpty(pprInfoBO.getFiles())){
            content.setFileList(BeanUtils.convert(pprInfoBO.getFiles(), ContentFileInfoDTO.class));
        }
        content.setSourceCode("_ppr");
        return content;
    }

    public PPRInfoBO get(String id) {
        if (StringUtils.isBlank(id)) return null;
        String redisKey = String.format("com:enableets:edu:package:ppr:%s", id);
        String paperStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(paperStr)){
            return JsonUtils.convert(paperStr, PPRInfoBO.class);
        }else{
            ExamInfoPO examInfoPO = examInfoDAO.get(id, null);
            if (examInfoPO == null) return null;
            List<QuestionInfoPO> examQuestions = examInfoDAO.getExamQuestions(id, null);
            ExamInfoBO exam = PaperInfoUtils.mergeExamAndQuestion(BeanUtils.convert(examInfoPO, ExamInfoBO.class), BeanUtils.convert(examQuestions, QuestionInfoBO.class));
            PPRInfoBO pprInfoBO = PprInfoUtils.transformExam2Paper(exam);
            ContentInfoDTO content = contentInfoServiceSDK.get(Long.valueOf(id)).getData();
            if (content != null) {
                pprInfoBO.setUser(new IdNameMapBO(content.getCreator(), content.getCreatorName()));
                pprInfoBO.setStage(new CodeNameMapBO(content.getStageCode(), content.getStageName()));
                pprInfoBO.setProviderCode(content.getProviderCode());
                if (CollectionUtils.isNotEmpty(content.getFileList())) {
                    List<FileInfoBO> files = new ArrayList<>();
                    for (ContentFileInfoDTO file : content.getFileList()) {
                        if (StringUtils.isBlank(file.getFileExt()) || file.getFileExt().equals("paper") || file.getFileExt().equals("ppr") || file.getFileName().equals("exam.xml"))
                            continue;
                        files.add(BeanUtils.convert(file, FileInfoBO.class));
                    }
                    pprInfoBO.setFiles(files);
                }
                if (CollectionUtils.isNotEmpty(content.getKnowledgeList())){
                    pprInfoBO.setKnowledges(BeanUtils.convert(content.getKnowledgeList(), QuestionKnowledgeInfoBO.class));
                }
            }
            stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.convert(pprInfoBO), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
            return pprInfoBO;
        }
    }

    /**
     * Edit PPR InfoBO
     * @param pprInfoBO
     * @return
     */
    public PPRInfoBO edit(PPRInfoBO pprInfoBO) {
        contentInfoServiceSDK.remove(pprInfoBO.getPaperId());
        new Thread(() -> {
            this.remove(pprInfoBO.getPaperId().toString());
            answerCardInfoService.remove(pprInfoBO.getAnswerCard().getAnswerCardId(), pprInfoBO.getPaperId(), pprInfoBO.getUser().getId());
        }).start();
        stringRedisTemplate.delete(String.format("com:enableets:edu:package:ppr:%s", pprInfoBO.getPaperId()));
        return this.add(pprInfoBO);
    }

    /**
     * Remove PPR Info
     * @param paperId
     */
    public void remove(String paperId){
        PPRInfoBO pprInfoBO = this.get(paperId);
        if (pprInfoBO == null) return;
        examInfoDAO.deleteById(paperId);
        if (CollectionUtils.isNotEmpty(pprInfoBO.getNodes())){
            List<String> questionIds = new ArrayList<>();
            for (PPRNodeInfoBO node : pprInfoBO.getNodes()) {
                if (node.getLevel().intValue() == 3 || node.getLevel().intValue() == 4){
                    if (node.getQuestion() != null) questionIds.add(node.getQuestion().getQuestionId());
                }
            }
            paperQuestionInfoDAO.batchDelete(questionIds);
        }
    }
}

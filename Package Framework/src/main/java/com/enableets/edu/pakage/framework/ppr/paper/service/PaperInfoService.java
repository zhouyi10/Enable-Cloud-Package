package com.enableets.edu.pakage.framework.ppr.paper.service;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.framework.core.util.token.ITokenGenerator;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.ppr.test.service.TestPaperService;
import com.enableets.edu.pakage.framework.ppr.bo.ExamInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.PaperNodeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.QuestionKnowledgeInfoBO;
import com.enableets.edu.pakage.framework.ppr.bo.SaveExamBO;
import com.enableets.edu.pakage.framework.ppr.core.PPRConstants;
import com.enableets.edu.pakage.framework.ppr.core.tablecopy.TableCopyComponent;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamKindInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamQuestionChildInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamQuestionInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.dao.ExamQuestionTypeInfoDAO;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamInfoPO;
import com.enableets.edu.pakage.framework.ppr.paper.po.QuestionInfoPO;
import com.enableets.edu.pakage.framework.ppr.utils.PaperInfoUtils;
import com.enableets.edu.pakage.ppr.action.PPRPackageLifecycle;
import com.enableets.edu.pakage.ppr.bean.EnablePPRBeanDefinition;
import com.enableets.edu.pakage.ppr.bean.PPRPackageWrapper;
import com.enableets.edu.pakage.ppr.bo.CodeNameMapBO;
import com.enableets.edu.pakage.ppr.bo.IdNameMapBO;
import com.enableets.edu.pakage.ppr.bo.NodeInfoBO;
import com.enableets.edu.ppr.adapter.service.SavePaperAdapterService;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentKnowledgeInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentTagDTO;
import com.enableets.edu.sdk.content.dto.KnowledgeInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.content.service.IContentKnowledgeInfoService;

import java.io.IOException;
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
 * Storage Paper Info
 * @author walle_yu@enable-ets.com
 * @since 2020/08/07
 **/
@Service
public class PaperInfoService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PaperInfoService.class);

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TestPaperService testPaperService;

    @Autowired
    private ExamInfoDAO examInfoDAO;

    @Autowired
    private ExamKindInfoDAO examKindInfoDAO;

    @Autowired
    private ExamQuestionTypeInfoDAO examQuestionTypeInfoDAO;

    @Autowired
    private ExamQuestionInfoDAO examQuestionInfoDAO;

    @Autowired
    private ExamQuestionChildInfoDAO examQuestionChildInfoDAO;

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private IContentKnowledgeInfoService contentKnowledgeInfoServiceSDK;

    @Autowired
    private OfflinePaperService offlinePaperService;

    @Autowired
    private ITokenGenerator tokenGenerator;

    @Autowired
    private SavePaperAdapterService savePaperAdapterService;

    /**
     * Get Paper Info
     * @param paperId Paper ID
     * @return
     */
    public PaperInfoBO get(String paperId){
        if (StringUtils.isBlank(paperId)) return null;
        String redisKey = String.format("com:enableets:edu:package:ppr:paper:%s", paperId);
        String paperStr = stringRedisTemplate.opsForValue().get(redisKey);
        if (StringUtils.isNotBlank(paperStr)){
            return JsonUtils.convert(paperStr, PaperInfoBO.class);
        }else{
            ExamInfoPO examInfoPO = examInfoDAO.get(paperId, null);
            if (examInfoPO != null) {
                List<QuestionInfoPO> examQuestions = examInfoDAO.getExamQuestions(paperId, null);
                ExamInfoBO exam = PaperInfoUtils.mergeExamAndQuestion(BeanUtils.convert(examInfoPO, ExamInfoBO.class), BeanUtils.convert(examQuestions, QuestionInfoBO.class));
                PaperInfoBO paperInfoBO = PaperInfoUtils.transformExam2Paper(exam);
                stringRedisTemplate.opsForValue().set(redisKey, JsonUtils.convert(paperInfoBO), Constants.DEFAULT_REDIS_CACHE_EXPIRE_TIME, TimeUnit.SECONDS);
                return paperInfoBO;
            }else{
                return testPaperService.get(paperId);
            }
        }
    }

    /**
     * Add New Paper
      * @param paperInfoBO
     * @return
     */
    public PaperInfoBO add(PaperInfoBO paperInfoBO) {
        //1、Add Paper Content Info
        ContentInfoDTO content = contentInfoServiceSDK.add(this.convertPaper2Content(paperInfoBO)).getData();
        paperInfoBO.setPaperId(content.getContentId());
        paperInfoBO.setContentId(content.getContentId());
        //2、Copy Paper Question From Question_Storage To Paper_Storage
        TableCopyComponent questionCopyService = SpringBeanUtils.getBean("questionCopyService");
        List<String> questionIds = paperInfoBO.getNodes().stream().filter(e -> e.getLevel().intValue() == PPRConstants.LEVEL_EXAM_QUES).map(e -> e.getQuestion().getQuestionId()).collect(Collectors.toList());
        Map<String, Object> params = new HashMap<>();
        params.put("questionIds", questionIds);
        try {
            questionCopyService.copyTable(params);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new MicroServiceException("38-02-001", "Copy Question To Paper Storage Error!");
        }
        //3、Add Paper Info To Paper_Storage
        this.generateNodeId(paperInfoBO.getNodes(), paperInfoBO.getPaperId());
        this.savePaperInfo(paperInfoBO);
        //4、Build&Add PPr File Info To Content

        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        // 2. ppr model
        EnablePPRBeanDefinition pprBeanDefinition =  this.convertPaperInfo2PprPaperBO(paperInfoBO);
        // 3. PPR wrapper
        PPRPackageWrapper pprPackageWrapper = new PPRPackageWrapper(configuration, pprBeanDefinition, null);
        // 4.PPR Lifecycle
        PPRPackageLifecycle packageLifecycle = new PPRPackageLifecycle(pprPackageWrapper);
        // 5.build .ppr
        packageLifecycle.build();
        // 6. Get .ppr document
        PackageFileInfo packageFileInfo = pprPackageWrapper.getPackageFileInfo();
        //PPR ppr = pprHandlerFactory.getPprBuilderHandler().build(new PPRFileContent(new PPRInfoBO(pprBO, null)));
        contentInfoServiceSDK.addFileToContent(this.convertPpr2ContentFile(packageFileInfo, content.getContentId(), content.getProviderCode()));
        //5、Build&Add .paper File Info To Content
        offlinePaperService.makeOfflinePaper(paperInfoBO);
        return paperInfoBO;
    }

    private void generateNodeId(List<PaperNodeInfoBO> nodes, Long paperId) {
        // 1 校验数据源
        if (CollectionUtils.isEmpty(nodes) || paperId == null) {
            return;
        }
        // 2 生成节点标识以及题目标识，map中存储 key(原始节点) -> value(新标识)
        Map<Long, Long> map = new HashMap<Long, Long>();
        for (PaperNodeInfoBO node : nodes) {
            Long id = (Long) tokenGenerator.getToken();// 节点标识
            map.put(node.getNodeId(), id);
        }
        // 3 设置主键
        for (PaperNodeInfoBO node : nodes) {
            Long oldNodeId = node.getNodeId();
            node.setNodeId(map.get(oldNodeId)); // 3.2 节点设置节点标识
            if (node.getParentNodeId() != null) {
                Long id = map.get(node.getParentNodeId());
                if (id == null) {
                    throw new MicroServiceException("38-02-001", "Not Found Parent Node："+ oldNodeId +"--------:" + node.getLevel() + "----:"+node.getName());
                }
                node.setParentNodeId(id); // 3.8 节点设置父节点标识
            }
        }
    }

    private ContentFileInfoDTO convertPpr2ContentFile(PackageFileInfo ppr, Long contentId, String providerCode) {
        ContentFileInfoDTO fileInfoDTO = new ContentFileInfoDTO();
        fileInfoDTO.setFileId(ppr.getFileId());
        fileInfoDTO.setFileName(ppr.getName());
        fileInfoDTO.setFileExt(ppr.getExt());
        fileInfoDTO.setUrl(ppr.getDownloadUrl());
        fileInfoDTO.setMd5(ppr.getMd5());
        fileInfoDTO.setSize(ppr.getSize());
        fileInfoDTO.setSizeDisplay(ppr.getSizeDisplay());
        fileInfoDTO.setContentId(contentId.toString());
        fileInfoDTO.setProviderCode(providerCode);
        fileInfoDTO.setFileOrder(0);
        return fileInfoDTO;
    }

    /**
     *
     * @param paperInfoBO
     * @return
     */
    private EnablePPRBeanDefinition convertPaperInfo2PprPaperBO(PaperInfoBO paperInfoBO) {
        Date today = Calendar.getInstance().getTime();
        EnablePPRBeanDefinition paper = new EnablePPRBeanDefinition();
        paper.setPaperId(paperInfoBO.getPaperId() + "");
        paper.setName(paperInfoBO.getName());
        paper.setStage(BeanUtils.convert(paperInfoBO.getStage(), CodeNameMapBO.class));
        paper.setGrade(BeanUtils.convert(paperInfoBO.getGrade(), CodeNameMapBO.class));
        paper.setSubject(BeanUtils.convert(paperInfoBO.getSubject(), CodeNameMapBO.class));
        paper.setUser(BeanUtils.convert(paperInfoBO.getUser(), IdNameMapBO.class));
        paper.setCreateTime(today);
        paper.setTotalPoints(paperInfoBO.getTotalPoints());
        paper.setAnswerCostTime(100L);
        paper.setNodes(BeanUtils.convert(paperInfoBO.getNodes(), NodeInfoBO.class));
        return paper;
    }

    /**
     * Save Paper Info
     * @param paperInfoBO
     */
    public void savePaperInfo(PaperInfoBO paperInfoBO) {
        SaveExamBO examInfo = PaperInfoUtils.transformExam(paperInfoBO);
        examInfoDAO.insertSelective(examInfo.getExam());
        examKindInfoDAO.insertList(examInfo.getExamKinds());
        examQuestionTypeInfoDAO.insertList(examInfo.getExamQuestionTypes());
        examQuestionInfoDAO.insertList(examInfo.getExamQuestions());
        if (CollectionUtils.isNotEmpty(examInfo.getExamQuestionChildren())) {
            examQuestionChildInfoDAO.insertList(examInfo.getExamQuestionChildren());
        }
        savePaperAdapterService.save(BeanUtils.convert(examInfo, com.enableets.edu.ppr.adapter.bo.SaveExamBO.class));
    }

    private ContentInfoDTO convertPaper2Content(PaperInfoBO paperInfoBO) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        Date today = Calendar.getInstance().getTime();
        ContentInfoDTO content = new ContentInfoDTO();
        content.setContentName(paperInfoBO.getName());
        content.setContentDescription(paperInfoBO.getName());
        content.setProviderCode(Constants.CONTENT_PRIVATE_TYPE);
        content.setTypeCode(Constants.CONTENT_TYPE_EXAM);
        content.setCreator(paperInfoBO.getUser().getId());
        content.setCreatorName(paperInfoBO.getUser().getName());
        content.setCreateTime(sdf.format(today));
        content.setUpdateTime(sdf.format(today));
        content.setSchoolId(paperInfoBO.getSchool().getId());
        content.setSchoolName(paperInfoBO.getSchool().getName());
        content.setStageCode(paperInfoBO.getStage().getCode());
        content.setStageName(paperInfoBO.getStage().getName());
        content.setGradeCode(paperInfoBO.getGrade().getCode());
        content.setGradeName(paperInfoBO.getGrade().getName());
        content.setSubjectCode(paperInfoBO.getSubject().getCode());
        content.setSubjectName(paperInfoBO.getSubject().getName());
        content.setHtmlContext(paperInfoBO.getName());
        content.setPlaintextContext(paperInfoBO.getName());
        content.setSourceCode("_ppr");
        if (CollectionUtils.isNotEmpty(paperInfoBO.getKnowledges())) {
            content.setKnowledgeList(BeanUtils.convert(paperInfoBO.getKnowledges(), KnowledgeInfoDTO.class));
        }else{
            List<QuestionKnowledgeInfoBO> knowledges = new ArrayList<>();
            for (PaperNodeInfoBO node : paperInfoBO.getNodes()) {
                if (node.getLevel().intValue() != 3 || node.getQuestion() == null || CollectionUtils.isEmpty(node.getQuestion().getKnowledges())) continue;
                knowledges.addAll(node.getQuestion().getKnowledges());
            }
            LOGGER.info("exam_knowledge_info:" + JsonUtils.convert(knowledges));
            if (CollectionUtils.isNotEmpty(knowledges)) {
                try {
                    content.setKnowledgeList(getExamKnowledge(knowledges));
                }catch (Exception e){
                    LOGGER.error("试卷知识点处理异常!");
                }
            }
        }
        content.setUsageCode(paperInfoBO.getUsageCode());
        List<ContentTagDTO> tags = new ArrayList<>();
        try{
            String examDifficultyLevel = getExamDifficultyLevel(paperInfoBO);
            ContentTagDTO contentTagDTO = new ContentTagDTO();
            contentTagDTO.setTagType("_difficulty");
            contentTagDTO.setName(examDifficultyLevel);
            tags.add(contentTagDTO);
            contentTagDTO = new ContentTagDTO();
            contentTagDTO.setTagType("_point");
            contentTagDTO.setName(this.getDifficultyPoint(examDifficultyLevel));
            tags.add(contentTagDTO);
        }catch (Exception e){
            LOGGER.error("Exam Difficulty Calc Fail!");
        }
        content.setTags(tags);
        return content;
    }


    /**
     * 根据题目的知识点获取试卷的知识点,同一个节点下面的子节点数大于2，取该节点
     * @return
     */
    private List<KnowledgeInfoDTO> getExamKnowledge(List<QuestionKnowledgeInfoBO> knowledges){
        List<QuestionKnowledgeInfoBO> knowledges2 = new ArrayList<>();  //先去重
        int levels = 0;
        List<String> searchCodes = new ArrayList<>();
        for (QuestionKnowledgeInfoBO knowledge : knowledges) {
            int length = knowledge.getSearchCode().split("-").length;
            if (length > levels) levels = length;
            if (knowledges2.contains(knowledge)) continue;
            knowledges2.add(knowledge);
            searchCodes.add(knowledge.getSearchCode());
        }
        List<String> codes = recursionKnowledge(searchCodes, levels);
        List<QuestionKnowledgeInfoBO> result = new ArrayList<>();
        if (codes.size() > 0){
            String ids = codes.stream().map(e -> {
                if (e.indexOf("-") == -1) return e;
                else return e.substring(e.lastIndexOf("-") + 1, e.length());
            }).reduce((x, y) -> x + "," + y).get();
            List<ContentKnowledgeInfoDTO> knowledgeInfos = contentKnowledgeInfoServiceSDK.getKnowledgeInfoListByIds(ids);
            result.addAll(BeanUtils.convert(knowledgeInfos, QuestionKnowledgeInfoBO.class));
        }
        return BeanUtils.convert(result, KnowledgeInfoDTO.class);
    }

    private List<String> recursionKnowledge(List<String> searchCodes, int length){  //同一级取父节点
        if (searchCodes != null && searchCodes.size() == 1) return searchCodes;
        List<String> result = new ArrayList<>();
        for (String code1 : searchCodes) {
            if (code1.split("-").length != length) {  //其它层次的节点不处理,一层层的处理
                result.add(code1); continue;
            }
            String pcode1 = null;
            if (code1.lastIndexOf("-") == -1) pcode1 = code1;
            else pcode1 = code1.substring(0, code1.lastIndexOf("-"));
            if (result.contains(pcode1)) continue;
            boolean ret = true; //code1是否保留标识
            for (String code2 : searchCodes) {
                String pcode2 = null;
                if (code2.lastIndexOf("-") == -1) pcode2 = code2;
                else pcode2 = code2.substring(0, code2.lastIndexOf("-"));
                if (pcode1.equals(pcode2)){
                    ret = false; break;
                }
            }
            if (ret) result.add(code1);
            else result.add(pcode1);
        }
        if (length == 1 || result.size() == 1) return result;
        else return recursionKnowledge(result, (length-1));
    }

    /**
     * 根据题目难易度算试卷难易度
     * 题目难度分3类 [1,2] 容易, [3]中 ,[4,5]困难
     * 1、如果只包含2类难度，取超过50%的作为试卷难度
     * 2、如果包含3类难度，困难 > 30% 试卷困难  中>35%就是中 ...
     * @param paperInfo
     * @return
     */
    private String getExamDifficultyLevel(PaperInfoBO paperInfo) {
        String difficultyLevel = "0"; //默认简单的
        int questionCount = 0;
        int hardCount = 0;
        int middleCount = 0;
        int easyCount = 0;
        for (PaperNodeInfoBO node : paperInfo.getNodes()) {
            if (node.getLevel().intValue() != 3) continue;
            questionCount ++;
            if (node.getQuestion() != null && node.getQuestion().getDifficulty() != null){
                String difficultyCode = node.getQuestion().getDifficulty().getCode();
                if ("5".equals(difficultyCode) || "4".equals(difficultyCode)) hardCount++;
                if ("3".equals(difficultyCode)) middleCount++;
                if ("2".equals(difficultyCode) || "1".equals(difficultyCode)) easyCount++;
            }
        }
        if (hardCount == 0 || middleCount == 0 || easyCount == 0){
            if (hardCount > 0.5 * questionCount) difficultyLevel = "2";
            if (middleCount > 0.5 * questionCount) difficultyLevel = "1";
            if (easyCount > 0.5 * questionCount) difficultyLevel = "0";
        }else{
            if (hardCount > 0.3 * questionCount) difficultyLevel = "2";
            else if (middleCount > 0.35 * questionCount) difficultyLevel = "1";
            else difficultyLevel = "0";
        }
        return difficultyLevel;
    }

    /**
     * 简单对应 5点 , 中等 对应 10点, 困难 对应 15点
     * @param difficultyLevel
     * @return
     */
    private String getDifficultyPoint(String difficultyLevel){
        if (difficultyLevel.equals("0")) return "5";
        else if (difficultyLevel.equals("1")) return "10";
        else return "15";
    }

}

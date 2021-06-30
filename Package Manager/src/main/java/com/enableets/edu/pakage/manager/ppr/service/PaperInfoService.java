package com.enableets.edu.pakage.manager.ppr.service;

import cn.hutool.core.io.FileUtil;
import cn.hutool.json.JSONObject;
import com.enableets.edu.framework.core.util.JsonUtils;
import com.enableets.edu.pakage.framework.ppr.paper.po.ExamStypeInfoPO;
import com.enableets.edu.pakage.manager.core.PackageConfigReader;
import com.enableets.edu.pakage.manager.ppr.bo.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.enableets.edu.pakage.manager.core.BaseInfoService;
import com.enableets.edu.pakage.manager.core.Constants;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.pakage.ppr.dto.AddPaperInfoDTO;
import com.enableets.edu.sdk.pakage.ppr.dto.QueryPaperInfoResultDTO;
import com.enableets.edu.sdk.pakage.ppr.service.IPPRPaperInfoService;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringWebFluxTemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/08/07
 **/
@Service
public class PaperInfoService {

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private BaseInfoService baseInfoService;

    @Autowired
    private IPPRPaperInfoService pprPaperInfoServiceSDK;

    @Autowired
    private ExamStypeInfoService examStypeInfoService;

    @Autowired
    private PackageConfigReader packageConfigReader;



    /**
     * Query Paper List
     * @param queryPaperBO
     * @return
     */
    public List<PaperInfoBO> query(QueryPaperBO queryPaperBO){
        List<ContentInfoDTO> contents = contentInfoServiceSDK.query(this.buildQueryPaperParam(queryPaperBO));
        return this.convertContent2Paper(contents);
    }

    /**
     * Query Paper By ID
     * @param paperId
     * @return
     */
    public PaperInfoBO get(Long paperId){
        QueryPaperInfoResultDTO queryPaperInfoResultDTO = pprPaperInfoServiceSDK.get(paperId);
        return BeanUtils.convert(queryPaperInfoResultDTO, PaperInfoBO.class);
    }

    public PaperInfoBO getV2(Long paperId){
        PaperInfoBO paperInfoBO = this.get(paperId);
        if (paperInfoBO == null) return null;
        for (PaperNodeInfoBO node : paperInfoBO.getNodes()) {
            if (node.getLevel().intValue() == 3 || node.getLevel().intValue() == 4) {
                if (node.getQuestion() != null && node.getQuestion().getStem() != null) {
                    if (StringUtils.isNotBlank(node.getQuestion().getStem().getRichText()) && node.getQuestion().getStem().getRichText().indexOf("questionoption") > -1){
                        node.getQuestion().setOptions(new ArrayList<PaperQuestionOptionBO>());
                    }
                }
            }
        }
        return paperInfoBO;
    }

    public PaperInfoBO getAboutLevel(Long paperId){
        QueryPaperInfoResultDTO queryPaperInfoResultDTO = pprPaperInfoServiceSDK.get(paperId);
        PaperInfoBO paper = BeanUtils.convert(queryPaperInfoResultDTO, PaperInfoBO.class);
        paper.getNodes().forEach(node -> {
            getChildren(paper, node);
        });
        Iterator<PaperNodeInfoBO> it = paper.getNodes().iterator();
        while (it.hasNext()) {
            PaperNodeInfoBO node = it.next();
            if (node.getLevel().intValue() > 1) it.remove();
        }
        return paper;
    }

    private void getChildren(PaperInfoBO paper, PaperNodeInfoBO node){
        List<PaperNodeInfoBO> children = new ArrayList<>();
        for (PaperNodeInfoBO e : paper.getNodes()) {
            if (node.getLevel().intValue() == 4) break;
            if (e.getLevel().intValue() == node.getLevel().intValue() + 1 && e.getParentNodeId().longValue() == node.getNodeId().longValue()){
                getChildren(paper, e);
                children.add(e);
            }
        }
        node.setChildren(children);
    }

    /**
     * Convert Content Info To Paper Info
     * @param contents Content List
     * @return
     */
    private List<PaperInfoBO> convertContent2Paper(List<ContentInfoDTO> contents) {
        if (CollectionUtils.isEmpty(contents)) return null;
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        List<PaperInfoBO> papers = new ArrayList<>();
        contents.forEach(e -> {
            PaperInfoBO paper = new PaperInfoBO();
            paper.setPaperId(e.getContentId());
            paper.setName(e.getContentName());
            paper.setStage(new CodeNameMapBO(e.getStageCode(), e.getStageName()));
            paper.setGrade(new CodeNameMapBO(e.getGradeCode(), e.getGradeName()));
            paper.setSubject(new CodeNameMapBO(e.getSubjectCode(), e.getSubjectName()));
            paper.setSchool(new IdNameMapBO(e.getSchoolId(), e.getSchoolName()));
            paper.setUser(new IdNameMapBO(e.getCreator(), e.getCreatorName()));
            paper.setCreator(e.getCreator());
            try {
                paper.setCreateTime(sdf.parse(e.getCreateTime()));
                paper.setUpdateTime(sdf.parse(e.getCreateTime()));
            }catch (Exception ex){

            }
            paper.setUpdator(e.getCreator());
            paper.setContentId(e.getContentId());
            papers.add(paper);
        });
        return papers;
    }

    private ContentInfoDTO buildQueryPaperParam(QueryPaperBO queryPaperBO){
        ContentInfoDTO content = new ContentInfoDTO();
        content.setStageCode(queryPaperBO.getStageCode());
        content.setGradeCode(queryPaperBO.getGradeCode());
        content.setSubjectCode(queryPaperBO.getSubjectCode());
        content.setMaterialVersion(queryPaperBO.getMaterialVersion());
        content.setKeyword(queryPaperBO.getName());
        content.setTypeCode(Constants.CONTENT_TYPE_EXAM);
        content.setProviderCode(queryPaperBO.getProviderCode());
        content.setCreator(queryPaperBO.getUserId());
        content.setSchoolId(baseInfoService.getUserSchoolInfo(queryPaperBO.getUserId()).getId());
        content.setZoneCode(baseInfoService.getUserZone(queryPaperBO.getUserId()));
        content.setOffset(queryPaperBO.getOffset());
        content.setRows(queryPaperBO.getRows());
        return content;
    }

    /**
     * Count Paper Quantity
     * @param queryPaperBO
     * @return
     */
    public Integer count(QueryPaperBO queryPaperBO){
        return contentInfoServiceSDK.count(this.buildQueryPaperParam(queryPaperBO));
    }

    /**
     * Add new Paper
     * @param paperInfoBO
     * @return
     */
    public PaperInfoBO add(PaperInfoBO paperInfoBO) {
        paperInfoBO.setUser(BeanUtils.convert(baseInfoService.getUserInfo(paperInfoBO.getUserId()), IdNameMapBO.class));
        paperInfoBO.setSchool(BeanUtils.convert(baseInfoService.getUserSchoolInfo(paperInfoBO.getUserId()), IdNameMapBO.class));
        QueryPaperInfoResultDTO paper = pprPaperInfoServiceSDK.add(BeanUtils.convert(paperInfoBO, AddPaperInfoDTO.class));
        JSONObject examStypeinfoPOjsonObj = new JSONObject(paperInfoBO.getExamStypeinfoPO());
        ExamStypeInfoPO ExamStypeinfoPO = BeanUtils.convert(examStypeinfoPOjsonObj, ExamStypeInfoPO.class);
        ExamStypeinfoPO.setExamId(String.valueOf(paper.getPaperId()));
        Timestamp timestamp = new Timestamp(new Date().getTime());
        ExamStypeinfoPO.setCreateTime(timestamp);
        ExamStypeinfoPO.setCreator(paperInfoBO.getUser().getName());
        Integer integer = examStypeInfoService.addExamStypeinfoPO(ExamStypeinfoPO);
        return BeanUtils.convert(paper, PaperInfoBO.class);
    }

    /**
     * Add Question Used Times
     * @param questionIds
     */
    public void increaseUsedTimes(String questionIds) {
        if (StringUtils.isBlank(questionIds)) return;
        String[] split = questionIds.split(",");
        if (split.length > 10){
            ExecutorService executorService = null;
            try {
                executorService = Executors.newFixedThreadPool(4);
                for (String s : split) {
                    if (StringUtils.isBlank(s)) continue;
                    executorService.submit(() -> {
                        contentInfoServiceSDK.incrementDownloadTimes(Long.valueOf(s), null);
                    });
                }
            }finally {
                executorService.shutdown();
            }
        }else{
            for (String s : split) {
                if (StringUtils.isBlank(s)) continue;
                contentInfoServiceSDK.incrementDownloadTimes(Long.valueOf(s), null);
            }
        }

    }

    /**
     * 查询资源列表
     * @param content
     * @return
     */
    public List<ContentInfoBO> queryContent(QueryContentBO content) {
        List<ContentInfoDTO> contents = contentInfoServiceSDK.query(this.convertQueryContentParam(content));
        return BeanUtils.convert(contents, ContentInfoBO.class);
    }

    /**
     * 统计资源数量
     * @param content
     * @return
     */
    public Integer countContent(QueryContentBO content) {
        return contentInfoServiceSDK.count(this.convertQueryContentParam(content));
    }

    private ContentInfoDTO convertQueryContentParam(QueryContentBO content){
        ContentInfoDTO contentDTO = BeanUtils.convert(content, ContentInfoDTO.class);
        contentDTO.setSchoolId(baseInfoService.getUserSchoolInfo(content.getUserId()).getId());
        contentDTO.setZoneCode(baseInfoService.getUserZone(content.getUserId()));
        return contentDTO;
    }

    public String getContentJson(Long paperId) {
        ContentInfoDTO content = contentInfoServiceSDK.get(paperId).getData();
        return JsonUtils.convert(BeanUtils.convert(content, ContentInfoBO.class));
    }

   /* public String createStaticHtml(Long paperId){
        PrintWriter writer = null;
        try {
            PaperInfoBO paper = this.getV2(paperId);
            if (paper == null) return null;
            //1、Template Resolver
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            //2、 Template Path
            resolver.setPrefix("/templates/ppr/paper/word/");
            //3、 Template Suffix
            resolver.setSuffix(".html");
            TemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
            templateEngine.setTemplateResolver(resolver);
            //4、 Template Context
            Context context = new Context();
            context.setVariable("paperInfo", paper);
            //5、 Print document
            File dir = new File(packageConfigReader.getPaperStaticPath());
            if (!dir.exists())  FileUtil.mkdir(dir);
            File file = new File(packageConfigReader.getPaperStaticPath(), paper.getPaperId() + ".html");
            if (!file.exists()) FileUtil.touch(file);
            //else return file.toString();
            writer = new PrintWriter(file, "UTF-8");
            templateEngine.process("word-preview", context, writer);
            return file.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return null;
    }*/


    public String createStaticHtml(Long paperId){
        PrintWriter writer = null;
        try {
            PaperInfoBO paper = this.getV2(paperId);
            if (paper == null) return null;
            List<PaperNodeInfoBO> nodes = paper.getNodes();
            List<PaperNodeInfoBO> newnodes = new ArrayList<PaperNodeInfoBO>();
            for (PaperNodeInfoBO paperNodeInfoBO:nodes){
                BigDecimal value = new BigDecimal(paperNodeInfoBO.getPoints());
                BigDecimal noZeros = value.stripTrailingZeros();
                String result = noZeros.toPlainString();
                paperNodeInfoBO.setRealPoints(result);
                newnodes.add(paperNodeInfoBO);
            }
            paper.setNodes(newnodes);
            ExamStypeInfoPO examStypeinfoPO = examStypeInfoService.querybyid(String.valueOf(paperId));
            //1、Template Resolver
            ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
            //2、 Template Path
            resolver.setPrefix("/templates/ppr/paper/word/");
            //3、 Template Suffix
            resolver.setSuffix(".html");
            TemplateEngine templateEngine = new SpringWebFluxTemplateEngine();
            templateEngine.setTemplateResolver(resolver);
            //4、 Template Context
            Context context = new Context();
            context.setVariable("paperInfo", paper);
            context.setVariable("examStypeinfoPO", examStypeinfoPO);
            /*HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("paperInfo", paper);
            hashMap.put("examStypeinfoPO",examStypeInfoPO);
            context.setVariables(hashMap);*/
            //5、 Print document
            File dir = new File(packageConfigReader.getPaperStaticPath());
            if (!dir.exists())  FileUtil.mkdir(dir);
            File file = new File(packageConfigReader.getPaperStaticPath(), paper.getPaperId() + ".html");
            if (!file.exists()) FileUtil.touch(file);
            //else return file.toString();
            writer = new PrintWriter(file, "UTF-8");
            templateEngine.process("word-preview", context, writer);
            return file.toString();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }
        return null;
    }
}

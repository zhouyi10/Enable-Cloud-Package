package com.enableets.edu.pakage.framework.etm.core;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.StringEscapeUtils;
import com.enableets.edu.pakage.etm.bean.EnableETMBeanDefinition;
import com.enableets.edu.pakage.etm.bo.CodeNameMapBO;
import com.enableets.edu.pakage.etm.bo.ContentBO;
import com.enableets.edu.pakage.etm.bo.IdNameMapBO;
import com.enableets.edu.pakage.etm.bo.ItemNodeInfoBO;
import com.enableets.edu.pakage.etm.bo.PageNodeInfoBO;
import com.enableets.edu.pakage.etm.bo.RegionBO;
import com.enableets.edu.pakage.framework.core.Constants;
import com.enableets.edu.pakage.framework.etm.bo.ETMInfoBO;
import com.enableets.edu.pakage.framework.etm.bo.ETMPageBO;
import com.enableets.edu.pakage.framework.etm.bo.EtmCodeNameMapBO;
import com.enableets.edu.pakage.framework.etm.bo.EtmIdNameMapBO;
import com.enableets.edu.pakage.framework.etm.bo.EtmRegionBO;
import com.enableets.edu.pakage.framework.etm.bo.PageBO;
import com.enableets.edu.pakage.framework.etm.bo.SemtenceInfoBO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.AddBusinessFileInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.AddPageQuestionAnswerInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.AddPageQuestionInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.EditBookInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.EditBookPageInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.EditPageInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.FileInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageQuestionAnswerInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageQuestionInfoDTO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.logging.log4j.util.Strings;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author justice_zhou@enable-ets.com
 * @date 2021/4/9
 **/

public class EtmTransfor {


    public static ContentInfoDTO convertETM2Content(ETMInfoBO etmInfoBO) {
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DEFAULT_DATE_TIME_FORMAT);
        Date today = Calendar.getInstance().getTime();
        ContentInfoDTO content = new ContentInfoDTO();
        content.setContentName(etmInfoBO.getTextBookName());
        content.setContentDescription(etmInfoBO.getTextBookName());

        //contentId
        if (etmInfoBO.getContentId() != null)
            content.setProviderContentId(etmInfoBO.getContentId().toString());

        content.setProviderCode(ETMConstants.CONTENT_PRIVATE_TYPE);
        content.setTypeCode(ETMConstants.CONTENT_TYPE_ETM);
        content.setTypeName(ETMConstants.CONTENT_TYPE_NAME);
        content.setCreator(etmInfoBO.getUser().getId());
        content.setCreatorName(etmInfoBO.getUser().getName());
        content.setCreateTime(sdf.format(today));
        content.setUpdateTime(sdf.format(today));
        content.setStageCode(etmInfoBO.getStage().getCode());
        content.setStageName(etmInfoBO.getStage().getName());
        content.setGradeCode(etmInfoBO.getStage().getCode());
        content.setGradeName(etmInfoBO.getGrade().getName());
        content.setSubjectCode(etmInfoBO.getSubject().getCode());
        content.setSubjectName(etmInfoBO.getSubject().getName());
        content.setHtmlContext(etmInfoBO.getTextBookName());
        content.setPlaintextContext(etmInfoBO.getTextBookName());
        //ETMConstants
        content.setUsageCode(ETMConstants.DEFAULT_ETM_USE_CODE);
        //fileList

        content.setSourceCode("_etm");
        return content;
    }

    public static EnableETMBeanDefinition convertETMVO2ETMBO(ETMInfoBO etmInfoBO) {

        EnableETMBeanDefinition etmBeanDefinition = new EnableETMBeanDefinition();

        etmBeanDefinition.setBookId(etmInfoBO.getEtmBookId());
        etmBeanDefinition.setTextBookName(etmInfoBO.getTextBookName());
        etmBeanDefinition.setIsbn(etmInfoBO.getIsbn());

        if (etmInfoBO.getCoverImgName() != null)
            etmBeanDefinition.setCover("files/images/" + etmInfoBO.getCoverImgName());
        etmBeanDefinition.setCoverUrl(etmInfoBO.getCoverUrl());

        etmBeanDefinition.setStage(BeanUtils.convert(etmInfoBO.getStage(), CodeNameMapBO.class));
        etmBeanDefinition.setGrade(BeanUtils.convert(etmInfoBO.getGrade(), CodeNameMapBO.class));
        etmBeanDefinition.setSubject(BeanUtils.convert(etmInfoBO.getSubject(), CodeNameMapBO.class));
        etmBeanDefinition.setTextBookVersion(BeanUtils.convert(etmInfoBO.getTextBookVersion(), CodeNameMapBO.class));
        etmBeanDefinition.setTerm(BeanUtils.convert(etmInfoBO.getTerm(), CodeNameMapBO.class));
        etmBeanDefinition.setUser(BeanUtils.convert(etmInfoBO.getUser(), IdNameMapBO.class));

        etmBeanDefinition.setCreateTime(etmInfoBO.getCreatTime());
        etmBeanDefinition.setCreator(etmInfoBO.getCreator());
        etmBeanDefinition.setContentId(etmInfoBO.getContentId());

        List<PageBO> pageList = etmInfoBO.getPageList();
        List<PageNodeInfoBO> pageNodeInfoBOS = new ArrayList<>();
        for (PageBO page : pageList) {

            PageNodeInfoBO pageNodeInfoBO = new PageNodeInfoBO();

            if (page.getPageInfoImgName() != null)
                pageNodeInfoBO.setFile("files/images/" + page.getPageInfoImgName());
            pageNodeInfoBO.setImgSrc(page.getPageInfoImgRealUrl());
            if (page.getPageInfoMp3Name() != null) {
                pageNodeInfoBO.setMedia("files/media/" + page.getPageInfoMp3Name());
                pageNodeInfoBO.setMediaSrc(page.getPageInfoMp3LoadUrl());
            }
            pageNodeInfoBO.setId(page.getPageInfoId());
            pageNodeInfoBO.setSequence(page.getSequence());
            pageNodeInfoBO.setWidth(page.getPageInfoImgWidth());
            pageNodeInfoBO.setHeight(page.getPageInfoImgHeight());


            List<SemtenceInfoBO> semtenceInfoList = page.getSemtenceInfoList();
            List<ItemNodeInfoBO> itemNodeInfoBOS = new ArrayList<>();
            for (SemtenceInfoBO semtenceInfo : semtenceInfoList) {
                ItemNodeInfoBO itemNodeInfoBO = new ItemNodeInfoBO();
                itemNodeInfoBO.setId(semtenceInfo.getSemtenceId());
                itemNodeInfoBO.setSequence(semtenceInfo.getSequence());
                if (semtenceInfo.getSemtenceInfoMp3Name() != null) {
                    itemNodeInfoBO.setMedia("files/medias/" + semtenceInfo.getSemtenceInfoMp3Name());
                    itemNodeInfoBO.setMediaSrc(semtenceInfo.getSemtenceInfoMp3LoadUrl());
                }
                ContentBO contentBO = new ContentBO();
                String value = StringEscapeUtils.unescapeHtml3(semtenceInfo.getSemtenceInfoText());
                contentBO.setValue(value);
                itemNodeInfoBO.setContentBO(contentBO);
                List<EtmRegionBO> regionList = semtenceInfo.getSemtenceInfoCoordinateList();
                List<RegionBO> regionBOS = new ArrayList<>();
                for (EtmRegionBO etmRegionBO : regionList) {
                    RegionBO regionBO = new RegionBO();
                    regionBO.setId(etmRegionBO.getRegionId());
                    regionBO.setX(etmRegionBO.getX());
                    regionBO.setY(etmRegionBO.getY());
                    regionBO.setHeight(etmRegionBO.getHeight());
                    regionBO.setWidth(etmRegionBO.getWidth());
                    regionBO.setSemtenceInfoDiv(etmRegionBO.getSemtenceInfoDiv());
                    regionBO.setSemtenceYtop(etmRegionBO.getSemtenceYtop());
                    regionBOS.add(regionBO);
                }
                itemNodeInfoBO.setRegionBOS(regionBOS);

                itemNodeInfoBOS.add(itemNodeInfoBO);

            }

            pageNodeInfoBO.setItemNodeInfoBOS(itemNodeInfoBOS);
            pageNodeInfoBOS.add(pageNodeInfoBO);
        }

        etmBeanDefinition.setNodes(pageNodeInfoBOS);
        return etmBeanDefinition;
    }

    public static ETMInfoBO convertETMBO2ETMBO(EnableETMBeanDefinition etmBean) {

        ETMInfoBO etmInfoBO = new ETMInfoBO();

        etmInfoBO.setEtmBookId(etmBean.getBookId());
        etmInfoBO.setTextBookName(etmBean.getTextBookName());
        etmInfoBO.setIsbn(etmBean.getIsbn());

        String[] cover = etmBean.getCover().split("/");
        etmInfoBO.setCoverImgName(cover[cover.length - 1]);
        etmInfoBO.setCoverUrl(etmBean.getCoverUrl());

        etmInfoBO.setStage(BeanUtils.convert(etmBean.getStage(), EtmCodeNameMapBO.class));
        etmInfoBO.setGrade(BeanUtils.convert(etmBean.getGrade(), EtmCodeNameMapBO.class));
        etmInfoBO.setSubject(BeanUtils.convert(etmBean.getSubject(), EtmCodeNameMapBO.class));
        etmInfoBO.setTextBookVersion(BeanUtils.convert(etmBean.getTextBookVersion(), EtmCodeNameMapBO.class));
        etmInfoBO.setTerm(BeanUtils.convert(etmBean.getTerm(), EtmCodeNameMapBO.class));
        etmInfoBO.setUser(BeanUtils.convert(etmBean.getUser(), EtmIdNameMapBO.class));

        etmInfoBO.setCreatTime(etmBean.getCreateTime());
        etmInfoBO.setContentId(etmBean.getContentId());
        etmInfoBO.setCreator(etmBean.getUser().getId());

        List<PageBO> pageList = new ArrayList<>();

        List<PageNodeInfoBO> nodes = etmBean.getNodes();
        for (PageNodeInfoBO pageNode : nodes) {

            PageBO pageBO = new PageBO();
            pageBO.setPageInfoId(pageNode.getId());
            pageBO.setSequence(pageNode.getSequence());
            pageBO.setEtmBookId(etmBean.getBookId());
            if (pageNode.getFile() != null) {
                String[] file = pageNode.getFile().split("/");
                pageBO.setPageInfoImgName(file[file.length - 1]);
                pageBO.setPageInfoImgRealUrl(pageNode.getImgSrc());
            }

            if (pageNode.getMedia() != null) {
                String[] media = pageNode.getMedia().split("/");
                pageBO.setPageInfoMp3Name(media[media.length - 1]);
                pageBO.setPageInfoMp3LoadUrl(pageNode.getMediaSrc());
            }

            pageBO.setPageInfoImgWidth(pageNode.getWidth());
            pageBO.setPageInfoImgHeight(pageNode.getHeight());

            List<SemtenceInfoBO> semtenceInfoVOList = new ArrayList<>();

            if (pageNode.getItemNodeInfoBOS() != null)
                for (ItemNodeInfoBO itemNode : pageNode.getItemNodeInfoBOS()) {

                    SemtenceInfoBO semtenceInfoBO = new SemtenceInfoBO();
                    semtenceInfoBO.setSemtenceId(itemNode.getId());
                    semtenceInfoBO.setSequence(itemNode.getSequence());
                    semtenceInfoBO.setPageInfoId(pageNode.getId());
                    if (itemNode.getMedia() != null) {
                        String[] medias = itemNode.getMedia().split("/");
                        semtenceInfoBO.setSemtenceInfoMp3Name(medias[medias.length - 1]);
                        semtenceInfoBO.setSemtenceInfoMp3LoadUrl(itemNode.getMediaSrc());
                    }

                    if (itemNode.getContentBO() != null) {
                        semtenceInfoBO.setSemtenceInfoText(itemNode.getContentBO().getValue());
                    }

                    semtenceInfoBO.setSemtenceInfoCoordinateList(BeanUtils.convert(itemNode.getRegionBOS(), EtmRegionBO.class));

                    semtenceInfoVOList.add(semtenceInfoBO);
                }
            pageBO.setSemtenceInfoList(semtenceInfoVOList);
            pageList.add(pageBO);

        }
        etmInfoBO.setPageList(pageList);
        return etmInfoBO;
    }


    public static EditBookPageInfoDTO convertBook(ETMPageBO etm) {
        EditBookPageInfoDTO editBookPageInfoDTO = new EditBookPageInfoDTO();

        editBookPageInfoDTO.setBookId(etm.getEtmBookId());
        editBookPageInfoDTO.setName(etm.getTextBookName());
        editBookPageInfoDTO.setIsbn(etm.getIsbn());
        editBookPageInfoDTO.setType(etm.getType());
        editBookPageInfoDTO.setLayoutType(etm.getLayoutType());

        EtmCodeNameMapBO stage = etm.getStage();
        EtmCodeNameMapBO grade = etm.getGrade();
        EtmCodeNameMapBO subject = etm.getSubject();
        EtmCodeNameMapBO textBookVersion = etm.getTextBookVersion();
        EtmCodeNameMapBO term = etm.getTerm();
        if (Objects.nonNull(stage)) {
            editBookPageInfoDTO.setStageCode(stage.getCode());
            editBookPageInfoDTO.setStageName(stage.getName());
        }
        if (Objects.nonNull(grade)) {
            editBookPageInfoDTO.setGradeCode(grade.getCode());
            editBookPageInfoDTO.setGradeName(grade.getName());
        }
        if (Objects.nonNull(subject)) {
            editBookPageInfoDTO.setSubjectCode(subject.getCode());
            editBookPageInfoDTO.setSubjectName(subject.getName());
        }
        if (Objects.nonNull(textBookVersion)) {
            editBookPageInfoDTO.setMaterialVersion(textBookVersion.getCode());
            editBookPageInfoDTO.setMaterialVersionName(textBookVersion.getName());
        }
        if (Objects.nonNull(term)) {
            editBookPageInfoDTO.setTermId(term.getCode());
            editBookPageInfoDTO.setTermName(term.getName());
        }
        if (Strings.isBlank(etm.getEtmBookId())) {
            //save first page set cover
            String coverImgName = etm.getCoverImgName();
            if (Strings.isNotBlank(coverImgName)) {
                String[] covers = coverImgName.split("\\.");
                FileInfoDTO cover = new FileInfoDTO();
                cover.setFileId(etm.getCoverId());
                cover.setFileExt(covers[covers.length - 1]);
                cover.setFileName(coverImgName);
                cover.setUrl(etm.getCoverUrl());
                editBookPageInfoDTO.setCover(cover);
            }
        }
        editBookPageInfoDTO.setUpdator(etm.getUpdator());
        editBookPageInfoDTO.setPage(convertPage(etm.getPage()));
        return editBookPageInfoDTO;
    }

    public static EditBookInfoDTO convertBookVO(ETMInfoBO etm) {
        EditBookInfoDTO editBookInfoDTO = new EditBookInfoDTO();

        editBookInfoDTO.setBookId(etm.getEtmBookId());
        editBookInfoDTO.setName(etm.getTextBookName());
        editBookInfoDTO.setIsbn(etm.getIsbn());
        editBookInfoDTO.setType(etm.getType());
        editBookInfoDTO.setLayoutType(etm.getLayoutType());

        EtmCodeNameMapBO stage = etm.getStage();
        EtmCodeNameMapBO grade = etm.getGrade();
        EtmCodeNameMapBO subject = etm.getSubject();
        EtmCodeNameMapBO textBookVersion = etm.getTextBookVersion();
        EtmCodeNameMapBO term = etm.getTerm();
        if (Objects.nonNull(stage)) {
            editBookInfoDTO.setStageCode(stage.getCode());
            editBookInfoDTO.setStageName(stage.getName());
        }
        if (Objects.nonNull(grade)) {
            editBookInfoDTO.setGradeCode(grade.getCode());
            editBookInfoDTO.setGradeName(grade.getName());
        }
        if (Objects.nonNull(subject)) {
            editBookInfoDTO.setSubjectCode(subject.getCode());
            editBookInfoDTO.setSubjectName(subject.getName());
        }
        if (Objects.nonNull(textBookVersion)) {
            editBookInfoDTO.setMaterialVersion(textBookVersion.getCode());
            editBookInfoDTO.setMaterialVersionName(textBookVersion.getName());
        }
        if (Objects.nonNull(term)) {
            editBookInfoDTO.setTermId(term.getCode());
            editBookInfoDTO.setTermName(term.getName());
        }
        String coverImgName = etm.getCoverImgName();
        if (Strings.isNotBlank(coverImgName)) {
            String[] covers = coverImgName.split("\\.");
            FileInfoDTO cover = new FileInfoDTO();
            cover.setFileId(etm.getCoverId());
            cover.setFileExt(covers[covers.length - 1]);
            cover.setFileName(coverImgName);
            cover.setUrl(etm.getCoverUrl());
            editBookInfoDTO.setCover(cover);
        }
        editBookInfoDTO.setUpdator(etm.getUpdator());
        List<EditPageInfoDTO> pages = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(etm.getPageList())) {
            for (PageBO pageBO : etm.getPageList()) {
                pages.add(convertPage(pageBO));
            }
        }
        editBookInfoDTO.setPages(pages);
        return editBookInfoDTO;
    }

    public static EditPageInfoDTO convertPage(PageBO pageBO) {
        EditPageInfoDTO editPageInfoDTO = new EditPageInfoDTO();
        if (Objects.nonNull(pageBO)) {
            editPageInfoDTO.setPageId(pageBO.getPageInfoId());
            editPageInfoDTO.setPageNo(Integer.valueOf(pageBO.getSequence()));
            editPageInfoDTO.setPageUrl(pageBO.getPageInfoImgRealUrl());
            editPageInfoDTO.setWidth(Float.valueOf(pageBO.getPageInfoImgWidth()));
            editPageInfoDTO.setHeight(Float.valueOf(pageBO.getPageInfoImgHeight()));
            editPageInfoDTO.setAffixUrl(pageBO.getPageInfoMp3LoadUrl());
            editPageInfoDTO.setQuestions(convertQuestion(pageBO.getSemtenceInfoList(), pageBO));

            String imgName = pageBO.getPageInfoImgName();
            if (Strings.isNotBlank(imgName)) {
                String[] imgs = imgName.split("\\.");
                FileInfoDTO fileInfoDTO = new FileInfoDTO();
                fileInfoDTO.setFileId(pageBO.getPageInfoImgId());
                fileInfoDTO.setUrl(pageBO.getPageInfoImgRealUrl());
                fileInfoDTO.setFileName(imgName);
                fileInfoDTO.setFileExt(imgs[imgs.length - 1]);
                editPageInfoDTO.setPageFile(fileInfoDTO);
            }
        }
        return editPageInfoDTO;
    }

    public static List<AddPageQuestionInfoDTO> convertQuestion(List<SemtenceInfoBO> semtenceInfoList, PageBO pageBO) {
        List<AddPageQuestionInfoDTO> questions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(semtenceInfoList)){
            for (SemtenceInfoBO semtenceInfoBO : semtenceInfoList) {

                AddPageQuestionInfoDTO addPageQuestionInfoDTO = new AddPageQuestionInfoDTO();
                addPageQuestionInfoDTO.setPageQuestionId(semtenceInfoBO.getSemtenceId());
                addPageQuestionInfoDTO.setText(semtenceInfoBO.getSemtenceInfoText());
                addPageQuestionInfoDTO.setAffixUrl(semtenceInfoBO.getSemtenceInfoMp3LoadUrl());
                addPageQuestionInfoDTO.setPageQuestionOrder(Integer.valueOf(semtenceInfoBO.getSequence()));
                addPageQuestionInfoDTO.setPageId(pageBO.getPageInfoId());

                addPageQuestionInfoDTO.setPoints(new Float(0));
                addPageQuestionInfoDTO.setQuestionOrder(Integer.valueOf(semtenceInfoBO.getSequence()));
                // addPageQuestionInfoDTO.setParentId();
                //addPageQuestionInfoDTO.setLeftAxis(Float.valueOf());
                //addPageQuestionInfoDTO.setTopAxis(Float.valueOf());
                //addPageQuestionInfoDTO.setHeight(Float.valueOf());
                //addPageQuestionInfoDTO.setWidth(Float.valueOf());
                //addPageQuestionInfoDTO.setTypeId();
                //addPageQuestionInfoDTO.setTypeName()

                List<EtmRegionBO> etmRegionBOList = semtenceInfoBO.getSemtenceInfoCoordinateList();
                if (CollectionUtils.isNotEmpty(etmRegionBOList)) {
                    addPageQuestionInfoDTO.setAnswers(convertAnswer(etmRegionBOList, semtenceInfoBO));

                }

                questions.add(addPageQuestionInfoDTO);
            }
        }
        return questions;
    }

    public static List<AddPageQuestionAnswerInfoDTO> convertAnswer(List<EtmRegionBO> etmRegionBOList, SemtenceInfoBO semtenceInfoBO) {
        List<AddPageQuestionAnswerInfoDTO> answers = new ArrayList<>();
        for (EtmRegionBO etmRegionBO : etmRegionBOList) {
            AddPageQuestionAnswerInfoDTO answer = new AddPageQuestionAnswerInfoDTO();
            answer.setLeftAxis(Float.valueOf(etmRegionBO.getX()));
            answer.setTopAxis(Float.valueOf(etmRegionBO.getY()));
            answer.setWidth(Float.valueOf(etmRegionBO.getWidth()));
            answer.setHeight(Float.valueOf(etmRegionBO.getHeight()));
            answer.setPageQuestionId(semtenceInfoBO.getSemtenceId());
            answer.setSequence(Integer.valueOf(etmRegionBO.getSemtenceYtop()));
            answer.setText(etmRegionBO.getSemtenceInfoDiv());
            //answer.setAffixUrl();
            //answer.setParentId();
            answers.add(answer);
        }
        return answers;
    }


    public static ETMInfoBO convertEtm(QueryBookInfoResultDTO bookInfoDTO, ETMInfoBO etmInfoBO) {
        if (Objects.isNull(bookInfoDTO))
            return etmInfoBO;
        etmInfoBO.setEtmBookId(bookInfoDTO.getBookId());
        etmInfoBO.setTextBookName(bookInfoDTO.getName());
        etmInfoBO.setIsbn(bookInfoDTO.getIsbn());
        etmInfoBO.setContentId(bookInfoDTO.getContentId());
        EtmCodeNameMapBO stage = new EtmCodeNameMapBO(bookInfoDTO.getStageCode(), bookInfoDTO.getStageName());
        EtmCodeNameMapBO grade = new EtmCodeNameMapBO(bookInfoDTO.getGradeCode(), bookInfoDTO.getGradeName());
        EtmCodeNameMapBO subject = new EtmCodeNameMapBO(bookInfoDTO.getSubjectCode(), bookInfoDTO.getSubjectName());
        EtmCodeNameMapBO textBookVersion = new EtmCodeNameMapBO(bookInfoDTO.getMaterialVersion(), bookInfoDTO.getMaterialVersionName());
        EtmCodeNameMapBO term = new EtmCodeNameMapBO(bookInfoDTO.getTermId(), bookInfoDTO.getTermName());
        EtmIdNameMapBO user = new EtmIdNameMapBO(bookInfoDTO.getCreator(), null);
        etmInfoBO.setStage(stage);
        etmInfoBO.setGrade(grade);
        etmInfoBO.setSubject(subject);
        etmInfoBO.setTextBookVersion(textBookVersion);
        etmInfoBO.setTerm(term);
        etmInfoBO.setUser(user);
        FileInfoDTO cover = bookInfoDTO.getCover();
        if (Objects.nonNull(cover)) {
            etmInfoBO.setCoverId(cover.getFileId());
            etmInfoBO.setCoverImgName(cover.getFileName());
            etmInfoBO.setCoverUrl(cover.getUrl());
        }
        etmInfoBO.setCreator(bookInfoDTO.getCreator());
        etmInfoBO.setCreatTime(bookInfoDTO.getCreateTime());
        etmInfoBO.setUpdator(bookInfoDTO.getUpdator());
        etmInfoBO.setUpdateTime(bookInfoDTO.getUpdateTime());
        etmInfoBO.setPageList(convertPageList(bookInfoDTO.getPages()));

        return etmInfoBO;
    }

    public static List<PageBO> convertPageList(List<QueryPageInfoResultDTO> pages) {
        List<PageBO> pageList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pages)) {
            for (QueryPageInfoResultDTO page : pages) {
                PageBO pageBO = new PageBO();
                pageBO.setPageInfoId(page.getPageId());
                pageBO.setSequence(page.getPageNo());
                pageBO.setContentId(page.getContentId());
                pageBO.setEtmBookId(page.getBookId());
                if (Objects.nonNull(page.getWidth())) {
                    pageBO.setPageInfoImgWidth(String.valueOf(page.getWidth()));

                }
                if (Objects.nonNull(page.getHeight())) {
                    pageBO.setPageInfoImgHeight(String.valueOf(page.getHeight()));

                }
                pageBO.setPageInfoMp3LoadUrl(page.getAffixUrl());
                pageBO.setCreator(page.getCreator());
                pageBO.setCreatTime(page.getCreateTime());
                pageBO.setUpdator(page.getUpdator());
                pageBO.setUpdateTime(page.getUpdateTime());
                FileInfoDTO pageFile = page.getPageFile();
                pageBO.setPageInfoImgName(pageFile.getFileName());
                pageBO.setPageInfoImgId(pageFile.getFileId());
                pageBO.setPageInfoImgRealUrl(pageFile.getUrl());

                pageBO.setSemtenceInfoList(converSemtenceList(page.getQuestions()));

                pageList.add(pageBO);
            }

        }

        return pageList;
    }

    public static List<SemtenceInfoBO> converSemtenceList(List<QueryPageQuestionInfoDTO> questions) {
        List<SemtenceInfoBO> semtenceList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(questions)) {
            for (QueryPageQuestionInfoDTO question : questions) {
                SemtenceInfoBO semtenceInfoBO = new SemtenceInfoBO();
                semtenceInfoBO.setSemtenceId(question.getPageQuestionId());
                semtenceInfoBO.setSemtenceInfoText(question.getText());
                semtenceInfoBO.setSemtenceInfoMp3LoadUrl(question.getAffixUrl());
                semtenceInfoBO.setSequence(question.getPageQuestionOrder().toString());
                semtenceInfoBO.setPageInfoId(question.getPageId());
                semtenceInfoBO.setCreator(question.getCreator());
                semtenceInfoBO.setCreatTime(question.getCreateTime());
                semtenceInfoBO.setUpdator(question.getUpdator());
                semtenceInfoBO.setUpdateTime(question.getUpdateTime());

                semtenceInfoBO.setSemtenceInfoCoordinateList(convertRegionList(question.getAnswers()));

                semtenceList.add(semtenceInfoBO);
            }
        }

        return semtenceList;
    }

    public static List<EtmRegionBO> convertRegionList(List<QueryPageQuestionAnswerInfoDTO> answers) {
        List<EtmRegionBO> regionList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(answers)) {
            for (QueryPageQuestionAnswerInfoDTO answer : answers) {
                EtmRegionBO etmRegionBO = new EtmRegionBO();
                etmRegionBO.setRegionId(answer.getPageQuestionAnswerId());
                if (Objects.nonNull(answer.getLeftAxis())) {
                    etmRegionBO.setX(String.valueOf(answer.getLeftAxis()));
                }
                if (Objects.nonNull(answer.getTopAxis())) {
                    etmRegionBO.setY(String.valueOf(answer.getTopAxis()));
                }
                if (Objects.nonNull(answer.getWidth())) {
                    etmRegionBO.setWidth(String.valueOf(answer.getWidth()));
                }
                if (Objects.nonNull(answer.getHeight())) {
                    etmRegionBO.setHeight(String.valueOf(answer.getHeight()));
                }
                if (Objects.nonNull(answer.getSequence())) {
                    etmRegionBO.setSemtenceYtop(String.valueOf(answer.getSequence()));
                }
                if (Objects.nonNull(answer.getText())) {
                    etmRegionBO.setSemtenceInfoDiv(answer.getText());
                }
                etmRegionBO.setSemtenceId(answer.getPageQuestionId());

                regionList.add(etmRegionBO);
            }
        }

        return regionList;
    }

    public static List<AddBusinessFileInfoDTO> covertBusinessList(PageBO page) {
        List<AddBusinessFileInfoDTO> files = new ArrayList<>();
        if (Objects.isNull(page)) return files;
        if (Strings.isNotBlank(page.getPageInfoId()) && Strings.isNotBlank(page.getPageInfoMp3Id())){
            AddBusinessFileInfoDTO pageBusinessFile = new AddBusinessFileInfoDTO();
            pageBusinessFile.setBusinessId(page.getPageInfoId());
            pageBusinessFile.setFileId(page.getPageInfoMp3Id());
            String pageInfoMp3Name = page.getPageInfoMp3Name();
            pageBusinessFile.setFileName(pageInfoMp3Name);
            if (Strings.isNotBlank(pageInfoMp3Name)) {
                String[] mp3 = pageInfoMp3Name.split("\\.");
                pageBusinessFile.setFileExt(mp3[mp3.length - 1]);
            }
            pageBusinessFile.setUrl(page.getPageInfoMp3LoadUrl());
            files.add(pageBusinessFile);
        }
        List<SemtenceInfoBO> semtenceInfoList = page.getSemtenceInfoList();
        if (CollectionUtils.isNotEmpty(semtenceInfoList)) {
            for (SemtenceInfoBO semtenceInfoBO : semtenceInfoList) {
                if (Strings.isNotBlank(semtenceInfoBO.getSemtenceId()) && Strings.isNotBlank(semtenceInfoBO.getSemtenceInfoMp3Id())){
                    AddBusinessFileInfoDTO semtenceBusinessFile = new AddBusinessFileInfoDTO();
                    semtenceBusinessFile.setBusinessId(semtenceInfoBO.getSemtenceId());
                    semtenceBusinessFile.setFileId(semtenceInfoBO.getSemtenceInfoMp3Id());
                    String semtenceInfoMp3Name = semtenceInfoBO.getSemtenceInfoMp3Name();
                    semtenceBusinessFile.setFileName(semtenceInfoMp3Name);
                    if (Strings.isNotBlank(semtenceInfoMp3Name)) {
                        String[] mp3 = semtenceInfoMp3Name.split("\\.");
                        semtenceBusinessFile.setFileExt(mp3[mp3.length - 1]);
                    }
                    semtenceBusinessFile.setUrl(semtenceInfoBO.getSemtenceInfoMp3LoadUrl());
                    files.add(semtenceBusinessFile);
                }
            }
        }
        return files;
    }


    public static void generatePrimaryKey(PageBO pageBO, QueryPageInfoResultDTO pageInfo) {

        pageBO.setPageInfoId(pageInfo.getPageId());
        List<SemtenceInfoBO> semtenceInfoList = pageBO.getSemtenceInfoList();
        List<QueryPageQuestionInfoDTO> questions = pageInfo.getQuestions();
        if (CollectionUtils.isNotEmpty(semtenceInfoList)) {
            for (int j = 0; j < semtenceInfoList.size(); j++) {
                SemtenceInfoBO semtenceInfoBO = semtenceInfoList.get(j);
                QueryPageQuestionInfoDTO question = questions.get(j);
                semtenceInfoBO.setSemtenceId(question.getPageQuestionId());
                semtenceInfoBO.setPageInfoId(pageInfo.getPageId());
                List<EtmRegionBO> regionBOList = semtenceInfoBO.getSemtenceInfoCoordinateList();
                List<QueryPageQuestionAnswerInfoDTO> answers = question.getAnswers();
                if (CollectionUtils.isNotEmpty(regionBOList)) {
                    for (int k = 0; k < regionBOList.size(); k++) {
                        EtmRegionBO etmRegionBO = regionBOList.get(k);
                        QueryPageQuestionAnswerInfoDTO answer = answers.get(k);
                        etmRegionBO.setRegionId(answer.getPageQuestionAnswerId());
                        etmRegionBO.setSemtenceId(question.getPageQuestionId());
                    }
                }
            }
        }
    }
}
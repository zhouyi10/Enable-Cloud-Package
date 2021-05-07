package com.enableets.edu.pakage.manager.book.service;

import com.enableets.edu.pakage.core.utils.BeanUtils;
import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.book.bo.*;
import com.enableets.edu.sdk.pakage.book.dto.AddBookInfoDTO;
import com.enableets.edu.sdk.pakage.book.service.IBookPackageService;
import com.enableets.edu.sdk.teachingassistant.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageQuestionAnswerInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageQuestionInfoDTO;
import com.enableets.edu.sdk.teachingassistant.service.IBookInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BookInfoService {

    @Autowired
    private IBookInfoService bookInfoServiceSDK;

    @Autowired
    private IBookPackageService bookPackageServiceSDK;

    public BookInfoBO addBookInfo(String bookId) {
       return BeanUtils.convert(bookPackageServiceSDK.addBookInfo(BeanUtils.convert(convertBook(bookId), AddBookInfoDTO.class)),BookInfoBO.class);
    }

    public BookInfoBO parseBookInfo(String contentId) {
        return BeanUtils.convert(bookPackageServiceSDK.parseBookInfo(contentId), BookInfoBO.class);
    }

    private BookInfoBO convertBook(String bookId) {
        BookInfoBO bookInfoBO = new BookInfoBO();

        QueryBookInfoResultDTO queryBookInfo = bookInfoServiceSDK.get(bookId);

        bookInfoBO.setBookId(bookId);
        bookInfoBO.setBookName(queryBookInfo.getName());
        bookInfoBO.setContentId(queryBookInfo.getContentId());
        bookInfoBO.setIsbn(queryBookInfo.getIsbn());
        bookInfoBO.setStage(new CodeNameMapBO(queryBookInfo.getStageCode(), queryBookInfo.getStageName()));
        bookInfoBO.setGrade(new CodeNameMapBO(queryBookInfo.getGradeCode(), queryBookInfo.getGradeName()));
        bookInfoBO.setSubject(new CodeNameMapBO(queryBookInfo.getSubjectCode(), queryBookInfo.getStageName()));
        bookInfoBO.setTextBookVersion(new CodeNameMapBO(queryBookInfo.getMaterialVersion(), queryBookInfo.getMaterialVersionName()));
        bookInfoBO.setTerm(new CodeNameMapBO(queryBookInfo.getTermId(), queryBookInfo.getTermName()));
        bookInfoBO.setCover(BeanUtils.convert(queryBookInfo.getCover(), FileInfoBO.class));
        bookInfoBO.setCreator(queryBookInfo.getCreator());
        bookInfoBO.setCreatTime(queryBookInfo.getCreateTime());
        bookInfoBO.setUpdator(queryBookInfo.getUpdator());
        bookInfoBO.setUpdateTime(queryBookInfo.getUpdateTime());

        bookInfoBO.setPageList(convert2Pages(queryBookInfo.getPages()));

        return bookInfoBO;
    }

    private List<PageBO> convert2Pages(List<QueryPageInfoResultDTO> queryPages) {
        List<PageBO> pages = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(queryPages)) {
            for (QueryPageInfoResultDTO page : queryPages) {
                PageBO pageBO = new PageBO();

                pageBO.setPageId(page.getPageId());
                pageBO.setSequence(page.getPageNo());
                pageBO.setName(page.getPageFile().getFileName());
                pageBO.setContentId(page.getContentId());
                pageBO.setBookId(page.getBookId());
                pageBO.setWidth(Objects.isNull(page.getWidth()) ? null : page.getWidth().toString());
                pageBO.setHeight(Objects.isNull(page.getHeight()) ? null : page.getHeight().toString());
                pageBO.setAffixUrl(page.getAffixUrl());
                pageBO.setCreator(page.getCreator());
                pageBO.setCreatTime(page.getCreateTime());
                pageBO.setUpdator(page.getUpdator());
                pageBO.setUpdateTime(page.getUpdateTime());
                pageBO.setPageFile(BeanUtils.convert(page.getPageFile(), FileInfoBO.class));
                pageBO.setSemtenceInfoList(convert2Semtentces(page.getQuestions()));
                pages.add(pageBO);
            }
        }
        return pages;
    }

    private List<SemtenceInfoBO> convert2Semtentces(List<QueryPageQuestionInfoDTO> pageQuestions) {
        List<SemtenceInfoBO> semtences = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(pageQuestions)) {
            for (QueryPageQuestionInfoDTO pageQuestion : pageQuestions) {
                SemtenceInfoBO semtence = new SemtenceInfoBO();
                semtence.setQuestionId(pageQuestion.getPageQuestionId());
                semtence.setParentId(pageQuestion.getParentId());
                semtence.setText(pageQuestion.getText());

                ContentRegionBO contentRegionBO = new ContentRegionBO();
                contentRegionBO.setX(Objects.isNull(pageQuestion.getLeftAxis()) ? null : pageQuestion.getLeftAxis().toString());
                contentRegionBO.setY(Objects.isNull(pageQuestion.getTopAxis()) ? null : pageQuestion.getTopAxis().toString());
                contentRegionBO.setWidth(Objects.isNull(pageQuestion.getWidth()) ? null : pageQuestion.getWidth().toString());
                contentRegionBO.setHeight(Objects.isNull(pageQuestion.getHeight()) ? null : pageQuestion.getHeight().toString());
                semtence.setContentRegionBO(contentRegionBO);

                semtence.setAffixUrl(pageQuestion.getAffixUrl());
                semtence.setSequenceId(Objects.isNull(pageQuestion.getQuestionOrder()) ? null : pageQuestion.getQuestionOrder().toString());
                semtence.setPageId(pageQuestion.getPageId());
                semtence.setCreator(pageQuestion.getCreator());
                semtence.setCreatTime(pageQuestion.getCreateTime());
                semtence.setUpdator(pageQuestion.getUpdator());
                semtence.setUpdateTime(pageQuestion.getUpdateTime());
                semtence.setBookRegionInfoList(convertRegions(pageQuestion.getAnswers()));

                semtences.add(semtence);
            }
        }

        return semtences;
    }

    private static List<RegionBO> convertRegions(List<QueryPageQuestionAnswerInfoDTO> answers) {
        List<RegionBO> regions = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(answers)) {
            for (QueryPageQuestionAnswerInfoDTO answer : answers) {
                RegionBO regionBO = new RegionBO();
                regionBO.setRegionId(answer.getPageQuestionAnswerId());
                regionBO.setX(Objects.isNull(answer.getLeftAxis()) ? null : answer.getLeftAxis().toString());
                regionBO.setY(Objects.isNull(answer.getTopAxis()) ? null : answer.getTopAxis().toString());
                regionBO.setWidth(Objects.isNull(answer.getWidth()) ? null : answer.getWidth().toString());
                regionBO.setHeight(Objects.isNull(answer.getHeight()) ? null : answer.getHeight().toString());
                regionBO.setText(answer.getText());
                regionBO.setSequence(answer.getSequence().toString());
                regionBO.setAffixUrl(answer.getAffixUrl());
                regionBO.setQuestionId(answer.getPageQuestionId());
                regionBO.setParentId(answer.getParentId());
                regionBO.setCreator(answer.getCreator());
                regionBO.setCreatTime(answer.getCreateTime());
                regionBO.setUpdator(answer.getUpdator());
                regionBO.setUpdateTime(answer.getUpdateTime());
                regions.add(regionBO);
            }
        }
        return regions;
    }
}

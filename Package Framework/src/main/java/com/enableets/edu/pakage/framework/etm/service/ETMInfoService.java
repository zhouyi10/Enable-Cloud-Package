package com.enableets.edu.pakage.framework.etm.service;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.etm.action.ETMPackageLifecycle;
import com.enableets.edu.pakage.etm.bean.ETMPackageWrapper;
import com.enableets.edu.pakage.etm.bean.EnableETMBeanDefinition;
import com.enableets.edu.pakage.framework.etm.bo.ETMInfoBO;
import com.enableets.edu.pakage.framework.etm.bo.ETMPageBO;
import com.enableets.edu.pakage.framework.etm.bo.EtmIdNameMapBO;
import com.enableets.edu.pakage.framework.etm.bo.PageBO;
import com.enableets.edu.pakage.framework.etm.bo.SemtenceInfoBO;
import com.enableets.edu.pakage.framework.etm.core.ETMConstants;
import com.enableets.edu.pakage.framework.etm.core.EtmTransfor;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.teachingassistant.dto.AddBusinessFileInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryBusinessFileInfoDTO;
import com.enableets.edu.sdk.teachingassistant.dto.QueryPageInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.service.IBookInfoService;
import com.enableets.edu.sdk.teachingassistant.service.IBusinessFileInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class ETMInfoService {

    @Autowired
    private Configuration configuration;

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private IBookInfoService bookInfoServiceSDK;

    @Autowired
    private IBusinessFileInfoService businessFileInfoServiceSDK;

    /*@Autowired
    private ETMInfoDAO etmInfoDAO;*/


    public ETMInfoBO add(ETMInfoBO etmInfoBO) {
        if (CollectionUtils.isEmpty(etmInfoBO.getPageList())) {
            throw new MicroServiceException("70-", "ETM Missing structure(Node)");
        }
        //ETM Data persistence
        QueryBookInfoResultDTO bookInfo = bookInfoServiceSDK.completeBook(etmInfoBO.getEtmBookId());
        ContentInfoDTO contentInfo = new ContentInfoDTO();
        if (Objects.nonNull(bookInfo)) {
            contentInfo = contentInfoServiceSDK.get(bookInfo.getContentId()).getData();
        }
        //Set ETM basic information through Content
        this.setBasicInfo(etmInfoBO, contentInfo);
        //Generate ETM File & Add it to ETM Content
        this.generateETMFile(etmInfoBO, contentInfo);
        return etmInfoBO;
    }

    public ETMInfoBO query(String bookId) {
        ETMInfoBO etmInfoBO = new ETMInfoBO();
        QueryBookInfoResultDTO bookInfoResultDTO = bookInfoServiceSDK.get(bookId);
        if (Objects.isNull(bookInfoResultDTO))
            return etmInfoBO;
        EtmTransfor.convertEtm(bookInfoResultDTO,etmInfoBO);
        // set affix
        return this.setAffix(etmInfoBO);
    }

    public ETMInfoBO parse(Long contentId) {

        ETMInfoBO etmInfoBO = new ETMInfoBO();
        ContentInfoDTO content = contentInfoServiceSDK.get(contentId).getData();
        if (Objects.isNull(content)) {
            return etmInfoBO;
        }
        List<ContentFileInfoDTO> contentFileList = content.getFileList();
        if (CollectionUtils.isNotEmpty(contentFileList)) {
            for (ContentFileInfoDTO contentFileInfoDTO : contentFileList) {
                if (contentFileInfoDTO.getFileOrder() == 0) {
                    String fileId = contentFileInfoDTO.getFileId();
                    PackageFileInfo packageFileInfo = new PackageFileInfo();
                    packageFileInfo.setFileId(fileId);
                    ETMPackageWrapper etmPackageWrapper2 = new ETMPackageWrapper(configuration, packageFileInfo);
                    IPackageLifecycle packageLifecycle = new ETMPackageLifecycle(etmPackageWrapper2);
                    packageLifecycle.parse();
                    EnableETMBeanDefinition enableETMBeanDefinition = etmPackageWrapper2.getEnableETMBeanDefinition();
                    enableETMBeanDefinition.setContentId(contentId);
                    enableETMBeanDefinition.getUser().setId(content.getCreator());
                    etmInfoBO = EtmTransfor.convertETMBO2ETMBO(enableETMBeanDefinition);
                }
            }
        }
        return etmInfoBO;
    }


    public ETMInfoBO edit(ETMInfoBO etmInfoBO) {
        //set Type
        etmInfoBO.setType(ETMConstants.ETM_BOOK_TYPE_CODE);
        etmInfoBO.setLayoutType(ETMConstants.ETM_LAYOUT_TYPE_CODE);
        if (Objects.nonNull(etmInfoBO.getUser())) {
            etmInfoBO.setUpdator(etmInfoBO.getUser().getId());
        }
        QueryBookInfoResultDTO bookInfoDTO = bookInfoServiceSDK.edit(EtmTransfor.convertBookVO(etmInfoBO));
        // add affix to business
        if (CollectionUtils.isNotEmpty(bookInfoDTO.getPages())) {
            List<QueryPageInfoResultDTO> bookPages = bookInfoDTO.getPages();
            List<AddBusinessFileInfoDTO> files = new ArrayList<>();
            for (int i = 0; i < bookPages.size(); i++) {
                List<PageBO> etmPages = etmInfoBO.getPageList();
                EtmTransfor.generatePrimaryKey(etmPages.get(i), bookPages.get(i));
                files.addAll(EtmTransfor.covertBusinessList(etmPages.get(i)));
            }
            if (CollectionUtils.isNotEmpty(files)) {
                businessFileInfoServiceSDK.insertList(files);
            }
        }
        return setAffix(EtmTransfor.convertEtm(bookInfoDTO, etmInfoBO));
    }

    public ETMInfoBO editOnePage(ETMPageBO etmPageBO) {
        //set Type
        etmPageBO.setType(ETMConstants.ETM_BOOK_TYPE_CODE);
        etmPageBO.setLayoutType(ETMConstants.ETM_LAYOUT_TYPE_CODE);
        if (Objects.nonNull(etmPageBO.getUser())) {
            etmPageBO.setUpdator(etmPageBO.getUser().getId());
        }
        // add one page
        QueryBookInfoResultDTO bookInfoDTO = bookInfoServiceSDK.editPage(EtmTransfor.convertBook(etmPageBO));
        // add affix to business
        if (Objects.nonNull(bookInfoDTO) && CollectionUtils.isNotEmpty(bookInfoDTO.getPages())) {
            EtmTransfor.generatePrimaryKey(etmPageBO.getPage(), bookInfoDTO.getPages().get(0));
            List<AddBusinessFileInfoDTO> files = EtmTransfor.covertBusinessList(etmPageBO.getPage());
            if (CollectionUtils.isNotEmpty(files)) {
                businessFileInfoServiceSDK.insertList(files);
            }
        }
        return EtmTransfor.convertEtm(bookInfoDTO, new ETMInfoBO());
    }

    public void deleteOnePage(String pageId) {
        bookInfoServiceSDK.deletePage(pageId);
    }

    private void setBasicInfo(ETMInfoBO etm, ContentInfoDTO content) {
        // set contentId
        etm.setContentId(content.getContentId());
        //user
        etm.setUser(new EtmIdNameMapBO(content.getCreator(), content.getCreatorName()));
        etm.setCreatTime(new Date());
        etm.setUpdateTime(new Date());
    }

    private ETMInfoBO setAffix(ETMInfoBO etmInfoBO) {
        List<PageBO> pageList = etmInfoBO.getPageList();
        if (CollectionUtils.isNotEmpty(pageList)) {
            QueryBusinessFileInfoDTO businessFileInfoDTO = new QueryBusinessFileInfoDTO();
            for (PageBO pageBO : pageList) {
                businessFileInfoDTO.setBusinessId(pageBO.getPageInfoId());
                businessFileInfoDTO.setFileExt("mp3");
                QueryBusinessFileInfoDTO pageFileInfoDTO = businessFileInfoServiceSDK.selectOne(businessFileInfoDTO);
                if (Objects.nonNull(pageFileInfoDTO)) {
                    pageBO.setPageInfoMp3Id(pageFileInfoDTO.getFileId());
                    pageBO.setPageInfoMp3Name(pageFileInfoDTO.getFileName());
                    pageBO.setPageInfoMp3LoadUrl(pageFileInfoDTO.getUrl());
                }
                List<SemtenceInfoBO> semtenceInfoList = pageBO.getSemtenceInfoList();
                if (CollectionUtils.isNotEmpty(semtenceInfoList)) {
                    for (SemtenceInfoBO semtenceInfoBO : semtenceInfoList) {
                        businessFileInfoDTO.setBusinessId(semtenceInfoBO.getSemtenceId());
                        businessFileInfoDTO.setFileExt("mp3");
                        QueryBusinessFileInfoDTO semtenceFileInfoDTO = businessFileInfoServiceSDK.selectOne(businessFileInfoDTO);
                        if (Objects.nonNull(semtenceFileInfoDTO)) {
                            semtenceInfoBO.setSemtenceInfoMp3Id(semtenceFileInfoDTO.getFileId());
                            semtenceInfoBO.setSemtenceInfoMp3Name(semtenceFileInfoDTO.getFileName());
                            semtenceInfoBO.setSemtenceInfoMp3LoadUrl(semtenceFileInfoDTO.getUrl());
                        }
                    }
                }
            }
            etmInfoBO.setPageList(pageList);
        }
        return etmInfoBO;
    }

    private void generateETMFile(ETMInfoBO etm, ContentInfoDTO contentInfo) {
        // 1. context
        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        // 2. ETM model
        EnableETMBeanDefinition etmBeanDefinition = EtmTransfor.convertETMVO2ETMBO(etm);
        // 3. ETM wrapper
        ETMPackageWrapper etmPackageWrapper = new ETMPackageWrapper(configuration, etmBeanDefinition);
        // 4.ETM Lifecycle
        ETMPackageLifecycle packageLifecycle = new ETMPackageLifecycle(etmPackageWrapper);
        // 5.build .etm
        packageLifecycle.build();
        // 6. Get .etm document
        PackageFileInfo packageFileInfo = etmPackageWrapper.getPackageFileInfo();
        // ------------------------ ----------------------------------
        if (Objects.nonNull(packageFileInfo)) {

            ContentFileInfoDTO file = new ContentFileInfoDTO();
            file.setFileId(packageFileInfo.getFileId());
            file.setFileName(packageFileInfo.getName());
            file.setFileExt(packageFileInfo.getExt());
            file.setUrl(packageFileInfo.getDownloadUrl());
            file.setMd5(packageFileInfo.getMd5());
            file.setSize(packageFileInfo.getSize());
            file.setSizeDisplay(packageFileInfo.getSizeDisplay());
            file.setProviderCode(contentInfo.getProviderCode());
            if (etm.getContentId() != null)
                file.setContentId(etm.getContentId().toString());

            file.setFileOrder(0);
            contentInfoServiceSDK.addFileToContent(file);
        }
    }

}

package com.enableets.edu.pakage.framework.book.service;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.framework.core.util.SpringBeanUtils;
import com.enableets.edu.framework.core.util.StringUtils;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.book.action.BookPackageLifecycle;
import com.enableets.edu.pakage.book.bean.BookPackageWrapper;
import com.enableets.edu.pakage.book.bean.EnableBookBeanDefinition;
import com.enableets.edu.pakage.book.bo.*;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.framework.book.bo.*;
import com.enableets.edu.sdk.content.dto.ContentFileInfoDTO;
import com.enableets.edu.sdk.content.dto.ContentInfoDTO;
import com.enableets.edu.sdk.content.service.IContentInfoService;
import com.enableets.edu.sdk.teachingassistant.dto.QueryBookInfoResultDTO;
import com.enableets.edu.sdk.teachingassistant.service.IBookInfoService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service("packageBookInfo")
public class BookInfoService {

    @Autowired
    private Configuration configuration;

    @Autowired
    private IContentInfoService contentInfoServiceSDK;

    @Autowired
    private IBookInfoService bookInfoServiceSDK;

    public BookInfoBO add(BookInfoBO bookInfoBO) {
        if (CollectionUtils.isEmpty(bookInfoBO.getPageList())) {
            throw new MicroServiceException("70-", "Book Missing structure(Node)");
        }
        QueryBookInfoResultDTO queryBookInfo = bookInfoServiceSDK.completeBook(String.valueOf(bookInfoBO.getBookId()));
        ContentInfoDTO contentInfoDTO = new ContentInfoDTO();
        if (Objects.nonNull(queryBookInfo)) {
            contentInfoDTO = contentInfoServiceSDK.get(queryBookInfo.getContentId()).getData();
        }
        //Set Book basic information through Content
        this.setBasicInfo(bookInfoBO, contentInfoDTO);
        //Generate Book File & Add it to Book Content
        this.generateBookFile(bookInfoBO);
        return bookInfoBO;
    }

    public BookInfoBO edit(BookInfoBO bookInfoBO) {
        contentInfoServiceSDK.remove(bookInfoBO.getContentId());
        return this.add(bookInfoBO);
    }

    public BookInfoBO parse(String contentId) {

        ContentInfoDTO content = contentInfoServiceSDK.get(Long.valueOf(contentId)).getData();
        List<ContentFileInfoDTO> contentFileList = content.getFileList();
        BookInfoBO bookInfoBO = new BookInfoBO();
        if (contentFileList.size() > 0) {
            String fileId = contentFileList.get(0).getFileId();
            PackageFileInfo packageFileInfo = new PackageFileInfo();
            packageFileInfo.setFileId(fileId);
            BookPackageWrapper bookPackageWrapper2 = new BookPackageWrapper(configuration, packageFileInfo);
            IPackageLifecycle packageLifecycle = new BookPackageLifecycle(bookPackageWrapper2);
            packageLifecycle.parse();
            EnableBookBeanDefinition enableBookBeanDefinition = bookPackageWrapper2.getEnableBookBeanDefinition();
            enableBookBeanDefinition.setContentId(Long.valueOf(contentId));
            enableBookBeanDefinition.getUser().setId(content.getCreator());
            bookInfoBO = this.convert2BookBO(enableBookBeanDefinition);
        }

        return bookInfoBO;
    }


    private void generateBookFile(BookInfoBO book) {
        // 1. context
        Configuration configuration = SpringBeanUtils.getBean(Configuration.class);
        // 2. Book model
        EnableBookBeanDefinition bookBeanDefinition = this.convert2BookBean(book);
        // 3. Book wrapper
        BookPackageWrapper bookPackageWrapper = new BookPackageWrapper(configuration, bookBeanDefinition);
        // 4.Book Lifecycle
        BookPackageLifecycle packageLifecycle = new BookPackageLifecycle(bookPackageWrapper);
        // 5.build .book
        packageLifecycle.build();
        // 6. Get .book document
        PackageFileInfo packageFileInfo = bookPackageWrapper.getPackageFileInfo();
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

            if (book.getContentId() != null)
                file.setContentId(book.getContentId().toString());

            file.setFileOrder(0);
            contentInfoServiceSDK.addFileToContent(file);
        }
        System.out.println(packageFileInfo.getDownloadUrl());
        System.out.println(packageFileInfo.getFileId());
    }

    private void setBasicInfo(BookInfoBO book, ContentInfoDTO content) {
        //contentId
        book.setContentId(content.getContentId());
        //user
        book.setUser(new BookIdNameMapBO(content.getCreator(), content.getCreatorName()));
        book.setCreatTime(new Date());
        book.setUpdateTime(new Date());
    }

    private EnableBookBeanDefinition convert2BookBean(BookInfoBO bookInfoBO) {

        EnableBookBeanDefinition bookBeanDefinition = new EnableBookBeanDefinition();

        bookBeanDefinition.setBookId(bookInfoBO.getBookId().toString());
        bookBeanDefinition.setBookName(bookInfoBO.getBookName());
        bookBeanDefinition.setIsbn(bookInfoBO.getIsbn());
        bookBeanDefinition.setCover(BeanUtils.convert(bookInfoBO.getCover(), FileInfoBO.class));
        bookBeanDefinition.setStage(BeanUtils.convert(bookInfoBO.getStage(), CodeNameMapBO.class));
        bookBeanDefinition.setGrade(BeanUtils.convert(bookInfoBO.getGrade(), CodeNameMapBO.class));
        bookBeanDefinition.setSubject(BeanUtils.convert(bookInfoBO.getSubject(), CodeNameMapBO.class));
        bookBeanDefinition.setTextBookVersion(BeanUtils.convert(bookInfoBO.getTextBookVersion(), CodeNameMapBO.class));
        bookBeanDefinition.setTerm(BeanUtils.convert(bookInfoBO.getTerm(), CodeNameMapBO.class));
        bookBeanDefinition.setUser(BeanUtils.convert(bookInfoBO.getUser(), IdNameMapBO.class));

        bookBeanDefinition.setContentId(bookInfoBO.getContentId());
        bookBeanDefinition.setCreateTime(bookInfoBO.getCreatTime());
        bookBeanDefinition.setCreator(bookInfoBO.getCreator());
        bookBeanDefinition.setUpdator(bookInfoBO.getUpdator());
        bookBeanDefinition.setUpdateTime(bookInfoBO.getUpdateTime());

        List<PageBO> pageList = bookInfoBO.getPageList();
        List<PageNodeInfoBO> pageNodeInfoBOS = new ArrayList<>();
        for (PageBO page : pageList) {

            PageNodeInfoBO pageNodeInfoBO = new PageNodeInfoBO();

            pageNodeInfoBO.setSequence(page.getSequence());
            if (StringUtils.isNotEmpty(page.getName())) {
                pageNodeInfoBO.setImage("files/images/" + page.getName());
            }
            if (StringUtils.isNotEmpty(page.getAffixUrl())) {
                pageNodeInfoBO.setAffixUrl(page.getAffixUrl());
            }

            pageNodeInfoBO.setWidth(page.getWidth());
            pageNodeInfoBO.setHeight(page.getHeight());

            pageNodeInfoBO.setPageFile(BeanUtils.convert(page.getPageFile(), FileInfoBO.class));

            List<SemtenceInfoBO> semtenceInfoList = page.getSemtenceInfoList();
            List<ItemNodeInfoBO> itemNodeInfoBOS = new ArrayList<>();
            for (SemtenceInfoBO semtenceInfo : semtenceInfoList) {
                ItemNodeInfoBO itemNodeInfoBO = new ItemNodeInfoBO();

                itemNodeInfoBO.setQuestionId(semtenceInfo.getQuestionId());
                itemNodeInfoBO.setParentId(semtenceInfo.getParentId());
                itemNodeInfoBO.setSequence(semtenceInfo.getSequenceId());
                if (StringUtils.isNotEmpty(semtenceInfo.getAffixUrl())) {
                    itemNodeInfoBO.setAffixUrl(semtenceInfo.getAffixUrl());
                }
                if (StringUtils.isNotEmpty(semtenceInfo.getText())) {
                    itemNodeInfoBO.setContentBO(new ContentBO(semtenceInfo.getText()));
                }

                itemNodeInfoBO.setContentRegionBO(BeanUtils.convert(semtenceInfo.getContentRegionBO(), ContentRegionBO.class));
                if (CollectionUtils.isNotEmpty(semtenceInfo.getBookRegionInfoList())) {
                    itemNodeInfoBO.setRegionBOS(convert2RegionBO(semtenceInfo.getBookRegionInfoList()));
                }
                itemNodeInfoBOS.add(itemNodeInfoBO);

            }

            pageNodeInfoBO.setItemNodeInfoBOS(itemNodeInfoBOS);
            pageNodeInfoBOS.add(pageNodeInfoBO);
        }

        bookBeanDefinition.setNodes(pageNodeInfoBOS);
        return bookBeanDefinition;
    }

    private List<RegionBO> convert2RegionBO(List<BookRegionBO> bookRegions) {
        List<RegionBO> regionBOList = new ArrayList<>();
        for (BookRegionBO bookRegion : bookRegions) {
            RegionBO regionBO = new RegionBO();
            regionBO.setSequence(bookRegion.getSequence());
            regionBO.setX(bookRegion.getX());
            regionBO.setY(bookRegion.getY());
            regionBO.setWidth(bookRegion.getWidth());
            regionBO.setHeight(bookRegion.getHeight());
            regionBO.setAffixUrl(bookRegion.getAffixUrl());
            regionBO.setContentBO(new ContentBO(bookRegion.getText()));
            regionBOList.add(regionBO);
        }
        return regionBOList;
    }

    private BookInfoBO convert2BookBO(EnableBookBeanDefinition bookBean) {

        BookInfoBO bookInfoBO = new BookInfoBO();

        bookInfoBO.setBookName(bookBean.getBookName());
        bookInfoBO.setIsbn(bookBean.getIsbn());

        bookInfoBO.setCover(BeanUtils.convert(bookBean.getCover(), BookFileInfoBO.class));
        bookInfoBO.setStage(BeanUtils.convert(bookBean.getStage(), BookCodeNameMapBO.class));
        bookInfoBO.setGrade(BeanUtils.convert(bookBean.getGrade(), BookCodeNameMapBO.class));
        bookInfoBO.setSubject(BeanUtils.convert(bookBean.getSubject(), BookCodeNameMapBO.class));
        bookInfoBO.setTextBookVersion(BeanUtils.convert(bookBean.getTextBookVersion(), BookCodeNameMapBO.class));
        bookInfoBO.setTerm(BeanUtils.convert(bookBean.getTerm(), BookCodeNameMapBO.class));
        bookInfoBO.setUser(BeanUtils.convert(bookBean.getUser(), BookIdNameMapBO.class));

        bookInfoBO.setContentId(bookBean.getContentId());

        List<PageBO> pageList = new ArrayList<>();

        List<PageNodeInfoBO> nodes = bookBean.getNodes();
        for (PageNodeInfoBO pageNode : nodes) {

            PageBO pageBO = new PageBO();

            pageBO.setSequence(pageNode.getSequence());
            if (StringUtils.isNotEmpty(pageNode.getImage())) {
                String[] images = pageNode.getImage().split("/");
                pageBO.setName(images[images.length - 1]);
            }

            pageBO.setWidth(pageNode.getWidth());
            pageBO.setHeight(pageNode.getHeight());
            pageBO.setAffixUrl(pageNode.getAffixUrl());

            List<SemtenceInfoBO> semtenceInfoVOList = new ArrayList<>();

            if (pageNode.getItemNodeInfoBOS() != null)
                for (ItemNodeInfoBO itemNode : pageNode.getItemNodeInfoBOS()) {

                    SemtenceInfoBO semtenceInfoBO = new SemtenceInfoBO();
                    semtenceInfoBO.setQuestionId(itemNode.getQuestionId());
                    semtenceInfoBO.setSequenceId(itemNode.getSequence());
                    semtenceInfoBO.setParentId(itemNode.getParentId());
                    semtenceInfoBO.setAffixUrl(itemNode.getAffixUrl());

                    if (Objects.nonNull(itemNode.getContentBO())) {
                        semtenceInfoBO.setText(itemNode.getContentBO().getValue());
                    }
                    if (Objects.nonNull(itemNode.getContentRegionBO())) {
                        semtenceInfoBO.setContentRegionBO(BeanUtils.convert(itemNode.getContentRegionBO(), BookContentRegionBO.class));
                    }
                    if (CollectionUtils.isNotEmpty(itemNode.getRegionBOS())) {
                        List<BookRegionBO> bookRegionBOList = new ArrayList<>();
                        for (RegionBO regionBO : itemNode.getRegionBOS()) {
                            BookRegionBO bookRegionBO = new BookRegionBO();
                            bookRegionBO.setSequence(regionBO.getSequence());
                            bookRegionBO.setX(regionBO.getX());
                            bookRegionBO.setY(regionBO.getY());
                            bookRegionBO.setWidth(regionBO.getWidth());
                            bookRegionBO.setHeight(regionBO.getHeight());
                            bookRegionBO.setAffixUrl(regionBO.getAffixUrl());
                            if (Objects.nonNull(regionBO.getContentBO())) {
                                bookRegionBO.setText(regionBO.getContentBO().getValue());
                            }
                            bookRegionBOList.add(bookRegionBO);
                        }
                        semtenceInfoBO.setBookRegionInfoList(bookRegionBOList);
                    }
                    semtenceInfoVOList.add(semtenceInfoBO);
                }
            pageBO.setSemtenceInfoList(semtenceInfoVOList);
            pageList.add(pageBO);

        }

        bookInfoBO.setPageList(pageList);


        return bookInfoBO;
    }

}

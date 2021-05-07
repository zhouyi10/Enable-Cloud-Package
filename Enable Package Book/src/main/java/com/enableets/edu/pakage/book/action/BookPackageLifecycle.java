package com.enableets.edu.pakage.book.action;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.enableets.edu.pakage.adapter.FileStorageAdapter;
import com.enableets.edu.pakage.book.bean.BookPackageWrapper;
import com.enableets.edu.pakage.book.bean.EnableBookBeanDefinition;
import com.enableets.edu.pakage.book.bean.EnableBookPackage;
import com.enableets.edu.pakage.book.bean.body.*;
import com.enableets.edu.pakage.book.bo.*;
import com.enableets.edu.pakage.core.action.DefaultPackageLifecycle;
import com.enableets.edu.pakage.core.bean.Header;
import com.enableets.edu.pakage.core.bean.Item;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.bean.Property;
import com.enableets.edu.pakage.core.core.Constants;
import com.enableets.edu.pakage.core.core.htmlparser.handler.AttachmentDownloadHandler;
import com.enableets.edu.pakage.core.core.xpath.XNode;
import com.enableets.edu.pakage.core.core.xpath.XPathParser;
import com.enableets.edu.pakage.core.core.xpath.XPathParserFactory;
import com.enableets.edu.pakage.core.core.xstream.EntityToXmlUtils;
import com.enableets.edu.pakage.core.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
public class BookPackageLifecycle extends DefaultPackageLifecycle {

    private BookPackageWrapper bookPackageWrapper;
    private EnableBookBeanDefinition bookBeanDefinition;

    public BookPackageLifecycle(BookPackageWrapper bookPackageWrapper) {
        super(bookPackageWrapper, bookPackageWrapper.getConfiguration());
        this.bookPackageWrapper = bookPackageWrapper;
        this.bookBeanDefinition = bookPackageWrapper.getEnableBookBeanDefinition();

    }

    @Override
    public void build() {
        String bookPathDir = new File(bookPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + bookBeanDefinition.getBookId() + File.separator).toString();
        try {
            //1.book.xml
            bookPackageWrapper.setHeader(new Header(Constants.VERSION, Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, this.getProperties()));
            bookPackageWrapper.setBody(new BookBody(this.buildBodyPage(bookBeanDefinition.getNodes())));
            /*bookPackageWrapper.setFiles(new Files(this.buildFilesItem()));*/
            EnableBookPackage bookPackage = new EnableBookPackage(bookPackageWrapper.getHeader(), (BookBody) bookPackageWrapper.getBody());
            EntityToXmlUtils.toFile(bookPackage, bookPathDir + File.separator + "book" + File.separator + "book.xml");

            //3.Zip book
            this.handlePhysicalImgFiles();
            this.handlePhysicalAffixFiles();
            this.zipFileAndUpload();
        } catch (Throwable e) {
            log.error("BookPackageLifecycle build error, error mesage: {}", e.getMessage(), e);
        } finally {
            FileUtil.del(bookPathDir);
            new File(bookPathDir + ".book").delete();
        }
    }

    private Property getProperties() {
        List<Item> items = new ArrayList<Item>();

        if (Objects.nonNull(bookBeanDefinition.getStage())) {
            items.add(new Item("stageCode", bookBeanDefinition.getStage().getCode()));
            items.add(new Item("stageName", bookBeanDefinition.getStage().getName()));
        }
        if (Objects.nonNull(bookBeanDefinition.getGrade())) {
            items.add(new Item("gradeCode", bookBeanDefinition.getGrade().getCode()));
            items.add(new Item("gradeName", bookBeanDefinition.getGrade().getName()));
        }
        if (Objects.nonNull(bookBeanDefinition.getSubject())) {
            items.add(new Item("subjectCode", bookBeanDefinition.getSubject().getCode()));
            items.add(new Item("subjectName", bookBeanDefinition.getSubject().getName()));
        }

        if (Objects.nonNull(bookBeanDefinition.getTextBookVersion())) {
            items.add(new Item("textBookVersionId", bookBeanDefinition.getTextBookVersion().getCode()));
            items.add(new Item("textBookVersionName", bookBeanDefinition.getTextBookVersion().getName()));
        }

        if (Objects.nonNull(bookBeanDefinition.getTerm())) {
            items.add(new Item("termCode", bookBeanDefinition.getTerm().getCode()));
            items.add(new Item("termName", bookBeanDefinition.getTerm().getName()));
        }

        items.add(new Item("textBookName", bookBeanDefinition.getBookName()));
        items.add(new Item("ISBN", bookBeanDefinition.getIsbn()));

        if (Objects.nonNull(bookBeanDefinition.getCover())) {
            items.add(new Item("cover", "files/images/" + bookBeanDefinition.getCover().getFileName()));
            items.add(new Item("coverUrl", bookBeanDefinition.getCover().getUrl()));
        }
        return new Property(items);
    }

    private void zipFileAndUpload() {
        String pprPathDir = new File(bookPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + bookBeanDefinition.getBookId() + File.separator).toString();
        ZipUtil.zip(pprPathDir, pprPathDir + ".book");
        PackageFileInfo file = FileStorageAdapter.upload(new File(pprPathDir + ".book"), bookPackageWrapper.getConfiguration());

        file.setExt("book");
        bookPackageWrapper.setPackageFileInfo(file);
    }

    private void handlePhysicalImgFiles() {
        String bookPathDir = new File(bookPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + bookBeanDefinition.getId() + File.separator).toString();
        AttachmentDownloadHandler handler = new AttachmentDownloadHandler();

        try {
            if (Objects.nonNull(bookBeanDefinition.getCover())) {
                FileInfoBO cover = bookBeanDefinition.getCover();
                handler.downloadFileRetry(cover.getUrl(), new File(bookPathDir + File.separator + "book/files/images" + File.separator + getFileName(cover.getFileName(), cover.getFileExt()))).execute();
            }

            if (CollectionUtils.isNotEmpty(bookBeanDefinition.getNodes())) {
                for (PageNodeInfoBO pageNode : bookBeanDefinition.getNodes()) {
                    if (Objects.nonNull(pageNode.getPageFile())) {
                        FileInfoBO pageFile = pageNode.getPageFile();
                        handler.downloadFileRetry(pageFile.getUrl(), new File(bookPathDir + File.separator + "book/files/images" + File.separator + getFileName(pageFile.getFileName(), pageFile.getFileExt()))).execute();
                    }

                }
            }
        } catch (InterruptedException e) {
            throw new BookPackageLifecycleException("Download book Paper File Failure!");
        }


    }

    private void handlePhysicalAffixFiles() {
        String bookPathDir = new File(bookPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + bookBeanDefinition.getId() + File.separator).toString();
        AttachmentDownloadHandler handler = new AttachmentDownloadHandler();
        try {
            if (CollectionUtils.isNotEmpty(bookBeanDefinition.getNodes())) {
                for (PageNodeInfoBO pageNode : bookBeanDefinition.getNodes()) {
                    if (StringUtils.isNotBlank(pageNode.getAffixUrl())) {
                        handler.downloadFileRetry(pageNode.getAffixUrl(), new File(bookPathDir + File.separator + "book/files/affix" + File.separator + UUID.randomUUID() + ".mp3")).execute();
                    }
                    if (CollectionUtils.isNotEmpty(pageNode.getItemNodeInfoBOS())) {
                        for (ItemNodeInfoBO itemNode : pageNode.getItemNodeInfoBOS()) {
                            if (StringUtils.isNotBlank(itemNode.getAffixUrl())) {
                                handler.downloadFileRetry(itemNode.getAffixUrl(), new File(bookPathDir + File.separator + "book/files/affix" + File.separator + UUID.randomUUID() + ".mp3")).execute();
                            }
                            if (CollectionUtils.isNotEmpty(itemNode.getRegionBOS())){
                                for (RegionBO regionBO : itemNode.getRegionBOS()) {
                                    if (StringUtils.isNotBlank(regionBO.getAffixUrl())){
                                        handler.downloadFileRetry(regionBO.getAffixUrl(),new File(bookPathDir + File.separator + "book/files/affix" + File.separator + UUID.randomUUID() + ".mp3")).execute();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new BookPackageLifecycleException("Download book Paper File Failure!");
        }

    }


    public String getFileName(String name, String ext) {
        if (name.endsWith("." + ext)) return name;
        return name + "." + ext;
    }

    private List<Page> buildBodyPage(List<PageNodeInfoBO> nodes) {

        if (CollectionUtils.isEmpty(nodes)) return null;
        List<Page> pages = new ArrayList<Page>();
        for (PageNodeInfoBO node : nodes) {
            pages.add(new Page(node.getSequence(),node.getImage(), node.getPageFile().getUrl(), node.getWidth(), node.getHeight(), node.getAffixUrl(), buildItem(node.getItemNodeInfoBOS())));
        }

        return pages;

    }

    private List<BodyItem> buildItem(List<ItemNodeInfoBO> items) {
        if (CollectionUtils.isEmpty(items)) return null;
        List<BodyItem> bodyItems = new ArrayList<BodyItem>();
        for (ItemNodeInfoBO node : items) {
            bodyItems.add(new BodyItem(node.getSequence(), node.getAffixUrl(), node.getParentId(), node.getQuestionId(), buildContent(node.getContentBO()), buildContentRegion(node.getContentRegionBO()), bulidRegionList(node.getRegionBOS())));
        }
        return bodyItems;
    }


    protected List<Region> bulidRegionList(List<RegionBO> regionBOS) {
        if (CollectionUtils.isEmpty(regionBOS)) return null;
        List<Region> regionList = new ArrayList<>();
        for (RegionBO node : regionBOS) {
            regionList.add(new Region(node.getSequence(), node.getX(), node.getY(), node.getWidth(), node.getHeight(), node.getAffixUrl(), buildContent(node.getContentBO())));
        }
        return regionList;
    }


    private Content buildContent(ContentBO node) {
        if (Objects.nonNull(node)) {
            return new Content(node.getValue());
        }
        return new Content();
    }

    private ContentRegion buildContentRegion(ContentRegionBO node) {
        if (Objects.nonNull(node)) {
            return new ContentRegion(node.getX(), node.getY(), node.getHeight(), node.getWidth());
        }
        return new ContentRegion();
    }


    @Override
    public void parse() {
        String unZipDestDir = null;
        try {
            //1. Get Book File Local Path
            this.downLoad();
            //2. UnZip Book
            String localZipPath = bookPackageWrapper.getPackageFileInfo().getLocalZipPath();
            if (StringUtils.isBlank(localZipPath)) return;
            unZipDestDir = this.unZip(localZipPath);
            //3. Parse Header Body Files ...
            this.parseBookConstruction(unZipDestDir);
            //4. convert package object to book BO define
            this.convert2BeanDefinition();
            //5. set Definition
            bookPackageWrapper.setEnableBookBeanDefinition(bookBeanDefinition);
        } catch (Throwable e) {
            log.error("BookPackageLifecycle parse error, error mesage: {}", e.getMessage(), e);
        } finally {
            if (StringUtils.isNotBlank(unZipDestDir)) {
                FileUtil.del(unZipDestDir);
                FileUtil.del(unZipDestDir + ".book");
            }
        }
    }

    private String unZip(String localZipPath) {
        String unZipDestDir = new StringBuilder(bookPackageWrapper.getConfiguration().getPPRTempPath()).append(File.separator).append(FileUtil.mainName(localZipPath)).toString();
        ZipUtil.unzip(bookPackageWrapper.getPackageFileInfo().getLocalZipPath(), unZipDestDir);
        return unZipDestDir;
    }

    private void parseBookConstruction(String unZipDestDir) {
        //File file = new File(unZipDestDir, File.separator + "book" + File.separator + "book.xml");

        XPathParser xPathParser = XPathParserFactory.buildToFile(unZipDestDir + "/book/book.xml");
        bookPackageWrapper.setHeader(Utils.parseHeader(xPathParser.evalNode( "/content/header")));
        bookPackageWrapper.setBody(parseBookBody(xPathParser.evalNode( "/content/body")));
        /*bookPackageWrapper.setFiles(parsePprFiles(xPathParser.evalNode("/package/files")));*/
    }

    private BookBody parseBookBody(XNode evalNode) {
        BookBody bookBody = new BookBody();
        List<XNode> pageXNodes = evalNode.evalNodes("./page");
        if (CollectionUtils.isEmpty(pageXNodes)) return bookBody;
        List<Page> pages = new ArrayList<Page>();
        for (XNode pageXNode : pageXNodes) {
            pages.add(parsePage(pageXNode));
        }
        bookBody.setPageList(pages);
        return bookBody;

    }

    private Page parsePage(XNode pageXNode) {
        Page page = new Page();
        page.setSequence(pageXNode.getAttribute("sequence"));
        page.setImage(pageXNode.getAttribute("image"));
        page.setUrl(pageXNode.getAttribute("url"));
        page.setHeight(pageXNode.getAttribute("height"));
        page.setWidth(pageXNode.getAttribute("width"));
        page.setAffixUrl(pageXNode.getAttribute("affixUrl"));
        List<XNode> itemNodes = pageXNode.evalNodes("./item");

        List<BodyItem> bodyItems = new ArrayList<BodyItem>();

        for (XNode itemNode : itemNodes) {
            bodyItems.add(parseBodyItem(itemNode));
        }

        page.setBodyItems(bodyItems);

        return page;
    }

    private BodyItem parseBodyItem(XNode itemNode) {

        BodyItem bodyItem = new BodyItem();

        bodyItem.setSequence(itemNode.getAttribute("sequence"));
        bodyItem.setAffixUrl(itemNode.getAttribute("affixUrl"));
        bodyItem.setParentId(itemNode.getAttribute("parentId"));
        bodyItem.setQuestionId(itemNode.getAttribute("questionId"));

        XNode contentXNode = itemNode.evalNode("./content");
        bodyItem.setContent(new Content(contentXNode.getBody()));

        XNode contentRegionXNode = itemNode.evalNode("./contentRegion");
        bodyItem.setContentRegion(parseContentRegion(contentRegionXNode));

        XNode regionListXNode = itemNode.evalNode("./regionList");
        if (Objects.nonNull(regionListXNode)) {
            List<XNode> regionNodes = regionListXNode.evalNodes("./region");
            List<Region> regionList = new ArrayList<>();
            for (XNode regionNode : regionNodes) {
                regionList.add(parseRegion(regionNode));
            }
            bodyItem.setRegionList(regionList);
        }

        return bodyItem;
    }

    private ContentRegion parseContentRegion(XNode contentRegionXNode) {
        ContentRegion contentRegion = new ContentRegion();

        contentRegion.setX(contentRegionXNode.getAttribute("x"));
        contentRegion.setY(contentRegionXNode.getAttribute("y"));
        contentRegion.setHeight(contentRegionXNode.getAttribute("height"));
        contentRegion.setWidth(contentRegionXNode.getAttribute("width"));
        return contentRegion;
    }

    private Region parseRegion(XNode regionNode) {
        Region region = new Region();

        region.setSequence(regionNode.getAttribute("sequence"));
        region.setX(regionNode.getAttribute("x"));
        region.setY(regionNode.getAttribute("y"));
        region.setWidth(regionNode.getAttribute("width"));
        region.setHeight(regionNode.getAttribute("height"));
        region.setAffixUrl(regionNode.getAttribute("affixUrl"));

        XNode contentNode = regionNode.evalNode("./content");
        region.setContent(new Content(contentNode.getBody()));
        return region;
    }

    private void convert2BeanDefinition() {
        if (Objects.isNull(bookBeanDefinition)) bookBeanDefinition = new EnableBookBeanDefinition();
        this.convertHeader2ToBeanDefinition();
        this.convertBody2BeanDefinition();
        /*this.convertFiles2BeanDefinition();*/
    }

    private void convertHeader2ToBeanDefinition() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Property property = bookPackageWrapper.getHeader().getProperty();
        List<Item> items = property.getItems();
        bookBeanDefinition.setStage(new CodeNameMapBO());
        bookBeanDefinition.setGrade(new CodeNameMapBO());
        bookBeanDefinition.setSubject(new CodeNameMapBO());
        bookBeanDefinition.setTextBookVersion(new CodeNameMapBO());
        bookBeanDefinition.setTerm(new CodeNameMapBO());
        bookBeanDefinition.setUser(new IdNameMapBO());
        bookBeanDefinition.setCover(new FileInfoBO());

        for (Item item : items) {
            if (item.getKey().equals("ISBN")) bookBeanDefinition.setIsbn(item.getValue());
            else if (item.getKey().equals("textBookName")) bookBeanDefinition.setBookName(item.getValue());

            else if (item.getKey().equals("cover")) bookBeanDefinition.getCover().setFileName(item.getValue());
            else if (item.getKey().equals("coverUrl")) bookBeanDefinition.getCover().setUrl(item.getValue());

            else if (item.getKey().equals("stageCode")) bookBeanDefinition.getStage().setCode(item.getValue());
            else if (item.getKey().equals("stageName")) bookBeanDefinition.getStage().setName(item.getValue());

            else if (item.getKey().equals("gradeCode")) bookBeanDefinition.getGrade().setCode(item.getValue());
            else if (item.getKey().equals("gradeName")) bookBeanDefinition.getGrade().setName(item.getValue());

            else if (item.getKey().equals("textBookVersionId"))
                bookBeanDefinition.getTextBookVersion().setCode(item.getValue());
            else if (item.getKey().equals("textBookVersionName"))
                bookBeanDefinition.getTextBookVersion().setName(item.getValue());

            else if (item.getKey().equals("subjectCode")) bookBeanDefinition.getSubject().setCode(item.getValue());
            else if (item.getKey().equals("subjectName")) bookBeanDefinition.getSubject().setName(item.getValue());

            else if (item.getKey().equals("termCode")) bookBeanDefinition.getTerm().setCode(item.getValue());
            else if (item.getKey().equals("termName")) bookBeanDefinition.getTerm().setName(item.getValue());


        }
    }

    private void convertBody2BeanDefinition() {
        BookBody body = bookPackageWrapper.getBody();
        if (CollectionUtils.isEmpty(body.getPageList())) return;
        List<PageNodeInfoBO> nodes = new ArrayList<>();
        for (Page page : body.getPageList()) {
            PageNodeInfoBO node = new PageNodeInfoBO();
            node.setSequence(page.getSequence());
            node.setImage(page.getImage());
            node.setWidth(page.getWidth());
            node.setHeight(page.getHeight());
            node.setAffixUrl(page.getAffixUrl());
            FileInfoBO fileInfoBO = new FileInfoBO();
            fileInfoBO.setUrl(page.getUrl());
            fileInfoBO.setFileName(page.getImage());
            node.setPageFile(fileInfoBO);
            if (CollectionUtils.isNotEmpty(page.getBodyItems())) {
                node.setItemNodeInfoBOS(this.convertItem2NodeDefinition(page.getBodyItems()));
            }

            nodes.add(node);
        }

        bookBeanDefinition.setNodes(nodes);
    }


    private List<ItemNodeInfoBO> convertItem2NodeDefinition(List<BodyItem> bodyItems) {
        List<ItemNodeInfoBO> nodes = new ArrayList<>();
        for (BodyItem bodyItem : bodyItems) {
            ItemNodeInfoBO node = new ItemNodeInfoBO();
            node.setSequence(bodyItem.getSequence());
            node.setAffixUrl(bodyItem.getAffixUrl());
            node.setParentId(bodyItem.getParentId());
            node.setQuestionId(bodyItem.getQuestionId());

            if (Objects.nonNull(bodyItem.getContent())) {
                node.setContentBO(new ContentBO(bodyItem.getContent().getValue()));
            }

            ContentRegion contentRegion = bodyItem.getContentRegion();
            if (Objects.nonNull(contentRegion)) {
                node.setContentRegionBO(new ContentRegionBO(contentRegion.getX(), contentRegion.getY(), contentRegion.getWidth(), contentRegion.getHeight()));
            }

            if (CollectionUtils.isNotEmpty(bodyItem.getRegionList())) {
                node.setRegionBOS(this.converRegion2NodeDefintion(bodyItem.getRegionList()));
            }

            nodes.add(node);
        }
        return nodes;
    }

    private List<RegionBO> converRegion2NodeDefintion(List<Region> regions) {
        List<RegionBO> nodes = new ArrayList<>();
        for (Region region : regions) {
            RegionBO node = new RegionBO();
            node.setSequence(region.getSequence());
            node.setX(region.getX());
            node.setY(region.getY());
            node.setWidth(region.getWidth());
            node.setHeight(region.getHeight());
            node.setAffixUrl(region.getAffixUrl());
            if (Objects.nonNull(region.getContent())) {
                node.setContentBO(new ContentBO(region.getContent().getValue()));
            }
            nodes.add(node);
        }
        return nodes;
    }


}

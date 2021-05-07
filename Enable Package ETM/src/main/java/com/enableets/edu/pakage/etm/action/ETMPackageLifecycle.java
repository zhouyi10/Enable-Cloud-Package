package com.enableets.edu.pakage.etm.action;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import com.enableets.edu.pakage.adapter.FileStorageAdapter;
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
import com.enableets.edu.pakage.etm.bean.ETMPackageWrapper;
import com.enableets.edu.pakage.etm.bean.EnableETMBeanDefinition;
import com.enableets.edu.pakage.etm.bean.EnableETMPackage;
import com.enableets.edu.pakage.etm.bean.body.*;
import com.enableets.edu.pakage.etm.bo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ETMPackageLifecycle extends DefaultPackageLifecycle {

    private ETMPackageWrapper etmPackageWrapper;
    private EnableETMBeanDefinition etmBeanDefinition;

    public ETMPackageLifecycle(ETMPackageWrapper etmPackageWrapper) {
        super(etmPackageWrapper, etmPackageWrapper.getConfiguration());
        this.etmPackageWrapper = etmPackageWrapper;
        this.etmBeanDefinition = etmPackageWrapper.getEnableETMBeanDefinition();

    }

    @Override
    public void build() {
        String etmPathDir = new File(etmPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + etmBeanDefinition.getBookId() + File.separator).toString();
        try {
            //1.etm.xml
            etmPackageWrapper.setHeader(new Header(Constants.VERSION, Constants.ENCODING, Constants.VERIFICATION, Constants.ENCRYPTION, Constants.COMPRESSION, this.getProperties()));
            etmPackageWrapper.setBody(new ETMBody(this.buildBodyPage(etmBeanDefinition.getNodes())));
            /*etmPackageWrapper.setFiles(new Files(this.buildFilesItem()));*/
            EnableETMPackage etmPackage = new EnableETMPackage(etmPackageWrapper.getHeader(), (ETMBody) etmPackageWrapper.getBody());
            EntityToXmlUtils.toFile(etmPackage, etmPathDir + File.separator + "etm" + File.separator + "etm.xml");

            //3.Zip etm
            this.handlePhysicalImgFiles();
            this.handlePhysicalMediaFiles();
            this.zipFileAndUpload();
        }catch (Throwable e){
            log.error("ETMPackageLifecycle build error, error mesage: {}", e.getMessage(), e);
        }finally {
            FileUtil.del(etmPathDir);
            FileUtil.del(etmPathDir + ".etm");
        }
    }

    private Property getProperties() {
        List<Item> items = new ArrayList<Item>();

        if (etmBeanDefinition.getStage() != null) {
            items.add(new Item("stageCode", etmBeanDefinition.getStage().getCode()));
            items.add(new Item("stageName", etmBeanDefinition.getStage().getName()));
        }
        if (etmBeanDefinition.getGrade() != null) {
            items.add(new Item("gradeCode", etmBeanDefinition.getGrade().getCode()));
            items.add(new Item("gradeName", etmBeanDefinition.getGrade().getName()));
        }
        if (etmBeanDefinition.getSubject() != null) {
            items.add(new Item("subjectCode", etmBeanDefinition.getSubject().getCode()));
            items.add(new Item("subjectName", etmBeanDefinition.getSubject().getName()));
        }

        if (etmBeanDefinition.getTextBookVersion() != null) {
            items.add(new Item("textBookVersionId", etmBeanDefinition.getTextBookVersion().getCode()));
            items.add(new Item("textBookVersionName", etmBeanDefinition.getTextBookVersion().getName()));
        }

        if (etmBeanDefinition.getTerm() != null) {
            items.add(new Item("termCode", etmBeanDefinition.getTerm().getCode()));
            items.add(new Item("termName", etmBeanDefinition.getTerm().getName()));
        }

        items.add(new Item("bookId",etmBeanDefinition.getBookId()));
        items.add(new Item("textBookName", etmBeanDefinition.getTextBookName()));
        items.add(new Item("ISBN", etmBeanDefinition.getIsbn()));
        items.add(new Item("cover", etmBeanDefinition.getCover()));
        items.add(new Item("coverUrl", etmBeanDefinition.getCoverUrl()));


        return new Property(items);
    }

    private void zipFileAndUpload() {
        String pprPathDir = new File(etmPackageWrapper.getConfiguration().getPPRTempPath() + File.separator + etmBeanDefinition.getBookId() + File.separator).toString();
        ZipUtil.zip(pprPathDir, pprPathDir + ".etm");
        PackageFileInfo file = FileStorageAdapter.upload(new File(pprPathDir + ".etm"), etmPackageWrapper.getConfiguration());

        file.setExt("etm");
        etmPackageWrapper.setPackageFileInfo(file);
    }

    private void handlePhysicalImgFiles() {
        String etmPathDir = new File(etmPackageWrapper.getConfiguration().getPPRTempPath() + "/" + etmBeanDefinition.getId() + "/").toString();
        AttachmentDownloadHandler handler = new AttachmentDownloadHandler();

        try {
            if (etmBeanDefinition.getCover() != null) {
                String[] coverInfo = getFileInfo(etmBeanDefinition.getCover());
                handler.downloadFileRetry(etmBeanDefinition.getCoverUrl(), new File(etmPathDir + "/etm/files/images/" + getFileName(coverInfo[0], coverInfo[1]))).execute();
            }

            if (CollectionUtils.isNotEmpty(etmBeanDefinition.getNodes())) {
                for (PageNodeInfoBO pageNode : etmBeanDefinition.getNodes()) {
                    if (pageNode.getFile() != null) {
                        String[] fileInfo = getFileInfo(pageNode.getFile());
                        handler.downloadFileRetry(pageNode.getImgSrc(), new File(etmPathDir + "/etm/files/images/" + getFileName(fileInfo[0], fileInfo[1]))).execute();
                    }

                }
            }
        } catch (InterruptedException e) {
            throw new ETMPackageLifecycleException("Download etm Paper File Failure!");
        }


    }

    private void handlePhysicalMediaFiles() {
        String etmPathDir = new File(etmPackageWrapper.getConfiguration().getPPRTempPath() + "/" + etmBeanDefinition.getId() + "/").toString();
        AttachmentDownloadHandler handler = new AttachmentDownloadHandler();
        try {
            if (CollectionUtils.isNotEmpty(etmBeanDefinition.getNodes())) {
                for (PageNodeInfoBO pageNode : etmBeanDefinition.getNodes()) {
                    if (pageNode.getMedia() != null) {
                        String[] fileInfo1 = getFileInfo(pageNode.getMedia());
                        handler.downloadFileRetry(pageNode.getMediaSrc(), new File(etmPathDir + "/etm/files/medias/" + getFileName(fileInfo1[0], fileInfo1[1]))).execute();
                    }
                    if (pageNode.getItemNodeInfoBOS() != null){
                        for (ItemNodeInfoBO itemNode : pageNode.getItemNodeInfoBOS()) {
                            if (itemNode.getMedia() != null) {
                                String[] fileInfo2 = getFileInfo(itemNode.getMedia());
                                handler.downloadFileRetry(itemNode.getMediaSrc(), new File(etmPathDir + "/etm/files/medias/" + getFileName(fileInfo2[0], fileInfo2[1]))).execute();
                            }
                        }
                    }
                }
            }
        } catch (InterruptedException e) {
            throw new ETMPackageLifecycleException("Download etm Paper File Failure!");
        }

    }


    public String getFileName(String name, String ext) {
        if (name.endsWith("." + ext)) return name;
        return name + "." + ext;
    }

    private String[] getFileInfo(String file) {

        String[] fileInfoList = new String[2];
        String[] files = file.split("/");
        String fileName = files[files.length - 1];
        String[] exts = file.split("\\.");
        String ext = exts[exts.length - 1];
        fileInfoList[0] = fileName;
        fileInfoList[1] = ext;
        return fileInfoList;
    }

    private List<Page> buildBodyPage(List<PageNodeInfoBO> nodes) {

        if (CollectionUtils.isEmpty(nodes)) return null;
        List<Page> pages = new ArrayList<Page>();
        for (PageNodeInfoBO node : nodes) {
            pages.add(new Page(node.getId(),node.getSequence(), node.getFile(), node.getImgSrc(), node.getWidth(), node.getHeight(), node.getMedia(), node.getMediaSrc(), buildItem(node.getItemNodeInfoBOS())));
        }

        return pages;

    }

    private List<BodyItem> buildItem(List<ItemNodeInfoBO> items) {
        if (CollectionUtils.isEmpty(items)) return null;
        List<BodyItem> bodyItems = new ArrayList<BodyItem>();
        for (ItemNodeInfoBO node : items) {
            bodyItems.add(new BodyItem(node.getId(),node.getSequence(), node.getMedia(), node.getMediaSrc(), buildContent(node.getContentBO()), buildRegionList(node.getRegionBOS())));
        }
        return bodyItems;
    }


    protected List<Region> buildRegionList(List<RegionBO> regionBOS) {
        if (CollectionUtils.isEmpty(regionBOS)) return null;
        List<Region> regionList = new ArrayList<>();
        for (RegionBO node : regionBOS) {
            regionList.add(new Region(node.getId(),node.getX(), node.getY(), node.getWidth(), node.getHeight(), node.getSemtenceInfoDiv(), node.getSemtenceYtop()));
        }
        return regionList;
    }


    private Content buildContent(ContentBO node) {
        if (node != null) {
            return new Content(node.getValue());
        }
        return new Content();
    }


    @Override
    public void parse() {
        String unZipDestDir = null;
        try {
            //1. Get .ETM File Local Path
            this.downLoad();
            //2. UnZip .ETM
            String localZipPath = etmPackageWrapper.getPackageFileInfo().getLocalZipPath();
            if (StringUtils.isBlank(localZipPath)) return;
            unZipDestDir = this.unZip(localZipPath);
            //3. Parse Header Body Files ...
            this.parseETMConstruction(unZipDestDir);
            //4. convert package object to etm BO define
            this.convert2BeanDefinition();
            //5. set Definition
            etmPackageWrapper.setEnableETMBeanDefinition(etmBeanDefinition);
        } catch (Throwable e) {
            log.error("ETMPackageLifecycle parse error, error mesage: {}", e.getMessage(), e);
        } finally {
            if (StringUtils.isNotBlank(unZipDestDir)) {
                FileUtil.del(unZipDestDir);
                FileUtil.del(unZipDestDir + ".etm");
            }
        }
    }

    private String unZip(String localZipPath) {
        String unZipDestDir = new StringBuilder(etmPackageWrapper.getConfiguration().getPPRTempPath()).append("/").append(FileUtil.mainName(localZipPath)).toString();
        ZipUtil.unzip(etmPackageWrapper.getPackageFileInfo().getLocalZipPath(), unZipDestDir);
        return unZipDestDir;
    }

    private void parseETMConstruction(String unZipDestDir) {
        File file = new File(unZipDestDir, "/etm/" + "etm.xml");

        XPathParser xPathParser = XPathParserFactory.buildToFile(unZipDestDir + "/etm/" + "etm.xml");
        etmPackageWrapper.setHeader(Utils.parseHeader(xPathParser.evalNode("/content/header")));
        etmPackageWrapper.setBody(parseEtmBody(xPathParser.evalNode("/content/body")));
        /*etmPackageWrapper.setFiles(parsePprFiles(xPathParser.evalNode("/package/files")));*/
    }

    private ETMBody parseEtmBody(XNode evalNode) {
        ETMBody etmBody = new ETMBody();
        List<XNode> pageXNodes = evalNode.evalNodes("./page");
        if (CollectionUtils.isEmpty(pageXNodes)) return etmBody;
        List<Page> pages = new ArrayList<Page>();
        for (XNode pageXNode : pageXNodes) {
            pages.add(parsePage(pageXNode));
        }
        etmBody.setPageList(pages);
        return etmBody;

    }

    private Page parsePage(XNode pageXNode) {
        Page page = new Page();
        page.setId(pageXNode.getAttribute("id"));
        page.setSequence(pageXNode.getAttribute("sequence"));
        page.setFile(pageXNode.getAttribute("file"));
        page.setImgSrc(pageXNode.getAttribute("imgSrc"));
        page.setHeight(pageXNode.getAttribute("height"));
        page.setWidth(pageXNode.getAttribute("width"));
        page.setMedia(pageXNode.getAttribute("media"));
        page.setMediaSrc(pageXNode.getAttribute("mediaSrc"));
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
        bodyItem.setId(itemNode.getAttribute("id"));
        bodyItem.setSequence(itemNode.getAttribute("sequence"));
        bodyItem.setMedia(itemNode.getAttribute("media"));
        bodyItem.setMediaSrc(itemNode.getAttribute("mediaSrc"));

        XNode xNode = itemNode.evalNode("./content");

        XNode regionListXNode = itemNode.evalNode("./regionList");

        if (regionListXNode != null) {
            List<XNode> regionNodes = regionListXNode.evalNodes("./region");
            bodyItem.setContent(new Content(xNode.getBody()));
            List<Region> regionList = new ArrayList<>();
            for (XNode regionNode : regionNodes) {
                regionList.add(parseRegion(regionNode));
            }
            bodyItem.setRegionList(regionList);
        }

        return bodyItem;
    }

    private Region parseRegion(XNode regionNode) {
        Region region = new Region();
        region.setId(regionNode.getAttribute("id"));
        region.setX(regionNode.getAttribute("x"));
        region.setY(regionNode.getAttribute("y"));
        region.setWidth(regionNode.getAttribute("width"));
        region.setHeight(regionNode.getAttribute("height"));
        region.setSemtenceInfoDiv(regionNode.getAttribute("semtenceInfoDiv"));
        region.setSemtenceYtop(regionNode.getAttribute("semtenceYtop"));
        return region;
    }

    private void convert2BeanDefinition() {
        if (etmBeanDefinition == null) etmBeanDefinition = new EnableETMBeanDefinition();
        this.convertHeader2ToBeanDefinition();
        this.convertBody2BeanDefinition();
        /*this.convertFiles2BeanDefinition();*/
    }

    private void convertHeader2ToBeanDefinition() {
        Property property = etmPackageWrapper.getHeader().getProperty();
        List<Item> items = property.getItems();
        etmBeanDefinition.setStage(new CodeNameMapBO());
        etmBeanDefinition.setGrade(new CodeNameMapBO());
        etmBeanDefinition.setSubject(new CodeNameMapBO());
        etmBeanDefinition.setTextBookVersion(new CodeNameMapBO());
        etmBeanDefinition.setTerm(new CodeNameMapBO());
        etmBeanDefinition.setUser(new IdNameMapBO());

        for (Item item : items) {
            if (item.getKey().equals("ISBN")) etmBeanDefinition.setIsbn(item.getValue());
            else if (item.getKey().equals("bookId")) etmBeanDefinition.setBookId(item.getValue());
            else if (item.getKey().equals("textBookName")) etmBeanDefinition.setTextBookName(item.getValue());
            else if (item.getKey().equals("cover")) etmBeanDefinition.setCover(item.getValue());
            else if (item.getKey().equals("coverUrl")) etmBeanDefinition.setCoverUrl(item.getValue());

            else if (item.getKey().equals("stageCode")) etmBeanDefinition.getStage().setCode(item.getValue());
            else if (item.getKey().equals("stageName")) etmBeanDefinition.getStage().setName(item.getValue());

            else if (item.getKey().equals("gradeCode")) etmBeanDefinition.getGrade().setCode(item.getValue());
            else if (item.getKey().equals("gradeName")) etmBeanDefinition.getGrade().setName(item.getValue());

            else if (item.getKey().equals("textBookVersionId"))
                etmBeanDefinition.getTextBookVersion().setCode(item.getValue());
            else if (item.getKey().equals("textBookVersionName"))
                etmBeanDefinition.getTextBookVersion().setName(item.getValue());

            else if (item.getKey().equals("subjectCode")) etmBeanDefinition.getSubject().setCode(item.getValue());
            else if (item.getKey().equals("subjectName")) etmBeanDefinition.getSubject().setName(item.getValue());

            else if (item.getKey().equals("termCode")) etmBeanDefinition.getTerm().setCode(item.getValue());
            else if (item.getKey().equals("termName")) etmBeanDefinition.getTerm().setName(item.getValue());


        }
    }

    private void convertBody2BeanDefinition() {
        ETMBody body = etmPackageWrapper.getBody();
        if (body == null || CollectionUtils.isEmpty(body.getPageList())) return;
        List<PageNodeInfoBO> nodes = new ArrayList<>();
        for (Page page : body.getPageList()) {
            PageNodeInfoBO node = new PageNodeInfoBO();
            node.setId(page.getId());
            node.setSequence(page.getSequence());
            node.setImgSrc(page.getImgSrc());
            node.setFile(page.getFile());
            node.setWidth(page.getWidth());
            node.setHeight(page.getHeight());
            node.setMedia(page.getMedia());
            node.setMediaSrc(page.getMediaSrc());

            if (CollectionUtils.isNotEmpty(page.getBodyItems())) {
                node.setItemNodeInfoBOS(this.convertItem2NodeDefinition(page.getBodyItems()));
            }

            nodes.add(node);
        }

        etmBeanDefinition.setNodes(nodes);
    }


    private List<ItemNodeInfoBO> convertItem2NodeDefinition(List<BodyItem> bodyItems) {
        List<ItemNodeInfoBO> nodes = new ArrayList<>();
        for (BodyItem bodyItem : bodyItems) {
            ItemNodeInfoBO node = new ItemNodeInfoBO();
            node.setId(bodyItem.getId());
            node.setSequence(bodyItem.getSequence());
            node.setMedia(bodyItem.getMedia());
            node.setMediaSrc(bodyItem.getMediaSrc());

            if (bodyItem.getContent() != null) {
                node.setContentBO(new ContentBO(bodyItem.getContent().getValue()));
            }

            if (bodyItem.getRegionList() != null && CollectionUtils.isNotEmpty(bodyItem.getRegionList())) {
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
            node.setId(region.getId());
            node.setX(region.getX());
            node.setY(region.getY());
            node.setWidth(region.getWidth());
            node.setHeight(region.getHeight());
            node.setSemtenceInfoDiv(region.getSemtenceInfoDiv());
            node.setSemtenceYtop(region.getSemtenceYtop());

            nodes.add(node);
        }
        return nodes;
    }


}

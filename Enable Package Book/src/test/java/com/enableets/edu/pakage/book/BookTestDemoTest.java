package com.enableets.edu.pakage.book;


import com.enableets.edu.pakage.book.action.BookPackageLifecycle;
import com.enableets.edu.pakage.book.bean.BookPackageWrapper;
import com.enableets.edu.pakage.book.bean.EnableBookBeanDefinition;
import com.enableets.edu.pakage.book.bo.*;
import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class BookTestDemoTest {
    
    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration().setClientId("wiedu_application_key").setClientSecret("wiedu_application_secret").setPPRTempPath("C:\\Users\\xuechuang\\Desktop\\testbook").setServerAddress("http://192.168.116.190");
    }


    //@Test
    public void testBuildBook() {

        EnableBookBeanDefinition bookBookInfo = new EnableBookBeanDefinition();
        bookBookInfo.setBookId("202103190909");
        bookBookInfo.setBookName("554545");
        bookBookInfo.setIsbn("2222");
        FileInfoBO cover = new FileInfoBO();
        cover.setFileName("01.jpg");
        cover.setUrl("http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/11/f65ec2eddfe347a29f2d4c76750d40bd.jpg");
        cover.setFileExt("jpg");
        bookBookInfo.setCover(cover);
        bookBookInfo.setStage(new CodeNameMapBO("11","222"));
        bookBookInfo.setSubject(new CodeNameMapBO("22","333"));
        bookBookInfo.setGrade(new CodeNameMapBO("33","小学"));
        bookBookInfo.setTextBookVersion(new CodeNameMapBO("vvv","v1"));
        bookBookInfo.setTerm(new CodeNameMapBO("11","上学期"));
        bookBookInfo.setUser(new IdNameMapBO("111","teacher"));
        bookBookInfo.setContentId(1222244615616l);

        List<PageNodeInfoBO> list1 = new ArrayList(){};
        List<ItemNodeInfoBO> list2 = new ArrayList(){};

        ContentBO contentBO = new ContentBO("222");
        ContentRegionBO contentRegionBO = new ContentRegionBO("123", "123", "123", "123");

        List<RegionBO> regionBOS = new ArrayList<>();
        RegionBO regionBO1 = new RegionBO("1","123", "123", "123", "123","http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/15/97c61472588140829ea5a30dad853d36.mp3",contentBO);
        RegionBO regionBO2 = new RegionBO("1","321", "321", "321", "321","http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/56530d8c3ed049bab7118f2170649c7c.mp3",contentBO);
        regionBOS.add(regionBO1);
        regionBOS.add(regionBO2);
        String img1Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/14/04754a7f66c348dc8a8325456edd244b.jpg";
        String img2Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/14/bc743d8250cf4e5492c5a1d8bb226bde.jpg";

        String mediaSrc = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/c46f3def7c8a488aac0adb6dd55ec1b7.mp3";
        String media1Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/14/e7d0a1caff5344bbabae1b854a2cc1ba.mp3";
        String media2Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/03f16da16dc14eea96170109438eb8ab.mp3";

        list2.add(new ItemNodeInfoBO("1",media1Src,"11111","222222",contentBO,contentRegionBO,regionBOS));
        list2.add(new ItemNodeInfoBO("2",media2Src,"11111","222222",contentBO,contentRegionBO,regionBOS));


        PageNodeInfoBO pageNode1 = new PageNodeInfoBO();
        pageNode1.setSequence("1");
        pageNode1.setImage("files/image/02.jpg");
        pageNode1.setHeight("222");
        pageNode1.setWidth("222");
        pageNode1.setAffixUrl(mediaSrc);
        FileInfoBO pageFile1 = new FileInfoBO();
        pageFile1.setUrl(img1Src);
        pageFile1.setFileName("02.jpg");
        pageFile1.setFileExt("jpg");
        pageNode1.setPageFile(pageFile1);



        PageNodeInfoBO pageNode2 = new PageNodeInfoBO();
        pageNode2.setSequence("2");
        pageNode2.setImage("files/image/03.jpg");
        pageNode2.setHeight("222");
        pageNode2.setWidth("222");
        pageNode2.setAffixUrl(mediaSrc);
        FileInfoBO pageFile2 = new FileInfoBO();
        pageFile2.setUrl(img2Src);
        pageFile2.setFileName("03.jpg");
        pageFile2.setFileExt("jpg");
        pageNode2.setPageFile(pageFile2);
        pageNode2.setItemNodeInfoBOS(list2);


        list1.add(pageNode1);
        list1.add(pageNode2);
        bookBookInfo.setNodes(list1);

        BookPackageWrapper bookPackageWrapper = new BookPackageWrapper(configuration, bookBookInfo);
        BookPackageLifecycle bookPackageLifecycle = new BookPackageLifecycle(bookPackageWrapper);
        bookPackageLifecycle.build();

        System.out.println(bookPackageWrapper.getPackageFileInfo().getDownloadUrl());
        System.out.println(bookPackageWrapper.getPackageFileInfo().getFileId());
    }


    //@Test
    public void testParseBook(){

        PackageFileInfo packageFileInfo = new PackageFileInfo();
        packageFileInfo.setFileId("02eef5e3b27648a49a3727accc7b123c");
        BookPackageWrapper bookPackageWrapper = new BookPackageWrapper(configuration, packageFileInfo);
        IPackageLifecycle packageLifecycle = new BookPackageLifecycle(bookPackageWrapper);
        packageLifecycle.parse();
        System.out.println(bookPackageWrapper.getEnableBookBeanDefinition().toString());
        System.out.println(bookPackageWrapper.getHeader().toString());
        System.out.println(bookPackageWrapper.getBody().toString());

    }
}

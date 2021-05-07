package com.enableets.edu.pakage.etm;


import com.enableets.edu.pakage.core.action.IPackageLifecycle;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.etm.action.ETMPackageLifecycle;
import com.enableets.edu.pakage.etm.bean.ETMPackageWrapper;
import com.enableets.edu.pakage.etm.bean.EnableETMBeanDefinition;
import com.enableets.edu.pakage.etm.bo.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ETMTestDemoTest {

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration().setClientId("wiedu_application_key").setClientSecret("wiedu_application_secret").setPPRTempPath("C:\\Users\\xuechuang\\Desktop\\testetm").setServerAddress("http://192.168.116.190");
    }

    //@Test
    public void testBuildETM() {

        EnableETMBeanDefinition etmBookInfo = new EnableETMBeanDefinition();
        etmBookInfo.setBookId("2020120211058");
        etmBookInfo.setTextBookName("554545");
        etmBookInfo.setIsbn("2222");
        etmBookInfo.setCover("file/image/02.jpg");
        etmBookInfo.setCoverUrl("http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/fa258b388ad44a8793adbabd3db3a40e.jpg");
        etmBookInfo.setStage(new CodeNameMapBO("11","222"));
        etmBookInfo.setSubject(new CodeNameMapBO("11","222"));
        etmBookInfo.setGrade(new CodeNameMapBO("11","小学"));
        etmBookInfo.setTextBookVersion(new CodeNameMapBO("11","222"));
        etmBookInfo.setTerm(new CodeNameMapBO("11","222"));
        etmBookInfo.setUser(new IdNameMapBO("111","555"));
        etmBookInfo.setContentId(1222244615616l);

        List list1 = new ArrayList(){};
        List list2 = new ArrayList(){};

        ContentBO contentBO = new ContentBO("222");
        List<RegionBO> regionBOS = new ArrayList<>();
        RegionBO regionBO1 = new RegionBO("1111","123", "123", "123", "123","111","111");
        RegionBO regionBO2 = new RegionBO("2222","321", "321", "321", "321","222","222");
        regionBOS.add(regionBO1);
        regionBOS.add(regionBO2);
        String img1Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/fa258b388ad44a8793adbabd3db3a40e.jpg";
        String img2Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/55e8258eccc0438a89256c17cf4bd8af.jpg";

        String mediaSrc = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/4784411b268b4312b8ea60a6e8f6260c.mp3";
        String media1Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/c46f3def7c8a488aac0adb6dd55ec1b7.mp3";
        String media2Src = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/12/3/128880d30d4144b1a087175d2fb4f827.mp3";

        list2.add(new ItemNodeInfoBO("1111","1","files/images/a0001.mp3",media1Src,contentBO,regionBOS));
        list2.add(new ItemNodeInfoBO("2222","1","files/images/a0001.mp3",media2Src,contentBO,regionBOS));

        PageNodeInfoBO pageNode1 = new PageNodeInfoBO();
        pageNode1.setId("1111");
        pageNode1.setSequence("1");
        pageNode1.setFile("files/images/01.jpg");
        pageNode1.setHeight("222");
        pageNode1.setWidth("222");
        pageNode1.setImgSrc(img1Src);
        pageNode1.setMedia("files/medias/01.mp3");
        
        PageNodeInfoBO pageNode2 = new PageNodeInfoBO();
        pageNode2.setId("2222");
        pageNode2.setSequence("1");
        pageNode2.setFile("files/images/01.jpg");
        pageNode2.setHeight("222");
        pageNode2.setWidth("222");
        pageNode2.setImgSrc(img2Src);
        pageNode2.setMedia("files/medias/01.mp3");
        pageNode2.setMediaSrc(mediaSrc);
        pageNode2.setItemNodeInfoBOS(list2);


        list1.add(pageNode1);
        list1.add(pageNode2);
        etmBookInfo.setNodes(list1);

        ETMPackageWrapper etmPackageWrapper = new ETMPackageWrapper(configuration, etmBookInfo);
        ETMPackageLifecycle etmPackageLifecycle = new ETMPackageLifecycle(etmPackageWrapper);
        etmPackageLifecycle.build();

        System.out.println(etmPackageWrapper.getPackageFileInfo().getDownloadUrl());
        System.out.println(etmPackageWrapper.getPackageFileInfo().getFileId());
    }


    //@Test
    public void testParseETM(){

        PackageFileInfo packageFileInfo = new PackageFileInfo();
        packageFileInfo.setFileId("663e6e7d6fbd4738a3a78701c3409ce7");
        ETMPackageWrapper etmPackageWrapper = new ETMPackageWrapper(configuration, packageFileInfo);
        IPackageLifecycle packageLifecycle = new ETMPackageLifecycle(etmPackageWrapper);
        packageLifecycle.parse();
        System.out.println(etmPackageWrapper.getEnableETMBeanDefinition().toString());
        System.out.println(etmPackageWrapper.getHeader().toString());
        System.out.println(etmPackageWrapper.getBody().toString());

    }




}

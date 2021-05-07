package com.enableets.edu.sdk.ppr;

import org.junit.Before;
import org.junit.Test;

import com.enableets.edu.sdk.ppr.adapter.PaperServiceAdapter;
import com.enableets.edu.sdk.ppr.configuration.Configuration;
import com.enableets.edu.sdk.ppr.exceptions.PPRVersionMismatchException;
import com.enableets.edu.sdk.ppr.ppr.bo.PPRInfoBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.AnswerCardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.card.CardBO;
import com.enableets.edu.sdk.ppr.ppr.bo.ppr.PPRBO;
import com.enableets.edu.sdk.ppr.ppr.builder.IPPRBuilderHandler;
import com.enableets.edu.sdk.ppr.ppr.builder.content.IContent;
import com.enableets.edu.sdk.ppr.ppr.builder.content.PPRFileContent;
import com.enableets.edu.sdk.ppr.ppr.core.PPR;
import com.enableets.edu.sdk.ppr.ppr.parse.IPPRParseHandler;
import com.enableets.edu.sdk.ppr.ppr.parse.resources.UrlPPRParseResource;

import java.io.File;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/28
 **/
public class PprHandlerFactoryTest {

    private IPprHandlerFactory pprHandlerFactory;

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration().setPPRTempPath("C:\\Users\\xuechuang\\Desktop\\testppr")
                .setServerAddress("http://192.168.116.190")
                .setClientId("wiedu_application_key").setClientSecret("wiedu_application_secret");
        pprHandlerFactory = new PprHandlerFactory(configuration);
    }

   // @Test
    public void testBuildPPR() {
        PPRBO pprbo = PaperServiceAdapter.get("830342887647514624", configuration);
        AnswerCardBO answerCard = PaperServiceAdapter.getAnswerCardAxis("830342887647514624", "820991207177035776", configuration);
        IPPRBuilderHandler pprBuilderHandler = pprHandlerFactory.getPprBuilderHandler();
        PPRInfoBO pprInfoBO = new PPRInfoBO();
        pprInfoBO.setPprBO(pprbo);
        pprInfoBO.setCardBO(new CardBO(answerCard));
        PPRFileContent pprFileContent = new PPRFileContent(pprInfoBO);
        PPR ppr = pprBuilderHandler.build(pprFileContent);
        System.out.println(ppr.getDownloadUrl());
    }

   // @Test
    public void testParsePPR(){
        IPPRParseHandler pprParseHandler = pprHandlerFactory.getPprParseHandler();
        try {
            PPRInfoBO pprInfoBO = pprParseHandler.parse(new UrlPPRParseResource("http://192.168.116.190/microservice/storageservice/v1/files/download/2020/10/29/ed709bf5c275471ab4a233c2bc65131c.ppr"));
            System.out.println(pprInfoBO.getPprBO().getName());
        } catch (PPRVersionMismatchException e) {
            e.printStackTrace();
        }
    }

    //@Test
    public void getFileName(){
        String str = "http://192.168.116.190/microservice/storageservice/v1/files/download/2020/10/29/51d8c1ce23ad4bcab5682175f06d10d6.ppr";
        File file = new File(str);
        System.out.println(file.getName().substring(0, file.getName().lastIndexOf("?")));
    }
}
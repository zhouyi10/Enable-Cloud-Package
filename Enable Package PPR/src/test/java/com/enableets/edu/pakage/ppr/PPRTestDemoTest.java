package com.enableets.edu.pakage.ppr;

import com.enableets.edu.pakage.card.bean.EnableCardBeanDefinition;
import com.enableets.edu.pakage.card.bo.CardAxisBO;
import com.enableets.edu.pakage.core.core.Configuration;
import com.enableets.edu.pakage.core.source.FileIdSource;
import com.enableets.edu.pakage.core.source.PPRIdSource;
import com.enableets.edu.pakage.core.source.URLSource;
import com.enableets.edu.pakage.ppr.bean.EnablePPR;
import com.enableets.edu.pakage.ppr.bean.EnablePPRBeanDefinition;
import com.enableets.edu.pakage.ppr.bo.*;
import com.enableets.edu.pakage.ppr.factory.EnablePPRPackageFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/19
 **/
public class PPRTestDemoTest {

    private Configuration configuration;

    @Before
    public void setUp() throws Exception {
        configuration = new Configuration().setClientId("wiedu_application_key").setClientSecret("wiedu_application_secret").setPPRTempPath("C:\\Users\\xuechuang\\Desktop\\testppr").setServerAddress("http://192.168.116.190");
    }

    //@Test
    public void testBuildPPR(){
        //1、Build the .ppr business object
        EnablePPRBeanDefinition pprBeanDefinition = new EnablePPRBeanDefinition();
        pprBeanDefinition.setPaperId("122333445566");
        pprBeanDefinition.setName("Test Paper");
        pprBeanDefinition.setStage(new CodeNameMapBO("3", "初中"));
        pprBeanDefinition.setGrade(new CodeNameMapBO("3101", "七年级"));
        pprBeanDefinition.setSubject(new CodeNameMapBO("14", "数学"));
        pprBeanDefinition.setTotalPoints(110f);
        pprBeanDefinition.setAnswerCostTime(100L);
        pprBeanDefinition.setUser(new IdNameMapBO("11111111", "Tom"));
        pprBeanDefinition.setCreateTime(new Date());
        pprBeanDefinition.setFiles(null);
        List<NodeInfoBO> nodes = new ArrayList<>();
        NodeInfoBO node = new NodeInfoBO();
        node.setNodeId("node_01");
        node.setParentNodeId(null);
        node.setName("node");
        node.setDescription("node");
        node.setLevel(1);
        node.setInternalNo(1);
        node.setExternalNo("一");
        node.setPoints(100f);
        node.setQuestion(null);
        nodes.add(node);
        NodeInfoBO section = new NodeInfoBO();
        section.setNodeId("section_01");
        section.setParentNodeId("node_01");
        section.setName("section");
        section.setDescription("section");
        section.setLevel(2);
        section.setInternalNo(1);
        section.setExternalNo("1");
        section.setPoints(100f);
        section.setQuestion(null);
        nodes.add(section);
        NodeInfoBO question = new NodeInfoBO();
        question.setNodeId("question_01");
        question.setParentNodeId("section_01");
        question.setName("question");
        question.setDescription("question");
        question.setLevel(3);
        question.setInternalNo(1);
        question.setExternalNo("1");
        question.setPoints(100f);
        QuestionInfoBO ques = new QuestionInfoBO();
        ques.setQuestionId("q_01");
        ques.setParentId(null);
        ques.setType(new CodeNameMapBO("01", "单选题"));
        ques.setStage(new CodeNameMapBO("3", "初中"));
        ques.setGrade(new CodeNameMapBO("3101", "七年级"));
        ques.setSubject(new CodeNameMapBO("14", "数学"));
        ques.setDifficulty(new CodeNameMapBO("1", "容易"));
        ques.setPoints(10f);
        ques.setStem(new QuestionStemInfoBO("题目", "题目"));
        QuestionAnswerInfoBO answer = new QuestionAnswerInfoBO();
        answer.setLabel("A&B&C");
        answer.setStrategy("A&B&C");
        answer.setAnalysis("A&B&C");
        ques.setAnswer(answer);
        ques.setAffixId("http://192.168.116.190/microservice/storageservice/v1/files/download/2020/10/13/b329587088954b3085b4db0a6ce4e082.jpg");
        ques.setQuestionNo("Q_NO");
        ques.setOptions(null);
        ques.setAxises(null);
        List<QuestionKnowledgeInfoBO> knowledges = new ArrayList<>();
        QuestionKnowledgeInfoBO knowledge = new QuestionKnowledgeInfoBO();
        knowledge.setKnowledgeId("1111");
        knowledge.setKnowledgeName("知识点1");
        knowledges.add(knowledge);
        ques.setKnowledges(knowledges);
        question.setQuestion(ques);
        nodes.add(question);
        pprBeanDefinition.setNodes(nodes);


        EnableCardBeanDefinition cardBeanDefinition = new EnableCardBeanDefinition();
        cardBeanDefinition.setAnswerCardId("card_0001");
        cardBeanDefinition.setExamId("111111111");
        cardBeanDefinition.setColumnType(2);
        cardBeanDefinition.setCandidateNumberEdition(1);
        cardBeanDefinition.setPageType("A4");
        cardBeanDefinition.setPageCount(2);

        List<CardAxisBO> axises = new ArrayList<>();
        CardAxisBO cardAxis = new CardAxisBO();
        cardAxis.setAxisId("ax_01");
        cardAxis.setAnswerCardId("card_0001");
        cardAxis.setQuestionId("q_01");
        cardAxis.setParentId("q_01");
        cardAxis.setSequencing(1L);
        cardAxis.setxAxis(100d);
        cardAxis.setyAxis(100d);
        cardAxis.setWidth(100d);
        cardAxis.setHeight(100d);
        cardAxis.setPageNo(1L);
        cardAxis.setTypeCode("01");
        cardAxis.setTypeName("单选");
        cardAxis.setOptionCount(4L);
        cardAxis.setRowCount(4L);
        axises.add(cardAxis);

        CardAxisBO cardAxis2 = new CardAxisBO();
        cardAxis2.setAxisId("ax_03");
        cardAxis2.setAnswerCardId("card_0001");
        cardAxis2.setQuestionId("q_03");
        cardAxis2.setParentId("q_02");
        cardAxis2.setSequencing(2L);
        cardAxis2.setxAxis(100d);
        cardAxis2.setyAxis(100d);
        cardAxis2.setWidth(100d);
        cardAxis2.setHeight(100d);
        cardAxis2.setPageNo(1L);
        cardAxis2.setTypeCode("01");
        cardAxis2.setTypeName("单选");
        cardAxis2.setOptionCount(4L);
        cardAxis2.setRowCount(4L);
        axises.add(cardAxis2);

        CardAxisBO cardAxis3 = new CardAxisBO();
        cardAxis3.setAxisId("ax_03");
        cardAxis3.setAnswerCardId("card_0001");
        cardAxis3.setQuestionId("q_04");
        cardAxis3.setParentId("q_02");
        cardAxis3.setSequencing(3L);
        cardAxis3.setxAxis(100d);
        cardAxis3.setyAxis(100d);
        cardAxis3.setWidth(100d);
        cardAxis3.setHeight(100d);
        cardAxis3.setPageNo(1L);
        cardAxis3.setTypeCode("02");
        cardAxis3.setTypeName("填空题");
        cardAxis3.setOptionCount(0L);
        cardAxis3.setRowCount(4L);
        axises.add(cardAxis3);

        CardAxisBO cardAxis4 = new CardAxisBO();
        cardAxis4.setAxisId("ax_03");
        cardAxis4.setAnswerCardId("card_0001");
        cardAxis4.setQuestionId("q_04");
        cardAxis4.setParentId("");
        cardAxis4.setSequencing(4L);
        cardAxis4.setxAxis(100d);
        cardAxis4.setyAxis(100d);
        cardAxis4.setWidth(100d);
        cardAxis4.setHeight(100d);
        cardAxis4.setPageNo(1L);
        cardAxis4.setTypeCode("02");
        cardAxis4.setTypeName("填空题");
        cardAxis4.setOptionCount(0L);
        cardAxis4.setRowCount(4L);
        axises.add(cardAxis4);

        cardBeanDefinition.setAxises(axises);

        //EnableCard enableCard = new EnableCard(cardBeanDefinition, configuration);
        // ------------Core code------------------
        //2、Build ppr Package Wrapper (Context, business Object, null)
        //PPRPackageWrapper pprPackageWrapper = new PPRPackageWrapper(configuration, pprBeanDefinition, enableCard);

        EnablePPR ppr = new EnablePPR(pprBeanDefinition,cardBeanDefinition,configuration);
        ppr.build();
        //3、Build ppr Lifecycle instance(Hold ppr wrapper)
        //PPRPackageLifecycle packageLifecycle = new PPRPackageLifecycle(pprPackageWrapper);
        //4、build
        //packageLifecycle.build();
        //  -----------------
        //5、print
        //System.out.println(pprPackageWrapper.getPackageFileInfo().getDownloadUrl());
        //System.out.println(pprPackageWrapper.getPackageFileInfo().getFileId());
    }


    //@Test
    public void testFactory(){
        //IEnablePackageFactory factory = new EnablePPRPackageFactory();
        EnablePPRPackageFactory factory = new EnablePPRPackageFactory(configuration);
        URLSource source = new URLSource("http://xxxxxxx");
        FileIdSource fileSource = new FileIdSource("cdba94cb85344fc891a7b4e538dd9f63");
        PPRIdSource idsource = new PPRIdSource("45464135431548541");
        EnablePPR ppr = factory.getPackage(fileSource);

    }
}
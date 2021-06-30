package com.enableets.edu.sdk.pakage.annotation;

import com.enableets.edu.sdk.pakage.assessment.feign.IAssessmentActionFlowServiceFeignClient;
import com.enableets.edu.sdk.pakage.assessment.impl.DefaultAssessmentActionFlowService;
import com.enableets.edu.sdk.pakage.assessment.service.IAssessmentActionFlowService;
import com.enableets.edu.sdk.pakage.book.feign.IBookInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.book.impl.DefaultBookInfoService;
import com.enableets.edu.sdk.pakage.book.service.IBookPackageService;
import com.enableets.edu.sdk.pakage.coursepackage.feign.ICoursePackagePlanFeignClient;
import com.enableets.edu.sdk.pakage.coursepackage.feign.ICoursePackageV2FeignClient;
import com.enableets.edu.sdk.pakage.coursepackage.impl.DefaultCoursePackagePlanService;
import com.enableets.edu.sdk.pakage.coursepackage.impl.DefaultCoursePackageV2Service;
import com.enableets.edu.sdk.pakage.coursepackage.service.ICoursePackagePlanService;
import com.enableets.edu.sdk.pakage.coursepackage.service.ICoursePackageV2Service;
import com.enableets.edu.sdk.pakage.etm.feign.IETMInfoServiceFeignClient;
import com.enableets.edu.sdk.pakage.etm.impl.DefaultETMInfoService;
import com.enableets.edu.sdk.pakage.etm.service.IETMInfoService;
import com.enableets.edu.sdk.pakage.ppr.feign.*;
import com.enableets.edu.sdk.pakage.ppr.impl.*;
import com.enableets.edu.sdk.pakage.ppr.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = {"com.enableets.edu.sdk.pakage.microcourse.feign", "com.enableets.edu.sdk.pakage.ppr.feign", "com.enableets.edu.sdk.pakage.etm.feign", "com.enableets.edu.sdk.pakage.book.feign","com.enableets.edu.sdk.pakage.assessment.feign","com.enableets.edu.sdk.pakage.coursepackage.feign"})
public class EnablePackageServiceSDKConfiguration {

    @Autowired
    private IPPRPaperInfoServiceFeignClient pprPaperInfoServiceFeignClient;

    @Autowired
    private IPPRTestInfoServiceFeignClient pprTestInfoServiceFeignClient;

    @Autowired
    private IPPRTestInfoUserServiceFeignClient pprTestInfoUserServiceFeignClient;

    @Autowired
    private IPPRInfoServiceFeignClient pprInfoServiceFeignClient;

    @Autowired
    private IPPRAnswerCardInfoServiceFeignClient pprAnswerCardInfoServiceFeignClient;

    @Autowired
    private IETMInfoServiceFeignClient etmInfoServiceFeignClient;

    @Autowired
    private IBookInfoServiceFeignClient bookInfoServiceFeignClient;

    @Autowired
    private IAssessmentActionFlowServiceFeignClient assessmentActionFlowServiceFeignClient;

    @Autowired
    private IPPRAnswerInfoServiceFeignClient pprAnswerInfoServiceFeignClient;

    @Autowired
    private IPPRXKWInfoServiceFeignClient pprXKWInfoServiceFeignClient;

    @Autowired
    private ICoursePackageV2FeignClient coursePackageV2FeignClient;

    @Autowired
    private ICoursePackagePlanFeignClient coursePackagePlanFeignClient;

    @Autowired
    private IPPRErrorQuestionInfoServiceFeignClient errorQuestionInfoServiceFeignClient;

    @Bean(name = "pprPaperInfoServiceSDK")
    public IPPRPaperInfoService pprPaperInfoServiceSDK() {
        return new DefaultPPRPaperInfoService(pprPaperInfoServiceFeignClient);
    }

    @Bean(name = "pprTestInfoServiceSDK")
    public IPPRTestInfoService pprTestInfoServiceSDK() {
        return new DefaultPPRTestInfoService(pprTestInfoServiceFeignClient);
    }

    @Bean(name = "pprTestUserInfoServiceSDK")
    public IPPRTestUserInfoService pprTestUserInfoServiceSDK() {
        return new DefaultPPRTestUserInfoService(pprTestInfoUserServiceFeignClient);
    }

    @Bean(name = "pprInfoServiceSDK")
    public IPPRInfoService pprInfoServiceSDK() {
        return new DefaultPPRInfoService(pprInfoServiceFeignClient);
    }

    @Bean(name = "pprAnswerCardServiceSDK")
    public IPPRAnswerCardInfoService pprAnswerCardServiceSDK() {
        return new DefaultPPRAnswerCardInfoService(pprAnswerCardInfoServiceFeignClient);
    }

    @Bean(name = "etmInfoServiceSDK")
    public IETMInfoService etmInfoServiceSDK() {
        return new DefaultETMInfoService(etmInfoServiceFeignClient);
    }

    @Bean(name = "bookPackageServiceSDK")
    public IBookPackageService bookPackageServiceSDK() {
        return new DefaultBookInfoService(bookInfoServiceFeignClient);
    }

    @Bean(name = "assessmentActionFlowServiceSDK")
    public IAssessmentActionFlowService assessmentActionFlowServiceSDK() {
        return new DefaultAssessmentActionFlowService(assessmentActionFlowServiceFeignClient);
    }

    @Bean(name = "pprAnswerInfoServiceSDK")
    public IPPRAnswerInfoService pprAnswerInfoServiceSDK(){
        return new DefaultPPRAnswerInfoService(pprAnswerInfoServiceFeignClient);
    }

    @Bean(name = "pprXKWInfoServiceSDK")
    public IPPRXKWInfoService pprXKWInfoService(){
        return new DefaultPPRXKWInfoService(pprXKWInfoServiceFeignClient);
    }

    @Bean(name = "coursePackageV2ServiceSDK")
    public ICoursePackageV2Service coursePackageV2ServiceSDK() {
        return  new DefaultCoursePackageV2Service(coursePackageV2FeignClient);
    }

    @Bean(name = "coursePackagePlanServiceSDK")
    public ICoursePackagePlanService coursePackagePlanServiceSDK(){
        return  new DefaultCoursePackagePlanService(coursePackagePlanFeignClient);
    }

    @Bean(name = "errorQuestionInfoServiceSDK")
    public IPPRErrorQuestionInfoService errorQuestionInfoServiceSDK() {
        return new DefaultPPRErrorQuestionInfoService(errorQuestionInfoServiceFeignClient);
    }

}

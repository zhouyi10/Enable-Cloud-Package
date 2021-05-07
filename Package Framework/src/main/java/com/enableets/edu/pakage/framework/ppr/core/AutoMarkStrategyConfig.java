package com.enableets.edu.pakage.framework.ppr.core;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import com.enableets.edu.framework.core.util.SpringBeanUtils;

import java.util.Arrays;

/**
 * @author duffy_ding
 * @since 2019/12/16
 */
@Configuration
@EnableConfigurationProperties(AutoMarkStrategyConfig.AutoMarkStrategyProperties.class)
public class AutoMarkStrategyConfig {

    /**
     * whether it can be automatically reviewed
     * @param type type
     * @return true/false
     */
    public static boolean canAutoMark(String type) {
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getAllType());
    }

    /**
     * whether it is a type of single-choose question
     * @param type type
     * @return true/false
     */
    public static boolean isSingleChoose(String type) {
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getSingleChooseQuestionType());
    }

    /**
     * whether it is a type of multi-choose question
     * @param type type
     * @return true/false
     */
    public static boolean isMultiChoose(String type) {
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getMultiChooseQuestionType());
    }

    /**
     * whether it is a type of judgment question
     * @param type type
     * @return true/false
     */
    public static boolean isJudge(String type) {
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getJudgeQuestionType());
    }

    /**
     * whether it is a type of drag question
     * @param type type
     * @return true/false
     */
    public static boolean isDrag(String type) {
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getDragQuestionType());
    }

    /**
     * whether it is a type of collect line question
     * @param type type
     * @return true/false
     */
    public static boolean isCollectLine(String type){
        return match(type, SpringBeanUtils.getBean(AutoMarkStrategyProperties.class).getCollectLineQuestionType());
    }

    /**
     * determine if the target is in an array
     * @param target target
     * @param arr array
     * @return true/false
     */
    private static boolean match(String target, String[] arr) {
        if (StringUtils.isBlank(target) || arr == null || arr.length == 0) {
            return Boolean.FALSE;
        }
        return Arrays.asList(arr).contains(target);
    }

    /**
     * 自动批阅 题型 配置
     */
    @ConfigurationProperties(prefix = "auto-mark")
    public static class AutoMarkStrategyProperties {

        private String[] allType;

        private String[] singleChooseQuestionType;

        private String[] multiChooseQuestionType;

        private String[] judgeQuestionType;

        private String[] DragQuestionType;

        private String[] collectLineQuestionType;

        public String[] getAllType() {
            return allType;
        }

        public void setAllType(String[] allType) {
            this.allType = allType;
        }

        public String[] getSingleChooseQuestionType() {
            return singleChooseQuestionType;
        }

        public void setSingleChooseQuestionType(String[] singleChooseQuestionType) {
            this.singleChooseQuestionType = singleChooseQuestionType;
        }

        public String[] getMultiChooseQuestionType() {
            return multiChooseQuestionType;
        }

        public void setMultiChooseQuestionType(String[] multiChooseQuestionType) {
            this.multiChooseQuestionType = multiChooseQuestionType;
        }

        public String[] getJudgeQuestionType() {
            return judgeQuestionType;
        }

        public void setJudgeQuestionType(String[] judgeQuestionType) {
            this.judgeQuestionType = judgeQuestionType;
        }

        public String[] getDragQuestionType() {
            return DragQuestionType;
        }

        public void setDragQuestionType(String[] dragQuestionType) {
            DragQuestionType = dragQuestionType;
        }

        public String[] getCollectLineQuestionType() {
            return collectLineQuestionType;
        }

        public void setCollectLineQuestionType(String[] collectLineQuestionType) {
            this.collectLineQuestionType = collectLineQuestionType;
        }
    }
}

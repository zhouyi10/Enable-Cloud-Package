package com.enableets.edu.pakage.ppr.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
public class QuestionKnowledgeInfoBO {

    public QuestionKnowledgeInfoBO(){}

    public QuestionKnowledgeInfoBO(String knowledgeId, String knowledgeName){
        this.knowledgeId = knowledgeId;
        this.knowledgeName = knowledgeName;
    }

    /**
     * knowledge point id
     */
    private String knowledgeId;

    /**
     * knowledge point name
     */

    private String knowledgeName;

}

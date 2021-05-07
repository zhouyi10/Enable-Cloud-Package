package com.enableets.edu.sdk.ppr.ppr.bo.ppr;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/06/16
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KnowledgeInfoBO {

    /**
     * knowledge point id
     */
    private String knowledgeId;

    /**
     * knowledge point name
     */

    private String knowledgeName;

}

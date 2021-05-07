package com.enableets.edu.sdk.pakage.ppr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/30
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddPPRInfoDTO{

    /** Paper Name */
    private String name;

    /** Stage Info  */
    private CodeNameMapDTO stage;

    /** Grade Info  */
    private CodeNameMapDTO grade;

    /** Subject Info  */
    private CodeNameMapDTO subject;

    /** Total Score */
    private Float totalPoints;

    /** Test paper node information  */
    private List<PPRNodeInfoDTO> nodes;

    /** User Info  */
    private IdNameMapDTO user;

    /** School Info  */
    private IdNameMapDTO school;

    /** Provider Code */
    private String providerCode;

    /** Textbook version*/
    private IdNameMapDTO materialVersion;

    /** Resource ID */
    private String contentId;

    /** Zone Info  */
    private IdNameMapDTO zone;

    /** Knowledge Info  */
    private List<KnowledgeInfoDTO> knowledges;

    /** Files */
    private List<PPRFileInfoDTO> files;

    /** Answer Card*/
    private AddAnswerCardInfoDTO answerCard;
}

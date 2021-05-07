package com.enableets.edu.pakage.microservice.ppr.vo;

import com.enableets.edu.framework.core.dao.BaseDao;
import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.enableets.edu.pakage.framework.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/10/23
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AddPPRInfoVO", description = "Add PPR")
public class AddPPRInfoVO extends BaseVO {

    @Override
    public void validate() throws MicroServiceException {

    }

    /** Paper Name */
    @ApiModelProperty(value = "Paper Name", required = false)
    private String name;

    /** Stage Info  */
    @ApiModelProperty(value = "Stage Info", required = false)
    private CodeNameMapVO stage;

    /** Grade Info  */
    @ApiModelProperty(value = "Grade Info", required = false)
    private CodeNameMapVO grade;

    /** Subject Info  */
    @ApiModelProperty(value = "Subject Info", required = false)
    private CodeNameMapVO subject;

    /** Total Score */
    @ApiModelProperty(value = "Total Score", required = false)
    private Float totalPoints;

    /** Test paper node information  */
    @ApiModelProperty(value = "Test paper node information", required = false)
    private List<PPRNodeInfoVO> nodes;

    /** User Info  */
    @ApiModelProperty(value = "User Info", required = false)
    private IdNameMapVO user;

    /** School Info  */
    @ApiModelProperty(value = "School Info", required = false)
    private IdNameMapVO school;

    /** Provider Code */
    @ApiModelProperty(value = "Provider Code", required = false)
    private String providerCode;

    /** Textbook version*/
    @ApiModelProperty(value = "Textbook version", required = false)
    private IdNameMapBO materialVersion;

    /** Resource ID */
    @ApiModelProperty(value = "Resource ID", required = false)
    private String contentId;

    /** Zone Info  */
    @ApiModelProperty(value = "Zone Info", required = false)
    private CodeNameMapVO zone;

    /** Knowledge Info  */
    @ApiModelProperty(value = "Knowledge Info", required = false)
    private List<KnowledgeInfoVO> knowledges;

    /** Files */
    @ApiModelProperty(value = "File Info", required = false)
    private List<PPRFileInfoVO> files;

    /** Answer Card*/
    @ApiModelProperty(value = "Answer Card Info", required = true)
    private AddAnswerCardInfoVO answerCard;
}

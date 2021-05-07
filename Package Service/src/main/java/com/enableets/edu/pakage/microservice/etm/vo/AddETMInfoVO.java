package com.enableets.edu.pakage.microservice.etm.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AddETMInfoVO", description = "Add ETM")
public class AddETMInfoVO extends BaseVO {

    @Override
    public void validate() throws MicroServiceException {

    }

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

    private CodeNameMapVO stage;

    private CodeNameMapVO grade;

    private CodeNameMapVO subject;

    private CodeNameMapVO textBookVersion;

    private CodeNameMapVO term;

    private IdNameMapVO user;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private String providerCode;

    private String contentId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageVO> pageList;


}

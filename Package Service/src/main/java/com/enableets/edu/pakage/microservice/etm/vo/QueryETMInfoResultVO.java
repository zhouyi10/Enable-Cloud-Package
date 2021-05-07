package com.enableets.edu.pakage.microservice.etm.vo;

import com.enableets.edu.sdk.paper.dto.CodeNameMapDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "QueryETMInfoResultVO", description = "Query ETM Info")
public class QueryETMInfoResultVO {

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

    private CodeNameMapVO stage;

    private CodeNameMapVO grade;

    private CodeNameMapVO subject;

    private CodeNameMapVO textBookVersion;

    private CodeNameMapDTO term;

    private IdNameMapVO user;

    private String semester;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private Long contentId;

    private List<PageVO> pageList;
}

package com.enableets.edu.sdk.pakage.etm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryETMInfoResultDTO {

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

    private CodeNameMapDTO stage;

    private CodeNameMapDTO grade;

    private CodeNameMapDTO subject;

    private CodeNameMapDTO textBookVersion;

    private CodeNameMapDTO term;

    private IdNameMapDTO user;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private Long contentId;

    private String providerCode;

    private String creator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creatTime;

    private String updator;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    private List<PageDTO> pageList;
}

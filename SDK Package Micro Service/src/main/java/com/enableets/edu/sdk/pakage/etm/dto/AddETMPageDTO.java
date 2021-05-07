package com.enableets.edu.sdk.pakage.etm.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddETMPageDTO {

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

    private String providerCode;

    private String contentId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private PageDTO page;
}

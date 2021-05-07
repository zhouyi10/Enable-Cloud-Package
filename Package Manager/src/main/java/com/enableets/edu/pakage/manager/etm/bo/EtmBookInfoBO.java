package com.enableets.edu.pakage.manager.etm.bo;

import com.enableets.edu.pakage.manager.bo.CodeNameMapBO;
import com.enableets.edu.pakage.manager.bo.IdNameMapBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EtmBookInfoBO {

    private  String etmBookId;

    private  String textBookName;

    private  String isbn;

   private CodeNameMapBO stage;

    private CodeNameMapBO grade;

    private CodeNameMapBO subject;

    private CodeNameMapBO textBookVersion;

    private CodeNameMapBO term;

    private IdNameMapBO user;

    private String coverId;

    private String coverUrl;

    private String coverImgName;

    private Long contentId;

    private String providerCode;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageBO> pageList;

}

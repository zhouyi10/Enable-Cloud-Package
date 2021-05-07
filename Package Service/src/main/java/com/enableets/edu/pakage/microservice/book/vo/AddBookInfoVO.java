package com.enableets.edu.pakage.microservice.book.vo;

import com.enableets.edu.module.service.core.BaseVO;
import com.enableets.edu.module.service.core.MicroServiceException;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(value = "AddBookInfoVO", description = "Add Book")
public class AddBookInfoVO extends BaseVO {

    @Override
    public void validate() throws MicroServiceException {

    }

    private  Long bookId;

    private  String bookName;

    private  String isbn;

    private CodeNameMapVO stage;

    private CodeNameMapVO grade;

    private CodeNameMapVO subject;

    private CodeNameMapVO textBookVersion;

    private CodeNameMapVO term;

    private IdNameMapVO user;

    private FileInfoVO cover;

    private Long contentId;

    private String creator;

    private Date creatTime;

    private String updator;

    private Date updateTime;

    private List<PageVO> pageList;


}

package com.enableets.edu.pakage.book.bean;

import com.enableets.edu.pakage.book.bo.CodeNameMapBO;
import com.enableets.edu.pakage.book.bo.FileInfoBO;
import com.enableets.edu.pakage.book.bo.IdNameMapBO;
import com.enableets.edu.pakage.book.bo.PageNodeInfoBO;
import com.enableets.edu.pakage.core.source.PackageSource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnableBookBeanDefinition extends PackageSource {


    /** book id  */
    private String bookId;

    /** book Name */
    private String bookName;

    /** ISBN */
    private String isbn;

    /** cover*/
    private FileInfoBO cover;

    /** Stage Info */
    private CodeNameMapBO stage;

    /** Grade Info */
    private CodeNameMapBO grade;

    /** Subject Info */
    private CodeNameMapBO subject;

    /** textBookVersion Info */
    private CodeNameMapBO textBookVersion;

    /** term Info */
    private CodeNameMapBO term;

    /** User Info */
    private IdNameMapBO user;

    /** Resource ID */
    private Long contentId;

    /** creator*/
    private String creator;

    /** Create Time*/
    private Date createTime;

    /** updator*/
    private String updator;

    /** update Time*/
    private Date updateTime;

    /** page node information */
    private List<PageNodeInfoBO> nodes;

    @Override
    public String getId() {
        return getBookId();
    }
}

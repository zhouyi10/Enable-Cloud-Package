package com.enableets.edu.pakage.etm.bean;

import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.etm.bo.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnableETMBeanDefinition extends PackageSource {

    /** etmbook id  */
    private String bookId;

    /** book Name */
    private String textBookName;

    /** ISBN */
    private String isbn;

    /** cover */
    private String cover;

    private String coverUrl;

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

    /** page node information */
    private List<PageNodeInfoBO>  nodes;

    @Override
    public String getId() {
        return getBookId();
    }


}

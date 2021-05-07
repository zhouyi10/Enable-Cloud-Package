package com.enableets.edu.pakage.ppr.bean;

import com.enableets.edu.pakage.core.source.PackageSource;
import com.enableets.edu.pakage.ppr.bo.CodeNameMapBO;
import com.enableets.edu.pakage.ppr.bo.FileInfoBO;
import com.enableets.edu.pakage.ppr.bo.IdNameMapBO;
import com.enableets.edu.pakage.ppr.bo.NodeInfoBO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.List;
import lombok.Data;

/**
 * PPR Service Entity
 * @author walle_yu@enable-ets.com
 * @since 2020/11/16
 **/
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EnablePPRBeanDefinition extends PackageSource {

    private String paperId;

    private String name;

    private CodeNameMapBO stage;

    private CodeNameMapBO grade;

    private CodeNameMapBO subject;

    private Float totalPoints;

    private Long answerCostTime;

    private IdNameMapBO user;

    private Date createTime;

    private List<FileInfoBO> files;

    private List<NodeInfoBO> nodes;

    @Override
    public String getId() {
        return getPaperId();
    }
}

package com.enableets.edu.pakage.manager.mark.bo;

import lombok.Data;

import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/23
 **/
@Data
public class MarkingCacheBO implements java.io.Serializable{

    private String questionIds;

    private List<String> userSearchCode;
}

package com.enableets.edu.pakage.framework.ppr.test.bo;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import java.util.Date;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2021/05/14
 **/
@Data
@Accessors(chain = true)
public class TestActionFlowBO {

    private String testId;

    private String sender;

    private List<String> recipientList;

    private Date startTime;

    private Date endTime;
}

package com.enableets.edu.pakage.manager.mark.bo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/05/08
 **/
@Data
public class UserAnswerCardBO {

    private String userId;

    private String userName;

    private Date updateTime;

    private Date createTime;

    private List<UserAnswerCardImgBO> answerCardList;
}

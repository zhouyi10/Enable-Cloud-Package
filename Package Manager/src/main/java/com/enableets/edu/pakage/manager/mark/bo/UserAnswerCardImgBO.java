package com.enableets.edu.pakage.manager.mark.bo;

import lombok.Data;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/05/08
 **/
@Data
public class UserAnswerCardImgBO {

    /* 答题卡原始图片下载地址 */
    private String originalUrl;

    /* 答题卡原始图片文件标识 */
    private String originalFileId;

    /* 答题卡校正后图片下载地址 */
    private String url;

    /* 答题卡校正后图片文件标识 */
    private String fileId;

    /* 答题卡页码 */
    private String pageNo;
}

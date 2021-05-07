package com.enableets.edu.pakage.framework.ppr.test.service.submit.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/07/01
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanvasBO {

    private String fileId;

    private String fileName;

    private String md5;

    private String url;
}

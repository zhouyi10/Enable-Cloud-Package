package com.enableets.edu.pakage.card.bo.answer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author walle_yu@enable-ets.com
 * @since 2020/11/20
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CanvasBO {

    private String fileId;

    private String fileName;

    private String fileExt;

    private String md5;

    private String url;
}

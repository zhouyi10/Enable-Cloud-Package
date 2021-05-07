package com.enableets.edu.sdk.ppr.ppr.parse.resources;

import cn.hutool.crypto.SecureUtil;
import com.enableets.edu.sdk.ppr.utils.Utils;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.parse.exceptions.PPRParserResourceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Date 2020/07/01$ 17:21$
 * @Author caleb_liu@enable-ets.com
 **/

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UrlPPRParseResource extends ZipPPRParseResource{

    protected String pprDownloadUrl;

    protected String pprZipPathPrefix = Constants.URL_PPR_ZIP_PATH_PREFIX;

    public UrlPPRParseResource(String pprDownloadUrl) {
        this.pprDownloadUrl = pprDownloadUrl;
    }

    @Override
    public void setPprZipPath() {
        pprDownloadUrl = getPprDownloadUrl();
        String filePath = configuration.getPPRTempPath() + getPprZipPathPrefix() + SecureUtil.md5(pprDownloadUrl) + "/" + Utils.getRandom() + Constants.PPR_ZIP_SUFFIX;
        try {
            Utils.downloadFile(pprDownloadUrl, filePath);
        } catch (Exception e) {
            throw new PPRParserResourceException(String.format("pprDownLoadUrl: [%s] downloadFile error! error message: [%s]", pprDownloadUrl), e);
        }
        pprZipPath = filePath;
    }
}

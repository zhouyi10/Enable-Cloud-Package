package com.enableets.edu.sdk.ppr.ppr.parse.resources;

import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.adapter.FileStorageAdapter;
import com.enableets.edu.sdk.ppr.ppr.core.Constants;
import com.enableets.edu.sdk.ppr.ppr.parse.exceptions.PPRParserResourceException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.File;

/**
 * @Date 2020/07/01$ 17:32$
 * @Author caleb_liu@enable-ets.com
 **/

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileIdPPRParseResource extends UrlPPRParseResource {

    private String pprZipPathPrefix = Constants.FILE_ID_PPR_ZIP_PATH_PREFIX;

    private String pprFileId;

    public FileIdPPRParseResource(String pprFileId) {
        setPprFileId(pprFileId);
    }

    @Override
    public String getPprDownloadUrl() {
        String downloadUrl = null;
        try {
            downloadUrl = FileStorageAdapter.getDownloadUrl(pprFileId, this.configuration);
            if (StringUtils.isBlank(downloadUrl)) throw new PPRParserResourceException("downloadUrl cannot be empty!");
        } catch (Exception e) {
            throw new PPRParserResourceException(String.format("pprFileId: [%s] getDownloadUrl error! error message: [%s]", pprFileId, e.getMessage()), e);
        }
        return downloadUrl;
    }

    @Override
    public String getPprZipPathPrefix() {
        return this.pprZipPathPrefix + this.pprFileId + File.separator;
    }
}

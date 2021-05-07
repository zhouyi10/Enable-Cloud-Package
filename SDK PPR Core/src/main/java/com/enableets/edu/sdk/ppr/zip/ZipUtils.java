package com.enableets.edu.sdk.ppr.zip;

import cn.hutool.core.util.ZipUtil;
import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author caleb_liu@enable-ets.com
 * @date 2020/06/16 10:52
 */

public class ZipUtils {

    public static void unZip(String filePath) {
        ZipUtil.unzip(filePath, StandardCharsets.UTF_8);
    }

    public static File zip(String folderPath) {
        return ZipUtil.zip(folderPath, StandardCharsets.UTF_8);
    }

    public static File zip(String folderPath, String zipPath){
        return ZipUtil.zip(folderPath, zipPath, StandardCharsets.UTF_8, false);
    }

}

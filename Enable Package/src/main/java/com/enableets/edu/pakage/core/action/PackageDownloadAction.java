package com.enableets.edu.pakage.core.action;

import com.enableets.edu.pakage.adapter.FileStorageAdapter;
import com.enableets.edu.pakage.core.bean.AbstractEnablePackage;
import com.enableets.edu.pakage.core.bean.PackageFileInfo;
import com.enableets.edu.pakage.core.core.Configuration;

import cn.hutool.http.HttpUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * 提供下载行为实现
 */
public class PackageDownloadAction extends AbstractPackageAction {

    public PackageDownloadAction(AbstractEnablePackage enablePackage, Configuration configuration) {
        super(enablePackage, configuration);
    }

    @Override
    public String getActionName() {
        return null;
    }

    @Override
    public Object execute() {
        PackageFileInfo packageFileInfo = this.getEnablePackage().getPackageFileInfo();
        if (packageFileInfo == null) return null;
        if (StringUtils.isNotEmpty(packageFileInfo.getLocalZipPath())) return packageFileInfo.getLocalZipPath();
        if (StringUtils.isNotBlank(packageFileInfo.getFileId()) && StringUtils.isBlank(packageFileInfo.getDownloadUrl())) {
            packageFileInfo = FileStorageAdapter.get(packageFileInfo.getFileId(), this.getConfiguration());
            this.getEnablePackage().setPackageFileInfo(packageFileInfo);
        }
        if (StringUtils.isNotBlank(packageFileInfo.getDownloadUrl())) {
            String destPath = new StringBuilder(this.getConfiguration().getPPRTempPath()).append("/").append(packageFileInfo.getDownloadUrl().substring(packageFileInfo.getDownloadUrl().lastIndexOf("/") + 1)).toString();
            HttpUtil.downloadFile(packageFileInfo.getDownloadUrl(), destPath);
            packageFileInfo.setLocalZipPath(destPath);
        }
        return packageFileInfo.getLocalZipPath();
    }
}

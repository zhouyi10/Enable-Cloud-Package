package com.enableets.edu.pakage.core.core.htmlparser.handler;

import org.apache.commons.lang3.StringUtils;

import com.enableets.edu.pakage.core.core.htmlparser.HandleResult;
import com.enableets.edu.pakage.core.core.htmlparser.PackageHtmlParseException;
import com.enableets.edu.pakage.core.core.htmlparser.retry.Retry;
import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;
import com.enableets.edu.pakage.core.utils.Utils;

import java.io.File;

/**
 * Download the attachment and save it locally and return the relative address
 * @author max
 * @since 2018/3/16
 */
public class AttachmentDownloadHandler implements IAttachmentHandler {

	private static final Log LOGGER = LogFactory.getLog(AttachmentDownloadHandler.class);

	/** Download root path  */
	private String basePath;

	public AttachmentDownloadHandler(){}

	public AttachmentDownloadHandler(String basePath) {
		this.basePath = basePath;
	}
	/**
	 * @param url
	 * @return
	 */
	@Override
	public HandleResult handle(String url) {
		if (StringUtils.isBlank(url)) {
			HandleResult result = new HandleResult();
			result.setResult(url);
			return result;
		}
		String sourceUrl = url;
		url = url.replace("\\\"", "").replace("\\\"", "");
		String fileName = "";
		if (StringUtils.isBlank(url)) {   //Url is base64, url = "\"";
			HandleResult result = new HandleResult();
			result.setResult(sourceUrl);
			return result;
		}
		try {
			fileName = getFileName(url);
		}catch (Exception e){
			System.out.println("exception-url:" + url);
			e.printStackTrace();
		}
		StringBuilder sb = new StringBuilder(basePath).append("/file/").append(fileName);
		StringBuilder absoluteSb = new StringBuilder("file/").append(fileName);
		File destFile = new File(sb.toString());
		boolean downloadSuccess = true; //download success;
		if (!destFile.exists()) {
			// 下载文件
			try {
				downloadSuccess = downloadFileRetry(url, destFile).execute();
			} catch (Exception e) {
				downloadSuccess = false;
			}
		}
		if (downloadSuccess) return HandleResult.success(absoluteSb.toString());
		else return HandleResult.fail(url);
	}

	private String getFileName(String url){
		File file = new File(url);
		String name = file.getName();
		if (name.indexOf("?") > -1) {
			name = file.getName().substring(0, file.getName().lastIndexOf("?"));
		}
		return name;
	}

	public Retry downloadFileRetry(String url, File destFile){
		return new Retry(){
			@Override
			public Boolean doBiz() {
				try {
					Utils.downloadFile(url, destFile.getPath());
					return Boolean.TRUE;
				}catch (Exception e){
					LOGGER.error("File[url='"+url+"'] download fail!", e);
					throw new PackageHtmlParseException("File[url='"+url+"'] download fail!");
				}
			}
		};
	}
	
	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}
}

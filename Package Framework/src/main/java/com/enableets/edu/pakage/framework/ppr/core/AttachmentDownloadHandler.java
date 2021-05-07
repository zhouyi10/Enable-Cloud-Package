package com.enableets.edu.pakage.framework.ppr.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.enableets.edu.framework.core.util.BeanUtils;
import com.enableets.edu.sdk.paper.util.htmlparser.HandleResult;
import com.enableets.edu.sdk.paper.util.htmlparser.handler.IAttachmentHandler;

/**
 * Download Document Return The Relative Path
 */
@Configuration
public class AttachmentDownloadHandler implements IAttachmentHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(AttachmentDownloadHandler.class);

	private com.enableets.edu.pakage.core.core.htmlparser.handler.AttachmentDownloadHandler handle;

	public AttachmentDownloadHandler() {
		handle = new com.enableets.edu.pakage.core.core.htmlparser.handler.AttachmentDownloadHandler();
	}
	public AttachmentDownloadHandler(String basePath) {
		handle = new com.enableets.edu.pakage.core.core.htmlparser.handler.AttachmentDownloadHandler(basePath);
	}

	/**
	 * @param url
	 * @return
	 */
	@Override
	public HandleResult handle(String url) {
		com.enableets.edu.pakage.core.core.htmlparser.HandleResult handle = this.handle.handle(url);
		return BeanUtils.convert(handle, HandleResult.class);
	}
	
	public void setBasePath(String basePath) {
		handle.setBasePath(basePath);
	}
}

package com.enableets.edu.sdk.ppr.ppr.htmlparser.handler;

import com.enableets.edu.sdk.ppr.ppr.htmlparser.HandleResult;

/**
 * Interface definition for obtaining attachment address and processing
 * @author max
 * @since 2018/3/16
 */
public interface IAttachmentHandler {

	public HandleResult handle(String url);

}

package com.enableets.edu.sdk.ppr.ppr.htmlparser.parser;

import com.enableets.edu.sdk.ppr.ppr.htmlparser.FetchResult;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.HandleResult;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.handler.IAttachmentHandler;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.multipart.ITask;

import java.util.HashMap;
import java.util.Map;

/**
 * Html Handler thread processing class
 *
 * @author caleb_liu@enable-ets.com
 * @since 2019-03-01 15:41
 */

public class AttachmentHtmlHandTask implements ITask<Map<String, Object>, FetchResult> {

	@Override
	public Map<String, Object> execute(FetchResult fetchResult, Map<String, Object> params) throws Exception {
		IAttachmentHandler handler = (IAttachmentHandler)params.get("handler");

		HandleResult handlerResult = handler.handle(fetchResult.getResult());

		Map<String, Object> handlerResultMap = new HashMap<>();

		handlerResultMap.put("handlerResult", handlerResult);
		handlerResultMap.put("fetchResult", fetchResult);

		return handlerResultMap;
	}
}

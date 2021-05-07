package com.enableets.edu.sdk.ppr.ppr.htmlparser.scanner;

import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.FetchResult;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.PPRHtmlParseException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caleb_liu@enable-ets.com
 * @since 2018-10-31 19:21
 */

public class SpanBackgroundUrlScanner extends BaseHtmlTagScanner {

	/*
	 * <span class="exerimg" style="width:14px;height:13px;background:url(/mixtexpic/002/558/463/002558463_s_b.png)background-position:0px 0px;background-size:14px 13px;background-image:url(mixtexpic/002/558/463/002558463_s.png)9;"></span>
	 * */
	private static final Log logger = LogFactory.getLog(SpanBackgroundUrlScanner.class);

	private static final String SRC_TAG = "span";

	private static final String IMG_TAG = "background:url";

	private static final String IMG_TAG_2 = "background-image:url";

	@Override
	public List<FetchResult> fetch(String htmlString) {
		 fetch(htmlString, SRC_TAG, IMG_TAG);
		return fetch(htmlString, SRC_TAG, IMG_TAG_2);

	}

	private List<FetchResult> fetch(String htmlString, String SRC_TAG, String IMG_TAG) {
		String regexForTag = "<\\s*" + SRC_TAG + "\\s+([^>]*)\\s*";  //span match
		String regexForTagAttrib = "(?<=" + IMG_TAG + "\\()[^\\)]+";  //background:url or background-image:url match
		List<FetchResult> srcUrls = new ArrayList<>();
		Pattern patternForTag = Pattern.compile(regexForTag, Pattern.CASE_INSENSITIVE);
		Pattern patternForAttrib = Pattern.compile(regexForTagAttrib, Pattern.CASE_INSENSITIVE);
		Matcher matcherForTag = patternForTag.matcher(htmlString);
		boolean result = matcherForTag.find();
		String oldSrcPath = null;
		try {
			while (result) {
				Matcher matcherForAttrib = patternForAttrib.matcher(matcherForTag.group(1));
				if (matcherForAttrib.find()) {
					oldSrcPath = matcherForAttrib.group();
					srcUrls.add(new FetchResult(urlFix(oldSrcPath)));
				}
				result = matcherForTag.find();
			}
		} catch (Exception e) {
			logger.error("URL conversion error!", e);
			throw new PPRHtmlParseException("URL conversion error!");
		}
		return srcUrls;
	}

}

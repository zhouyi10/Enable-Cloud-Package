package com.enableets.edu.sdk.ppr.ppr.htmlparser.scanner;

import com.enableets.edu.sdk.ppr.ppr.htmlparser.FetchResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author caleb_liu@enable-ets.com
 * @since 2018-10-31 19:21
 */

public class JsonAffixIdUrlScanner extends BaseHtmlTagScanner {

	/*
	 * <span class="exerimg" style="width:14px;height:13px;background:url(/mixtexpic/002/558/463/002558463_s_b.png)background-position:0px 0px;background-size:14px 13px;background-image:url(mixtexpic/002/558/463/002558463_s.png)9;"></span>
	 * */

	private static final String SRC_TAG = "affixId";

	@Override
	public List<FetchResult> fetch(String htmlString) {
		return fetchAffixId(htmlString, SRC_TAG);
	}

	private List<FetchResult> fetchAffixId(String json, String jsonKey) {
		List<FetchResult> strings = new ArrayList<>();
		String regex = "\"" + jsonKey + "\":\"(.*?)\\\"";
		Matcher matcher = Pattern.compile(regex).matcher(json);
		while (matcher.find()) {
			FetchResult fetchResult = new FetchResult();
			fetchResult.setResult(urlFix(matcher.group(1)));
			strings.add(fetchResult);
		}
		return strings;
	}
}

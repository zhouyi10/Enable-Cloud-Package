package com.enableets.edu.sdk.ppr.ppr.htmlparser.scanner;

import com.enableets.edu.sdk.ppr.ppr.htmlparser.FetchResult;

import java.util.List;

/**
 * html Scanner universal interface for internal tags
 * @author max
 * @since 2018/3/16
 */
public interface IHtmlTagScanner {
	public List<FetchResult> fetch(String html);

	public String replace(String html, String oldUrl, String newUrl);
}

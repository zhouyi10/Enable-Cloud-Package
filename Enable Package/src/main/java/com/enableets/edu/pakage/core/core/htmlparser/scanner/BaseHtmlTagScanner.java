package com.enableets.edu.pakage.core.core.htmlparser.scanner;

import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;

/**
 * @author max
 * @since 2018年3月16日
 */
public abstract class BaseHtmlTagScanner implements IHtmlTagScanner {

	private static final Log logger = LogFactory.getLog(BaseHtmlTagScanner.class);
	
	/**
	 * @param htmlString
	 * @return
	 */
	public NodeList fetch(String htmlString, String tag) {
		Parser parser = ParserFactory.instance();
		try {
			parser.setInputHTML(htmlString);
		} catch (ParserException e) {
			logger.error("Failed to convert the question HTML format",e);
		}
		NodeFilter filter = new TagNameFilter(tag);
		NodeList nodes = null;
		try {
			nodes = parser.extractAllNodesThatMatch(filter);
		} catch (ParserException e) {
			logger.error("Failed to obtain question node format",e);
		}
		return nodes;
	}

	/**
	 * @param html
	 * @param newStr
	 * @return
	 */
	@Override
	public String replace(String html,String oldStr,String newStr) {
		int start = 0, newUrlLength = newStr.length();
		while((start = html.indexOf(oldStr, start)) > -1){
			html = html.replace(oldStr, newStr);
			start += newUrlLength;
		}
		return html;
	}

	public String urlFix(String url){
		if (url.startsWith("\\\"")) url = url.substring(2);
		if (url.endsWith("\\\"")) url = url.substring(0, url.length() - 2);
		return url;
	}
}

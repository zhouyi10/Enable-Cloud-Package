package com.enableets.edu.pakage.core.core.htmlparser.scanner;

import org.htmlparser.Node;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;

import com.enableets.edu.pakage.core.core.htmlparser.FetchResult;

import java.util.ArrayList;
import java.util.List;

/**
 * @author max
 * @since 2018/3/16
 */
public class AHrefScanner extends BaseHtmlTagScanner {

	private static final String SRC_TAG = "href";
	private static final String IMG_TAG = "a";

	/**
	 * @param htmlString
	 * @return
	 */
	@Override
	public List<FetchResult> fetch(String htmlString) {
		NodeList nodes = this.fetch(htmlString, IMG_TAG);
		List<FetchResult> srcUrls = null;
		Node eachNode = null;
		LinkTag linkTag = null;
		String oldSrcPath = null;
		if (nodes != null && nodes.size() > 0) {
			srcUrls = new ArrayList<>();
			for (int i = 0; i < nodes.size(); i++) {
				eachNode = (Node) nodes.elementAt(i);
				if (eachNode instanceof LinkTag) {
					linkTag = (LinkTag) eachNode;
					oldSrcPath = linkTag.getAttribute(SRC_TAG);
					srcUrls.add(new FetchResult(urlFix(oldSrcPath)));
				}
			}
		}
		return srcUrls;
	}
}

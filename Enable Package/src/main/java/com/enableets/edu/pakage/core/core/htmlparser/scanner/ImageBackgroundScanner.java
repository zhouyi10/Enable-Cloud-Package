package com.enableets.edu.pakage.core.core.htmlparser.scanner;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.util.NodeList;

import com.enableets.edu.pakage.core.core.htmlparser.FetchResult;
import com.enableets.edu.pakage.core.core.htmlparser.PackageHtmlParseException;
import com.enableets.edu.pakage.core.core.logging.Log;
import com.enableets.edu.pakage.core.core.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * @author max
 * @since 2018/3/16
 */
public class ImageBackgroundScanner extends BaseHtmlTagScanner {

	private static final Log LOGGER = LogFactory.getLog(ImageBackgroundScanner.class);

	private static final String BACKGROUND_IMAGE_TAG = "background-image";

	private static final String STYLE_TAG = "style";

	private static final String SRC_TAG = "src";

	private static final String IMG_TAG = "img";

	/**
	 * @param htmlString
	 * @return
	 */
	@Override
	public List<FetchResult> fetch(String htmlString) {
		NodeList nodes = this.fetch(htmlString, IMG_TAG);
		List<FetchResult> srcUrls = null;
		Node eachNode = null;
		ImageTag imageTag = null;
		String oldSrcPath = null;
		if (nodes != null) {
			srcUrls = new ArrayList<FetchResult>();
			for (int i = 0; i < nodes.size(); i++) {
				eachNode = (Node) nodes.elementAt(i);
				if (eachNode instanceof ImageTag) {
					imageTag = (ImageTag) eachNode;
					oldSrcPath = imageTag.getAttribute(SRC_TAG);
					String styleStr = imageTag.getAttribute(STYLE_TAG);
					if(!StringUtils.isBlank(styleStr)){
						String[] styles = styleStr.split(";");
						for(String sty:styles){
							if(sty.indexOf(BACKGROUND_IMAGE_TAG)>-1){
								try{
									String imgUrl = sty.substring(sty.indexOf("(")+1,sty.indexOf(")"));
									imgUrl = imgUrl.replaceAll("&quot;", "");
									srcUrls.add(new FetchResult(urlFix(imgUrl)));
								}catch(Exception e){
									LOGGER.error("background-image no url statement", e);
									throw new PackageHtmlParseException("question css style error, background-image no url!");
								}
							}
						}
					}else{
						srcUrls.add(new FetchResult(urlFix(oldSrcPath)));
					}
				}
			}
		}
		return srcUrls;
	}
}

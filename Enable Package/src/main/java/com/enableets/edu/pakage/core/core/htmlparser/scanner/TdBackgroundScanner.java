package com.enableets.edu.pakage.core.core.htmlparser.scanner;

import org.apache.commons.lang3.StringUtils;
import org.htmlparser.Node;
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
public class TdBackgroundScanner extends BaseHtmlTagScanner {

	/** Log */
	private static final Log LOGGER = LogFactory.getLog(ImageSrcScanner.class);
	
	/**
	 * 
	 */
	private static final String BACKGROUND_IMAGE_TAG = "background-image";
	/**
	 * 
	 */
	private static final String TD_TAG = "td";

	/**
	 * @param htmlString
	 * @return
	 */
	@Override
	public List<FetchResult> fetch(String htmlString) {
		NodeList nodes = this.fetch(htmlString, TD_TAG);
		List<FetchResult> bgUrls = null;
		Node eachNode = null;
		if (nodes != null) {
			bgUrls = new ArrayList<FetchResult>();
			for (int i = 0; i < nodes.size(); i++) {
				eachNode = (Node) nodes.elementAt(i);
				String stylestr = eachNode.getText();
					if(!StringUtils.isBlank(stylestr)){
						String[] styles = stylestr.split(";");
						
						for(String sty:styles){
							if(sty.indexOf(BACKGROUND_IMAGE_TAG)>-1){
								String[] urls = sty.substring(sty.indexOf(BACKGROUND_IMAGE_TAG)+17, sty.lastIndexOf(")")).split(",");
								try{
									
									for (String url : urls) {
										FetchResult restult = new FetchResult();
										restult.setResult(url.replaceAll("url","").replaceAll("\"", "").replace("(", "").replace(")", "").trim());
										bgUrls.add(restult);
									}
								}catch(Exception ex){
									LOGGER.error("background-image no url statement", ex);
									throw new PackageHtmlParseException("background-image no url statement");
								}
							}
							
						}
					}
				}
			}
		return bgUrls;
	}
}

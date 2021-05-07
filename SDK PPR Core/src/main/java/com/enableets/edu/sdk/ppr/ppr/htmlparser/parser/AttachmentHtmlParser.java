package com.enableets.edu.sdk.ppr.ppr.htmlparser.parser;

import com.enableets.edu.sdk.ppr.utils.StringUtils;
import com.enableets.edu.sdk.ppr.logging.Log;
import com.enableets.edu.sdk.ppr.logging.LogFactory;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.FetchResult;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.HandleResult;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.PPRHtmlParseException;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.handler.IAttachmentHandler;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.multipart.MultiThreadUtils;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.multipart.ResultBean;
import com.enableets.edu.sdk.ppr.ppr.htmlparser.scanner.*;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * handle html all internal attachment address conversion tool
 * @author max
 * @since 2018/3/16
 */
public class AttachmentHtmlParser {

	private static final Log LOGGER = LogFactory.getLog(AttachmentHtmlParser.class);

	public static ImageSrcScanner imageSrcScanner = new ImageSrcScanner();

	public static ImageBackgroundScanner imageBackgroundScanner = new ImageBackgroundScanner();

	public static TdBackgroundScanner tdBackgroundScanner = new TdBackgroundScanner();

	public static SpanBackgroundUrlScanner spanBackgroundUrlScanner = new SpanBackgroundUrlScanner();

	public static JsonAffixIdUrlScanner jsonAffixIdUrlScanner = new JsonAffixIdUrlScanner();

	public static AHrefScanner aHrefScanner = new AHrefScanner();

	public static String parse(String html, IAttachmentHandler handler) {
		return AttachmentHtmlParser.parse(html, handler, imageSrcScanner, imageBackgroundScanner, tdBackgroundScanner, spanBackgroundUrlScanner, jsonAffixIdUrlScanner, aHrefScanner);
	}

	public static String parse(String html, IAttachmentHandler handler, IHtmlTagScanner... scanners) {
		if (StringUtils.isEmpty(html))
			return "";

		List<FetchResult> fetchResults = new ArrayList<>();

		// scan all img or background
		for (IHtmlTagScanner scanner : scanners) {
			List<FetchResult> fetch = scanner.fetch(html);
			if (fetch != null) {
				fetchResults.addAll(scanner.fetch(html));
			}
		}
		//duplicate removal
		ArrayList<FetchResult> images = fetchResults.stream().filter(e -> StringUtils.isNotBlank(e.getResult())).collect(Collectors.collectingAndThen(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing(FetchResult::getResult))), ArrayList::new));

		if (images == null) {
			return html;
		}

		// Multi-task uploading pictures
		MultiThreadUtils multiThreadUtils = new  MultiThreadUtils(20);
		Map<String, Object> params = new HashMap<>();
		params.put("handler", handler);
		AttachmentHtmlHandTask attachmentHtmlHandTask = new AttachmentHtmlHandTask();
		ResultBean resultBean = multiThreadUtils.execute(images, params, attachmentHtmlHandTask);
		List<Map<String, Object>> executeList = (List<Map<String, Object>>)resultBean.getData();
		boolean fileHandlerStatus = true;
		String url = null;
		for (Map<String, Object> execute : executeList) {
			HandleResult handleResult =  (HandleResult)execute.get("handlerResult");
			if (!handleResult.getStatus()) {
				url = handleResult.getResult();
				fileHandlerStatus = false; break;
			}
			String result = handleResult.getResult();
			String oldUrl = ((FetchResult)execute.get("fetchResult")).getResult();
			if (StringUtils.isNotBlank(result) && !oldUrl.equals(result)) {
				// Matcher.quoteReplacement Special character conversion
				// The ? Sign is a special character in regular rules, and all ? Is processed into [?] and then replaced
				/*oldUrl = oldUrl.replaceAll("[?]", "[?]");
				html = html.replaceAll(Matcher.quoteReplacement(oldUrl), Matcher.quoteReplacement(result));*/
				Matcher matcher = Pattern.compile(oldUrl, Pattern.LITERAL).matcher(html);
				html = matcher.replaceAll(Matcher.quoteReplacement(result));
			}
		}
		if (!fileHandlerStatus){
            LOGGER.error("Static file[\""+url+"\"] download fail!");
			throw new PPRHtmlParseException("Static file[\""+url+"\"] download fail!");
		}
		return html;
	}

	public static String parse(String html, String parentPath){
		return parse(html, parentPath, imageSrcScanner, imageBackgroundScanner, tdBackgroundScanner, spanBackgroundUrlScanner);
	}

	public static String parse(String html, String parentPath, IHtmlTagScanner... scanners){
		if (StringUtils.isBlank(html)) return html;
		List<FetchResult> fetchResults = new ArrayList<>();
		for (IHtmlTagScanner scanner : scanners) {
			fetchResults.addAll(scanner.fetch(html));
		}
		for (FetchResult fetchResult : fetchResults) {
			String relativeUrl = fetchResult.getResult();
			String absoluteUrl = new File(parentPath + "/" + relativeUrl).getPath();
			Matcher matcher = Pattern.compile(relativeUrl, Pattern.LITERAL).matcher(html);
			html = matcher.replaceAll(Matcher.quoteReplacement(absoluteUrl));
		}
		return html;
	}
}

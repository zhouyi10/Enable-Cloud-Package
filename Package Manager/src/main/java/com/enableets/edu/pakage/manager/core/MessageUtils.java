package com.enableets.edu.pakage.manager.core;

import com.enableets.edu.framework.core.util.SpringBeanUtils;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

/**
 * 多语言工具类
 *
 * @author sherry_yin@enable-ets.com
 * @since 2017/09/07
 */
public class MessageUtils {
	
	/**
	 * 语系
	 */
	private static Locale _locale;
	
	/**
	 * 
	 */
	private static MessageSource messageSource;
	
	static {
		_locale = ((LocaleResolver) SpringBeanUtils.getBean(LocaleResolver.class))
				.resolveLocale((HttpServletRequest) null);
		messageSource = (MessageSource) SpringBeanUtils.getBean(MessageSource.class);
		
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key) {
		return messageSource.getMessage(key, null, _locale);
	}
	
	/**
	 * 
	 * @param key
	 * @param args
	 * @return
	 */
	public static String getMessage(String key, Object[] args) {
		return messageSource.getMessage(key, args, _locale);
	}
	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getMessage(String key, Locale locale) {
		return messageSource.getMessage(key, null, locale);
	}
	
	/**
	 * 
	 * @param key
	 * @param args
	 * @param locale
	 * @return
	 */
	public static String getMessage(String key, Object[] args, Locale locale) {
		return messageSource.getMessage(key, args, locale);
	}
}

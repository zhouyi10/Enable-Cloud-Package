package com.enableets.edu.pakage.core.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author toy_liu@enable-ets.com
 * @since 2020/06/22
 */
public class BeanUtils {

	    private static Mapper mapper = new DozerBeanMapper();

	    public static <F, T> T convert(F source, T destination) {
	        if (source == null || destination == null) return null;
	        mapper.map(source, destination);
	        return destination;
	    }

	    public static <F, T> List<T> convert(List<F> fromList, final Class<T> toClass) {
	    	if (fromList == null) return Collections.emptyList();
	    	List<T> destination = new ArrayList<T>();
	    	for(F from : fromList) {
	    		if (from == null) {
	    			continue;
	    		}
	    		destination.add(convert(from,toClass));
	    	}
	    	return destination;
	    }

	    public static <F, T> T convert(F from, final Class<T> toClass) {
	        if (from == null) return null;
	        return mapper.map(from, toClass);
	    }
}

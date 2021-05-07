package com.enableets.edu.pakage.core.core.htmlparser.multipart;

import java.util.Map;

/**
 * Task processing interface
 *
 * @author caleb_liu@enable-ets.com
 * @since 2019-01-30 10:51
 */
public interface ITask<T, E> {

	/**
	 * @param e
	 * @param params Other auxiliary parameters
	 * @return
	 * @exception <BR>
	 * @since  2.0
	 */
	T execute(E e, Map<String, Object> params) throws Exception;
}

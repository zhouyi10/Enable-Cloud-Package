package com.enableets.edu.sdk.ppr.ppr.htmlparser.multipart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Task processing
 *
 * @author caleb_liu@enable-ets.com
 * @since 2019-01-30 10:53
 */

@SuppressWarnings("rawtypes")
public class HandleCallable<E> implements Callable<ResultBean> {
	//private static final Logger LOGGER = LoggerFactory.getLogger(HandleCallable.class);
	// Thread name
	private String threadName = "";
	// Data to be processed
	private List<E> data;
	// Auxiliary parameters
	private Map<String, Object> params;
	// Specific tasks
	private ITask<ResultBean<String>, E> task;

	public HandleCallable(String threadName, List<E> data, Map<String, Object> params,
	                      ITask<ResultBean<String>, E> task) {
		this.threadName = threadName;
		this.data = data;
		this.params = params;
		this.task = task;
	}

	@Override
	public ResultBean<List<ResultBean<String>>> call() throws Exception {
		// All data processing in this thread returns results
		ResultBean<List<ResultBean<String>>> resultBean = ResultBean.newInstance();
		if (data != null && data.size() > 0) {
			List<ResultBean<String>> resultList = new ArrayList<>();
			for (int i = 0; i < data.size(); i++) {
				E e = data.get(i);
				long start = System.currentTimeMillis();
				try {
					resultList.add(task.execute(e, params));
				} catch (Exception e1) {

				}
			}
			resultBean.setData(resultList);
		}
		return resultBean;
	}

}
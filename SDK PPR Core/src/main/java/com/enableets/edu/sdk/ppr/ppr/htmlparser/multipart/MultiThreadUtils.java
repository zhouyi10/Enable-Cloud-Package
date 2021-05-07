package com.enableets.edu.sdk.ppr.ppr.htmlparser.multipart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Multi-threaded tools
 *
 * @author caleb_liu@enable-ets.com
 * @since 2019-01-30 10:54
 */

public class MultiThreadUtils<T> {

	//private static Logger logger = LoggerFactory.getLogger(MultiThreadUtils.class);

	/** Number of threads， default 5 */
	private int threadCount = 5;
	/** Specific business tasks */
	private ITask<ResultBean<String>, T> task;
	/** Thread pool manager */
	private CompletionService<ResultBean> pool = null;

	public MultiThreadUtils (int threadCount) {
		this.threadCount = threadCount;
	}

	/**
	 *
	 * Multi-threaded batch execution of tasks in the list<BR>
	 * @param data  Large amount of data processed by threads list
	 * @param params    Processing data is auxiliary parameter transfer
	 * @param task        Task interface for specific execution business
	 * @return ResultBean<BR>
	 * @exception <BR>
	 * @since  2.0
	 */
	@SuppressWarnings("rawtypes")
	public ResultBean execute(List<T> data, Map<String, Object> params, ITask<ResultBean<String>, T> task) {
		// Create thread pool
		ExecutorService threadpool = Executors.newFixedThreadPool(threadCount);
		// Initialize the thread pool manager according to the thread pool
		pool = new ExecutorCompletionService<ResultBean>(threadpool);
		// Starting time（ms）
		long l = System.currentTimeMillis();
		// Data size
		int length = data.size();
		// Number of data processed by each thread
		int taskCount = length / threadCount;
		// Divide the data called by each thread
		for (int i = 0; i < threadCount; i++) {
			// Task data per thread list
			List<T> subData = null;
			if (i == (threadCount - 1)) {
				subData = data.subList(i * taskCount, length);
			} else {
				subData = data.subList(i * taskCount, (i + 1) * taskCount);
			}
			// Distribute data to individual threads
			HandleCallable execute = new HandleCallable<T>(String.valueOf(i), subData, params, task);
			// Add threads to the thread pool
			pool.submit(execute);
		}

		// Total return result set
		List<ResultBean<T>> result = new ArrayList<>();
		for (int i = 0; i < threadCount; i++) {
			// Each thread processes the result set
			ResultBean<List<ResultBean<T>>> threadResult;
			try {
				threadResult = pool.take().get();
				if (threadResult.getData() != null && threadResult.getData().size() > 0) {
					result.addAll(threadResult.getData());
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		// Close thread pool
		threadpool.shutdownNow();
		// Execution end time
		long end_l = System.currentTimeMillis();
		return ResultBean.newInstance().setData(result);
	}

	public int getThreadCount() {
		return threadCount;
	}

	public void setThreadCount(int threadCount) {
		this.threadCount = threadCount;
	}

}

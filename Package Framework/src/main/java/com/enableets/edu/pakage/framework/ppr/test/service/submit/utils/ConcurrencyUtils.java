package com.enableets.edu.pakage.framework.ppr.test.service.submit.utils;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.enableets.edu.module.service.core.MicroServiceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import lombok.Data;

/**
 * @author duffy_ding
 * @since 2019/12/27
 */
public class ConcurrencyUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcurrencyUtils.class);

    /**
     * when the number of threads is greater than the core,
     * this is the maximum time that excess idle threads will wait for new tasks before terminating
     */
    private static final long BLOCK_THREAD_POOL_KEEP_ALIVE_TIME = 0L;

    /**
     * 阻塞式线程池
     * @param nThreads 线程数
     * @return 线程池
     */
    public static ThreadPoolExecutor newBlockThreadPool(int nThreads) {
        return new ThreadPoolExecutor(nThreads, nThreads, BLOCK_THREAD_POOL_KEEP_ALIVE_TIME, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(nThreads),
            Executors.defaultThreadFactory(), (r, executor) -> {
                try {
                    executor.getQueue().put(r);
                } catch (InterruptedException e) {
                    LOGGER.error(e.getMessage(), e);
                }
            }
        );
    }

    /**
     * default identify function
     * @param <T>  input data type
     * @return identify function
     */
    private static <T> Function<T, String> defaultIdentifyFunction() {
        return new Function<T, String>() {
            private AtomicInteger id = new AtomicInteger();
            @Override
            public String apply(T t) {
                return String.valueOf(id.incrementAndGet());
            }
        };
    }

    /**
     * 合并请求executor
     * @param batchProcessFunc 批量处理实现类
     * @param <T> input data type
     * @param <R> output data type
     * @return union request executor
     */
    public static <T, R> UnionRequestExecutor<T, String, R> newUnionRequestExecutor(Function<Map<String, T>, Map<String, R>> batchProcessFunc) {
        return newUnionRequestExecutor(batchProcessFunc, defaultIdentifyFunction());
    }

    /**
     * 合并请求executor
     * @param batchProcessFunc 批量处理实现类
     * @param identifyFunc id 生成规则
     * @param <T> input data type
     * @param <F> id type
     * @param <R> output data type
     * @return union request executor
     */
    public static <T, F, R> UnionRequestExecutor<T, F, R> newUnionRequestExecutor(Function<Map<F, T>, Map<F, R>> batchProcessFunc, Function<T, F> identifyFunc) {
        return new UnionRequestExecutor<>(batchProcessFunc, identifyFunc);
    }

    /**
     * 合并请求多线程阻塞队列executor
     * @param batchProcessFunc 批量处理实现类
     * @param <T> input data type
     * @param <R> output data type
     * @return union request executor
     */
    public static <T, R> UnionRequestBlockThreadPoolExecutor<T, String, R> newUnionRequestBlockThreadPoolExecutor(Function<Map<String, T>, Map<String, R>> batchProcessFunc, int poolSize) {
        return newUnionRequestBlockThreadPoolExecutor(batchProcessFunc, defaultIdentifyFunction(), poolSize);
    }

    /**
     * 合并请求多线程阻塞队列executor
     * @param batchProcessFunc 批量处理实现类
     * @param identifyFunc id 生成规则
     * @param <T> input data type
     * @param <F> id type
     * @param <R> output data type
     * @return union request executor
     */
    public static <T, F, R> UnionRequestBlockThreadPoolExecutor<T, F, R> newUnionRequestBlockThreadPoolExecutor(Function<Map<F, T>, Map<F, R>> batchProcessFunc, Function<T, F> identifyFunc, int poolSize) {
        return new UnionRequestBlockThreadPoolExecutor<>(batchProcessFunc, identifyFunc, poolSize);
    }

    /**
     * 合并请求executor
     * @param <T> input data type
     * @param <F> id type
     * @param <R> output data type
     */
    public static class UnionRequestExecutor<T, F, R> {

        /** logger */
        private Logger logger = LoggerFactory.getLogger(getClass());

        /** 合并请求单次处理数量 */
        private static final int FRAME_SIZE = 20;

        /** 合并请求定时调度executor */
        private ScheduledExecutorService executor;

        /** 请求队列 */
        private LinkedBlockingQueue<UnionRequest<T, F, R>> requestQueue;

        /** 批量处理逻辑 */
        private Function<Map<F, T>, Map<F, R>> batchProcessFunc;

        /** id 生成规则 */
        private Function<T, F> identifyFunc;

        private UnionRequestExecutor(Function<Map<F, T>, Map<F, R>> batchProcessFunc, Function<T, F> identifyFunc) {
            requestQueue = new LinkedBlockingQueue<>();
            this.batchProcessFunc = batchProcessFunc;
            this.identifyFunc = identifyFunc;

            executor = Executors.newSingleThreadScheduledExecutor();
            executor.scheduleWithFixedDelay(this::run, 0, 200, TimeUnit.MILLISECONDS);
        }

        private void run() {
            try {
                int size = requestQueue.size();
                if (size == 0) {
                    return;
                }
//                size = size > FRAME_SIZE ? FRAME_SIZE : size;
                List<UnionRequest<T, F, R>> requests = new ArrayList<>();
                for (int index = 0; index < size; index++) {
                    try {
                        requests.add(requestQueue.take());
                    } catch (InterruptedException e) {
                        LOGGER.error(e.getMessage(), e);
                        return;
                    }
                    if (requests.size() >= FRAME_SIZE) {
                        doTask(requests);
                        requests = new ArrayList<>();
                    }
                }
                if (requests.size() > 0) {
                    doTask(requests);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        protected void doTask(List<UnionRequest<T, F, R>> requests) {
            if (CollectionUtils.isEmpty(requests)) {
                return;
            }
            Map<F, R> resultMap = Collections.emptyMap();
            try {
                Map<F, T> dataMap = new HashMap<F, T>();
                for (UnionRequest<T, F, R> request : requests) {
                    dataMap.put(request.getId(), request.getData());
                }
                resultMap = batchProcessFunc.apply(dataMap);
                for (UnionRequest<T, F, R> request : requests) {
                    request.getFuture().complete(resultMap.get(request.getId()));
                }
            } catch (Exception e) {
                for (UnionRequest<T, F, R> request : requests) {
                    request.getFuture().completeExceptionally(e);
                }
            }
        }

        public R mergeRequest(T data) {
            UnionRequest<T, F, R> request = new UnionRequest(data, identifyFunc.apply(data));
            requestQueue.add(request);
            try {
                return request.getFuture().get();
            } catch (InterruptedException e) {
                throw new MicroServiceException("38-01-001", "Thread pool processing exception");
            } catch (ExecutionException e) {
                if (e.getCause() instanceof MicroServiceException) {
                    throw (MicroServiceException) e.getCause();
                }
                throw new MicroServiceException("38-01-001", "Thread pool processing exception");
            }
        }

        public int getRequestCount() {
            return requestQueue.size();
        }

        public String getStatus() {
            return String.format("%d-%d-%d-%d:%d-%d-%d-%d",1,1,1,1, 0,0,0, getRequestCount());
        }
    }

    /**
     * 合并请求多线程阻塞队列executor
     * @param <T> input data type
     * @param <F> id type
     * @param <R> output data type
     */
    public static class UnionRequestBlockThreadPoolExecutor<T, F, R> extends UnionRequestExecutor<T, F, R> {

        private Logger logger = LoggerFactory.getLogger(getClass());

        private ThreadPoolExecutor threadPool;

        private UnionRequestBlockThreadPoolExecutor(Function<Map<F, T>, Map<F, R>> batchProcessFunc, Function<T, F> identifyFunc, int poolSize) {
            super(batchProcessFunc, identifyFunc);
            threadPool = newBlockThreadPool(poolSize);
        }

        @Override
        protected void doTask(List<UnionRequest<T, F, R>> unionRequests) {
            threadPool.submit(() -> {
                try {
                    super.doTask(unionRequests);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            });
        }

        @Override
        public String getStatus() {
            return String.format("%d-%d-%d-%d:%d-%d-%d-%d",
                    threadPool.getMaximumPoolSize(), threadPool.getLargestPoolSize(), threadPool.getPoolSize(), threadPool.getCorePoolSize(),
                    threadPool.getTaskCount(), threadPool.getCompletedTaskCount(), threadPool.getActiveCount(), getRequestCount());
        }
    }

    /**
     * 合并请求 包装类
     * @param <T> input data type
     * @param <F> id type
     * @param <R> output data type
     */
    @Data
    private static class UnionRequest<T, F, R> {
        private F id;
        private T data;
        private CompletableFuture<R> future = new CompletableFuture<>();

        private UnionRequest(T data, F id) {
            this.data = data;
            this.id = id;
        }
    }
}

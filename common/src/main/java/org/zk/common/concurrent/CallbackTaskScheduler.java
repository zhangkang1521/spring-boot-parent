package org.zk.common.concurrent;

import com.google.common.util.concurrent.*;

import java.util.concurrent.*;

/**
 * Created by 尼恩 at 疯狂创客圈
 */
public class CallbackTaskScheduler {
    //方法二是使用自建的线程池时，专用于处理耗时操作
    // 扩展java线程池，可以使用回调函数
    static ListeningExecutorService guavaPool = null;

    static {
        ExecutorService jPool =  new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(500));
        guavaPool = MoreExecutors.listeningDecorator(jPool);
    }


    private CallbackTaskScheduler() {
    }

    /**
     * 添加任务
     * @param executeTask
     */
    public static <R> void add(CallbackTask<R> executeTask) {


        ListenableFuture<R> future = guavaPool.submit(new Callable<R>() {
            public R call() throws Exception {

                R r = executeTask.execute();
                return r;
            }

        });

        Futures.addCallback(future, new FutureCallback<R>() {
            public void onSuccess(R r) {
                executeTask.onBack(r);
            }

            public void onFailure(Throwable t) {
                executeTask.onException(t);
            }
        });


    }

}

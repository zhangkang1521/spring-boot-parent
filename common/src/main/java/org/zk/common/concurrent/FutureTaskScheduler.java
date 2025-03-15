/**
 * Created by 尼恩 at 疯狂创客圈
 */

package org.zk.common.concurrent;


import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.*;

@Slf4j
public class FutureTaskScheduler {
    //方法二是使用自建的线程池时，专用于处理耗时操作
    // 无回调
    static ExecutorService mixPool;


    static {
        mixPool = new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(500));
    }

    private static FutureTaskScheduler inst = new FutureTaskScheduler();

    private FutureTaskScheduler() {

    }

    /**
     * 添加任务
     *
     * @param executeTask
     */


    public static void add(ExecuteTask executeTask) {
        mixPool.submit(() -> {
            try {
                executeTask.execute();
            } catch (Exception e) {
                log.error("线程池执行任务异常", e);
            }
        });
    }


}

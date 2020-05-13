package com.base.mykotlintest;

import android.os.Handler;

import java.util.Timer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 用 {@link ScheduledFuture} 实现的 {@link Timer}
 * runnable 会在构造的线程上被调用
 *
 * @author chentian
 */
public class ScheduledTimer {

    private final ScheduledExecutorService schedule;
    private final Handler callerHandler;

    private ScheduledFuture<?> scheduleFuture;
    private boolean canceled;

    public ScheduledTimer() {
        schedule = Executors.newScheduledThreadPool(1);
        callerHandler = new Handler();
    }

    /**
     * 定期循环执行任务
     * @param runnable 需要执行的任务
     * @param initialDelay 首次执行的延迟
     * @param period 两次任务之间的间隔
     */
    public void scheduleAtFixedRate(final Runnable runnable, long initialDelay, long period) {
        cancel();
        canceled = false;

        scheduleFuture = schedule.scheduleAtFixedRate(() -> {
            if (!canceled && !scheduleFuture.isCancelled()) {
                callerHandler.post(runnable);
            }
        }, initialDelay, period, TimeUnit.MILLISECONDS);
    }

    public void cancel() {
        if (scheduleFuture != null) {
            scheduleFuture.cancel(true);
            scheduleFuture = null;
        }
        canceled = true;
    }
}

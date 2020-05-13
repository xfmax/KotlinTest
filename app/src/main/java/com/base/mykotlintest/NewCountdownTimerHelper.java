package com.base.mykotlintest;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 训练流程重构时每个Step使用的技术局
 *
 * @author huangchen
 */
public class NewCountdownTimerHelper {

    private ScheduledExecutorService schedule;

    private int index;
    private int maxLength;
    private boolean paused;
    private boolean stop;
    private volatile boolean completeCall;
    private boolean mainThreadCallback;
    private OnCountdownTimerCallback callback;
    private boolean isReverse;

    public NewCountdownTimerHelper(int maxLength, OnCountdownTimerCallback callback) {
        this(maxLength,0, callback, false);
    }

    public NewCountdownTimerHelper(int maxLength, int initTime, OnCountdownTimerCallback callback, boolean isReverse) {
        this(maxLength, initTime, false, callback,isReverse);
    }

    public NewCountdownTimerHelper(int maxLength, int initTime, boolean mainThreadCallback,
                                   OnCountdownTimerCallback callback,boolean isReverse) {
        this.maxLength = maxLength;
        this.mainThreadCallback = mainThreadCallback;
        this.callback = callback;
        schedule = Executors.newScheduledThreadPool(1);
        index = initTime;
        this.isReverse = isReverse;
    }


    public void start(long delay, long rate) throws NewCountdownTimerStartException {
        if (stop) {
            throw new NewCountdownTimerStartException("NewCountdownTimerHelper already stop");
        }
        if (rate <= 0) {
            throw new IllegalArgumentException();
        }
        Runnable runnable = () -> {
            if (paused || completeCall) {
                return;
            }
            if (!isReverse) {
                if (index >= maxLength) {
                    completeCall = true;
                    onComplete();
                } else {
                    onCountDown();
                    index++;
                }
            }else{
                if (index <= 0) {
                    completeCall = true;
                    onComplete();
                } else {
                    onCountDown();
                    index--;
                }
            }
        };
        schedule.scheduleAtFixedRate(runnable, delay, rate, TimeUnit.MILLISECONDS);
    }

    private void onCountDown() {
        if (mainThreadCallback) {
            final int tmpIndex = index;
            MainThreadUtils.post(() -> {
                if (callback != null) {
                    callback.onCountdown(tmpIndex);
                }
            });
        } else {
            if (callback != null) {
                callback.onCountdown(index);
            }
        }
    }

    private void onComplete() {
        if (mainThreadCallback) {
            MainThreadUtils.post(() -> {
                if (callback != null) {
                    callback.onComplete();
                }
            });
        } else {
            if (callback != null) {
                callback.onComplete();
            }
        }
    }

    public void pause() {
        paused = true;
    }

    public void resume() {
        paused = false;
    }

    public void stop() {
        stop = true;
        schedule.shutdownNow();
        // 将callback置为null，防止内存泄漏
        callback = null;
    }

    public void reset(){
        schedule.shutdown();
        completeCall = false;
        stop = false;
        schedule = Executors.newScheduledThreadPool(1);
    }

    /**
     * 增加计时时间
     */
    public void updateFinishCount(int addCount) {
        maxLength += addCount;
    }

    public int getIndex() {
        return index;
    }

    public interface OnCountdownTimerCallback {

        /**
         * 每次的回调
         */
        void onCountdown(int index);

        /**
         * 完成
         */
        void onComplete();
    }

    public class NewCountdownTimerStartException extends Exception {
        NewCountdownTimerStartException(String message) {
            super(message);
        }
    }
}

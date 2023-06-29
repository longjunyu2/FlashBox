package com.aof.flashbox.input.event;

import java.util.Timer;
import java.util.TimerTask;

public class EventLooper<T> {
    private final EventQueue<T> mQueue;
    private Timer mTimer;
    private int sleepTime = 1;

    public EventLooper() {
        this.mQueue = new EventQueue<>();
        this.mTimer = new Timer();
    }

    /**
     * 设置循环等待时间
     *
     * @param i 等待时间
     */
    public void setSleepTime(int i) {
        this.sleepTime = i;
        start();
    }

    /**
     * 获取循环等待时间
     *
     * @return 等待时间
     */
    public int getSleepTime() {
        return this.sleepTime;
    }

    /**
     * 暂停定时器
     */
    public void pause() {
        if (mTimer != null) {
            this.mTimer.cancel();
            this.mTimer = null;
        }
    }

    /**
     * 判断循环石头暂停
     *
     * @return 循环暂停返回true，反之返回false
     */
    public boolean isPaused() {
        return mTimer == null;
    }

    /**
     * 开始定时器
     */
    public void start() {
        pause();
        mTimer = startNewTimer();
    }

    /**
     * 向队列添加一个事件
     *
     * @param event 事件
     */
    public void offer(T event) {
        this.mQueue.offer(event);
    }

    /**
     * 创建并开始一个新的定时器
     *
     * @return 定时器
     */
    private Timer startNewTimer() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                T event = mQueue.poll();
                if (event != null)
                    doEvent(event);
            }
        }, 10, sleepTime);
        return timer;
    }

    /**
     * 处理事件
     *
     * @param event 事件
     */
    private void doEvent(T event) {
        // to be overwrite
    }
}

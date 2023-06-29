package com.aof.flashbox.input.event;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.LinkedList;

public class EventQueue<T> {

    private final LinkedList<T> data;

    public EventQueue() {
        this.data = new LinkedList<>();
    }

    /**
     * 将一个元素放入队列尾部
     *
     * @param e 放入队列的元素
     */
    public void offer(T e) {
        data.offer(e);
    }

    /**
     * 将一个元素从队列头部取出
     *
     * @return 队列头部的元素
     */
    @Nullable
    public T poll() {
        return data.poll();
    }

    /**
     * 检索队列头部的元素，但不取出
     *
     * @return 队列头部的元素
     */
    @Nullable
    public T peek() {
        return data.peek();
    }

    /**
     * 检索队列头部的元素，但不取出
     *
     * @return 队列头部的元素
     * @throws RuntimeException 当队列头部为null时，抛出运行时异常
     */
    public T element() throws RuntimeException {
        return data.element();
    }

    /**
     * 检索队列头部的元素，并删除它
     *
     * @return 队列头部的元素
     * @throws RuntimeException 当队列头部为null时，抛出运行时异常
     */
    public T remove() throws RuntimeException {
        return data.remove();
    }

    /**
     * 判断队列是否为空
     *
     * @return 队列为空返回true，反之false
     */
    public boolean empty() {
        return data.isEmpty();
    }

    /**
     * 清空队列中全部的元素
     */
    public void clear() {
        data.clear();
    }

    /**
     * 字符串化队列元素
     *
     * @return 所有队列元素的字符串
     */
    @NonNull
    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        for (T v : data)
            b.append(v.toString()).append(" ");
        return b.toString();
    }

}

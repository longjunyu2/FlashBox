package com.aof.flashbox.input.view.graphics;

import android.graphics.PointF;
import android.os.Build;

import androidx.annotation.NonNull;

public class Circle {
    public PointF center;
    public float radius;

    public Circle() {
        this(0, 0, 0);
    }

    public Circle(@NonNull Circle circle) {
        this(circle.center, circle.radius);
    }

    public Circle(float cx, float cy, float radius) {
        this(new PointF(cx, cy), radius);
    }

    public Circle(PointF center, float radius) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            this.center = new PointF(center);
        else
            this.center = new PointF(center.x, center.y);
        this.radius = radius;
    }

    /**
     * 判断圆形是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return radius == 0;
    }

    /**
     * 判断某个点是否在圆形内
     *
     * @param p 点
     * @return 是否在圆形内
     */
    public boolean contains(PointF p) {
        if (isEmpty())
            return false;
        return Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2)) < radius;
    }

    /**
     * 判断某个点是否在圆形内
     *
     * @param x 点的x坐标
     * @param y 点的y坐标
     * @return 是否在圆形内
     */
    public boolean contains(float x, float y) {
        return contains(new PointF(x, y));
    }

    /**
     * 设置圆形参数
     *
     * @param cx     圆心x坐标
     * @param cy     圆心y坐标
     * @param radius 半径
     */
    public void set(float cx, float cy, float radius) {
        center.x = cx;
        center.y = cy;
        this.radius = radius;
    }

    /**
     * 设置圆形参数
     *
     * @param cx 圆心x坐标
     * @param cy 圆心y坐标
     */
    public void set(float cx, float cy) {
        set(cx, cy, radius);
    }
}

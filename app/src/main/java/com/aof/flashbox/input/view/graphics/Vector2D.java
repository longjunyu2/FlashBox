package com.aof.flashbox.input.view.graphics;

import android.graphics.Point;
import android.graphics.PointF;
import android.os.Build;

public class Vector2D {
    private final PointF point;

    public Vector2D() {
        this(0, 0);
    }

    public Vector2D(float x, float y) {
        point = new PointF(x, y);
    }

    /**
     * 设置向量参数
     *
     * @param x 向量x分量
     * @param y 向量y分量
     */
    public void set(float x, float y) {
        point.set(x, y);
    }

    /**
     * 设置向量参数
     *
     * @param point 向量终点
     */
    public void set(PointF point) {
        point.set(point);
    }

    /**
     * 设置向量参数
     *
     * @param point 向量终点
     */
    public void set(Point point) {
        point.set(point.x, point.y);
    }

    /**
     * 获取向量x分量
     *
     * @return x分量
     */
    public float getX() {
        return point.x;
    }

    /**
     * 获取向量y分量
     *
     * @return y分量
     */
    public float getY() {
        return point.y;
    }

    /**
     * 获取向量终点
     *
     * @return 向量终点
     */
    public PointF getPoint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            return new PointF(point);
        else
            return new PointF(point.x, point.y);
    }

    /**
     * 向量加
     *
     * @param vector 目标向量
     * @return 结果向量
     */
    public Vector2D add(Vector2D vector) {
        return add(vector.getX(), vector.getY());
    }

    /**
     * 向量加
     *
     * @param x 目标向量x分量
     * @param y 目标向量y分量
     * @return 结果向量
     */
    public Vector2D add(float x, float y) {
        return new Vector2D(x + point.x, y + point.y);
    }

    /**
     * 向量点乘
     *
     * @param vector 目标向量
     * @return 结果向量
     */
    public float dotProduct(Vector2D vector) {
        return dotProduct(vector.getX(), vector.getY());
    }

    /**
     * 向量点乘
     *
     * @param x 目标向量x分量
     * @param y 目标向量y分量
     * @return 结果向量
     */
    public float dotProduct(float x, float y) {
        return point.x * x + point.y * y;
    }

    /**
     * 向量叉乘
     *
     * @param vector 目标向量
     * @return 结果向量
     */
    public float crossProduct(Vector2D vector) {
        return crossProduct(vector.getX(), vector.getY());
    }

    /**
     * 向量叉乘
     *
     * @param x 目标向量x分量
     * @param y 目标向量y分量
     * @return 结果向量
     */
    public float crossProduct(float x, float y) {
        return point.x * y - point.y * x;
    }

    /**
     * 向量的模
     *
     * @return 模
     */
    public float length() {
        return (float) Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2));
    }

    /**
     * 向量的x分量归一化结果
     *
     * @return x分量归一化
     */
    public float getUnitX() {
        return point.x / length();
    }

    /**
     * 向量的y分量归一化结果
     *
     * @return y分量归一化
     */
    public float getUnitY() {
        return point.y / length();
    }

    /**
     * 向量的标量乘法
     *
     * @param scale 标量
     * @return 标量结果
     */
    public Vector2D scalarMultiply(float scale) {
        return new Vector2D(point.x * scale, point.y * scale);
    }
}
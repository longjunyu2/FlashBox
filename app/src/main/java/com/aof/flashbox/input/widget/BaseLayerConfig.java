package com.aof.flashbox.input.widget;

public abstract class BaseLayerConfig {

    public enum Type {
        Root,
        GameButton,
        GameDPad
    }

    // 比例因子
    public static int ScreenDiv = 100;

    // 控制层宽
    private int width;

    // 控制层高
    private int height;

    // 控制层中心x坐标 以屏幕左上角为原点
    private float x;

    // 控制层中心y坐标，以屏幕左上角为原点
    private float y;

    /**
     * 获取控制层类型
     *
     * @return 控制层类型
     */
    abstract public Type getType();

    /**
     * 设置控制层宽度
     *
     * @param width 控制层宽度
     * @return this
     */
    public BaseLayerConfig setWidth(int width) {
        this.width = width;
        return this;
    }

    /**
     * 获取控制层宽度
     *
     * @return 控制层宽度
     */
    public int getWidth() {
        return width;
    }

    /**
     * 设置控制层高度
     *
     * @param height 控制层高度
     * @return this
     */
    public BaseLayerConfig setHeight(int height) {
        this.height = height;
        return this;
    }

    /**
     * 获取控制层高度
     *
     * @return 控制层高度
     */
    public int getHeight() {
        return height;
    }

    /**
     * 设置控制层中心的X坐标
     *
     * @param x x坐标
     * @return this
     */
    public BaseLayerConfig setX(float x) {
        this.x = x;
        return this;
    }

    /**
     * 获取控制层中心的X坐标
     *
     * @return x坐标
     */
    public float getX() {
        return x;
    }

    /**
     * 设置控制层中心的Y坐标
     *
     * @param y y坐标
     * @return this
     */
    public BaseLayerConfig setY(float y) {
        this.y = y;
        return this;
    }

    /**
     * 获取控制层中心的Y坐标
     *
     * @return y坐标
     */
    public float getY() {
        return y;
    }

}

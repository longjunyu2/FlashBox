package com.aof.flashbox.input.widget;

public class RootLayerConfig extends BaseLayerConfig {

    private boolean enableKeyboard;

    private boolean enableController;

    private String keyboard = "";

    private String controller = "";

    public RootLayerConfig() {
        super();

        // 根控制层应该占满屏幕
        setHeight(ScreenDiv);
        setWidth(ScreenDiv);
        setX(ScreenDiv / 2f);
        setY(ScreenDiv / 2f);
    }

    /**
     * 设置启用物理键盘输入
     *
     * @param enable 是否启用
     * @return this
     */
    public RootLayerConfig setEnableKeyboard(boolean enable) {
        enableKeyboard = enable;
        return this;
    }

    /**
     * 获取是否启用物理哦键盘输入
     *
     * @return 是否启用
     */
    public boolean isEnableKeyboard() {
        return enableKeyboard;
    }

    /**
     * 设置启用物理控制器输入
     *
     * @param enable 是否启用
     * @return this
     */
    public RootLayerConfig setEnableController(boolean enable) {
        enableController = enable;
        return this;
    }

    /**
     * 获取是否启用物理控制器输入
     *
     * @return 是否启用
     */
    public boolean isEnableController() {
        return enableController;
    }

    /**
     * 设置物理键盘设备名
     *
     * @param keyboardName 设备名
     * @return this
     */
    public RootLayerConfig setKeyboard(String keyboardName) {
        keyboard = keyboardName;
        return this;
    }

    /**
     * 获取物理键盘设备名
     *
     * @return 设备名
     */
    public String getKeyboard() {
        return keyboard;
    }

    /**
     * 设置物理控制器设备名
     *
     * @param controllerName 设备名
     * @return this
     */
    public RootLayerConfig setController(String controllerName) {
        controller = controllerName;
        return this;
    }

    /**
     * 获取物理控制器设备名
     *
     * @return 设备名
     */
    public String getController() {
        return controller;
    }

    @Override
    public Type getType() {
        return Type.Root;
    }
}

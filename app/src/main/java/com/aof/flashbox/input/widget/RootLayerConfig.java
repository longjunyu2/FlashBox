package com.aof.flashbox.input.widget;

import android.view.InputDevice;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Objects;

public class RootLayerConfig extends BaseLayerConfig {

    private boolean enableKeyboard;

    private boolean enableController;

    private InputDeviceInfo keyboard = new InputDeviceInfo();

    private InputDeviceInfo controller = new InputDeviceInfo();

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
     * 设置物理键盘设备
     *
     * @param keyboardInfo 设备
     * @return this
     */
    public RootLayerConfig setKeyboard(InputDeviceInfo keyboardInfo) {
        keyboard = keyboardInfo;
        return this;
    }

    /**
     * 获取物理键盘设备
     *
     * @return 设备
     */
    public InputDeviceInfo getKeyboard() {
        return keyboard;
    }

    /**
     * 设置物理控制器设备
     *
     * @param controllerInfo 设备
     * @return this
     */
    public RootLayerConfig setController(InputDeviceInfo controllerInfo) {
        controller = controllerInfo;
        return this;
    }

    /**
     * 获取物理控制器设备
     *
     * @return 设备
     */
    public InputDeviceInfo getController() {
        return controller;
    }

    @Override
    public Type getType() {
        return Type.Root;
    }

    public static class InputDeviceInfo {
        // 四者构成唯一标识符
        public String name;
        public int vendorId;
        public int productId;
        public String descriptor;
        // DeviceId是动态的，有可能在系统状态发生时改变，因此不足以作为唯一标识符
        public int deviceId;

        public InputDeviceInfo() {
            this("", 0, 0, 0, "");
        }

        public InputDeviceInfo(InputDevice device) {
            this(device.getName(), device.getVendorId(), device.getProductId(), device.getId(), device.getDescriptor());
        }

        public InputDeviceInfo(String name, int vendorId, int productId, int deviceId, String descriptor) {
            this.name = name;
            this.vendorId = vendorId;
            this.productId = productId;
            this.deviceId = deviceId;
            this.descriptor = descriptor;
        }

        @Override
        public boolean equals(@Nullable Object obj) {
            if (this == obj)
                return true;

            if (obj != null && getClass() == obj.getClass()) {
                InputDeviceInfo target = (InputDeviceInfo) obj;
                return Objects.equals(name, target.name)
                        && vendorId == target.vendorId
                        && productId == target.productId
                        && Objects.equals(descriptor, target.descriptor);
            }

            return false;
        }

        @NonNull
        @Override
        public String toString() {
            return String.format("%s (%X:%X:%s)", name, vendorId, productId, descriptor.substring(0, 4));
        }
    }
}

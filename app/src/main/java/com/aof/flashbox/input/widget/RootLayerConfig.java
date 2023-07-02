package com.aof.flashbox.input.widget;

import android.view.InputDevice;
import android.view.KeyEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.aof.flashbox.input.key.KeyCodes;

import java.util.Objects;

public class RootLayerConfig extends BaseLayerConfig {

    private boolean enableKeyboard;

    private boolean enableController;

    private InputDeviceInfo keyboard = new InputDeviceInfo();

    private InputDeviceInfo controller = new InputDeviceInfo();

    private ControllerBtn controllerBtn = new ControllerBtn();

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

    /**
     * 设置物理控制器的按键映射
     *
     * @param controllerBtn 物理控制器按键
     * @return this
     */
    public RootLayerConfig setControllerBtn(ControllerBtn controllerBtn) {
        this.controllerBtn = controllerBtn;
        return this;
    }

    /**
     * 获取物理控制器的按键映射
     *
     * @return 物理控制器按键
     */
    public ControllerBtn getControllerBtn() {
        return controllerBtn;
    }

    @Override
    public Type getType() {
        return Type.Root;
    }

    public static class InputDeviceInfo {
        // 四者构成唯一标识符
        public final String name;
        public final int vendorId;
        public final int productId;
        public final String descriptor;
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

    public static class ControllerBtn {
        public String key_Button_A = "";
        public String key_Button_B = "";
        public String key_Button_X = "";
        public String key_Button_Y = "";
        public String key_Button_L1 = "";
        public String key_Button_R1 = "";
        public String key_Button_L2 = "";
        public String key_Button_R2 = "";
        public String key_Button_Back = "";
        public String key_Button_Select = "";
        public String key_Button_Start = "";
        public String key_Button_ThumbL = "";
        public String key_Button_ThumbR = "";
        public String key_Button_Up = "";
        public String key_Button_Down = "";
        public String key_Button_Left = "";
        public String key_Button_Right = "";

        public KeyCodes.Codes getKey(int androidKeyCode) {
            switch (androidKeyCode) {
                case KeyEvent.KEYCODE_BUTTON_A:
                    return keyCode(key_Button_A);
                case KeyEvent.KEYCODE_BUTTON_B:
                    return keyCode(key_Button_B);
                case KeyEvent.KEYCODE_BUTTON_X:
                    return keyCode(key_Button_X);
                case KeyEvent.KEYCODE_BUTTON_Y:
                    return keyCode(key_Button_Y);
                case KeyEvent.KEYCODE_BUTTON_L1:
                    return keyCode(key_Button_L1);
                case KeyEvent.KEYCODE_BUTTON_R1:
                    return keyCode(key_Button_R1);
                case KeyEvent.KEYCODE_BUTTON_L2:
                    return keyCode(key_Button_L2);
                case KeyEvent.KEYCODE_BUTTON_R2:
                    return keyCode(key_Button_R2);
                case KeyEvent.KEYCODE_BACK:
                    return keyCode(key_Button_Back);
                case KeyEvent.KEYCODE_BUTTON_SELECT:
                    return keyCode(key_Button_Select);
                case KeyEvent.KEYCODE_BUTTON_START:
                    return keyCode(key_Button_Start);
                case KeyEvent.KEYCODE_BUTTON_THUMBL:
                    return keyCode(key_Button_ThumbL);
                case KeyEvent.KEYCODE_BUTTON_THUMBR:
                    return keyCode(key_Button_ThumbR);
                case KeyEvent.KEYCODE_DPAD_UP:
                    return keyCode(key_Button_Up);
                case KeyEvent.KEYCODE_DPAD_DOWN:
                    return keyCode(key_Button_Down);
                case KeyEvent.KEYCODE_DPAD_LEFT:
                    return keyCode(key_Button_Left);
                case KeyEvent.KEYCODE_DPAD_RIGHT:
                    return keyCode(key_Button_Right);
                default:
                    return null;
            }
        }

        /**
         * 从键名获取键值，同时排除实际键值为Unknown的键
         *
         * @param keyName 键名
         * @return 键值
         */
        private KeyCodes.Codes keyCode(String keyName) {
            if (keyName.equals(""))
                return null;

            KeyCodes.Codes key = null;

            try {
                key = KeyCodes.Codes.valueOf(keyName);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            if (key != null && key.value() == KeyCodes.Codes.Unknown.value())
                return null;

            return key;
        }

    }
}

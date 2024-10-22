package com.aof.flashbox.input.widget;

import android.view.InputDevice;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Objects;

public class RootLayerConfig extends BaseLayerConfig {

    private boolean enableKeyboard;

    private boolean enableController;

    private InputDeviceInfo keyboard;

    private InputDeviceInfo controller;

    private ControllerBtn controllerBtn;

    private final ControllerAxes controllerAxes;

    public RootLayerConfig() {
        super();
        this.keyboard = new InputDeviceInfo();
        this.controller = new InputDeviceInfo();
        this.controllerBtn = new ControllerBtn();
        this.controllerAxes = new ControllerAxes();

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
     * 获取物理控制器的按键映射
     *
     * @return 物理控制器按键
     */
    public ControllerBtn getControllerBtn() {
        return controllerBtn;
    }

    /**
     * 获取所有轴配置
     *
     * @return 轴配置
     */
    public ControllerAxes getControllerAxes() {
        return this.controllerAxes;
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

    public static class ControllerAxis {
        // 轴标识
        public int axis_flag;
        // 轴方向
        public int dir;
        // 触发值 百分比
        public int trigger_per_value;
        // 键名
        public ArrayList<String> key;
        // 默认触发值
        private final static int DefaultTriggerValue = 20;

        public ControllerAxis(int axis_flag, int dir, int trigger_per_value, ArrayList<String> key) {
            this.axis_flag = axis_flag;
            this.dir = dir;
            this.trigger_per_value = trigger_per_value;
            this.key = key;
        }

        public ControllerAxis(int axis_flag, int dir, ArrayList<String> key) {
            this(axis_flag, dir, DefaultTriggerValue, key);
        }

        public ControllerAxis(int axis_flag, int dir) {
            this(axis_flag, dir, DefaultTriggerValue, new ArrayList<>());
        }

        /**
         * 生成默认的轴配置
         *
         * @return 全部的轴配置
         */
        public static ControllerAxis[] createDefaultAxes() {
            return new ControllerAxis[]{
                    // 苦力帽(方向键/十字键)
                    new ControllerAxis(MotionEvent.AXIS_HAT_X, 1),
                    new ControllerAxis(MotionEvent.AXIS_HAT_X, -1),
                    new ControllerAxis(MotionEvent.AXIS_HAT_Y, 1),
                    new ControllerAxis(MotionEvent.AXIS_HAT_Y, -1),
                    // 左摇杆
                    new ControllerAxis(MotionEvent.AXIS_X, 1),
                    new ControllerAxis(MotionEvent.AXIS_X, -1),
                    new ControllerAxis(MotionEvent.AXIS_Y, 1),
                    new ControllerAxis(MotionEvent.AXIS_Y, -1),
                    // 右摇杆
                    new ControllerAxis(MotionEvent.AXIS_Z, 1),
                    new ControllerAxis(MotionEvent.AXIS_Z, -1),
                    new ControllerAxis(MotionEvent.AXIS_RZ, 1),
                    new ControllerAxis(MotionEvent.AXIS_RZ, -1),
                    // 左扳机
                    new ControllerAxis(MotionEvent.AXIS_BRAKE, 1),
                    // 右扳机
                    new ControllerAxis(MotionEvent.AXIS_GAS, 1)
            };
        }
    }

    public static class ControllerBtn {
        public ArrayList<String> key_Button_A;
        public ArrayList<String> key_Button_B;
        public ArrayList<String> key_Button_X;
        public ArrayList<String> key_Button_Y;
        public ArrayList<String> key_Button_L1;
        public ArrayList<String> key_Button_R1;
        public ArrayList<String> key_Button_Select;
        public ArrayList<String> key_Button_Start;
        public ArrayList<String> key_Button_ThumbL;
        public ArrayList<String> key_Button_ThumbR;

        public ControllerBtn() {
            key_Button_A = new ArrayList<>();
            key_Button_B = new ArrayList<>();
            key_Button_X = new ArrayList<>();
            key_Button_Y = new ArrayList<>();
            key_Button_L1 = new ArrayList<>();
            key_Button_R1 = new ArrayList<>();
            key_Button_Select = new ArrayList<>();
            key_Button_Start = new ArrayList<>();
            key_Button_ThumbL = new ArrayList<>();
            key_Button_ThumbR = new ArrayList<>();
        }
    }

    public static class ControllerAxes {
        // 左摇杆
        public ControllerAxis axis_x_p;
        public ControllerAxis axis_x_n;
        public ControllerAxis axis_y_p;
        public ControllerAxis axis_y_n;
        // 右摇杆
        public ControllerAxis axis_z_p;
        public ControllerAxis axis_z_n;
        public ControllerAxis axis_rz_p;
        public ControllerAxis axis_rz_n;
        // 左扳机
        public ControllerAxis axis_brake;
        // 右扳机
        public ControllerAxis axis_gas;
        // 苦力帽(方向键/十字键)
        public ControllerAxis axis_hat_x_p;
        public ControllerAxis axis_hat_x_n;
        public ControllerAxis axis_hat_y_p;
        public ControllerAxis axis_hat_y_n;

        public ControllerAxes() {
            axis_x_p = new ControllerAxis(MotionEvent.AXIS_X, 1);
            axis_x_n = new ControllerAxis(MotionEvent.AXIS_X, -1);
            axis_y_p = new ControllerAxis(MotionEvent.AXIS_Y, 1);
            axis_y_n = new ControllerAxis(MotionEvent.AXIS_Y, -1);
            axis_z_p = new ControllerAxis(MotionEvent.AXIS_Z, 1);
            axis_z_n = new ControllerAxis(MotionEvent.AXIS_Z, -1);
            axis_rz_p = new ControllerAxis(MotionEvent.AXIS_RZ, 1);
            axis_rz_n = new ControllerAxis(MotionEvent.AXIS_RZ, -1);
            axis_brake = new ControllerAxis(MotionEvent.AXIS_BRAKE, 1);
            axis_gas = new ControllerAxis(MotionEvent.AXIS_GAS, 1);
            axis_hat_x_p = new ControllerAxis(MotionEvent.AXIS_HAT_X, 1);
            axis_hat_x_n = new ControllerAxis(MotionEvent.AXIS_HAT_X, -1);
            axis_hat_y_p = new ControllerAxis(MotionEvent.AXIS_HAT_Y, 1);
            axis_hat_y_n = new ControllerAxis(MotionEvent.AXIS_HAT_Y, -1);
        }

        public ControllerAxis[] getAxes() {
            return new ControllerAxis[]{axis_x_p, axis_x_n, axis_y_p, axis_y_n,
                    axis_z_p, axis_z_n, axis_rz_p, axis_rz_n, axis_brake, axis_gas,
                    axis_hat_x_p, axis_hat_x_n, axis_hat_y_p, axis_hat_y_n};
        }
    }
}

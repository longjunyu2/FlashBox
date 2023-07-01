package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.input.InputManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aof.flashbox.R;
import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.dialog.RootEditDialog;
import com.aof.flashbox.input.driver.BaseDriver;
import com.aof.flashbox.input.driver.ControllerDriver;
import com.aof.flashbox.input.driver.KeyboardDriver;
import com.aof.flashbox.input.event.BaseInputEvent;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class RootLayer extends BaseLayer implements View.OnKeyListener, InputManager.InputDeviceListener {

    private FrameLayout layout;

    private KeyboardDriver keyboardDriver;

    private ControllerDriver controllerDriver;

    private final InputManager inputManager;

    public RootLayer(IInputAgent agent, RootLayerConfig config) {
        super(agent, config);
        inputManager = (InputManager) agent.getContext().getSystemService(Context.INPUT_SERVICE);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        layout = new FrameLayout(getAgent().getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));
        layout.setFocusable(true);

        // 设置触摸监听器
        layout.setOnTouchListener(this);

        // 创建键盘输入驱动器
        keyboardDriver = new KeyboardDriver(getConfig());
        keyboardDriver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });

        // 创建手柄控制器
        controllerDriver = new ControllerDriver(getConfig());
        keyboardDriver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });

        // 设置物理按键监听器
        layout.setOnKeyListener(this);

        // 设置输入设备监听器
        inputManager.registerInputDeviceListener(this, null);

        // 更新物理设备的DeviceId
        updateInputDeviceId(getConfig().getKeyboard());
        updateInputDeviceId(getConfig().getController());

        // TODO: 添加loop进程实现对手柄轴事件的处理
    }

    @Override
    public ViewGroup getView() {
        return layout;
    }

    @Override
    public void updateDivX() {
        // 禁止修改x坐标
    }

    @Override
    public void updateDivY() {
        // 禁止修改y坐标
    }

    @Override
    public void updateDivW() {
        // 禁止修改宽度
    }

    @Override
    public void updateDivH() {
        // 禁止修改高度
    }

    @Override
    public void setSelected(boolean selected) {
        // 禁止选中
    }

    @Override
    public void openEditDialog() {
        new RootEditDialog(getAgent().getContext(), getConfig()).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getAgent().getStatus() == Status.Lock) {
            // TODO: 使用驱动器生成事件
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
            openEditDialog();
        return true;
    }

    @Override
    public RootLayerConfig getConfig() {
        return (RootLayerConfig) super.getConfig();
    }

    @Override
    public void destroy() {
        super.destroy();
        // 注销输入设备监听器
        inputManager.unregisterInputDeviceListener(this);
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        boolean ret = false;

        // 优先使用键盘驱动器
        if (getConfig().isEnableKeyboard())
            ret = keyboardDriver.keyEvent_to_inputEvent(event);

        // 其次使用手柄驱动器
        if (!ret && getConfig().isEnableController())
            ret = controllerDriver.keyEvent_to_inputEvent(event);

        return ret;
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        // 物理键盘连接
        if (getConfig().isEnableKeyboard()
                && getConfig().getKeyboard().equals(
                new RootLayerConfig.InputDeviceInfo(inputManager.getInputDevice(deviceId)))) {
            // 更新DeviceId
            updateInputDeviceId(getConfig().getKeyboard());
            // 显示键盘可用提示对话框
            new MaterialAlertDialogBuilder(getAgent().getContext())
                    .setTitle(R.string.note)
                    .setMessage(R.string.keyboard_available)
                    .setPositiveButton(R.string.ok, null)
                    .create()
                    .show();
        }

        // 物理控制器连接
        if (getConfig().isEnableController()
                && getConfig().getController().equals(
                new RootLayerConfig.InputDeviceInfo(inputManager.getInputDevice(deviceId)))) {
            // 更新DeviceId
            updateInputDeviceId(getConfig().getController());
            // 显示控制器可用提示对话框
            new MaterialAlertDialogBuilder(getAgent().getContext())
                    .setTitle(R.string.note)
                    .setMessage(R.string.controller_available)
                    .setPositiveButton(R.string.ok, null)
                    .create()
                    .show();
        }
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        // 物理键盘断开
        if (getConfig().isEnableKeyboard() && getConfig().getKeyboard().deviceId == deviceId)
            // 显示键盘断开提示对话框
            new MaterialAlertDialogBuilder(getAgent().getContext())
                    .setTitle(R.string.note)
                    .setMessage(R.string.keyboard_unavailable)
                    .setPositiveButton(R.string.ok, null)
                    .create()
                    .show();

        // 物理控制器断开
        if (getConfig().isEnableController() && getConfig().getController().deviceId == deviceId)
            // 显示控制器断开对话框
            new MaterialAlertDialogBuilder(getAgent().getContext())
                    .setTitle(R.string.note)
                    .setMessage(R.string.controller_unavailable)
                    .setPositiveButton(R.string.ok, null)
                    .create()
                    .show();
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    /**
     * 更新输入设备动态的DeviceId值
     *
     * @param deviceInfo 设备信息
     */
    private void updateInputDeviceId(RootLayerConfig.InputDeviceInfo deviceInfo) {
        for (int deviceId : inputManager.getInputDeviceIds()) {
            if (deviceInfo.equals(new RootLayerConfig.InputDeviceInfo(
                    inputManager.getInputDevice(deviceId)))) {
                deviceInfo.deviceId = deviceId;
                return;
            }
        }
    }
}

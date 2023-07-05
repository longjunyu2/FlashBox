package com.aof.flashbox.input.driver;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.ACTION_UP;

import android.view.KeyEvent;
import android.view.MotionEvent;

import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.widget.RootLayerConfig;

import java.util.ArrayList;
import java.util.List;

public class ControllerDriver extends BaseDriver {

    private final List<RootLayerConfig.ControllerAxis> triggeredList;

    public ControllerDriver(RootLayerConfig config) {
        super(config);
        triggeredList = new ArrayList<>();
    }

    @Override
    public RootLayerConfig getConfig() {
        return (RootLayerConfig) super.getConfig();
    }

    /**
     * 从MotionEvent生成BaseInputEvent
     *
     * @param event MotionEvent
     * @return 是否拦截事件
     */
    public boolean motionEvent_to_inputEvent(MotionEvent event) {
        // 判断是否来自选定的控制器设备
        if (getConfig().getController().deviceId != event.getDeviceId())
            return false;

        // 遍历所有轴
        RootLayerConfig.ControllerAxis[] axes = getConfig().getControllerAxes();
        for (RootLayerConfig.ControllerAxis axis : axes) {
            // 检查键值是否可用
            ArrayList<KeyCodes.Codes> keys = keyCode(axis.key);
            if (keys.size() == 0)
                continue;

            // 处理轴信息
            float realValue = event.getAxisValue(axis.axis_flag) * axis.dir;
            float triggerValue = axis.trigger_per_value * 0.01f;
            com.aof.flashbox.input.event.KeyEvent.Action action;

            if (realValue > triggerValue) {
                // 正触发
                if (!triggeredList.contains(axis)) {
                    action = com.aof.flashbox.input.event.KeyEvent.Action.Down;
                    triggeredList.add(axis);
                } else
                    continue;
            } else if (realValue < triggerValue) {
                // 负触发
                if (triggeredList.contains(axis)) {
                    action = com.aof.flashbox.input.event.KeyEvent.Action.Up;
                    triggeredList.remove(axis);
                } else
                    continue;
            } else {
                continue;
            }

            // 生成输入事件
            for (KeyCodes.Codes key : keys)
                getOnEventGenCallback().onEventGen(new com.aof.flashbox.input.event.KeyEvent(action, key));
        }

        return true;
    }

    /**
     * 从KeyEvent生成BaseInputEvent
     *
     * @param event KeyEvent
     * @return 是否拦截事件
     */
    public boolean keyEvent_to_inputEvent(KeyEvent event) {
        // 判断是否来自选定的控制器设备, 是否为第一次触发
        if (getConfig().getController().deviceId != event.getDeviceId()
                || event.getRepeatCount() != 0)
            return false;

        // 获取映射键值并判断键值是否可用
        ArrayList<KeyCodes.Codes> keys = getKeys(event.getKeyCode());
        assert keys != null;
        if (keys.size() == 0)
            return true;

        // 获取动作
        com.aof.flashbox.input.event.KeyEvent.Action action;
        switch (event.getAction()) {
            case ACTION_DOWN:
                action = com.aof.flashbox.input.event.KeyEvent.Action.Down;
                break;
            case ACTION_UP:
                action = com.aof.flashbox.input.event.KeyEvent.Action.Up;
                break;
            default:
                return true;
        }

        // 生成输入事件
        for (KeyCodes.Codes key : keys)
            getOnEventGenCallback().onEventGen(new com.aof.flashbox.input.event.KeyEvent(action, key));

        return true;
    }

    /**
     * 获取手柄按钮对应的键
     *
     * @param androidKeyCode Android键值
     * @return 键
     */
    private ArrayList<KeyCodes.Codes> getKeys(int androidKeyCode) {
        RootLayerConfig.ControllerBtn btn = getConfig().getControllerBtn();
        switch (androidKeyCode) {
            case KeyEvent.KEYCODE_BUTTON_A:
                return keyCode(btn.key_Button_A);
            case KeyEvent.KEYCODE_BUTTON_B:
                return keyCode(btn.key_Button_B);
            case KeyEvent.KEYCODE_BUTTON_X:
                return keyCode(btn.key_Button_X);
            case KeyEvent.KEYCODE_BUTTON_Y:
                return keyCode(btn.key_Button_Y);
            case KeyEvent.KEYCODE_BUTTON_L1:
                return keyCode(btn.key_Button_L1);
            case KeyEvent.KEYCODE_BUTTON_R1:
                return keyCode(btn.key_Button_R1);
            case KeyEvent.KEYCODE_BUTTON_SELECT:
                return keyCode(btn.key_Button_Select);
            case KeyEvent.KEYCODE_BUTTON_START:
                return keyCode(btn.key_Button_Start);
            case KeyEvent.KEYCODE_BUTTON_THUMBL:
                return keyCode(btn.key_Button_ThumbL);
            case KeyEvent.KEYCODE_BUTTON_THUMBR:
                return keyCode(btn.key_Button_ThumbR);
            default:
                return null;
        }
    }

    /**
     * 从键名获取键值，同时排除实际键值为Unknown的键
     *
     * @param keyNames 键名
     * @return 键值
     */
    private ArrayList<KeyCodes.Codes> keyCode(ArrayList<String> keyNames) {
        ArrayList<KeyCodes.Codes> keys = new ArrayList<>();

        for (String keyName : keyNames) {
            KeyCodes.Codes key = null;

            try {
                key = KeyCodes.Codes.valueOf(keyName);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }

            if (key != null && key.value() != KeyCodes.Codes.Unknown.value())
                keys.add(key);
        }

        return keys;
    }
}

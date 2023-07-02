package com.aof.flashbox.input.driver;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.ACTION_UP;

import android.view.KeyEvent;

import com.aof.flashbox.input.key.AndroidKeyMap;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.widget.RootLayerConfig;

public class KeyboardDriver extends BaseDriver {

    public KeyboardDriver(RootLayerConfig config) {
        super(config);
    }

    @Override
    public RootLayerConfig getConfig() {
        return (RootLayerConfig) super.getConfig();
    }

    /**
     * 从KeyEvent生成BaseInputEvent
     *
     * @param event KeyEvent
     * @return 是否拦截事件
     */
    public boolean keyEvent_to_inputEvent(KeyEvent event) {
        // 判断是否来自选定的键盘设备，是否为第一次触发
        if (getConfig().getKeyboard().deviceId != event.getDeviceId()
                || event.getRepeatCount() != 0)
            return false;

        // 判断是否是可用键值
        KeyCodes.Codes key = AndroidKeyMap.getAndroidKeyMap().get(event.getKeyCode());
        if (key == null)
            // 即使键值不可用依然消费掉该事件
            return true;

        // 判断是否为可用的动作
        com.aof.flashbox.input.event.KeyEvent.Action action;
        switch (event.getAction()) {
            case ACTION_DOWN:
                action = com.aof.flashbox.input.event.KeyEvent.Action.Down;
                break;
            case ACTION_UP:
                action = com.aof.flashbox.input.event.KeyEvent.Action.Up;
                break;
            default:
                return false;
        }

        // 生成输入事件
        getOnEventGenCallback().onEventGen(new com.aof.flashbox.input.event.KeyEvent(action, key));
        return true;
    }
}

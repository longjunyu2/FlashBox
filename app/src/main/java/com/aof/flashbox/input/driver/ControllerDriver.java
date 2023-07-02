package com.aof.flashbox.input.driver;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.ACTION_UP;

import android.view.KeyEvent;

import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.widget.RootLayerConfig;

public class ControllerDriver extends BaseDriver {

    public ControllerDriver(RootLayerConfig config) {
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
        // 判断是否来自选定的控制器设备
        if (getConfig().getController().deviceId != event.getDeviceId())
            return false;

        // 获取映射键值并判断键值是否可用
        KeyCodes.Codes key = getConfig().getControllerBtn().getKey(event.getKeyCode());
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

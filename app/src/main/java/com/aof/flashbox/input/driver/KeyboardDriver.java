package com.aof.flashbox.input.driver;

import android.view.KeyEvent;

import com.aof.flashbox.input.widget.RootLayerConfig;

public class KeyboardDriver extends BaseDriver {

    public KeyboardDriver(RootLayerConfig config) {
        super(config);
    }

    /**
     * 从KeyEvent生成BaseInputEvent
     *
     * @param event KeyEvent
     * @return 是否拦截事件
     */
    public boolean keyEvent_to_inputEvent(KeyEvent event) {
        // TODO: 实现对按键事件的处理
        return true;
    }
}

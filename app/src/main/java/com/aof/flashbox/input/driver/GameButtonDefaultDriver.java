package com.aof.flashbox.input.driver;

import android.view.MotionEvent;

import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.event.KeyEvent;
import com.aof.flashbox.input.widget.GameButtonLayerConfig;

import java.util.List;

public class GameButtonDefaultDriver extends BaseDriver {

    private boolean keepPressing = false;

    public GameButtonDefaultDriver(GameButtonLayerConfig config) {
        super(config);
    }

    /**
     * 从MotionEvent生成BaseInputEvent
     *
     * @param event MotionEvent
     */
    public void motionEvent_to_inputEvent(MotionEvent event) {
        KeyEvent.Action action = null;

        if (!getConfig().isKeepPress()) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    action = KeyEvent.Action.Down;
                    break;
                case MotionEvent.ACTION_UP:
                    action = KeyEvent.Action.Up;
                    break;
            }
        } else {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (keepPressing) {
                    action = KeyEvent.Action.Up;
                    keepPressing = false;
                } else {
                    action = KeyEvent.Action.Down;
                    keepPressing = true;
                }
            }
        }

        if (action != null) {
            List<KeyCodes.Codes> keys = getConfig().getKeys();
            for (KeyCodes.Codes key : keys)
                getOnEventGenCallback().onEventGen(new KeyEvent(action, key));
        }
    }

    @Override
    public GameButtonLayerConfig getConfig() {
        return (GameButtonLayerConfig) super.getConfig();
    }
}

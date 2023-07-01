package com.aof.flashbox.input.event;

import com.aof.flashbox.input.key.KeyCodes;

public class KeyEvent extends BaseInputEvent {
    public enum Action {
        Down,
        Up
    }

    public Action action;

    public KeyCodes.Codes key;

    public KeyEvent(Action action, KeyCodes.Codes key) {
        this.action = action;
        this.key = key;
    }

    @Override
    public Type getType() {
        return Type.KeyEvent;
    }
}

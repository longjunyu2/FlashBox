package com.aof.flashbox.input.event;

public abstract class BaseInputEvent {

    public enum Type {
        KeyEvent,
        PointerEvent
    }

    /**
     * 获取输入事件类型
     *
     * @return 事件类型
     */
    abstract public Type getType();

}

package com.aof.flashbox.input.widget;

public class RootLayerConfig extends BaseLayerConfig {

    public RootLayerConfig() {
        super();

        // 根控制层应该占满屏幕
        setHeight(ScreenDiv);
        setWidth(ScreenDiv);
        setX(ScreenDiv / 2f);
        setY(ScreenDiv / 2f);
    }

    @Override
    public Type getType() {
        return Type.Root;
    }
}

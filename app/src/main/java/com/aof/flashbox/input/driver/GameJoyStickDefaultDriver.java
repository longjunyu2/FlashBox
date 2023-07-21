package com.aof.flashbox.input.driver;

import com.aof.flashbox.input.widget.GameJoystickLayerConfig;

public class GameJoyStickDefaultDriver extends GameDPadDefaultDriver {

    public GameJoyStickDefaultDriver(GameJoystickLayerConfig config) {
        super(config);
    }

    @Override
    public GameJoystickLayerConfig getConfig() {
        return (GameJoystickLayerConfig) super.getConfig();
    }
}

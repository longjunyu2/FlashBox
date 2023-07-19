package com.aof.flashbox.input.widget;

import com.aof.flashbox.input.IInputAgent;

public class LayerBuilder {
    private BaseLayerConfig mConfig;
    private IInputAgent mAgent;

    /**
     * 选择构建时使用的控制层配置
     *
     * @param config 控制层配置
     * @return 建造器
     */
    public LayerBuilder withConfig(BaseLayerConfig config) {
        this.mConfig = config;
        return this;
    }

    /**
     * 选择构建时使用的输入代理
     *
     * @param agent 输入代理
     * @return 建造器
     */
    public LayerBuilder withAgent(IInputAgent agent) {
        this.mAgent = agent;
        return this;
    }

    /**
     * 开始建造
     *
     * @return 控制层实例
     */
    public BaseLayer build() {
        BaseLayer layer;

        switch (mConfig.getType()) {
            case Root:
                layer = new RootLayer(mAgent, (RootLayerConfig) mConfig);
                break;
            case GameButton:
                layer = new GameButtonLayer(mAgent, (GameButtonLayerConfig) mConfig);
                break;
            case GameJoyStick:
                layer = new GameJoyStickLayer(mAgent, (GameJoystickLayerConfig) mConfig);
                break;
            default:
                layer = null;
        }

        return layer;
    }


}

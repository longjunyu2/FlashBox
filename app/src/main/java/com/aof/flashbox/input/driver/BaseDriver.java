package com.aof.flashbox.input.driver;

import com.aof.flashbox.input.event.BaseInputEvent;
import com.aof.flashbox.input.widget.BaseLayerConfig;

public abstract class BaseDriver {

    private final BaseLayerConfig config;

    private OnEventGenCallback onEventGencallback;

    public BaseDriver(BaseLayerConfig config) {
        this.config = config;
    }

    /**
     * 获取配置
     *
     * @return 配置
     */
    public BaseLayerConfig getConfig() {
        return config;
    }

    /**
     * 设置事件生成完成的回调
     *
     * @param callback 回调
     */
    public void setOnEventGenCallback(OnEventGenCallback callback) {
        this.onEventGencallback = callback;
    }

    /**
     * 获取事件生成完成的回调
     *
     * @return 回调
     */
    public OnEventGenCallback getOnEventGenCallback() {
        return onEventGencallback;
    }

    public interface OnEventGenCallback {
        void onEventGen(BaseInputEvent event);
    }
}

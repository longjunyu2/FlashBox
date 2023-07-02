package com.aof.flashbox.input;

import android.content.Context;

import com.aof.flashbox.input.event.BaseInputEvent;
import com.aof.flashbox.input.widget.BaseLayer;

public interface IInputAgent {
    /**
     * 获取控制层状态
     *
     * @return 控制层状态
     */
    BaseLayer.Status getStatus();

    /**
     * 获取上下文
     *
     * @return 上下文
     */
    Context getContext();

    /**
     * 获取屏幕像素宽度
     *
     * @return 像素宽度
     */
    int getWinPixelWidth();

    /**
     * 获取屏幕像素高度
     *
     * @return 像素高度
     */
    int getWinPixelHeight();

    /**
     * 获取每份的像素大小
     *
     * @return 每份的像素大小
     */
    float getPixelPerScreenDiv();

    /**
     * 提供输入事件
     * 实现IInputAgent的对象应该持有一个能够处理BaseInputEvent的实例
     *
     * @param event 输入事件
     */
    void offerEvent(BaseInputEvent event);

    /**
     * 添加到被选中列表
     *
     * @param layer 控制层
     */
    void addSelected(BaseLayer layer);

    /**
     * 获取是否启用震动
     *
     * @return 是否启用震动
     */
    boolean isVibrateEnabled();

}

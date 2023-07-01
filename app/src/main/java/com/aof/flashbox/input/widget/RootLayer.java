package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.dialog.RootEditDialog;
import com.aof.flashbox.input.driver.BaseDriver;
import com.aof.flashbox.input.driver.KeyboardDriver;
import com.aof.flashbox.input.event.BaseInputEvent;

public class RootLayer extends BaseLayer implements View.OnKeyListener {

    private FrameLayout layout;

    private KeyboardDriver keyboardDriver;

    public RootLayer(IInputAgent agent, RootLayerConfig config) {
        super(agent, config);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        layout = new FrameLayout(getAgent().getContext());
        layout.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // 设置触摸监听器
        layout.setOnTouchListener(this);

        // 创建键盘输入驱动器
        keyboardDriver = new KeyboardDriver(getConfig());
        keyboardDriver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });

        // 设置物理按键监听器
        layout.setOnKeyListener(this);

        // TODO: 添加loop进程实现对手柄轴事件的处理
    }

    @Override
    public ViewGroup getView() {
        return layout;
    }

    @Override
    public void updateDivX() {
        // 禁止修改x坐标
    }

    @Override
    public void updateDivY() {
        // 禁止修改y坐标
    }

    @Override
    public void updateDivW() {
        // 禁止修改宽度
    }

    @Override
    public void updateDivH() {
        // 禁止修改高度
    }

    @Override
    public void setSelected(boolean selected) {
        // 禁止选中
    }

    @Override
    public void openEditDialog() {
        new RootEditDialog(getAgent().getContext(), getConfig()).show();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (getAgent().getStatus() == Status.Lock) {
            // TODO: 使用驱动器生成事件
        } else if (motionEvent.getAction() == MotionEvent.ACTION_UP)
            openEditDialog();
        return true;
    }

    @Override
    public RootLayerConfig getConfig() {
        return (RootLayerConfig) super.getConfig();
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        // TODO: 使用键盘驱动器生成事件
        return false;
    }
}

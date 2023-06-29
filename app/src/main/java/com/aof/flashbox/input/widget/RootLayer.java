package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aof.flashbox.input.IInputAgent;

public class RootLayer extends BaseLayer {

    private FrameLayout layout;

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
        layout.setOnTouchListener(this);
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
        // TODO: 实现并创建一个编辑对话框
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
}

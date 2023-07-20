package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;

import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.dialog.GameButtonEditDialog;
import com.aof.flashbox.input.dialog.OnEditFinishedCallback;
import com.aof.flashbox.input.driver.BaseDriver;
import com.aof.flashbox.input.driver.GameButtonDefaultDriver;
import com.aof.flashbox.input.event.BaseInputEvent;

public class GameButtonLayer extends BaseLayer {

    private GameButton button;

    private GameButtonDefaultDriver driver;

    public GameButtonLayer(IInputAgent agent, GameButtonLayerConfig config) {
        super(agent, config);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        button = new GameButton(getAgent().getContext());
        button.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        button.setOnTouchListener(this);

        // 按config设置View
        GameButtonLayerConfig config = getConfig();
        // 如果未设置坐标则不做改变
        if (config.getX() != 0 || config.getY() != 0) {
            updateDivX();
            updateDivY();
        }
        // 如果未设置大小则不做改变
        if (config.getWidth() != 0 || config.getHeight() != 0) {
            updateDivW();
            updateDivH();
        }
        // 设置文本
        updateText();

        // 创建输入事件驱动器
        driver = new GameButtonDefaultDriver(getConfig());
        driver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });
    }

    /**
     * 更新文本
     */
    public void updateText() {
        String text = getConfig().getText();
        button.setText(text);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        // 状态为非Lock时，事件全部由父类处理，因此在这里只需要实现对Lock状态的处理
        if (!super.onTouch(view, motionEvent))
            driver.motionEvent_to_inputEvent(motionEvent);
        return true;
    }

    @Override
    public void updateAll() {
        super.updateAll();
        updateText();
    }

    @Override
    public View getView() {
        return button;
    }

    @Override
    public GameButtonLayerConfig getConfig() {
        return (GameButtonLayerConfig) super.getConfig();
    }

    @Override
    public void setSelected(boolean selected) {
        button.setSelected(selected);
    }

    @Override
    public void openEditDialog() {
        GameButtonEditDialog dialog = new GameButtonEditDialog(getAgent().getContext(), getConfig());
        dialog.setOnEditFinishedCallback(new OnEditFinishedCallback() {
            @Override
            public void onEditFinished() {
                // 编辑完成后更新控件
                updateAll();
            }
        });
        dialog.show();
    }

    private static class GameButton extends AppCompatButton {

        GradientDrawable drawable;

        private final static int StrokeWidth = 5;

        private boolean selected = false;

        private final Rect drawRect = new Rect();

        private final Paint mPaint = new Paint();

        public GameButton(@NonNull Context context) {
            super(context);

            init();
        }

        private void init() {
            // 设置画笔
            mPaint.setColor(Color.RED);
            mPaint.setStrokeWidth(StrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);

            // 设置背景
            drawable = new GradientDrawable();
            drawable.setColor(Color.TRANSPARENT);
            drawable.setStroke(8, Color.BLACK);
            drawable.setCornerRadius(180f);
            setBackground(drawable);

            // 设置文字对齐居中
            setGravity(Gravity.CENTER);
            // 设置文字单行显示
            setSingleLine(true);
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (selected) {
                getDrawingRect(drawRect);
                canvas.drawRect(drawRect, mPaint);
            }
        }
    }

}

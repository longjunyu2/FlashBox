package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.view.JoyView;
import com.aof.flashbox.input.view.graphics.Circle;

public class GameJoyStickLayer extends BaseLayer {

    private MJoyView joyView;

    public GameJoyStickLayer(IInputAgent agent, GameJoystickLayerConfig config) {
        super(agent, config);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        GameJoystickLayerConfig config = getConfig();

        joyView = new MJoyView(getAgent().getContext());
        joyView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        joyView.setOnTouchListener(this);
        // TODO: 实现相应的驱动器
        joyView.setOnJoyChangedListener(new JoyView.OnJoyChangedListener() {
            @Override
            public void onUp() {
                Log.e("test", "Up");
            }

            @Override
            public void onDown() {
                Log.e("test", "Down");
            }

            @Override
            public void onLeft() {
                Log.e("test", "Left");
            }

            @Override
            public void onRight() {
                Log.e("test", "Right");
            }

            @Override
            public void onCenter() {
                Log.e("test", "Center");
            }
        });

        if (config.getX() != 0 || config.getY() != 0) {
            updateDivX();
            updateDivY();
        }

        if (config.getWidth() != 0 || config.getHeight() != 0) {
            updateDivW();
            updateDivH();
        }

    }

    @Override
    public View getView() {
        return joyView;
    }

    @Override
    public void setSelected(boolean selected) {
        joyView.setSelected(selected);
    }

    @Override
    public void openEditDialog() {
        // TODO: 实现相应的编辑功能
    }

    @Override
    public GameJoystickLayerConfig getConfig() {
        return (GameJoystickLayerConfig) super.getConfig();
    }

    private static class MJoyView extends JoyView {
        private final static int MiddleBlue = Color.parseColor("#3498D8");

        private final static int StrokeWidth = 5;

        private boolean selected = false;

        private final Paint mPaint;

        public MJoyView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(MiddleBlue);
            mPaint.setStrokeWidth(StrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if (selected) {
                Circle circle = getCircle();
                canvas.drawCircle(circle.center.x, circle.center.y, circle.radius, mPaint);
            }
        }
    }
}

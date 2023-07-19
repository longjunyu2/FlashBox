package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.dialog.GameButtonEditDialog;
import com.aof.flashbox.input.dialog.GameJoyStickEditDialog;
import com.aof.flashbox.input.dialog.OnEditFinishedCallback;
import com.aof.flashbox.input.driver.BaseDriver;
import com.aof.flashbox.input.driver.GameJoyStickDefaultDriver;
import com.aof.flashbox.input.event.BaseInputEvent;
import com.aof.flashbox.input.view.JoyView;
import com.aof.flashbox.input.view.graphics.Circle;

public class GameJoyStickLayer extends BaseLayer {

    private MJoyView joyView;

    private GameJoyStickDefaultDriver driver;

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
        joyView.setOnJoyChangedListener(new JoyView.OnJoyChangedListener() {
            @Override
            public void onUp() {
                driver.up();
            }

            @Override
            public void onDown() {
                driver.down();
            }

            @Override
            public void onLeft() {
                driver.left();
            }

            @Override
            public void onRight() {
                driver.right();
            }

            @Override
            public void onRight_Up() {
                driver.right_up();
            }

            @Override
            public void onRight_Down() {
                driver.right_down();
            }

            @Override
            public void onLeft_Up() {
                driver.left_up();
            }

            @Override
            public void onLeft_Down() {
                driver.left_down();
            }

            @Override
            public void onCenter() {
                driver.center();
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

        driver = new GameJoyStickDefaultDriver(getConfig());
        driver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });

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
        GameJoyStickEditDialog dialog = new GameJoyStickEditDialog(getAgent().getContext(), getConfig());
        dialog.setOnEditFinishedCallback(new OnEditFinishedCallback() {
            @Override
            public void onEditFinished() {
                // 编辑完成后更新控件
                updateAll();
            }
        });
        dialog.show();
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

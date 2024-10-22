package com.aof.flashbox.input.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.aof.flashbox.input.IInputAgent;
import com.aof.flashbox.input.dialog.GameDPadEditDialog;
import com.aof.flashbox.input.dialog.OnEditFinishedCallback;
import com.aof.flashbox.input.driver.BaseDriver;
import com.aof.flashbox.input.driver.GameDPadDefaultDriver;
import com.aof.flashbox.input.event.BaseInputEvent;
import com.aof.flashbox.input.view.DPadView;

public class GameDPadLayer extends BaseLayer {

    private MDPadView dPadView;

    private GameDPadDefaultDriver driver;

    public GameDPadLayer(IInputAgent agent, GameDPadLayerConfig config) {
        super(agent, config);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void initView() {
        GameDPadLayerConfig config = getConfig();

        dPadView = new MDPadView(getAgent().getContext());
        dPadView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        dPadView.setOnDPadChangedListener(new DPadView.OnDPadChangedListener() {
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
        dPadView.setOnTouchListener(this);

        if (config.getX() != 0 || config.getY() != 0) {
            updateDivX();
            updateDivY();
        }

        if (config.getWidth() != 0 || config.getHeight() != 0) {
            updateDivW();
            updateDivH();
        }

        driver = new GameDPadDefaultDriver(getConfig());
        driver.setOnEventGenCallback(new BaseDriver.OnEventGenCallback() {
            @Override
            public void onEventGen(BaseInputEvent event) {
                getAgent().offerEvent(event);
            }
        });
    }

    @Override
    public View getView() {
        return dPadView;
    }

    @Override
    public void setSelected(boolean selected) {
        dPadView.setSelected(selected);
    }

    @Override
    public void openEditDialog() {
        GameDPadEditDialog dialog = new GameDPadEditDialog(getAgent().getContext(), getConfig());
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
    public GameDPadLayerConfig getConfig() {
        return (GameDPadLayerConfig) super.getConfig();
    }

    private static class MDPadView extends DPadView {

        private final static int StrokeWidth = 5;

        private boolean selected = false;

        private final Paint mPaint;

        private final Rect drawRect = new Rect();

        public MDPadView(Context context) {
            super(context);
            mPaint = new Paint();
            mPaint.setColor(Color.RED);
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
                getDrawingRect(drawRect);
                canvas.drawRect(drawRect, mPaint);
            }
        }
    }
}

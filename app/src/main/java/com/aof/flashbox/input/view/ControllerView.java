package com.aof.flashbox.input.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.aof.flashbox.R;

public class ControllerView extends View {

    private final static int SelectedColor = Color.parseColor("#BB00FF00");

    private final static int PointerColor = Color.parseColor("#FF0000FF");

    private final static int EndLineColor = Color.parseColor("#FFFF0000");

    private final Drawable xboxFrontDrawable;

    private final Rect xboxFrontRect = new Rect();

    private final NormalButton buttonX = new NormalButton(ControllerModuleType.Button_X);

    private final NormalButton buttonY = new NormalButton(ControllerModuleType.Button_Y);

    private final NormalButton buttonA = new NormalButton(ControllerModuleType.Button_A);

    private final NormalButton buttonB = new NormalButton(ControllerModuleType.Button_B);

    private final NormalButton buttonStart = new NormalButton(ControllerModuleType.Button_Start);

    private final NormalButton buttonBack = new NormalButton(ControllerModuleType.Button_Back);

    private final Joystick joystickLeft = new Joystick(ControllerModuleType.Joystick_Left);

    private final Joystick joystickRight = new Joystick(ControllerModuleType.Joystick_Right);

    private final Dpad dpad = new Dpad(ControllerModuleType.DPad);

    private final LRB l1 = new LRB(ControllerModuleType.Button_L1);

    private final LRB r1 = new LRB(ControllerModuleType.Button_R1);

    private final Trigger triggerLeft = new Trigger(ControllerModuleType.Trigger_L2);

    private final Trigger triggerRight = new Trigger(ControllerModuleType.Trigger_R2);

    private final Region[] regions = new Region[]{buttonX, buttonY, buttonA,
            buttonB, buttonStart, buttonBack, joystickLeft, joystickRight, dpad,
            l1, r1, triggerLeft, triggerRight};

    private OnModuleClickCallback onModuleClickCallback;

    private final Rect cachedRect = new Rect();

    private final Rect tmpRect = new Rect();

    private String inputDeviceDescriptor = "";

    public enum ControllerModuleType {
        Button_A,
        Button_B,
        Button_X,
        Button_Y,
        Button_Back,
        Button_Start,
        Button_L1,
        Button_R1,
        Button_L3,
        Button_R3,
        Joystick_Left,
        Joystick_Right,
        Trigger_L2,
        Trigger_R2,
        DPad
    }

    public interface OnModuleClickCallback {
        void onModuleClick(ControllerModuleType type);
    }

    public ControllerView(Context context) {
        this(context, null);
    }

    public ControllerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        xboxFrontDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.xbox_front, null);
    }

    public void setOnModuleClickCallback(OnModuleClickCallback callback) {
        onModuleClickCallback = callback;
    }

    public void setDescriptor(String descriptor) {
        inputDeviceDescriptor = descriptor;
    }

    private static class Circle {
        public PointF center;
        public float radius;

        public Circle() {
            this(0, 0, 0);
        }

        public Circle(float cx, float cy, float radius) {
            this(new PointF(cx, cy), radius);
        }

        public Circle(PointF center, float radius) {
            this.center = center;
            this.radius = radius;
        }

        public boolean isEmpty() {
            return radius == 0;
        }

        public boolean contains(PointF p) {
            if (isEmpty())
                return false;
            return Math.sqrt(Math.pow(p.x - center.x, 2) + Math.pow(p.y - center.y, 2)) < radius;
        }

        public boolean contains(float x, float y) {
            return contains(new PointF(x, y));
        }

        public void set(float cx, float cy, float radius) {
            center.x = cx;
            center.y = cy;
            this.radius = radius;
        }
    }

    private interface Region {
        boolean contains(float x, float y);

        void onKeyEvent(KeyEvent event);

        void onMotionEvent(MotionEvent event);

        void onDraw(Canvas canvas);

        ControllerModuleType getType();
    }

    private static abstract class CircleRegion extends Circle implements Region {
        private final ControllerModuleType type;

        public CircleRegion(ControllerModuleType type) {
            this.type = type;
        }

        public ControllerModuleType getType() {
            return type;
        }
    }

    private static class NormalButton extends CircleRegion {
        private final Paint paint = new Paint();
        private final int targetKeyCode;
        private boolean down;

        public NormalButton(ControllerModuleType type) {
            super(type);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(SelectedColor);

            switch (type) {
                case Button_A:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_A;
                    break;
                case Button_B:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_B;
                    break;
                case Button_X:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_X;
                    break;
                case Button_Y:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_Y;
                    break;
                case Button_Start:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_START;
                    break;
                case Button_Back:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_SELECT;
                    break;
                case Button_L3:
                case Joystick_Left:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_THUMBL;
                    break;
                case Button_R3:
                case Joystick_Right:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_THUMBR;
                    break;
                default:
                    targetKeyCode = -1;
            }
        }

        @Override
        public void onKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == targetKeyCode) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    down = true;
                else if (event.getAction() == KeyEvent.ACTION_UP)
                    down = false;
            }
        }

        @Override
        public void onMotionEvent(MotionEvent event) {

        }

        @Override
        public void onDraw(Canvas canvas) {
            if (down)
                canvas.drawCircle(center.x, center.y, radius, paint);
        }
    }

    private static class Joystick extends NormalButton {
        private final static int PointerLength = 25;
        private final static int PointerWidth = 5;
        private final Paint paint = new Paint();
        private final int axisX;
        private final int axisY;
        private final PointF point;

        public Joystick(ControllerModuleType type) {
            super(type);
            paint.setColor(PointerColor);
            paint.setStrokeWidth(PointerWidth);

            switch (type) {
                case Joystick_Left:
                    axisX = MotionEvent.AXIS_X;
                    axisY = MotionEvent.AXIS_Y;
                    break;
                case Joystick_Right:
                    axisX = MotionEvent.AXIS_Z;
                    axisY = MotionEvent.AXIS_RZ;
                    break;
                default:
                    axisX = -1;
                    axisY = -1;
            }

            point = new PointF();
        }

        @Override
        public void onMotionEvent(MotionEvent event) {
            super.onMotionEvent(event);
            if (event == null)
                point.set(center.x, center.y);
            else
                point.set(center.x + radius * event.getAxisValue(axisX),
                        center.y + radius * event.getAxisValue(axisY));
        }

        @Override
        public void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            canvas.drawCircle(center.x, center.y, 5, paint);
            canvas.drawLine(point.x - PointerLength / 2f, point.y,
                    point.x + PointerLength / 2f, point.y, paint);
            canvas.drawLine(point.x, point.y - PointerLength / 2f,
                    point.x, point.y + PointerLength / 2f, paint);
        }
    }

    private static class Dpad extends CircleRegion {
        private final Paint paint = new Paint();
        private float hatX;
        private float hatY;

        public Dpad(ControllerModuleType type) {
            super(type);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(SelectedColor);
        }

        @Override
        public void onKeyEvent(KeyEvent event) {

        }

        @Override
        public void onMotionEvent(MotionEvent event) {
            if (event == null)
                return;
            hatX = event.getAxisValue(MotionEvent.AXIS_HAT_X);
            hatY = event.getAxisValue(MotionEvent.AXIS_HAT_Y);
        }

        @Override
        public void onDraw(Canvas canvas) {
            if (Math.abs(hatX) - 1f >= 0)
                canvas.drawCircle(hatX < 0 ? center.x - radius + 20 : center.x + radius - 20,
                        center.y, 20, paint);
            if (Math.abs(hatY) - 1f >= 0)
                canvas.drawCircle(center.x,
                        hatY < 0 ? center.y - radius + 20 : center.y + radius - 20, 20, paint);
        }
    }

    private static abstract class RectRegion extends RectF implements Region {
        private final ControllerModuleType type;

        public RectRegion(ControllerModuleType type) {
            this.type = type;
        }

        public ControllerModuleType getType() {
            return type;
        }
    }

    private static class LRB extends RectRegion {
        private final Paint paint = new Paint();
        private final int targetKeyCode;
        private boolean down;

        public LRB(ControllerModuleType type) {
            super(type);
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(SelectedColor);

            switch (type) {
                case Button_L1:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_L1;
                    break;
                case Button_R1:
                    targetKeyCode = KeyEvent.KEYCODE_BUTTON_R1;
                    break;
                default:
                    targetKeyCode = -1;
            }
        }

        @Override
        public void onKeyEvent(KeyEvent event) {
            if (event.getKeyCode() == targetKeyCode) {
                if (event.getAction() == KeyEvent.ACTION_DOWN)
                    down = true;
                else if (event.getAction() == KeyEvent.ACTION_UP)
                    down = false;
            }
        }

        @Override
        public void onMotionEvent(MotionEvent event) {

        }

        @Override
        public void onDraw(Canvas canvas) {
            if (down)
                canvas.drawRect(this, paint);
        }
    }

    private static class Trigger extends RectRegion {
        private final static int PointerWidth = 5;
        private final Paint paint1 = new Paint();
        private final Paint paint2 = new Paint();
        private final int axis;
        private float axisValue;

        public Trigger(ControllerModuleType type) {
            super(type);
            paint1.setColor(EndLineColor);
            paint1.setStrokeWidth(PointerWidth);
            paint2.setColor(PointerColor);
            paint2.setStrokeWidth(PointerWidth);

            switch (type) {
                case Trigger_L2:
                    axis = MotionEvent.AXIS_BRAKE;
                    break;
                case Trigger_R2:
                    axis = MotionEvent.AXIS_GAS;
                    break;
                default:
                    axis = -1;
            }
        }

        @Override
        public void onKeyEvent(KeyEvent event) {

        }

        @Override
        public void onMotionEvent(MotionEvent event) {
            if (event != null)
                axisValue = event.getAxisValue(axis);
        }

        @Override
        public void onDraw(Canvas canvas) {
            float height = bottom - top;
            float y = bottom - height * axisValue;

            canvas.drawLine(left, top, right, top, paint1);
            canvas.drawRect(left, y, right, bottom, paint2);
            canvas.drawLine(left, bottom, right, bottom, paint2);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 只有当绘制区域变化时才更新各子区域的位置，以减少计算量
        getDrawingRect(tmpRect);
        if (!tmpRect.equals(cachedRect)) {
            // 缓存绘制区域
            getDrawingRect(cachedRect);

            // 计算
            updateRegions();
        }

        // 绘制
        xboxFrontDrawable.setBounds(xboxFrontRect);
        xboxFrontDrawable.draw(canvas);

        for (Region region : regions)
            region.onDraw(canvas);
    }

    private void updateRegions() {
        xboxFrontRect.set(0, 0, (int) (1.4174f * getHeight()), getHeight());

        int width = xboxFrontRect.right - xboxFrontRect.left;
        int height = xboxFrontRect.bottom - xboxFrontRect.top;
        int left = xboxFrontRect.left;
        int top = xboxFrontRect.top;
        float buttonRadius = 0.0464f * height;
        float button2Radius = 0.0314f * height;
        float joystickRadius = 0.0879f * height;
        float dpadRadius = 0.108f * height;
        float lWidth = 0.1257f * height;
        float lHeight = 0.0427f * height;
        float triggerWidth = 0.0321f * height;
        float triggerHeight = 0.1006f * height;

        // X
        buttonX.set(width * 0.7f + left, height * 0.274f + top, buttonRadius);
        // B
        buttonB.set(width * 0.845f + left, height * 0.274f + top, buttonRadius);
        // Y
        buttonY.set(width * 0.773f + left, height * 0.171f + top, buttonRadius);
        // A
        buttonA.set(width * 0.773f + left, height * 0.377f + top, buttonRadius);
        // Start
        buttonStart.set(width * 0.599f + left, height * 0.283f + top, button2Radius);
        // Back
        buttonBack.set(width * 0.403f + left, height * 0.283f + top, button2Radius);
        // Joystick Left
        joystickLeft.set(width * 0.227f + left, height * 0.277f + top, joystickRadius);
        // Joystick Right
        joystickRight.set(width * 0.630f + left, height * 0.504f + top, joystickRadius);
        // D-Pad
        dpad.set(width * 0.359f + left, height * 0.503f + top, dpadRadius);
        // L1
        float l1Left = height * 0.0125f;
        float l1Top = height * 0.159f;
        l1.set(l1Left, l1Top, l1Left + lWidth, l1Top + lHeight);
        // R1
        float r1Left = height * 1.285f;
        float r1Top = height * 0.159f;
        r1.set(r1Left, r1Top, r1Left + lWidth, r1Top + lHeight);
        // Trigger Left
        float l2Left = height * 0.0748f;
        float l2Top = height * 0.0251f;
        triggerLeft.set(l2Left, l2Top, l2Left + triggerWidth, l2Top + triggerHeight);
        // Trigger Right
        float r2Left = height * 1.3135f;
        float r2Top = height * 0.0251f;
        triggerRight.set(r2Left, r2Top, r2Left + triggerWidth, r2Top + triggerHeight);

        for (Region region : regions)
            region.onMotionEvent(null);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onModuleClickCallback == null || event.getAction() != MotionEvent.ACTION_UP)
            return true;

        for (Region region : regions) {
            if (region.contains(event.getX(), event.getY())) {
                onModuleClickCallback.onModuleClick(region.getType());
                break;
            }
        }

        return true;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getRepeatCount() == 0 && event.getDevice().getDescriptor().equals(inputDeviceDescriptor)) {
            for (Region region : regions)
                region.onKeyEvent(event);
            invalidate();
        }

        return true;
    }

    @Override
    public boolean dispatchGenericMotionEvent(MotionEvent event) {
        if (event.getDevice().getDescriptor().equals(inputDeviceDescriptor)) {
            for (Region region : regions)
                region.onMotionEvent(event);
            invalidate();
        }

        return true;
    }
}

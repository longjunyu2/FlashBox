package com.aof.flashbox.input.view;

import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.aof.flashbox.input.view.graphics.Circle;
import com.aof.flashbox.input.view.graphics.Vector2D;

public class JoyView extends View {

    public enum Dir {
        UP,
        DOWN,
        LEFT,
        RIGHT,
        RIGHT_UP,
        RIGHT_DOWN,
        LEFT_UP,
        LEFT_DOWN,
        CENTER
    }

    private OnJoyChangedListener joyChangedListener;

    private final static int StrokeWidth = 5;

    private final Paint outPaint = new Paint();

    private final Paint innerPaint = new Paint();

    private final Circle outCircle = new Circle();

    private final Circle innerCircle = new Circle();

    private final Circle deadCircle = new Circle();

    private final Rect drawRect = new Rect();

    private final Rect cachedRect = new Rect();

    private final Rect tmpRect = new Rect();

    private final Vector2D touchPointVector = new Vector2D();

    private boolean clicked = false;

    private Dir lastDir = Dir.CENTER;

    private Dir currentDir = Dir.CENTER;

    public JoyView(Context context) {
        this(context, null);
    }

    public JoyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        outPaint.setColor(Color.GRAY);
        outPaint.setStyle(Paint.Style.STROKE);
        outPaint.setStrokeWidth(StrokeWidth);
        innerPaint.setColor(Color.GRAY);
        innerPaint.setStyle(Paint.Style.FILL);
        innerPaint.setAlpha(0x30);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 更新内圆
        updateInnerJoyWithEvent(event);
        // 更新视图
        invalidate();
        // 更新方向
        updateDir(event);
        // 调用回调
        callListener();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        getDrawingRect(tmpRect);
        if (!tmpRect.equals(cachedRect)) {
            getDrawingRect(cachedRect);
            updateJoy();
        }

        // 绘制
        drawJoy(canvas);
    }

    /**
     * 绘制虚拟摇杆
     *
     * @param canvas 画布
     */
    private void drawJoy(Canvas canvas) {
        // 绘制外圆
        canvas.drawCircle(outCircle.center.x, outCircle.center.y, outCircle.radius - StrokeWidth, outPaint);
        // 绘制内圆
        canvas.drawCircle(innerCircle.center.x, innerCircle.center.y, innerCircle.radius, innerPaint);
        canvas.drawCircle(innerCircle.center.x, innerCircle.center.y, innerCircle.radius, outPaint);
    }

    /**
     * 更新虚拟摇杆参数
     */
    private void updateJoy() {
        // 摇杆的绘制区域始终是正方形的
        int size = Math.min(cachedRect.width(), cachedRect.height());
        drawRect.set(cachedRect.left, cachedRect.top, cachedRect.left + size, cachedRect.top + size);
        // 更新外圆
        outCircle.center.set(drawRect.centerX(), drawRect.centerY());
        outCircle.radius = size * 0.45f;
        // 更新内圆
        innerCircle.center.set(outCircle.center);
        innerCircle.radius = size * 0.20f;
        // 更新不触发区
        deadCircle.center.set(outCircle.center);
        deadCircle.radius = size * 0.1f;
    }

    /**
     * 根据事件更新摇杆的位置
     *
     * @param event 事件
     */
    private void updateInnerJoyWithEvent(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_UP:
                // 内圆回到原点
                innerCircle.center.set(outCircle.center);
                currentDir = Dir.CENTER;
                clicked = false;
                break;
            case ACTION_DOWN:
                // 判断内圆是否被点击
                clicked = innerCircle.contains(event.getX(), event.getY());
                break;
            case ACTION_MOVE:
                // 只有当内圆被点击时才移动内圆，内圆不能超过边界
                if (clicked) {
                    touchPointVector.set(event.getX() - outCircle.center.x, event.getY() - outCircle.center.y);
                    if (touchPointVector.length() + innerCircle.radius <= outCircle.radius) {
                        innerCircle.set(event.getX(), event.getY());
                    } else {
                        double radian = Math.acos(touchPointVector.getX() / touchPointVector.length()) * (event.getY() < outCircle.center.y ? -1 : 1);
                        float x = (float) (outCircle.center.x + (outCircle.radius - innerCircle.radius) * Math.cos(radian));
                        float y = (float) (outCircle.center.y + (outCircle.radius - innerCircle.radius) * Math.sin(radian));
                        innerCircle.set(x, y);
                    }
                }
                break;
        }
    }

    /**
     * 根据事件获取摇杆的触发方向
     *
     * @param event 事件
     */
    private void updateDir(MotionEvent event) {
        // 判断不触发区
        if (deadCircle.contains(event.getX(), event.getY()) || !clicked) {
            currentDir = Dir.CENTER;
            return;
        }
        // 更新向量
        touchPointVector.set(event.getX() - outCircle.center.x, event.getY() - outCircle.center.y);
        // 角度运算
        float radians = (float) Math.acos(touchPointVector.dotProduct(0, -1) / touchPointVector.length());
        if (touchPointVector.crossProduct(0, -1) > 0)
            radians = (float) (2 * Math.PI - radians);
        float angle = (float) Math.toDegrees(radians);
        // 判断位置
        if (angle >= 22.5f && angle < 67.5f)
            // 右上
            currentDir = Dir.RIGHT_UP;
        else if (angle >= 67.5f && angle < 112.5f)
            // 右
            currentDir = Dir.RIGHT;
        else if (angle >= 112.5f && angle < 157.5f)
            // 右下
            currentDir = Dir.RIGHT_DOWN;
        else if (angle >= 157.5f && angle < 202.5f)
            // 下
            currentDir = Dir.DOWN;
        else if (angle >= 202.5f && angle < 247.5f)
            // 左下
            currentDir = Dir.LEFT_DOWN;
        else if (angle >= 247.5f && angle < 292.5f)
            // 左
            currentDir = Dir.LEFT;
        else if (angle >= 292.5f && angle < 337.5f)
            // 左上
            currentDir = Dir.LEFT_UP;
        else
            // 上
            currentDir = Dir.UP;
    }

    /**
     * 执行回调
     */
    private void callListener() {
        if (joyChangedListener != null && lastDir != currentDir) {
            lastDir = currentDir;
            switch (currentDir) {
                case UP:
                    joyChangedListener.onUp();
                    break;
                case DOWN:
                    joyChangedListener.onDown();
                    break;
                case LEFT:
                    joyChangedListener.onLeft();
                    break;
                case RIGHT:
                    joyChangedListener.onRight();
                    break;
                case RIGHT_UP:
                    joyChangedListener.onRight_Up();
                    break;
                case RIGHT_DOWN:
                    joyChangedListener.onRight_Down();
                    break;
                case LEFT_UP:
                    joyChangedListener.onLeft_Up();
                    break;
                case LEFT_DOWN:
                    joyChangedListener.onLeft_Down();
                    break;
                case CENTER:
                    joyChangedListener.onCenter();
                    break;
            }
        }
    }

    /**
     * 设置方向改变监听器
     *
     * @param listener 监听器
     */
    public void setOnJoyChangedListener(OnJoyChangedListener listener) {
        joyChangedListener = listener;
    }

    public interface OnJoyChangedListener {
        void onUp();

        void onDown();

        void onLeft();

        void onRight();

        void onRight_Up();

        void onRight_Down();

        void onLeft_Up();

        void onLeft_Down();

        void onCenter();
    }
}

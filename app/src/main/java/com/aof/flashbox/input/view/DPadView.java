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
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.aof.flashbox.R;
import com.aof.flashbox.input.view.graphics.Circle;

public class DPadView extends View {

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

    private OnDPadChangedListener dPadChangedListener;

    private final Paint fillPaint = new Paint();

    private final Circle upBtnCircle = new Circle();

    private final Circle downBtnCircle = new Circle();

    private final Circle leftBtnCircle = new Circle();

    private final Circle rightBtnCircle = new Circle();

    private final Circle rightUpBtnCircle = new Circle();

    private final Circle rightDownBtnCircle = new Circle();

    private final Circle leftUpBtnCircle = new Circle();

    private final Circle leftDownBtnCircle = new Circle();

    private final Rect btnRect = new Rect();

    private final RectF centerRect = new RectF();

    private final Circle[] bigBtnCircles = new Circle[]{upBtnCircle, downBtnCircle, leftBtnCircle, rightBtnCircle};

    private final Circle[] smallBtnCircles = new Circle[]{rightUpBtnCircle, rightDownBtnCircle, leftUpBtnCircle, leftDownBtnCircle};

    private final Rect drawRect = new Rect();

    private final Rect cachedRect = new Rect();

    private final Rect tmpRect = new Rect();

    private final Drawable upBtnDrawable;

    private final Drawable downBtnDrawable;

    private final Drawable leftBtnDrawable;

    private final Drawable rightBtnDrawable;

    private Dir lastDir = Dir.CENTER;

    private Dir currentDir = Dir.CENTER;

    public DPadView(Context context) {
        this(context, null);
    }

    public DPadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        fillPaint.setColor(Color.GRAY);
        fillPaint.setStyle(Paint.Style.FILL);
        fillPaint.setAlpha(0x30);
        upBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.dpad_up_btn, null);
        downBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.dpad_down_btn, null);
        leftBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.dpad_left_btn, null);
        rightBtnDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.dpad_right_btn, null);
    }

    /**
     * 更新按钮的图形参数
     */
    private void updateBtnCircle() {
        float cellSize = drawRect.width() / 3f;
        float halfCellSize = cellSize / 2f;
        float bigBtnRadius = cellSize / 2f;
        float smallBtnRadius = bigBtnRadius * 0.8f;

        leftUpBtnCircle.set(drawRect.left + halfCellSize, drawRect.top + halfCellSize, smallBtnRadius);
        upBtnCircle.set(leftUpBtnCircle.center.x + cellSize, leftUpBtnCircle.center.y, bigBtnRadius);
        rightUpBtnCircle.set(upBtnCircle.center.x + cellSize, upBtnCircle.center.y, smallBtnRadius);
        leftBtnCircle.set(leftUpBtnCircle.center.x, leftUpBtnCircle.center.y + cellSize, bigBtnRadius);
        rightBtnCircle.set(rightUpBtnCircle.center.x, leftBtnCircle.center.y, bigBtnRadius);
        leftDownBtnCircle.set(leftBtnCircle.center.x, leftBtnCircle.center.y + cellSize, smallBtnRadius);
        downBtnCircle.set(upBtnCircle.center.x, leftDownBtnCircle.center.y, bigBtnRadius);
        rightDownBtnCircle.set(rightBtnCircle.center.x, downBtnCircle.center.y, smallBtnRadius);
        centerRect.set(drawRect.left + cellSize, drawRect.top + cellSize,
                drawRect.left + 2 * cellSize, drawRect.top + 2 * cellSize);
    }

    /**
     * 更新绘制区域
     */
    private void updateDrawRect() {
        int size = Math.min(cachedRect.width(), cachedRect.height());
        drawRect.set(cachedRect.left, cachedRect.top, cachedRect.left + size, cachedRect.top + size);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        getDrawingRect(tmpRect);
        if (!tmpRect.equals(cachedRect)) {
            getDrawingRect(cachedRect);
            updateDrawRect();
            updateBtnCircle();
        }

        upBtnCircle.genRect(btnRect);
        upBtnDrawable.setBounds(btnRect);
        upBtnDrawable.draw(canvas);

        downBtnCircle.genRect(btnRect);
        downBtnDrawable.setBounds(btnRect);
        downBtnDrawable.draw(canvas);

        leftBtnCircle.genRect(btnRect);
        leftBtnDrawable.setBounds(btnRect);
        leftBtnDrawable.draw(canvas);

        rightBtnCircle.genRect(btnRect);
        rightBtnDrawable.setBounds(btnRect);
        rightBtnDrawable.draw(canvas);

        Circle btnCircle;
        switch (currentDir) {
            case UP:
                btnCircle = upBtnCircle;
                break;
            case DOWN:
                btnCircle = downBtnCircle;
                break;
            case LEFT:
                btnCircle = leftBtnCircle;
                break;
            case RIGHT:
                btnCircle = rightBtnCircle;
                break;
            default:
                btnCircle = null;
        }

        if (btnCircle != null)
            canvas.drawCircle(btnCircle.center.x, btnCircle.center.y, btnCircle.radius, fillPaint);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 更新方向
        updateDir(event);
        // 更新视图
        invalidate();
        // 调用回调
        callListener();
        return true;
    }

    /**
     * 根据按钮的图形获取方向
     *
     * @param circle 图形
     * @return 方向
     */
    private Dir getDirByCircle(Circle circle) {
        if (circle == upBtnCircle)
            return Dir.UP;
        else if (circle == downBtnCircle)
            return Dir.DOWN;
        else if (circle == leftBtnCircle)
            return Dir.LEFT;
        else if (circle == rightBtnCircle)
            return Dir.RIGHT;
        else if (circle == rightUpBtnCircle)
            return Dir.RIGHT_UP;
        else if (circle == rightDownBtnCircle)
            return Dir.RIGHT_DOWN;
        else if (circle == leftUpBtnCircle)
            return Dir.LEFT_UP;
        else if (circle == leftDownBtnCircle)
            return Dir.LEFT_DOWN;
        else
            return Dir.CENTER;
    }

    /**
     * 根据事件更新方向
     *
     * @param event 事件
     */
    private void updateDir(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN:
            case ACTION_MOVE:
                // 当十字键触发四方向时才能触发对角的四个方向
                if (lastDir == Dir.UP || lastDir == Dir.DOWN || lastDir == Dir.LEFT || lastDir == Dir.RIGHT) {
                    for (Circle circle : smallBtnCircles) {
                        if (circle.contains(event.getX(), event.getY())) {
                            currentDir = getDirByCircle(circle);
                            return;
                        }
                    }
                }

                // 判断其余四个方向
                for (Circle circle : bigBtnCircles) {
                    if (circle.contains(event.getX(), event.getY())) {
                        currentDir = getDirByCircle(circle);
                        return;
                    }
                }

                // 判断中间
                if (centerRect.contains(event.getX(), event.getY()))
                    currentDir = Dir.CENTER;

                break;
            case ACTION_UP:
                currentDir = Dir.CENTER;
                break;
        }
    }

    /**
     * 执行回调
     */
    private void callListener() {
        if (dPadChangedListener != null && lastDir != currentDir) {
            lastDir = currentDir;
            switch (currentDir) {
                case UP:
                    dPadChangedListener.onUp();
                    break;
                case DOWN:
                    dPadChangedListener.onDown();
                    break;
                case LEFT:
                    dPadChangedListener.onLeft();
                    break;
                case RIGHT:
                    dPadChangedListener.onRight();
                    break;
                case RIGHT_UP:
                    dPadChangedListener.onRight_Up();
                    break;
                case RIGHT_DOWN:
                    dPadChangedListener.onRight_Down();
                    break;
                case LEFT_UP:
                    dPadChangedListener.onLeft_Up();
                    break;
                case LEFT_DOWN:
                    dPadChangedListener.onLeft_Down();
                    break;
                case CENTER:
                    dPadChangedListener.onCenter();
                    break;
            }
        }
    }

    /**
     * 设置按键变化监听器
     *
     * @param listener 监听器
     */
    public void setOnDPadChangedListener(OnDPadChangedListener listener) {
        dPadChangedListener = listener;
    }

    public interface OnDPadChangedListener {
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

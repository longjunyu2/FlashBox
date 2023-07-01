package com.aof.flashbox.input.widget;

import android.content.Context;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.aof.flashbox.input.IInputAgent;

import java.lang.ref.WeakReference;

public abstract class BaseLayer implements View.OnTouchListener {

    private final IInputAgent agent;

    private final BaseLayerConfig config;

    private WeakReference<Trigger> weakTrigger;

    public enum Status {
        Lock,
        Edit,
        VAlign,
        HAlign
    }

    public BaseLayer(IInputAgent agent, BaseLayerConfig config) {
        this.agent = agent;
        this.config = config;
        this.weakTrigger = new WeakReference<>(null);
    }

    /**
     * 触摸事件回调，子类应该在首行使用super()以正确处理Edit状态
     * 同时View控件应调用View.setOnTouchListener(this)以便能获取事件
     *
     * @param view        目标控件
     * @param motionEvent 触摸事件
     * @return 事件是否被消费
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (agent.getStatus() != Status.Lock) {
            if (weakTrigger.get() == null)
                weakTrigger = new WeakReference<>(new Trigger(this));
            weakTrigger.get().onTouch(motionEvent);
            return true;
        }
        return false;
    }

    /**
     * 初始化和创建View控件，应保证调用此函数后view实例创建完成
     */
    public abstract void initView();

    /**
     * 获取控制层视图，调用时请保证View控件已生成
     *
     * @return 控制层视图
     */
    public abstract View getView();

    /**
     * 获取控制层的配置
     *
     * @return 控制层配置
     */
    public BaseLayerConfig getConfig() {
        return config;
    }

    /**
     * 获取输入代理
     *
     * @return 输入代理
     */
    public IInputAgent getAgent() {
        return agent;
    }

    /**
     * 更新控制层中心x坐标, 此方法会将中心坐标转为按左上角的原坐标
     */
    public void updateDivX() {
        float x = getConfig().getX();
        float baseDivX = x - getConfig().getWidth() / 2f;
        float realX = baseDivX * agent.getPixelPerScreenDiv();

        getView().setX(realX);
    }

    /**
     * 更新控制层中心y坐标, 此方法会将中心坐标转为按左上角的原坐标
     */
    public void updateDivY() {
        float y = getConfig().getY();
        float baseDivY = y - getConfig().getHeight() / 2f;
        float realY = baseDivY * agent.getPixelPerScreenDiv();

        getView().setY(realY);
    }

    /**
     * 更新控制层宽度
     */
    public void updateDivW() {
        int w = getConfig().getWidth();
        ViewGroup.LayoutParams params = getView().getLayoutParams();
        params.width = (int) (w * agent.getPixelPerScreenDiv());

        getView().setLayoutParams(params);
        updateDivX();
    }

    /**
     * 更新控制层高度
     */
    public void updateDivH() {
        int h = getConfig().getHeight();
        ViewGroup.LayoutParams params = getView().getLayoutParams();
        params.height = (int) (h * agent.getPixelPerScreenDiv());

        getView().setLayoutParams(params);
        updateDivY();
    }

    /**
     * 更新Layer的全部可选项,子类务必重写此方法以便能够完全更新
     */
    public void updateAll() {
        updateDivX();
        updateDivY();
        updateDivH();
        updateDivW();
    }

    /**
     * 设置被选中状态
     *
     * @param selected 被选中状态
     */
    public abstract void setSelected(boolean selected);

    /**
     * 打开编辑对话框
     */
    public abstract void openEditDialog();

    /**
     * 销毁时执行
     */
    public void destroy() {
        // 销毁时回收资源
    }

    private static class Trigger {
        private final WeakReference<BaseLayer> weakLayer;

        private static final int PressTriggerTime = 200;

        private final float pixelPerDiv;

        private float downPixelX;

        private float downPixelY;

        private boolean needVibrate = true;

        public Trigger(BaseLayer layer) {
            weakLayer = new WeakReference<>(layer);
            pixelPerDiv = weakLayer.get().getAgent().getPixelPerScreenDiv();
        }

        public void onTouch(MotionEvent motionEvent) {
            if (weakLayer.get() == null)
                return;

            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // 记录按下时的坐标
                downPixelX = motionEvent.getRawX();
                downPixelY = motionEvent.getRawY();
                // 更新触发震动条件
                needVibrate = true;
                return;
            }

            if ((motionEvent.getActionMasked() & MotionEvent.ACTION_MOVE) == MotionEvent.ACTION_MOVE
                    && motionEvent.getEventTime() - motionEvent.getDownTime() >= PressTriggerTime) {
                // 触发震动
                if (weakLayer.get().getAgent().isVibrateEnabled() && needVibrate) {
                    Vibrator vibrator = (Vibrator) weakLayer.get().getAgent().getContext().getSystemService(Context.VIBRATOR_SERVICE);
                    if (vibrator.hasVibrator()) {
                        VibrationEffect vibrationEffect = VibrationEffect.createWaveform(new long[]{0, 20}, -1);
                        vibrator.vibrate(vibrationEffect);
                    }
                    needVibrate = false;
                }

                // 触发拖拽
                float deltaX = motionEvent.getRawX() - downPixelX;
                float deltaY = motionEvent.getRawY() - downPixelY;
                float deltaDivX = deltaX / pixelPerDiv;
                float deltaDivY = deltaY / pixelPerDiv;

                if (deltaDivX != 0) {
                    downPixelX = motionEvent.getRawX();
                    weakLayer.get().getConfig().setX(weakLayer.get().getConfig().getX() + deltaDivX);
                    weakLayer.get().updateDivX();
                }
                if (deltaDivY != 0) {
                    downPixelY = motionEvent.getRawY();
                    weakLayer.get().getConfig().setY(weakLayer.get().getConfig().getY() + deltaDivY);
                    weakLayer.get().updateDivY();
                }
            }

            if ((motionEvent.getActionMasked() & MotionEvent.ACTION_UP) == MotionEvent.ACTION_UP
                    && motionEvent.getEventTime() - motionEvent.getDownTime() < PressTriggerTime) {
                // 设为被选中
                weakLayer.get().getAgent().addSelected(weakLayer.get());
            }
        }
    }

}

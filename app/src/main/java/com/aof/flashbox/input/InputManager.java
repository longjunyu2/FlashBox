package com.aof.flashbox.input;

import static com.aof.flashbox.input.widget.BaseLayerConfig.ScreenDiv;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.aof.flashbox.R;
import com.aof.flashbox.input.event.BaseInputEvent;
import com.aof.flashbox.input.event.EventLooper;
import com.aof.flashbox.input.event.KeyEvent;
import com.aof.flashbox.input.widget.BaseLayer;
import com.aof.flashbox.input.widget.BaseLayerConfig;
import com.aof.flashbox.input.widget.GameButtonLayerConfig;
import com.aof.flashbox.input.widget.GameDPadLayerConfig;
import com.aof.flashbox.input.widget.LayerBuilder;
import com.aof.flashbox.input.widget.RootLayer;
import com.aof.flashbox.player.IPlayerBridge;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class InputManager {

    private final Context mContext;
    private final DefaultInputAgent mAgent;
    private final IPlayerBridge mBridge;

    private final ArrayList<BaseLayer> layers;

    public InputManager(Context context) {
        this(context, null);
    }

    public InputManager(Context context, IPlayerBridge bridge) {
        mContext = context;
        mBridge = bridge;

        mAgent = new DefaultInputAgent(context, new DefaultInputEventLooper(mBridge));
        layers = new ArrayList<>();
    }

    // TODO: 实现一个销毁方法，以便在退出是正确回收资源

    /**
     * 开始创建并初始化控制层
     *
     * @param viewGroup    根容器控件
     * @param layerConfigs 控制层配置列表
     */
    @SuppressLint("InflateParams")
    public void start(ViewGroup viewGroup, List<BaseLayerConfig> layerConfigs) {
        RootLayer rootLayer = null;

        // 创建控制层
        for (BaseLayerConfig config : layerConfigs) {
            BaseLayer layer = new LayerBuilder()
                    .withAgent(mAgent)
                    .withConfig(config)
                    .build();

            // 建立对RootLayer实例的引用
            if (rootLayer == null && config.getType() == BaseLayerConfig.Type.Root) {
                rootLayer = (RootLayer) layer;
                continue;
            }

            // 将不是RootLayer的Layer放入列表
            layers.add(layer);
        }

        // 若没有构建RootLayer，则放弃继续初始化
        if (rootLayer == null) {
            layers.clear();
            return;
        }
        RootLayer finalRootLayer = rootLayer;

        // 优先初始化RootLayer，以确保其在父容器最底层
        rootLayer.initView();
        viewGroup.addView(rootLayer.getView());

        // 将其余Layer初始化并加RootLayer
        for (BaseLayer layer : layers) {
            layer.initView();
            rootLayer.getView().addView(layer.getView());
        }

        // 将RootLayer也放入列表
        layers.add(rootLayer);

        // 创建导航栏
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewGroup navigationView = (ViewGroup) inflater.inflate(R.layout.layout_control_bar, null);
        navigationView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        rootLayer.getView().addView(navigationView);
        navigationView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                navigationView.setX(mAgent.getWinPixelWidth() - navigationView.getWidth());
                navigationView.setY(0);
                navigationView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = v.getId();

                // 更新按钮的背景-未选中
                navigationView.findViewById(R.id.btn_align_h)
                        .setBackgroundResource(R.drawable.ic_baseline_border_horizontal_24);
                navigationView.findViewById(R.id.btn_align_v)
                        .setBackgroundResource(R.drawable.ic_baseline_border_vertical_24);

                if (id == R.id.btn_lock) {
                    // 锁定
                    if (mAgent.getStatus() != BaseLayer.Status.Lock) {
                        mAgent.setStatus(BaseLayer.Status.Lock);
                        navigationView.findViewById(R.id.btn_add).setVisibility(View.GONE);
                        v.setBackgroundResource(R.drawable.ic_baseline_settings_24);
                    } else {
                        mAgent.setStatus(BaseLayer.Status.Edit);
                        navigationView.findViewById(R.id.btn_add).setVisibility(View.VISIBLE);
                        v.setBackgroundResource(R.drawable.ic_baseline_settings_24_blue);
                    }
                } else if (id == R.id.btn_align_v) {
                    // 垂直对齐
                    mAgent.setStatus(BaseLayer.Status.VAlign);
                    // 更新背景-选中
                    navigationView.findViewById(R.id.btn_align_v)
                            .setBackgroundResource(R.drawable.ic_baseline_border_vertical_24_blue);
                } else if (id == R.id.btn_align_h) {
                    // 水平对齐
                    mAgent.setStatus(BaseLayer.Status.HAlign);
                    // 更新背景-选中
                    navigationView.findViewById(R.id.btn_align_h)
                            .setBackgroundResource(R.drawable.ic_baseline_border_horizontal_24_blue);
                } else if (id == R.id.btn_add) {
                    // 添加控件
                    new MaterialAlertDialogBuilder(mContext)
                            .setTitle(mContext.getResources().getString(R.string.select_add_layer))
                            .setItems(R.array.array_add_layer_options, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    // 处理选项选中事件
                                    String[] options = mContext.getResources().getStringArray(R.array.array_add_layer_options);
                                    String selOption = options[i];
                                    BaseLayerConfig config = null;
                                    BaseLayer layer;
                                    if (selOption.equals(mContext.getResources().getString(R.string.layer_button))) {
                                        // 创建按钮配置
                                        config = new GameButtonLayerConfig()
                                                .setX(50)
                                                .setY(50)
                                                .setHeight(15)
                                                .setWidth(15)
                                                .setText("New");
                                    } else if (selOption.equals(mContext.getResources().getString(R.string.layer_dpad))) {
                                        // 创建方向键配置
                                        config = new GameDPadLayerConfig();
                                    }
                                    // 创建并初始化Layer
                                    layer = (new LayerBuilder()).withAgent(mAgent).withConfig(config).build();
                                    layer.initView();
                                    // 显示控件并放入列表
                                    finalRootLayer.getView().addView(layer.getView());
                                    layers.add(layer);
                                    // 使导航栏始终保持在最上层
                                    finalRootLayer.getView().bringChildToFront(navigationView);
                                }
                            })
                            .setNegativeButton(mContext.getResources().getString(R.string.cancel), null)
                            .setCancelable(false)
                            .create()
                            .show();
                } else if (id == R.id.btn_del) {
                    // 删除控件
                    mAgent.tryDeleteLayer();
                } else if (id == R.id.btn_edit) {
                    // 编辑控件
                    mAgent.tryEditLayer();
                }
            }
        };
        for (int i = 0; i < navigationView.getChildCount(); i++)
            navigationView.getChildAt(i).setOnClickListener(clickListener);

        // 设置控制层选中状态改变的回调
        mAgent.setOnLayerSelectedCallback(new DefaultInputAgent.OnLayerSelectedCallback() {
            private final int[] btnIds = new int[]{
                    R.id.btn_del, R.id.btn_edit, R.id.btn_align_v, R.id.btn_align_h};

            /**
             * 批量设置控件的可见性
             * @param viewParent 要设置的控件的父控件
             * @param visibility 控件可见性
             * @param ids 控件id
             */
            private void setBtnVisibility(int visibility, View viewParent, int... ids) {
                for (int id : ids)
                    viewParent.findViewById(id).setVisibility(visibility);
            }

            @Override
            public void onSelectedChanged(boolean selected) {
                setBtnVisibility(selected ? View.VISIBLE : View.GONE, navigationView, btnIds);
            }
        });

        // 设置删除控制层的回调
        mAgent.setOnLayerDeleteCallback(new DefaultInputAgent.OnLayerDeleteCallback() {
            @Override
            public void onLayerDelete(BaseLayer layer) {
                // 显示一个确认对话框
                new MaterialAlertDialogBuilder(mContext)
                        .setTitle(mContext.getResources().getString(R.string.warning))
                        .setMessage(mContext.getResources().getString(R.string.confirm_del_layer))
                        .setPositiveButton(mContext.getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // 实际上控件的删除仍然是在InputManager对象中完成的
                                finalRootLayer.getView().removeView(layer.getView());
                                layers.remove(layer);
                                layer.destroy();
                            }
                        })
                        .setNegativeButton(mContext.getResources().getString(R.string.cancel), null)
                        .setCancelable(false)
                        .create()
                        .show();
            }
        });

        // 设置状态结束的回调
        mAgent.setOnStatusEndCallback(new DefaultInputAgent.OnStatusEndCallback() {
            @Override
            public void onStatusEnd(BaseLayer.Status status) {
                // 对齐状态结束后，改变相关按钮的背景
                switch (status) {
                    case HAlign:
                        navigationView.findViewById(R.id.btn_align_h)
                                .setBackgroundResource(R.drawable.ic_baseline_border_horizontal_24);
                    case VAlign:
                        navigationView.findViewById(R.id.btn_align_v)
                                .setBackgroundResource(R.drawable.ic_baseline_border_vertical_24);
                }
            }
        });

        // 启用震动
        mAgent.setEnableVibrate(true);
    }

    private static class DefaultInputEventLooper extends EventLooper<BaseInputEvent> {
        private final IPlayerBridge mBridge;

        public DefaultInputEventLooper(IPlayerBridge bridge) {
            mBridge = bridge;
        }

        @Override
        public void offer(BaseInputEvent event) {
            super.offer(event);
            // TODO: 处理各种输入事件
            if (event.getType() == BaseInputEvent.Type.KeyEvent) {
                KeyEvent e = (KeyEvent) event;
                Log.e("InputManager", "Action: " + e.action.name() + " Key: " + e.key.name());
            }
        }
    }

    /**
     * 输入器代理
     */
    private static class DefaultInputAgent implements IInputAgent {

        private final Context mContext;

        private BaseLayer.Status currentStatus = BaseLayer.Status.Lock;

        private int winPixelWidth;

        private int winPixelHeight;

        private float perDiv;

        private WeakReference<BaseLayer> currentSelectedLayer;

        private OnLayerSelectedCallback selectedCallback;

        private OnLayerDeleteCallback deleteCallback;

        private OnStatusEndCallback statusEndCallback;

        private boolean enableVibrate = false;

        private final EventLooper<BaseInputEvent> eventLooper;

        public interface OnLayerSelectedCallback {
            void onSelectedChanged(boolean selected);
        }

        public interface OnLayerDeleteCallback {
            void onLayerDelete(BaseLayer layer);
        }

        public interface OnStatusEndCallback {
            void onStatusEnd(BaseLayer.Status status);
        }

        public DefaultInputAgent(Context context, EventLooper<BaseInputEvent> eventLooper) {
            mContext = context;
            this.eventLooper = eventLooper;
            initWinPixelSize();
        }

        /**
         * 设置选中状态改变的回调
         *
         * @param callback 回调
         */
        public void setOnLayerSelectedCallback(OnLayerSelectedCallback callback) {
            selectedCallback = callback;
        }

        /**
         * 设置控件删除的回调
         *
         * @param callback 回调
         */
        public void setOnLayerDeleteCallback(OnLayerDeleteCallback callback) {
            deleteCallback = callback;
        }

        /**
         * 设置状态结束的回调
         *
         * @param callback 回调
         */
        public void setOnStatusEndCallback(OnStatusEndCallback callback) {
            statusEndCallback = callback;
        }

        /**
         * 初始化屏幕像素大小
         */
        private void initWinPixelSize() {
            DisplayMetrics displayMetrics = new DisplayMetrics();

            // 获取的屏幕大小为实际显示区域大小，去除了裁剪区域
            ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE))
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);

            winPixelWidth = displayMetrics.widthPixels;
            winPixelHeight = displayMetrics.heightPixels;

            // 为保证横竖屏下的一致性，选择较小的为基准
            perDiv = (float) Math.min(winPixelWidth, winPixelHeight) / ScreenDiv;
        }

        /**
         * 设置状态
         *
         * @param status 状态
         */
        public void setStatus(BaseLayer.Status status) {
            // 锁定时取消当前被选中的控件的选中状态
            if (status == BaseLayer.Status.Lock) {
                BaseLayer layer = null;
                if (currentSelectedLayer != null)
                    layer = currentSelectedLayer.get();

                if (layer != null) {
                    layer.setSelected(false);
                    currentSelectedLayer = null;
                    // 触发选中状态改变回调
                    selectedCallback.onSelectedChanged(false);
                }
            }
            currentStatus = status;
        }

        /**
         * 尝试删除选中的控件
         */
        public void tryDeleteLayer() {
            if (currentSelectedLayer != null) {
                BaseLayer layer = currentSelectedLayer.get();
                if (layer != null) {
                    // 触发控件删除的回调
                    deleteCallback.onLayerDelete(layer);
                    // 移除对layer的引用以使其被正确回收
                    currentSelectedLayer = null;
                    // 触发选中状态改变回调
                    selectedCallback.onSelectedChanged(false);
                }
            }
        }

        /**
         * 尝试编辑选中的控件
         */
        public void tryEditLayer() {
            if (currentSelectedLayer != null) {
                BaseLayer layer = currentSelectedLayer.get();
                if (layer != null)
                    layer.openEditDialog();
            }
        }

        /**
         * 设置是否启用震动
         *
         * @param enable 是否启用震动
         */
        public void setEnableVibrate(boolean enable) {
            this.enableVibrate = enable;
        }

        @Override
        public BaseLayer.Status getStatus() {
            return currentStatus;
        }

        @Override
        public Context getContext() {
            return mContext;
        }

        @Override
        public int getWinPixelWidth() {
            return winPixelWidth;
        }

        @Override
        public int getWinPixelHeight() {
            return winPixelHeight;
        }

        @Override
        public float getPixelPerScreenDiv() {
            return perDiv;
        }

        @Override
        public void offerEvent(BaseInputEvent event) {
            eventLooper.offer(event);
        }

        @Override
        public void addSelected(BaseLayer layer) {
            BaseLayer l = null;

            // 尝试获取已经被选中对象
            if (currentSelectedLayer != null)
                l = currentSelectedLayer.get();

            // 默认只有当存在已经被选中的对象时，才可能出现以下情况
            if (l != null && getStatus() != BaseLayer.Status.Edit) {
                switch (getStatus()) {
                    case VAlign:
                        l.getConfig().setX(layer.getConfig().getX());
                        l.updateDivX();
                        break;
                    case HAlign:
                        l.getConfig().setY(layer.getConfig().getY());
                        l.updateDivY();
                        break;
                }
                // 操作完成后，取消已经被选中的控件的选中状态
                l.setSelected(false);
                currentSelectedLayer = null;
                // 触发选中状态改变回调
                selectedCallback.onSelectedChanged(false);
                // 触发状态结束回调
                statusEndCallback.onStatusEnd(getStatus());
                // 将状态切换为Edit
                setStatus(BaseLayer.Status.Edit);
            } else {
                if (l == null) {
                    // 如果没有已经被选中的控件， 则将目标控件设为已经被选中的控件
                    layer.setSelected(true);
                    // 更新已经被选中的控件的引用
                    currentSelectedLayer = new WeakReference<>(layer);
                    // 触发选中状态改变回调
                    selectedCallback.onSelectedChanged(true);
                } else {
                    if (l == layer) {
                        // 如果已经被选中的控件是目标控件，则取消目标控件的选中状态
                        layer.setSelected(false);
                        currentSelectedLayer = null;
                        // 触发选中状态改变回调
                        selectedCallback.onSelectedChanged(false);
                    } else {
                        // 如果已经被选中控件不是目标控件，则更改已经被选中的控件为目标控件
                        l.setSelected(false);
                        layer.setSelected(true);
                        // 更新已经被选中的控件的引用
                        currentSelectedLayer = new WeakReference<>(layer);
                        // 此处不需要触发回调，因为总体上仍有一个控件被选中
                    }
                }
            }
        }

        @Override
        public boolean isVibrateEnabled() {
            return enableVibrate;
        }
    }
}

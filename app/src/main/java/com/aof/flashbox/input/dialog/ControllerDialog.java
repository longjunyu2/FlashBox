package com.aof.flashbox.input.dialog;

import static android.view.MotionEvent.AXIS_HAT_X;
import static android.view.MotionEvent.AXIS_HAT_Y;
import static android.view.MotionEvent.AXIS_RZ;
import static android.view.MotionEvent.AXIS_X;
import static android.view.MotionEvent.AXIS_Y;
import static android.view.MotionEvent.AXIS_Z;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.aof.flashbox.R;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.view.ControllerView;
import com.aof.flashbox.input.widget.RootLayerConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class ControllerDialog extends AppCompatDialog {

    private final RootLayerConfig config;

    public ControllerDialog(@NonNull Context context, RootLayerConfig config) {
        super(context);
        setTitle(R.string.config_controller);
        this.config = config;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_controller);

        ControllerView controllerView = findViewById(R.id.controller_view);
        assert controllerView != null;
        controllerView.setFocusable(true);
        controllerView.requestFocus();
        controllerView.setDescriptor(config.getController().descriptor);
        controllerView.setOnModuleClickCallback(new ControllerView.OnModuleClickCallback() {
            @Override
            public void onModuleClick(ControllerView.ControllerModuleType type) {
                new ControllerSelectKeyDialog(getContext(), type, config).show();
            }
        });
    }

    private static class ControllerSelectKeyDialog extends AppCompatDialog {
        private final ControllerView.ControllerModuleType moduleType;

        private final ViewGroup layout;

        private final LayoutInflater inflater;

        private final RootLayerConfig config;

        @SuppressLint("InflateParams")
        public ControllerSelectKeyDialog(@NonNull Context context, ControllerView.ControllerModuleType moduleType,
                                         RootLayerConfig config) {
            super(context);
            setTitle(R.string.config_map);
            this.moduleType = moduleType;
            this.config = config;
            inflater = LayoutInflater.from(getContext());
            layout = (ViewGroup) inflater.inflate(R.layout.controller_select_key_layout, null);
            initLayout();
        }

        private void initLayout() {
            ViewGroup parent = layout.findViewById(R.id.layout);
            View item;
            View[] items;

            // 名称项
            inflater.inflate(R.layout.controller_select_key_name_item, parent);
            ((TextView) layout.findViewById(R.id.text_name)).setText(moduleType.name());

            // 按钮编辑项
            if ((item = createBtnItem()) != null)
                parent.addView(item);

            // 轴编辑项
            if ((items = createAxisItem()) != null)
                for (View v : items)
                    parent.addView(v);
        }

        @SuppressLint("InflateParams")
        private View[] createAxisItem() {
            RootLayerConfig.ControllerAxis[] useAxes;
            RootLayerConfig.ControllerAxes axes = config.getControllerAxes();

            switch (moduleType) {
                case Joystick_Left:
                    useAxes = new RootLayerConfig.ControllerAxis[]{
                            axes.axis_y_n, axes.axis_y_p, axes.axis_x_n, axes.axis_x_p
                    };
                    break;
                case Joystick_Right:
                    useAxes = new RootLayerConfig.ControllerAxis[]{
                            axes.axis_rz_n, axes.axis_rz_p, axes.axis_z_n, axes.axis_z_p
                    };
                    break;
                case DPad:
                    useAxes = new RootLayerConfig.ControllerAxis[]{
                            axes.axis_hat_y_n, axes.axis_hat_y_p, axes.axis_hat_x_n, axes.axis_hat_x_p
                    };
                    break;
                case Trigger_L2:
                    useAxes = new RootLayerConfig.ControllerAxis[]{axes.axis_brake};
                    break;
                case Trigger_R2:
                    useAxes = new RootLayerConfig.ControllerAxis[]{axes.axis_gas};
                    break;
                default:
                    return null;
            }

            ArrayList<View> items = new ArrayList<>();
            for (RootLayerConfig.ControllerAxis axis : useAxes) {
                View item = inflater.inflate(R.layout.controller_select_key_axis_item, null);
                AppCompatTextView textDir = item.findViewById(R.id.text_dir);
                AppCompatTextView textKey = item.findViewById(R.id.text_key);
                AppCompatTextView textTriggerValue = item.findViewById(R.id.text_trigger_value);
                AppCompatButton btnSelect = item.findViewById(R.id.btn_select);
                AppCompatSeekBar seekBarTrigger = item.findViewById(R.id.seekbar_trigger);

                if ((axis.axis_flag == AXIS_X && axis.dir == 1)
                        || (axis.axis_flag == AXIS_Z && axis.dir == 1)
                        || (axis.axis_flag == AXIS_HAT_X && axis.dir == 1)) {
                    // 右
                    textDir.setText(R.string.right);
                } else if ((axis.axis_flag == AXIS_X && axis.dir == -1)
                        || (axis.axis_flag == AXIS_Z && axis.dir == -1)
                        || (axis.axis_flag == AXIS_HAT_X && axis.dir == -1)) {
                    // 左
                    textDir.setText(R.string.left);
                } else if ((axis.axis_flag == AXIS_Y && axis.dir == -1)
                        || (axis.axis_flag == AXIS_RZ && axis.dir == -1)
                        || (axis.axis_flag == AXIS_HAT_Y && axis.dir == -1)) {
                    // 上
                    textDir.setText(R.string.up);
                } else {
                    // 下
                    textDir.setText(R.string.down);
                }

                updateTextKey(textKey, getKeys(axis.key));
                textTriggerValue.setText(String.valueOf(axis.trigger_per_value));
                seekBarTrigger.setMin(10);
                seekBarTrigger.setMax(90);
                seekBarTrigger.setProgress(axis.trigger_per_value);
                seekBarTrigger.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser) {
                            textTriggerValue.setText(String.valueOf(progress));
                            axis.trigger_per_value = progress;
                        }
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        List<KeyCodes.Codes> tmpList = getKeys(axis.key);
                        SelectKeyDialog dialog = new SelectKeyDialog(getContext(), tmpList);
                        dialog.setOnSelectKeyFinishedCallback(new SelectKeyDialog.OnSelectKeyFinishedCallback() {
                            @Override
                            public void OnSelectKeyFinished(List<KeyCodes.Codes> keys) {
                                // 最多映射3个按钮
                                if (keys.size() > 3) {
                                    new MaterialAlertDialogBuilder(getContext())
                                            .setTitle(getContext().getString(R.string.error))
                                            .setMessage(getContext().getString(R.string.map_up_limit))
                                            .setPositiveButton(getContext().getString(R.string.ok), null)
                                            .create()
                                            .show();
                                } else {
                                    // 更新配置
                                    axis.key.clear();
                                    for (KeyCodes.Codes key : keys)
                                        axis.key.add(key.name());

                                    // 更新文本
                                    updateTextKey(textKey, keys);
                                }
                            }
                        });
                        dialog.show();
                    }
                });

                items.add(item);
            }

            return items.toArray(new View[0]);
        }

        @SuppressLint("InflateParams")
        private View createBtnItem() {
            ArrayList<String> keyList;
            RootLayerConfig.ControllerBtn btn = config.getControllerBtn();
            View item;

            switch (moduleType) {
                case Button_A:
                    keyList = btn.key_Button_A;
                    break;
                case Button_B:
                    keyList = btn.key_Button_B;
                    break;
                case Button_X:
                    keyList = btn.key_Button_X;
                    break;
                case Button_Y:
                    keyList = btn.key_Button_Y;
                    break;
                case Button_Back:
                    keyList = btn.key_Button_Select;
                    break;
                case Button_Start:
                    keyList = btn.key_Button_Start;
                    break;
                case Button_L1:
                    keyList = btn.key_Button_L1;
                    break;
                case Button_R1:
                    keyList = btn.key_Button_R1;
                    break;
                case Button_L3:
                case Joystick_Left:
                    keyList = btn.key_Button_ThumbL;
                    break;
                case Button_R3:
                case Joystick_Right:
                    keyList = btn.key_Button_ThumbR;
                    break;
                default:
                    return null;
            }

            item = inflater.inflate(R.layout.controller_select_key_btn_item, null);
            AppCompatTextView textBtnName = item.findViewById(R.id.text_btn_name);
            AppCompatTextView textKey = item.findViewById(R.id.text_key);
            AppCompatButton btnSelect = item.findViewById(R.id.btn_select);
            if (moduleType == ControllerView.ControllerModuleType.Joystick_Left)
                textBtnName.setText(ControllerView.ControllerModuleType.Button_L3.name());
            else if (moduleType == ControllerView.ControllerModuleType.Joystick_Right)
                textBtnName.setText(ControllerView.ControllerModuleType.Button_R3.name());
            else
                textBtnName.setText(moduleType.name());

            updateTextKey(textKey, getKeys(keyList));

            ArrayList<String> finalKeyList = keyList;
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<KeyCodes.Codes> tmpList = getKeys(finalKeyList);
                    SelectKeyDialog dialog = new SelectKeyDialog(getContext(), tmpList);
                    dialog.setOnSelectKeyFinishedCallback(new SelectKeyDialog.OnSelectKeyFinishedCallback() {
                        @Override
                        public void OnSelectKeyFinished(List<KeyCodes.Codes> keys) {
                            // 最多映射3个按钮
                            if (keys.size() > 3) {
                                new MaterialAlertDialogBuilder(getContext())
                                        .setTitle(getContext().getString(R.string.error))
                                        .setMessage(getContext().getString(R.string.map_up_limit))
                                        .setPositiveButton(getContext().getString(R.string.ok), null)
                                        .create()
                                        .show();
                            } else {
                                // 更新配置
                                finalKeyList.clear();
                                for (KeyCodes.Codes key : keys)
                                    finalKeyList.add(key.name());

                                // 更新文本
                                updateTextKey(textKey, keys);
                            }
                        }
                    });
                    dialog.show();
                }
            });

            return item;
        }

        private void updateTextKey(TextView textView, List<KeyCodes.Codes> keys) {
            StringBuilder str = new StringBuilder();

            for (int i = 0; i < keys.size(); i++) {
                str.append(keys.get(i).name());
                if (i != keys.size() - 1)
                    str.append("+");
            }
            textView.setText(str.toString());
        }

        private List<KeyCodes.Codes> getKeys(List<String> list) {
            ArrayList<KeyCodes.Codes> tmp = new ArrayList<>();
            for (String keyName : list) {
                try {
                    tmp.add(KeyCodes.Codes.valueOf(keyName));
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            return tmp;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(layout);
        }

    }

}

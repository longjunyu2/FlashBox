package com.aof.flashbox.input.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.hardware.input.InputManager;
import android.os.Build;
import android.os.Bundle;
import android.view.InputDevice;
import android.view.View;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.aof.flashbox.R;
import com.aof.flashbox.input.widget.RootLayerConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class RootEditDialog extends AppCompatDialog implements InputManager.InputDeviceListener {

    private final RootLayerConfig config;

    private final InputManager inputManager;

    private AppCompatTextView textSelectedKeyboard;

    private AppCompatTextView textSelectedController;

    private List<String> keyboardList;

    private List<String> controllerList;

    public RootEditDialog(@NonNull Context context, RootLayerConfig config) {
        super(context);
        this.config = config;
        setTitle(context.getString(R.string.edit_view));
        inputManager = (InputManager) getContext().getSystemService(Context.INPUT_SERVICE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_root_edit);

        // 启用物理键盘项
        SwitchCompat switchEnableKeyboard = findViewById(R.id.switch_enable_keyboard);
        assert switchEnableKeyboard != null;
        switchEnableKeyboard.setChecked(config.isEnableKeyboard());

        // 选择物理键盘项
        View viewSelectKeyboard = findViewById(R.id.select_keyboard);
        textSelectedKeyboard = findViewById(R.id.text_selected_keyboard);
        AppCompatButton btnSelectKeyboard = findViewById(R.id.btn_select_keyboard);
        assert viewSelectKeyboard != null && textSelectedKeyboard != null && btnSelectKeyboard != null;
        if (!config.isEnableKeyboard())
            viewSelectKeyboard.setVisibility(View.GONE);
        switchEnableKeyboard.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewSelectKeyboard.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                config.setEnableKeyboard(isChecked);
            }
        });
        btnSelectKeyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建选择键盘的对话框
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.select_keyboard)
                        .setItems(keyboardList.toArray(new String[0]), new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                config.setKeyboard(keyboardList.get(which));
                                updateKeyboardAndController();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
            }
        });

        // 启用物理控制器项
        SwitchCompat switchEnableController = findViewById(R.id.switch_enable_controller);
        assert switchEnableController != null;
        switchEnableController.setChecked(config.isEnableController());

        // 选择物理控制器项
        View viewSelectController = findViewById(R.id.select_controller);
        textSelectedController = findViewById(R.id.text_selected_controller);
        AppCompatButton btnSelectController = findViewById(R.id.btn_select_controller);
        assert viewSelectController != null && textSelectedController != null && btnSelectController != null;
        if (!config.isEnableController())
            viewSelectController.setVisibility(View.GONE);
        switchEnableController.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                viewSelectController.setVisibility(isChecked ? View.VISIBLE : View.GONE);
                config.setEnableController(isChecked);
            }
        });
        btnSelectController.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 创建选择控制器的对话框
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle(R.string.select_controller)
                        .setItems(controllerList.toArray(new String[0]), new OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                config.setController(controllerList.get(which));
                                updateKeyboardAndController();
                            }
                        })
                        .setNegativeButton(R.string.cancel, null)
                        .create()
                        .show();
            }
        });

        // 更新选择的物理键盘和物理控制器
        updateKeyboardAndController();

        // TODO: 添加和实现对触屏控制的配置

        // 注册外部输入设备监听器
        inputManager.registerInputDeviceListener(this, null);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        // 注销外部输入设备监听器
        inputManager.unregisterInputDeviceListener(this);
    }

    @Override
    public void onInputDeviceAdded(int deviceId) {
        updateKeyboardAndController();
    }

    @Override
    public void onInputDeviceRemoved(int deviceId) {
        updateKeyboardAndController();
    }

    @Override
    public void onInputDeviceChanged(int deviceId) {

    }

    /**
     * 更新选择的物理键盘和物理控制器
     */
    private void updateKeyboardAndController() {
        // 更新选择的物理键盘
        if (keyboardList == null)
            keyboardList = new ArrayList<>();
        else
            keyboardList.clear();

        for (int id : getKeyboardDeviceIds(InputDevice.SOURCE_KEYBOARD)) {
            InputDevice device = inputManager.getInputDevice(id);
            if (device != null)
                keyboardList.add(device.getName());
        }

        if (keyboardList.contains(config.getKeyboard()))
            textSelectedKeyboard.setText(config.getKeyboard());
        textSelectedKeyboard.setText(keyboardList.contains(config.getKeyboard())
                ? config.getKeyboard()
                : getContext().getString(R.string.unavailable));

        // 更新选择的物理控制器
        if (controllerList == null)
            controllerList = new ArrayList<>();
        else
            controllerList.clear();

        for (int id : getKeyboardDeviceIds(InputDevice.SOURCE_GAMEPAD)) {
            InputDevice device = inputManager.getInputDevice(id);
            if (device != null)
                controllerList.add(device.getName());
        }

        if (controllerList.contains(config.getKeyboard()))
            textSelectedController.setText(config.getKeyboard());
        textSelectedController.setText(controllerList.contains(config.getKeyboard())
                ? config.getController()
                : getContext().getString(R.string.unavailable));
    }

    /**
     * 获取目标设备id列表
     *
     * @param sourceTarget 目标设备源
     * @return 目标设备id列表
     */
    private ArrayList<Integer> getKeyboardDeviceIds(int sourceTarget) {
        int[] inputDeviceIds = inputManager.getInputDeviceIds();
        ArrayList<Integer> extDeviceIds = new ArrayList<>();

        for (int deviceId : inputDeviceIds) {
            InputDevice device = inputManager.getInputDevice(deviceId);
            if (device == null)
                continue;
            int sources = device.getSources();

            // 只获取外部键盘设备
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // 对于安卓Q及以上，判断是否是外部设备
                if ((sources & sourceTarget) == sourceTarget && device.isExternal()) {
                    extDeviceIds.add(deviceId);
                }
            } else {
                if ((sources & sourceTarget) == sourceTarget) {
                    extDeviceIds.add(deviceId);
                }
            }
        }

        return extDeviceIds;
    }
}

package com.aof.flashbox.input.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.aof.flashbox.R;
import com.aof.flashbox.input.view.ControllerView;
import com.aof.flashbox.input.widget.RootLayerConfig;

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
                // TODO: 创建并显示手柄映射对话框
                Log.e("test", type.name());
            }
        });
    }
}

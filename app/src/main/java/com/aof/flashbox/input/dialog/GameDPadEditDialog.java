package com.aof.flashbox.input.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.aof.flashbox.R;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.widget.GameDPadLayerConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class GameDPadEditDialog extends AppCompatDialog {

    private final GameDPadLayerConfig config;

    private OnEditFinishedCallback finishedCallback;

    public GameDPadEditDialog(@NonNull Context context, GameDPadLayerConfig config) {
        super(context);
        this.setTitle(R.string.edit_view);
        this.config = config;
    }

    /**
     * 设置编辑完成回调
     *
     * @param callback 回调
     */
    public void setOnEditFinishedCallback(OnEditFinishedCallback callback) {
        finishedCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_dialog_gamejoystick_edit);

        // 控件大小编辑项
        AppCompatSeekBar seekbarSize = findViewById(R.id.seekbar_size);
        AppCompatTextView textSize = findViewById(R.id.text_size);
        assert seekbarSize != null && textSize != null;
        seekbarSize.setMax(100);
        seekbarSize.setMin(40);
        seekbarSize.setProgress(config.getWidth());
        textSize.setText(String.valueOf(config.getWidth()));
        seekbarSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    config.setWidth(progress);
                    config.setHeight(progress);
                }
                textSize.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 上键值编辑项
        AppCompatButton upKeyBtn = findViewById(R.id.btn_select_up);
        AppCompatTextView upKeyText = findViewById(R.id.text_key_up);
        assert upKeyBtn != null && upKeyText != null;
        updateKeysText(1, upKeyText);
        upKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectKeyDialog(1, upKeyText);
            }
        });

        // 右键值编辑项
        AppCompatButton rightKeyBtn = findViewById(R.id.btn_select_right);
        AppCompatTextView rightKeyText = findViewById(R.id.text_key_right);
        assert rightKeyBtn != null && rightKeyText != null;
        updateKeysText(2, rightKeyText);
        rightKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectKeyDialog(2, rightKeyText);
            }
        });

        // 下键值编辑项
        AppCompatButton downKeyBtn = findViewById(R.id.btn_select_down);
        AppCompatTextView downKeyText = findViewById(R.id.text_key_down);
        assert downKeyBtn != null && downKeyText != null;
        updateKeysText(3, downKeyText);
        downKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectKeyDialog(3, downKeyText);
            }
        });

        // 左键值编辑项
        AppCompatButton leftKeyBtn = findViewById(R.id.btn_select_left);
        AppCompatTextView leftKeyText = findViewById(R.id.text_key_left);
        assert leftKeyBtn != null && leftKeyText != null;
        updateKeysText(4, leftKeyText);
        leftKeyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectKeyDialog(4, leftKeyText);
            }
        });


        // 设置结束回调
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (finishedCallback != null)
                    finishedCallback.onEditFinished();
            }
        });
    }

    /**
     * 根据方向获取键
     *
     * @param dir 方向
     * @return 键
     */
    private List<KeyCodes.Codes> getKeys(int dir) {
        List<KeyCodes.Codes> keys = null;

        switch (dir) {
            case 1:
                keys = config.getUpKeys();
                break;
            case 2:
                keys = config.getRightKeys();
                break;
            case 3:
                keys = config.getDownKeys();
                break;
            case 4:
                keys = config.getLeftKeys();
                break;
        }

        return keys;
    }

    /**
     * 更新显示键的文本
     *
     * @param dir  方向
     * @param view 文本视图
     */
    private void updateKeysText(int dir, TextView view) {
        StringBuilder str = new StringBuilder();
        List<KeyCodes.Codes> keys = getKeys(dir);

        for (int i = 0; i < keys.size(); i++) {
            str.append(keys.get(i).name());
            if (i != keys.size() - 1)
                str.append("+");
        }
        view.setText(str.toString());
    }

    /**
     * 创建并显示选择键值的对话框
     *
     * @param dir  方向
     * @param view 要更新的文本视图
     */
    private void showSelectKeyDialog(int dir, TextView view) {
        SelectKeyDialog dialog = new SelectKeyDialog(getContext(), getKeys(dir));
        dialog.setOnSelectKeyFinishedCallback(new SelectKeyDialog.OnSelectKeyFinishedCallback() {
            @Override
            public void OnSelectKeyFinished(List<KeyCodes.Codes> keys) {
                if (keys.size() > 3) {
                    new MaterialAlertDialogBuilder(getContext())
                            .setTitle(R.string.error)
                            .setMessage(R.string.map_up_limit)
                            .setPositiveButton(R.string.ok, null)
                            .create()
                            .show();
                } else {
                    KeyCodes.Codes[] codes = keys.toArray(new KeyCodes.Codes[0]);
                    switch (dir) {
                        case 1:
                            config.setUpKeys(codes);
                            break;
                        case 2:
                            config.setRightKeys(codes);
                            break;
                        case 3:
                            config.setDownKeys(codes);
                            break;
                        case 4:
                            config.setLeftKeys(codes);
                            break;
                    }
                    updateKeysText(dir, view);
                }
            }
        });
        dialog.show();
    }
}

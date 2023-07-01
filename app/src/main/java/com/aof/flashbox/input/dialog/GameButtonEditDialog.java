package com.aof.flashbox.input.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;

import com.aof.flashbox.R;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.widget.GameButtonLayerConfig;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;
import java.util.Objects;

public class GameButtonEditDialog extends AppCompatDialog {

    private final GameButtonLayerConfig config;

    private OnEditFinishedCallback finishedCallback;

    public GameButtonEditDialog(@NonNull Context context, GameButtonLayerConfig config) {
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
        setContentView(R.layout.layout_dialog_gamebutton_edit);

        // 控件宽度编辑项
        AppCompatSeekBar seekbarWidth = findViewById(R.id.seekbar_width);
        AppCompatTextView textWidth = findViewById(R.id.text_width);
        assert seekbarWidth != null && textWidth != null;
        seekbarWidth.setMax(50);
        seekbarWidth.setMin(10);
        seekbarWidth.setProgress(config.getWidth());
        textWidth.setText(String.valueOf(config.getWidth()));
        seekbarWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    config.setWidth(progress);
                textWidth.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 控件高度编辑项
        AppCompatSeekBar seekBarHeight = findViewById(R.id.seekbar_height);
        AppCompatTextView textHeight = findViewById(R.id.text_height);
        assert seekBarHeight != null && textHeight != null;
        seekBarHeight.setMax(50);
        seekBarHeight.setMin(10);
        seekBarHeight.setProgress(config.getHeight());
        textHeight.setText(String.valueOf(config.getHeight()));
        seekBarHeight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    config.setHeight(progress);
                textHeight.setText(String.valueOf(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        // 按键值编辑项
        AppCompatButton keycodeBtn = findViewById(R.id.btn_select);
        AppCompatTextView textKeys = findViewById(R.id.text_key);
        assert keycodeBtn != null && textKeys != null;
        updateKeysText(textKeys);
        keycodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectKeyDialog dialog = new SelectKeyDialog(getContext(), config.getKeys());
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
                            config.setKeys(keys.toArray(new KeyCodes.Codes[0]));
                            updateKeysText(textKeys);
                        }
                    }
                });
                dialog.show();
            }
        });

        // 保持按住编辑项
        SwitchCompat switchKeep = findViewById(R.id.switch_keep);
        assert switchKeep != null;
        switchKeep.setChecked(config.isKeepPress());
        switchKeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                config.setKeepPress(isChecked);
            }
        });

        // 控件文本编辑项
        AppCompatEditText editText = findViewById(R.id.edit_text);
        assert editText != null;
        editText.setText(config.getText());

        // X坐标编辑项
        AppCompatEditText editX = findViewById(R.id.edit_x);
        assert editX != null;
        editX.setText(String.valueOf(config.getX()));

        // Y坐标编辑项
        AppCompatEditText editY = findViewById(R.id.edit_y);
        assert editY != null;
        editY.setText(String.valueOf(config.getY()));

        // 设置结束回调
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                config.setText(Objects.requireNonNull(editText.getText()).toString());
                config.setX(Float.parseFloat(Objects.requireNonNull(editX.getText()).toString()));
                config.setY(Float.parseFloat(Objects.requireNonNull(editY.getText()).toString()));
                if (finishedCallback != null)
                    finishedCallback.onEditFinished();
            }
        });
    }

    /**
     * 更新显示键的文本
     *
     * @param view 文本视图
     */
    private void updateKeysText(TextView view) {
        StringBuilder str = new StringBuilder();
        List<KeyCodes.Codes> keys = config.getKeys();
        for (int i = 0; i < keys.size(); i++) {
            str.append(keys.get(i).name());
            if (i != keys.size() - 1)
                str.append("+");
        }
        view.setText(str.toString());
    }

    public interface OnEditFinishedCallback {
        void onEditFinished();
    }
}

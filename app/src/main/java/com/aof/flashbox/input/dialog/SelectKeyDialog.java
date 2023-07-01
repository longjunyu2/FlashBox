package com.aof.flashbox.input.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDialog;

import com.aof.flashbox.R;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.view.KeyboardView;

import java.util.List;

public class SelectKeyDialog extends AppCompatDialog {

    private final List<KeyCodes.Codes> keys;

    private OnSelectKeyFinishedCallback selectFinishedCallback;

    public SelectKeyDialog(@NonNull Context context, List<KeyCodes.Codes> keys) {
        super(context);
        setTitle(R.string.select_key);
        this.keys = keys;
    }

    /**
     * 设置选择完成回调
     *
     * @param callback 回调
     */
    public void setOnSelectKeyFinishedCallback(OnSelectKeyFinishedCallback callback) {
        selectFinishedCallback = callback;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        KeyboardView keyboardView = new KeyboardView(getContext(), 0.9f, KeyboardView.PERFORM_SELECT);
        keyboardView.setOnKeySelectionChangedListener(new KeyboardView.KeySelectionChangedListener() {
            @Override
            public void onKeySelectionChanged(KeyCodes.Codes key, boolean selected) {
                if (selected)
                    keys.add(key);
                else
                    keys.remove(key);
            }
        });
        keyboardView.setPreSelectedKeys(keys.toArray(new KeyCodes.Codes[0]));
        setContentView(keyboardView);
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (selectFinishedCallback != null)
                    selectFinishedCallback.OnSelectKeyFinished(keys);
            }
        });
    }

    public interface OnSelectKeyFinishedCallback {
        void OnSelectKeyFinished(List<KeyCodes.Codes> keys);
    }
}

package com.aof.flashbox.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.aof.flashbox.input.InputManager;
import com.aof.flashbox.input.widget.BaseLayerConfig;
import com.aof.flashbox.input.widget.RootLayerConfig;

import java.util.ArrayList;

public class PlayerFragment extends Fragment {

    private InputManager inputManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FrameLayout layout = new FrameLayout(getContext());
        layout.setLayoutParams(new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // 测试
        ArrayList<BaseLayerConfig> configs = new ArrayList<>();
        configs.add(new RootLayerConfig());
        inputManager = new InputManager(getContext());
        inputManager.start(layout, configs);

        return layout;
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}

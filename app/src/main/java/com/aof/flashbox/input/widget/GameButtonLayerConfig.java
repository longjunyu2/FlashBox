package com.aof.flashbox.input.widget;

import com.aof.flashbox.input.key.KeyCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameButtonLayerConfig extends BaseLayerConfig {

    private String text = "";

    private ArrayList<String> keys = new ArrayList<>();

    private boolean keepPress = false;

    @Override
    public Type getType() {
        return Type.GameButton;
    }

    /**
     * 获取控件的文本
     *
     * @return 文本
     */
    public String getText() {
        return text;
    }

    /**
     * 设置控件的文本
     *
     * @param text 文本
     * @return this
     */
    public GameButtonLayerConfig setText(String text) {
        this.text = text;
        return this;
    }

    /**
     * 获取键
     *
     * @return 键
     */
    public List<KeyCodes.Codes> getKeys() {
        ArrayList<KeyCodes.Codes> list = new ArrayList<>();
        for (String name : keys)
            list.add(KeyCodes.Codes.valueOf(name));
        return list;
    }

    /**
     * 设置键值
     *
     * @param keys 键值名
     * @return this
     */
    public GameButtonLayerConfig setKeys(String... keys) {
        this.keys = new ArrayList<>(Arrays.asList(keys));
        return this;
    }

    /**
     * 设置键值
     *
     * @param keys 键
     * @return this
     */
    public GameButtonLayerConfig setKeys(KeyCodes.Codes... keys) {
        String[] strKeys = new String[keys.length];
        for (int i = 0; i < strKeys.length; i++) {
            strKeys[i] = keys[i].name();
        }
        return setKeys(strKeys);
    }

    /**
     * 设置键值
     *
     * @param keys 键值
     * @return this
     */
    public GameButtonLayerConfig setKeys(int... keys) {
        KeyCodes.Codes[] codeKeys = new KeyCodes.Codes[keys.length];
        for (int i = 0; i < codeKeys.length; i++) {
            codeKeys[i] = KeyCodes.getInstance().fromKeyCode(keys[i]);
        }
        return setKeys(codeKeys);
    }

    /**
     * 设置按下保持
     *
     * @param keepPress 是否按下保持
     */
    public GameButtonLayerConfig setKeepPress(boolean keepPress) {
        this.keepPress = keepPress;
        return this;
    }

    /**
     * 获取是否按下保持
     *
     * @return 是否按下保持
     */
    public boolean isKeepPress() {
        return this.keepPress;
    }

    @Override
    public GameButtonLayerConfig setWidth(int width) {
        return (GameButtonLayerConfig) super.setWidth(width);
    }

    @Override
    public GameButtonLayerConfig setHeight(int height) {
        return (GameButtonLayerConfig) super.setHeight(height);
    }

    @Override
    public GameButtonLayerConfig setX(float x) {
        return (GameButtonLayerConfig) super.setX(x);
    }

    @Override
    public GameButtonLayerConfig setY(float y) {
        return (GameButtonLayerConfig) super.setY(y);
    }
}

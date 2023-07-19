package com.aof.flashbox.input.widget;

import com.aof.flashbox.input.key.KeyCodes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameDPadLayerConfig extends BaseLayerConfig {

    private ArrayList<String> up_keys = new ArrayList<>();

    private ArrayList<String> right_keys = new ArrayList<>();

    private ArrayList<String> down_keys = new ArrayList<>();

    private ArrayList<String> left_keys = new ArrayList<>();

    public List<KeyCodes.Codes> getUpKeys() {
        return getKeys(1);
    }

    public List<KeyCodes.Codes> getRightKeys() {
        return getKeys(2);
    }

    public List<KeyCodes.Codes> getDownKeys() {
        return getKeys(3);
    }

    public List<KeyCodes.Codes> getLeftKeys() {
        return getKeys(4);
    }

    private List<KeyCodes.Codes> getKeys(int dir) {
        ArrayList<KeyCodes.Codes> list = new ArrayList<>();
        ArrayList<String> keys = null;

        switch (dir) {
            case 1:
                keys = up_keys;
                break;
            case 2:
                keys = right_keys;
                break;
            case 3:
                keys = down_keys;
                break;
            case 4:
                keys = left_keys;
                break;
        }

        assert keys != null;
        for (String name : keys)
            list.add(KeyCodes.Codes.valueOf(name));
        return list;
    }

    private GameDPadLayerConfig setKeys(int dir, String... keys) {
        ArrayList<String> list = new ArrayList<>(Arrays.asList(keys));
        switch (dir) {
            case 1:
                this.up_keys = list;
                break;
            case 2:
                this.right_keys = list;
                break;
            case 3:
                this.down_keys = list;
                break;
            case 4:
                this.left_keys = list;
                break;
        }
        return this;
    }

    private GameDPadLayerConfig setKeys(int dir, KeyCodes.Codes... keys) {
        String[] strKeys = new String[keys.length];
        for (int i = 0; i < strKeys.length; i++) {
            strKeys[i] = keys[i].name();
        }
        return setKeys(dir, strKeys);
    }

    public GameDPadLayerConfig setUpKeys(KeyCodes.Codes... keys) {
        return setKeys(1, keys);
    }

    public GameDPadLayerConfig setRightKeys(KeyCodes.Codes... keys) {
        return setKeys(2, keys);
    }

    public GameDPadLayerConfig setDownKeys(KeyCodes.Codes... keys) {
        return setKeys(3, keys);
    }

    public GameDPadLayerConfig setLeftKeys(KeyCodes.Codes... keys) {
        return setKeys(4, keys);
    }

    @Override
    public Type getType() {
        return Type.GameDPad;
    }
}

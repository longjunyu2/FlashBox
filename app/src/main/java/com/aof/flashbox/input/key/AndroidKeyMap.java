package com.aof.flashbox.input.key;

import static android.view.KeyEvent.*;

import static com.aof.flashbox.input.key.KeyCodes.Codes.*;

import java.util.HashMap;

public class AndroidKeyMap {
    private static HashMap<Integer, KeyCodes.Codes> androidKeyMap;

    /**
     * 获取键值表
     *
     * @return 键值表
     */
    public static HashMap<Integer, KeyCodes.Codes> getAndroidKeyMap() {
        if (androidKeyMap == null)
            init();
        return androidKeyMap;
    }

    /**
     * 初始化键值表，建立安卓键值->Ruffle键值的映射
     */
    private static void init() {
        androidKeyMap = new HashMap<>();
        add(KEYCODE_BACK, Backspace);
        add(KEYCODE_TAB, Tab);
        add(KEYCODE_ENTER, Return);
        // Command 键存在吗？
        add(KEYCODE_SHIFT_LEFT, LShift);
        add(KEYCODE_SHIFT_RIGHT, RShift);
        add(KEYCODE_CTRL_LEFT, LCtrl);
        add(KEYCODE_CTRL_RIGHT, RCtrl);
        add(KEYCODE_ALT_LEFT, LAlt);
        add(KEYCODE_ALT_RIGHT, RAlt);
        add(KEYCODE_MEDIA_PAUSE, Pause);
        add(KEYCODE_CAPS_LOCK, CapsLock);
        add(KEYCODE_NUM_LOCK, NumLock);
        add(KEYCODE_ESCAPE, Escape);
        add(KEYCODE_SPACE, Space);
        add(KEYCODE_PAGE_UP, PgUp);
        add(KEYCODE_PAGE_DOWN, PgDown);
        add(KEYCODE_ENDCALL, End);
        add(KEYCODE_HOME, Home);
        add(KEYCODE_DPAD_LEFT, Left);
        add(KEYCODE_DPAD_UP, Up);
        add(KEYCODE_DPAD_RIGHT, Right);
        add(KEYCODE_DPAD_DOWN, Down);
        add(KEYCODE_INSERT, Insert);
        add(KEYCODE_DEL, Delete);
        add(KEYCODE_0, Key0);
        add(KEYCODE_1, Key1);
        add(KEYCODE_2, Key2);
        add(KEYCODE_3, Key3);
        add(KEYCODE_4, Key4);
        add(KEYCODE_5, Key5);
        add(KEYCODE_6, Key6);
        add(KEYCODE_7, Key7);
        add(KEYCODE_8, Key8);
        add(KEYCODE_9, Key9);
        add(KEYCODE_A, A);
        add(KEYCODE_B, B);
        add(KEYCODE_C, C);
        add(KEYCODE_D, D);
        add(KEYCODE_E, E);
        add(KEYCODE_F, F);
        add(KEYCODE_G, G);
        add(KEYCODE_H, H);
        add(KEYCODE_I, I);
        add(KEYCODE_J, J);
        add(KEYCODE_K, K);
        add(KEYCODE_L, L);
        add(KEYCODE_M, M);
        add(KEYCODE_N, N);
        add(KEYCODE_O, O);
        add(KEYCODE_P, P);
        add(KEYCODE_Q, Q);
        add(KEYCODE_R, R);
        add(KEYCODE_S, S);
        add(KEYCODE_T, T);
        add(KEYCODE_U, U);
        add(KEYCODE_V, V);
        add(KEYCODE_W, W);
        add(KEYCODE_X, X);
        add(KEYCODE_Y, Y);
        add(KEYCODE_Z, Z);
        add(KEYCODE_NUMPAD_0, Numpad0);
        add(KEYCODE_NUMPAD_1, Numpad1);
        add(KEYCODE_NUMPAD_2, Numpad2);
        add(KEYCODE_NUMPAD_3, Numpad3);
        add(KEYCODE_NUMPAD_4, Numpad4);
        add(KEYCODE_NUMPAD_5, Numpad5);
        add(KEYCODE_NUMPAD_6, Numpad6);
        add(KEYCODE_NUMPAD_7, Numpad7);
        add(KEYCODE_NUMPAD_8, Numpad8);
        add(KEYCODE_NUMPAD_9, Numpad9);
        add(KEYCODE_NUMPAD_MULTIPLY, Multiply);
        add(KEYCODE_NUMPAD_ADD, Plus);
        add(KEYCODE_NUMPAD_ENTER, NumpadEnter);
        add(KEYCODE_NUMPAD_SUBTRACT, Minus);
        add(KEYCODE_NUMPAD_DOT, NumpadPeriod);
        add(KEYCODE_NUMPAD_DIVIDE, NumpadSlash);
        add(KEYCODE_F1, F1);
        add(KEYCODE_F2, F2);
        add(KEYCODE_F3, F3);
        add(KEYCODE_F4, F4);
        add(KEYCODE_F5, F5);
        add(KEYCODE_F6, F6);
        add(KEYCODE_F7, F7);
        add(KEYCODE_F8, F8);
        add(KEYCODE_F9, F9);
        add(KEYCODE_F10, F10);
        add(KEYCODE_F11, F11);
        add(KEYCODE_F12, F12);
        add(KEYCODE_SCROLL_LOCK, ScrollLock);
        add(KEYCODE_SEMICOLON, Semicolon);
        add(KEYCODE_EQUALS, Equals);
        add(KEYCODE_COMMA, Comma);
        add(KEYCODE_MINUS, Minus);
        add(KEYCODE_PERIOD, Period);
        add(KEYCODE_SLASH, Slash);
        add(KEYCODE_GRAVE, Grave);
        add(KEYCODE_LEFT_BRACKET, LBracket);
        add(KEYCODE_RIGHT_BRACKET, RBracket);
        add(KEYCODE_APOSTROPHE, Apostrophe);
    }

    private static void add(int androidKey, KeyCodes.Codes key) {
        androidKeyMap.put(androidKey, key);
    }
}

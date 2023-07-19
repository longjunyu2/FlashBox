package com.aof.flashbox.input.driver;

import com.aof.flashbox.input.event.KeyEvent;
import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.view.JoyView;
import com.aof.flashbox.input.widget.GameJoystickLayerConfig;

import java.util.ArrayList;
import java.util.List;

public class GameJoyStickDefaultDriver extends BaseDriver {
    private JoyView.Dir lastDir = JoyView.Dir.CENTER;

    public GameJoyStickDefaultDriver(GameJoystickLayerConfig config) {
        super(config);
    }

    /**
     * 生成输入事件
     * @param keys 键
     * @param action 动作
     */
    private void genInputEvent(List<KeyCodes.Codes> keys, KeyEvent.Action action) {
        for (KeyCodes.Codes key : keys)
            getOnEventGenCallback().onEventGen(new KeyEvent(action, key));
    }

    /**
     * 根据方向获取键
     * @param dir 方向
     * @return 键
     */
    private List<KeyCodes.Codes> getKeys(JoyView.Dir dir) {
        ArrayList<KeyCodes.Codes> keys = new ArrayList<>();

        switch (dir) {
            case UP:
                keys.addAll(getConfig().getUpKeys());
                break;
            case DOWN:
                keys.addAll(getConfig().getDownKeys());
                break;
            case LEFT:
                keys.addAll(getConfig().getLeftKeys());
                break;
            case RIGHT:
                keys.addAll(getConfig().getRightKeys());
                break;
            case RIGHT_UP:
                keys.addAll(getConfig().getUpKeys());
                keys.addAll(getConfig().getRightKeys());
                break;
            case RIGHT_DOWN:
                keys.addAll(getConfig().getRightKeys());
                keys.addAll(getConfig().getDownKeys());
                break;
            case LEFT_DOWN:
                keys.addAll(getConfig().getLeftKeys());
                keys.addAll(getConfig().getDownKeys());
                break;
            case LEFT_UP:
                keys.addAll(getConfig().getLeftKeys());
                keys.addAll(getConfig().getUpKeys());
                break;
        }

        return keys;
    }

    /**
     * 上
     */
    public void up() {
        switch (lastDir) {
            case LEFT_UP:
                genInputEvent(getConfig().getLeftKeys(), KeyEvent.Action.Up);
                break;
            case RIGHT_UP:
                genInputEvent(getConfig().getRightKeys(), KeyEvent.Action.Up);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getConfig().getUpKeys(), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.UP;
    }

    /**
     * 下
     */
    public void down() {
        switch (lastDir) {
            case LEFT_DOWN:
                genInputEvent(getConfig().getLeftKeys(), KeyEvent.Action.Up);
                break;
            case RIGHT_DOWN:
                genInputEvent(getConfig().getRightKeys(), KeyEvent.Action.Up);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getConfig().getDownKeys(), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.DOWN;
    }

    /**
     * 左
     */
    public void left() {
        switch (lastDir) {
            case LEFT_UP:
                genInputEvent(getConfig().getUpKeys(), KeyEvent.Action.Up);
                break;
            case LEFT_DOWN:
                genInputEvent(getConfig().getDownKeys(), KeyEvent.Action.Up);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getConfig().getLeftKeys(), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.LEFT;
    }

    /**
     * 右
     */
    public void right() {
        switch (lastDir) {
            case RIGHT_UP:
                genInputEvent(getConfig().getUpKeys(), KeyEvent.Action.Up);
                break;
            case RIGHT_DOWN:
                genInputEvent(getConfig().getDownKeys(), KeyEvent.Action.Up);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getConfig().getRightKeys(), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.RIGHT;
    }

    /**
     * 右上
     */
    public void right_up() {
        switch (lastDir) {
            case UP:
                genInputEvent(getConfig().getRightKeys(), KeyEvent.Action.Down);
                break;
            case RIGHT:
                genInputEvent(getConfig().getUpKeys(), KeyEvent.Action.Down);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getKeys(JoyView.Dir.RIGHT_UP), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.RIGHT_UP;
    }

    /**
     * 右下
     */
    public void right_down() {
        switch (lastDir) {
            case RIGHT:
                genInputEvent(getConfig().getDownKeys(), KeyEvent.Action.Down);
                break;
            case DOWN:
                genInputEvent(getConfig().getRightKeys(), KeyEvent.Action.Down);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getKeys(JoyView.Dir.RIGHT_DOWN), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.RIGHT_DOWN;
    }

    /**
     * 左上
     */
    public void left_up() {
        switch (lastDir) {
            case LEFT:
                genInputEvent(getConfig().getUpKeys(), KeyEvent.Action.Down);
                break;
            case UP:
                genInputEvent(getConfig().getLeftKeys(), KeyEvent.Action.Down);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getKeys(JoyView.Dir.LEFT_UP), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.LEFT_UP;
    }

    /**
     * 左下
     */
    public void left_down() {
        switch (lastDir) {
            case LEFT:
                genInputEvent(getConfig().getDownKeys(), KeyEvent.Action.Down);
                break;
            case DOWN:
                genInputEvent(getConfig().getLeftKeys(), KeyEvent.Action.Down);
                break;
            default:
                genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
            case CENTER:
                genInputEvent(getKeys(JoyView.Dir.LEFT_DOWN), KeyEvent.Action.Down);
        }
        lastDir = JoyView.Dir.LEFT_DOWN;
    }

    /**
     * 中间
     */
    public void center() {
        genInputEvent(getKeys(lastDir), KeyEvent.Action.Up);
        lastDir = JoyView.Dir.CENTER;
    }

    @Override
    public GameJoystickLayerConfig getConfig() {
        return (GameJoystickLayerConfig) super.getConfig();
    }
}

package com.aof.flashbox.input.key;

import java.util.HashMap;

public class KeyMap {

    private static HashMap<KeyCodes.Codes, KeyItem> keyboardMap;

    public static HashMap<KeyCodes.Codes, KeyItem> getMap() {
        if (keyboardMap == null)
            init();
        return keyboardMap;
    }

    private static void init() {
        keyboardMap = new HashMap<>();
        for (KeyCodes.Codes key : KeyCodes.Codes.values()) {
            String[] symbols = null;
            switch (key) {
                case Escape:
                    symbols = new String[]{"Esc"};
                    break;
                case F1:
                    symbols = new String[]{"F1"};
                    break;
                case F2:
                    symbols = new String[]{"F2"};
                    break;
                case F3:
                    symbols = new String[]{"F3"};
                    break;
                case F4:
                    symbols = new String[]{"F4"};
                    break;
                case F5:
                    symbols = new String[]{"F5"};
                    break;
                case F6:
                    symbols = new String[]{"F6"};
                    break;
                case F7:
                    symbols = new String[]{"F7"};
                    break;
                case F8:
                    symbols = new String[]{"F8"};
                    break;
                case F9:
                    symbols = new String[]{"F9"};
                    break;
                case F10:
                    symbols = new String[]{"F10"};
                    break;
                case F11:
                    symbols = new String[]{"F11"};
                    break;
                case F12:
                    symbols = new String[]{"F12"};
                    break;
                case Tilde:
                    symbols = new String[]{"~", "`"};
                    break;
                case Key1:
                    symbols = new String[]{"!", "1"};
                    break;
                case Key2:
                    symbols = new String[]{"@", "2"};
                    break;
                case Key3:
                    symbols = new String[]{"#", "3"};
                    break;
                case Key4:
                    symbols = new String[]{"$", "4"};
                    break;
                case Key5:
                    symbols = new String[]{"%", "5"};
                    break;
                case Key6:
                    symbols = new String[]{"^", "6"};
                    break;
                case Key7:
                    symbols = new String[]{"&", "7"};
                    break;
                case Key8:
                    symbols = new String[]{"*", "8"};
                    break;
                case Key9:
                    symbols = new String[]{"(", "9"};
                    break;
                case Key0:
                    symbols = new String[]{")", "0"};
                    break;
                case Minus:
                    symbols = new String[]{"_", "-"};
                    break;
                case Equals:
                    symbols = new String[]{"+", "="};
                    break;
                case Backspace:
                    symbols = new String[]{"Backspace"};
                    break;
                case NumLock:
                    symbols = new String[]{"Num", "Lock"};
                    break;
                case NumpadSlash:
                    symbols = new String[]{"/"};
                    break;
                case Multiply:
                    symbols = new String[]{"*"};
                    break;
                case NumpadMinus:
                    symbols = new String[]{"-"};
                    break;
                case Tab:
                    symbols = new String[]{"Tab"};
                    break;
                case Q:
                    symbols = new String[]{"Q"};
                    break;
                case W:
                    symbols = new String[]{"W"};
                    break;
                case E:
                    symbols = new String[]{"E"};
                    break;
                case R:
                    symbols = new String[]{"R"};
                    break;
                case T:
                    symbols = new String[]{"T"};
                    break;
                case Y:
                    symbols = new String[]{"Y"};
                    break;
                case U:
                    symbols = new String[]{"U"};
                    break;
                case I:
                    symbols = new String[]{"I"};
                    break;
                case O:
                    symbols = new String[]{"O"};
                    break;
                case P:
                    symbols = new String[]{"P"};
                    break;
                case LBracket:
                    symbols = new String[]{"{", "["};
                    break;
                case RBracket:
                    symbols = new String[]{"}", "]"};
                    break;
                case Backslash:
                    symbols = new String[]{"|", "\\"};
                    break;
                case Numpad7:
                    symbols = new String[]{"7"};
                    break;
                case Numpad8:
                    symbols = new String[]{"8"};
                    break;
                case Numpad9:
                    symbols = new String[]{"9"};
                    break;
                case Plus:
                    symbols = new String[]{"+"};
                    break;
                case CapsLock:
                    symbols = new String[]{"Caps Lock"};
                    break;
                case A:
                    symbols = new String[]{"A"};
                    break;
                case S:
                    symbols = new String[]{"S"};
                    break;
                case D:
                    symbols = new String[]{"D"};
                    break;
                case F:
                    symbols = new String[]{"F"};
                    break;
                case G:
                    symbols = new String[]{"G"};
                    break;
                case H:
                    symbols = new String[]{"H"};
                    break;
                case J:
                    symbols = new String[]{"J"};
                    break;
                case K:
                    symbols = new String[]{"K"};
                    break;
                case L:
                    symbols = new String[]{"L"};
                    break;
                case Semicolon:
                    symbols = new String[]{":", ";"};
                    break;
                case Apostrophe:
                    symbols = new String[]{"\"", "'"};
                    break;
                case Return:
                    symbols = new String[]{"Enter"};
                    break;
                case Numpad4:
                    symbols = new String[]{"4"};
                    break;
                case Numpad5:
                    symbols = new String[]{"5"};
                    break;
                case Numpad6:
                    symbols = new String[]{"6"};
                    break;
                case NumpadEnter:
                    symbols = new String[]{"Enter"};
                    break;
                case LShift:
                    symbols = new String[]{"Shift"};
                    break;
                case Z:
                    symbols = new String[]{"Z"};
                    break;
                case X:
                    symbols = new String[]{"X"};
                    break;
                case C:
                    symbols = new String[]{"C"};
                    break;
                case V:
                    symbols = new String[]{"V"};
                    break;
                case B:
                    symbols = new String[]{"B"};
                    break;
                case N:
                    symbols = new String[]{"N"};
                    break;
                case M:
                    symbols = new String[]{"M"};
                    break;
                case Comma:
                    symbols = new String[]{"<", ","};
                    break;
                case Period:
                    symbols = new String[]{">", "."};
                    break;
                case Slash:
                    symbols = new String[]{"?", "/"};
                    break;
                case RShift:
                    symbols = new String[]{"Shift"};
                    break;
                case Numpad1:
                    symbols = new String[]{"1"};
                    break;
                case Numpad2:
                    symbols = new String[]{"2"};
                    break;
                case Numpad3:
                    symbols = new String[]{"3"};
                    break;
                case MouseLeft:
                    symbols = new String[]{"Mouse", "Left"};
                    break;
                case LCtrl:
                    symbols = new String[]{"Ctrl"};
                    break;
                case LAlt:
                    symbols = new String[]{"Alt"};
                    break;
                case Space:
                    symbols = new String[]{"Space"};
                    break;
                case RAlt:
                    symbols = new String[]{"Alt"};
                    break;
                case RCtrl:
                    symbols = new String[]{"Ctrl"};
                    break;
                case Insert:
                    symbols = new String[]{"Insert"};
                    break;
                case Home:
                    symbols = new String[]{"Home"};
                    break;
                case PgUp:
                    symbols = new String[]{"PgUp"};
                    break;
                case Delete:
                    symbols = new String[]{"Delete"};
                    break;
                case End:
                    symbols = new String[]{"End"};
                    break;
                case PgDown:
                    symbols = new String[]{"PgDn"};
                    break;
                case Up:
                    symbols = new String[]{"↑"};
                    break;
                case Left:
                    symbols = new String[]{"←"};
                    break;
                case Down:
                    symbols = new String[]{"↓"};
                    break;
                case Right:
                    symbols = new String[]{"→"};
                    break;
                case Numpad0:
                    symbols = new String[]{"0"};
                    break;
                case NumpadPeriod:
                    symbols = new String[]{"."};
                    break;
                case MouseMiddle:
                    symbols = new String[]{"Mouse", "Mid"};
                    break;
                case MouseRight:
                    symbols = new String[]{"Mouse", "Right"};
                    break;
            }
            keyboardMap.put(key, new KeyItem(key, symbols));
        }
    }

    public static class KeyItem {
        private final KeyCodes.Codes key;
        private final String[] symbols;

        // 符号的顺序: 上、下、左、右 最多支持四个
        private KeyItem(KeyCodes.Codes key, String... symbols) {
            this.key = key;
            this.symbols = symbols;
        }

        public KeyItem(KeyCodes.Codes key) {
            this(key, (String) null);
        }

        public KeyCodes.Codes getKey() {
            return key;
        }

        public boolean hasSymbol() {
            return symbols != null && symbols.length != 0;
        }

        public boolean isNone() {
            return key.value() == 0;
        }

        public String getKeyName() {
            return key.name();
        }

        public String[] getSymbols() {
            return this.symbols;
        }
    }
}


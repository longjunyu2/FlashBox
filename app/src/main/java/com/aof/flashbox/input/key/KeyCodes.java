package com.aof.flashbox.input.key;

import java.util.HashMap;

public class KeyCodes {

    public enum Codes {
        Unknown(0),
        MouseLeft(1),
        MouseRight(2),
        MouseMiddle(4),
        Backspace(8),
        Tab(9),
        Return(13),
        Command(15),
        Shift(16),
        Control(17),
        Alt(18),
        Pause(19),
        CapsLock(20),
        Numpad(21),
        Escape(27),
        Space(32),
        PgUp(33),
        PgDown(34),
        End(35),
        Home(36),
        Left(37),
        Up(38),
        Right(39),
        Down(40),
        Insert(45),
        Delete(46),
        Key0(48),
        Key1(49),
        Key2(50),
        Key3(51),
        Key4(52),
        Key5(53),
        Key6(54),
        Key7(55),
        Key8(56),
        Key9(57),
        A(65),
        B(66),
        C(67),
        D(68),
        E(69),
        F(70),
        G(71),
        H(72),
        I(73),
        J(74),
        K(75),
        L(76),
        M(77),
        N(78),
        O(79),
        P(80),
        Q(81),
        R(82),
        S(83),
        T(84),
        U(85),
        V(86),
        W(87),
        X(88),
        Y(89),
        Z(90),
        Numpad0(96),
        Numpad1(97),
        Numpad2(98),
        Numpad3(99),
        Numpad4(100),
        Numpad5(101),
        Numpad6(102),
        Numpad7(103),
        Numpad8(104),
        Numpad9(105),
        Multiply(106),
        Plus(107),
        NumpadEnter(108),
        NumpadMinus(109),
        NumpadPeriod(110),
        NumpadSlash(111),
        F1(112),
        F2(113),
        F3(114),
        F4(115),
        F5(116),
        F6(117),
        F7(118),
        F8(119),
        F9(120),
        F10(121),
        F11(122),
        F12(123),
        F13(124),
        F14(125),
        F15(126),
        ScrollLock(145),
        Semicolon(186),
        Equals(187),
        Comma(188),
        Minus(189),
        Period(190),
        Slash(191),
        Grave(192),
        LBracket(219),
        Backslash(220),
        RBracket(221),
        Apostrophe(222),
        //
        LShift(Shift.value()),
        RShift(Shift.value()),
        LCtrl(Control.value()),
        RCtrl(Control.value()),
        RAlt(Alt.value()),
        LAlt(Alt.value()),
        Tilde(Grave.value),
        NumLock(Numpad.value()),
        // Unknown
        Menu(Unknown.value()),
        RMeta(Unknown.value()),
        LMeta(Unknown.value()),
        Print(Unknown.value());

        private final int value;

        Codes(int value) {
            this.value = value;
        }

        public int value() {
            return value;
        }
    }

    private static KeyCodes mKeyCodes;

    private final HashMap<Integer, Codes> keyMap;

    private KeyCodes() {
        keyMap = new HashMap<>();
        for (Codes codes : Codes.values())
            keyMap.put(codes.value(), codes);
    }

    /**
     * 获取单例
     *
     * @return 单例
     */
    public static KeyCodes getInstance() {
        if (mKeyCodes == null)
            mKeyCodes = new KeyCodes();
        return mKeyCodes;
    }

    /**
     * 从键值生成Codes实例
     *
     * @param keyCode 键值
     * @return 实例
     */
    public Codes fromKeyCode(int keyCode) {
        return keyMap.get(keyCode);
    }

}

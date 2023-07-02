package com.aof.flashbox.input.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.aof.flashbox.input.key.KeyCodes;
import com.aof.flashbox.input.key.KeyMap;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressLint("ViewConstructor")
public class KeyboardView extends FrameLayout {
    private final Context context;
    private final KeyboardParams keyboardParams;

    private final static int SCALE_TYPE_HEIGHT = -1;
    private final static int SCALE_TYPE_WIDTH = -2;
    public final static int PERFORM_CLICK = -1;
    public final static int PERFORM_SELECT = -2;
    private final static int BackgroundColor = Color.parseColor("#E0E0E0");
    private final static float MIN_KEY_CELL_SIZE_DP = 30;

    private KeySelectionChangedListener keySelectionChangedListener;
    private KeyClickListener keyClickListener;
    private int performType;

    public KeyboardView(Context context, float scale, int performType) {
        super(context);
        this.context = context;
        this.performType = performType;
        if (scale < 0.5f)
            throw new RuntimeException("Unsupported scale size: " + scale);
        keyboardParams = new KeyboardParams();
        keyboardParams.setKeyCellSize(
                (int) (getPxFromDp(MIN_KEY_CELL_SIZE_DP) * scale)
        );
        setBackgroundColor(BackgroundColor);
        init();
    }

    public KeyboardView(Context context, float scale) {
        this(context, scale, PERFORM_CLICK);
    }

    public KeyboardView(Context context, int performType, int scaleType, int size) {
        this(context, (scaleType == SCALE_TYPE_WIDTH) ? size / 23f : size / 7f, performType);
    }

    public KeyboardView(Context context, int scaleType, int size) {
        this(context, (scaleType == SCALE_TYPE_WIDTH) ? size / 23f : size / 7f);
    }

    public int getPxFromDp(float dpValue) {
        TypedValue typedValue = new TypedValue();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dpValue, getResources().getDisplayMetrics());
    }

    public void setOnKeySelectionChangedListener(KeySelectionChangedListener listener) {
        this.keySelectionChangedListener = listener;
    }

    public void setOnKeyClickListener(KeyClickListener listener) {
        this.keyClickListener = listener;
    }

    private void init() {
        if (getChildCount() != 0)
            removeAllViews();
        for (KeyCodes.Codes key : KeyCodes.Codes.values()) {
            KeyParams keyParam = keyboardParams.getKeyParams(key);
            if (keyParam.key != KeyCodes.Codes.Unknown) {
                addView(new KeyboardButton(keyParam));
            }
        }
        initReserved();
    }

    private void initReserved() {
        KeyCodes.Codes[] keyNames = {
                KeyCodes.Codes.Print,
                KeyCodes.Codes.ScrollLock,
                KeyCodes.Codes.Pause,
                KeyCodes.Codes.LMeta,
                KeyCodes.Codes.RMeta,
                KeyCodes.Codes.Menu
        };
        for (KeyCodes.Codes keyName : keyNames) {
            addView(new KeyboardButton(keyboardParams.getKeyParams(keyName)));
        }
    }

    public void setPerformType(int type) {
        this.performType = type;
        init();
    }

    public void setPreSelectedKeys(KeyCodes.Codes[] preSelectedKeys) {
        ArrayList<KeyCodes.Codes> keyNamesList = new ArrayList<>(Arrays.asList(preSelectedKeys));
        if (performType == PERFORM_SELECT) {
            for (int i = 0; i < getChildCount(); i++) {
                View v = getChildAt(i);
                if (v instanceof KeyboardButton &&
                        keyNamesList.contains(((KeyboardButton) v).key))
                    ((KeyboardButton) v).setKeySelected(true);
            }
        }
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = keyboardParams.getKeyboardViewWidth();
        params.height = keyboardParams.getKeyboardViewHeight();
        super.setLayoutParams(params);
    }


    private class KeyboardButton extends FrameLayout {
        private final int[] DEFAULT_INSET_MARGINS = {15, 5, 15, 25};
        private final static float DEFAULT_OUTER_RADIUS = 10;
        private final static float DEFAULT_INNER_RADIUS = 5;
        private final static int DEFAULT_EDGE_SIZE = 2;
        private final static int DEFAULT_EDGE_COLOR = Color.BLACK;
        private final static int DEFAULT_INNER_COLOR = Color.WHITE;
        private final static int DEFAULT_INNER_PRESSED_COLOR = 0xFFB3B3B3;
        private final static int DEFAULT_OUTER_COLOR = 0xFFB3B3B3;
        private final static int DEFAULT_OUTER_PRESSED_COLOR = 0xFF737373;
        private final static int DEFAULT_SYMBOL_COLOR = Color.BLACK;
        private final static int DEFAULT_MAIN_SYMBOL_SIZE = 6;
        private final static int DEFAULT_VICE_SYMBOL_SIZE = DEFAULT_MAIN_SYMBOL_SIZE - 1;
        private final static int DEFAULT_SYMBOL_OFFSET = 5;

        private final KeyCodes.Codes key;
        private final int height;
        private boolean selected = false;
        final int[] insetMargins = DEFAULT_INSET_MARGINS;
        final float outerRadius = DEFAULT_OUTER_RADIUS;
        final float innerRadius = DEFAULT_INNER_RADIUS;
        int edgeSize = DEFAULT_EDGE_SIZE;
        int edgeColor = DEFAULT_EDGE_COLOR;
        final int innerColor = DEFAULT_INNER_COLOR;
        final int innerPressedColor = DEFAULT_INNER_PRESSED_COLOR;
        final int outerColor = DEFAULT_OUTER_COLOR;
        final int outerPressedColor = DEFAULT_OUTER_PRESSED_COLOR;
        int symbolColor = DEFAULT_SYMBOL_COLOR;
        final int mainSymbolSizeSp = DEFAULT_MAIN_SYMBOL_SIZE;
        final int viceSymbolSizeSp = DEFAULT_VICE_SYMBOL_SIZE;
        final int symbolOffset = DEFAULT_SYMBOL_OFFSET;

        public KeyboardButton(KeyParams keyParams) {
            super(context);
            this.key = keyParams.key;
            this.height = keyParams.height;
            this.setLayoutParams(new FrameLayout.LayoutParams(keyParams.width, keyParams.height));
            this.setX(keyParams.x);
            this.setY(keyParams.y);
            this.setClickable(true);
            this.setOnClickListener(v -> {
                if (performType == PERFORM_CLICK) {
                    KeyClickListener listener = keyClickListener;
                    if (listener != null)
                        listener.onKeyClick(key);
                } else if (performType == PERFORM_SELECT) {
                    KeySelectionChangedListener listener = keySelectionChangedListener;
                    if (listener != null) {
                        setKeySelected(!selected);
                        keySelectionChangedListener.onKeySelectionChanged(key, selected);
                    }
                } else
                    throw new RuntimeException("Unknown Perform Type: " + performType);
            });
            this.setBackground(createKeyboardButtonDrawable());
            addSymbol();
        }

        public void setKeySelected(boolean selected) {
            if (performType == PERFORM_SELECT) {
                this.selected = selected;
                if (selected) {
                    edgeColor = Color.RED;
                    symbolColor = Color.RED;
                    edgeSize = 5;
                } else {
                    edgeColor = DEFAULT_EDGE_COLOR;
                    symbolColor = DEFAULT_SYMBOL_COLOR;
                    edgeSize = DEFAULT_EDGE_SIZE;
                }
                setBackground(createKeyboardButtonDrawable());
                addSymbol();
            }
        }

        private void addSymbol() {
            if (getChildCount() != 0)
                removeAllViews();
            KeyMap.KeyItem keyItem =
                    KeyMap.getMap().get(key);
            if (keyItem != null && keyItem.hasSymbol()) {
                String[] symbols = keyItem.getSymbols();
                if (symbols.length == 1) {
                    TextView symbolText = createKeyboardSymbolView(symbols[0], mainSymbolSizeSp);
                    symbolText.setX(insetMargins[0] + symbolOffset);
                    symbolText.setY(insetMargins[1]);
                    addView(symbolText);
                } else if (symbols.length == 2) {
                    TextView viceSymbolText = createKeyboardSymbolView(symbols[0], viceSymbolSizeSp);
                    viceSymbolText.setX(insetMargins[0] + symbolOffset);
                    viceSymbolText.setY(insetMargins[1]);
                    addView(viceSymbolText);

                    TextView mainSymbolText = createKeyboardSymbolView(symbols[1], viceSymbolSizeSp);
                    mainSymbolText.setX(insetMargins[0] + symbolOffset);
                    mainSymbolText.setY(height - insetMargins[3]
                            - this.getUndisplayedViewSize(mainSymbolText)[1]);
                    addView(mainSymbolText);
                }
            } else {
                setClickable(false);
            }
        }

        public int[] getUndisplayedViewSize(View v) {
            int[] size = new int[2];
            int width = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            int height = View.MeasureSpec.makeMeasureSpec(0,
                    View.MeasureSpec.UNSPECIFIED);
            v.measure(width, height);
            size[0] = v.getMeasuredWidth();
            size[1] = v.getMeasuredHeight();
            return size;
        }

        private Drawable createKeyboardButtonDrawable() {
            Paint paint;

            float[] outerR = new float[8];
            Arrays.fill(outerR, outerRadius);
            float[] innerR = new float[8];
            Arrays.fill(innerR, innerRadius);
            RectF insetRect = new RectF(insetMargins[0], insetMargins[1], insetMargins[2], insetMargins[3]);

            // released
            ShapeDrawable shapeDrawableEdge = new ShapeDrawable(new RoundRectShape(outerR, null, innerR));
            paint = shapeDrawableEdge.getPaint();
            paint.setColor(edgeColor);
            paint.setStyle(Paint.Style.FILL);

            ShapeDrawable shapeDrawableInner = new ShapeDrawable(new RoundRectShape(outerR, null, innerR));
            paint = shapeDrawableInner.getPaint();
            paint.setColor(innerColor);
            paint.setStyle(Paint.Style.FILL);

            ShapeDrawable shapeDrawableOuter = new ShapeDrawable(new RoundRectShape(outerR, insetRect, innerR));
            paint = shapeDrawableOuter.getPaint();
            paint.setColor(outerColor);
            paint.setStyle(Paint.Style.FILL);

            LayerDrawable layerDrawable = new LayerDrawable(
                    new Drawable[]{shapeDrawableEdge, shapeDrawableInner, shapeDrawableOuter});
            layerDrawable.setLayerInset(1, edgeSize, edgeSize, edgeSize, edgeSize);
            layerDrawable.setLayerInset(2, edgeSize, edgeSize, edgeSize, edgeSize);

            // pressed
            ShapeDrawable shapeDrawableInnerPressed = new ShapeDrawable(new RoundRectShape(outerR, null, innerR));
            paint = shapeDrawableInnerPressed.getPaint();
            paint.setColor(innerPressedColor);
            paint.setStyle(Paint.Style.FILL);

            ShapeDrawable shapeDrawableOuterPressed = new ShapeDrawable(new RoundRectShape(outerR, insetRect, innerR));
            paint = shapeDrawableOuterPressed.getPaint();
            paint.setColor(outerPressedColor);
            paint.setStyle(Paint.Style.FILL);

            LayerDrawable layerDrawablePressed = new LayerDrawable(
                    new Drawable[]{shapeDrawableEdge, shapeDrawableInnerPressed, shapeDrawableOuterPressed});
            layerDrawable.setLayerInset(1, edgeSize, edgeSize, edgeSize, edgeSize);
            layerDrawable.setLayerInset(2, edgeSize, edgeSize, edgeSize, edgeSize);

            // selector
            StateListDrawable stateListDrawable = new StateListDrawable();
            stateListDrawable.addState(new int[]{/* released */-android.R.attr.state_pressed}, layerDrawable);
            stateListDrawable.addState(new int[]{/* pressed  */ android.R.attr.state_pressed}, layerDrawablePressed);

            return stateListDrawable;
        }

        private TextView createKeyboardSymbolView(String symbol, int textSizeSp) {
            TextView textView = new TextView(context);
            textView.setText(symbol);
            textView.setTextSize(textSizeSp);
            textView.setTextColor(symbolColor);
            textView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            return textView;
        }
    }

    public interface KeySelectionChangedListener {
        void onKeySelectionChanged(KeyCodes.Codes key, boolean selected);
    }

    public interface KeyClickListener {
        void onKeyClick(KeyCodes.Codes key);
    }

    private static class KeyboardParams {
        public int symbolTextSizeSp;
        public int keyCellSizePx;
        public int boundMarginSizePx;
        public int lineMarginSizePx;
        public int keyFunctionMarginSizePx;
        public int backspaceKeyWidthPx;
        public int tabKeyWidthPx;
        public int capslockKeyWidthPx;
        public int enterKeyWidthPx;
        public int shiftLKeyWidthPx;
        public int shiftRKeyWidthPx;
        public int ctrlKeyWidthPx;
        public int spaceKeyWidthPx;

        public void setKeyCellSize(int pixel) {
            this.keyCellSizePx = pixel;
            init();
        }

        private void init() {
            symbolTextSizeSp = 13;
            boundMarginSizePx = keyCellSizePx / 4;
            lineMarginSizePx = keyCellSizePx / 2;
            keyFunctionMarginSizePx = keyCellSizePx / 2;
            backspaceKeyWidthPx = keyCellSizePx * 2;
            tabKeyWidthPx = (int) (keyCellSizePx * 1.5f);
            capslockKeyWidthPx = (int) (keyCellSizePx * 1.75f);
            enterKeyWidthPx = (int) (keyCellSizePx * 2.25f);
            shiftLKeyWidthPx = (int) (keyCellSizePx * 2.25f);
            shiftRKeyWidthPx = (int) (keyCellSizePx * 2.75f);
            ctrlKeyWidthPx = (int) (keyCellSizePx * 1.25f);
            spaceKeyWidthPx = (int) (keyCellSizePx * 6.25f);
        }

        public int getKeyboardViewWidth() {
            return keyCellSizePx * 23;
        }

        public int getKeyboardViewHeight() {
            return keyCellSizePx * 7;
        }

        public KeyParams getKeyParams(KeyCodes.Codes key) {
            int x = 0;
            int y = 0;
            int width = 0;
            int height = keyCellSizePx;

            switch (key) {
                // line 1
                case Pause:
                    x += keyCellSizePx;
                case ScrollLock:
                    x += keyCellSizePx;
                case Print:
                    x += keyCellSizePx + boundMarginSizePx;
                case F12:
                    x += keyCellSizePx;
                case F11:
                    x += keyCellSizePx;
                case F10:
                    x += keyCellSizePx;
                case F9:
                    x += keyCellSizePx + keyFunctionMarginSizePx;
                case F8:
                    x += keyCellSizePx;
                case F7:
                    x += keyCellSizePx;
                case F6:
                    x += keyCellSizePx;
                case F5:
                    x += keyCellSizePx + keyFunctionMarginSizePx;
                case F4:
                    x += keyCellSizePx;
                case F3:
                    x += keyCellSizePx;
                case F2:
                    x += keyCellSizePx;
                case F1:
                    x += 2 * keyCellSizePx;
                case Escape:
                    x += boundMarginSizePx;
                    y += boundMarginSizePx;
                    width = keyCellSizePx;
                    break;
                // line2
                case NumpadMinus:
                    x += keyCellSizePx;
                case Multiply:
                    x += keyCellSizePx;
                case NumpadSlash:
                    x += keyCellSizePx;
                case NumLock:
                    x += keyCellSizePx + boundMarginSizePx;
                case PgUp:
                    x += keyCellSizePx;
                case Home:
                    x += keyCellSizePx;
                case Insert:
                    x += backspaceKeyWidthPx + boundMarginSizePx;
                    width = keyCellSizePx;
                case Backspace:
                    x += keyCellSizePx;
                    if (width == 0)
                        width = backspaceKeyWidthPx;
                case Equals:
                    x += keyCellSizePx;
                case Minus:
                    x += keyCellSizePx;
                case Key0:
                    x += keyCellSizePx;
                case Key9:
                    x += keyCellSizePx;
                case Key8:
                    x += keyCellSizePx;
                case Key7:
                    x += keyCellSizePx;
                case Key6:
                    x += keyCellSizePx;
                case Key5:
                    x += keyCellSizePx;
                case Key4:
                    x += keyCellSizePx;
                case Key3:
                    x += keyCellSizePx;
                case Key2:
                    x += keyCellSizePx;
                case Key1:
                    x += keyCellSizePx;
                case Tilde:
                    x += boundMarginSizePx;
                    y += getKeyParams(KeyCodes.Codes.Escape).y + keyCellSizePx + lineMarginSizePx;
                    if (width == 0)
                        width = keyCellSizePx;
                    break;
                // line 3
                case Plus:
                    x += keyCellSizePx;
                case Numpad9:
                    x += keyCellSizePx;
                case Numpad8:
                    x += keyCellSizePx;
                case Numpad7:
                    x += keyCellSizePx + boundMarginSizePx;
                case PgDown:
                    x += keyCellSizePx;
                case End:
                    x += keyCellSizePx;
                case Delete:
                    x += tabKeyWidthPx + boundMarginSizePx;
                    width = keyCellSizePx;
                case Backslash:
                    x += keyCellSizePx;
                    if (width == 0)
                        width = tabKeyWidthPx;
                case RBracket:
                    x += keyCellSizePx;
                case LBracket:
                    x += keyCellSizePx;
                case P:
                    x += keyCellSizePx;
                case O:
                    x += keyCellSizePx;
                case I:
                    x += keyCellSizePx;
                case U:
                    x += keyCellSizePx;
                case Y:
                    x += keyCellSizePx;
                case T:
                    x += keyCellSizePx;
                case R:
                    x += keyCellSizePx;
                case E:
                    x += keyCellSizePx;
                case W:
                    x += keyCellSizePx;
                case Q:
                    x += tabKeyWidthPx;
                    if (width == 0)
                        width = keyCellSizePx;
                case Tab:
                    x += boundMarginSizePx;
                    y += getKeyParams(KeyCodes.Codes.Tilde).y + keyCellSizePx;
                    if (width == 0)
                        width = tabKeyWidthPx;
                    break;
                // line 4
                case NumpadEnter:
                    x += keyCellSizePx;
                case Numpad6:
                    x += keyCellSizePx;
                case Numpad5:
                    x += keyCellSizePx;
                case Numpad4:
                    x += 5 * keyCellSizePx + 3 * boundMarginSizePx;
                    width = keyCellSizePx;
                case Return:
                    x += keyCellSizePx;
                    if (width == 0)
                        width = enterKeyWidthPx;
                case Apostrophe:
                    x += keyCellSizePx;
                case Semicolon:
                    x += keyCellSizePx;
                case L:
                    x += keyCellSizePx;
                case K:
                    x += keyCellSizePx;
                case J:
                    x += keyCellSizePx;
                case H:
                    x += keyCellSizePx;
                case G:
                    x += keyCellSizePx;
                case F:
                    x += keyCellSizePx;
                case D:
                    x += keyCellSizePx;
                case S:
                    x += keyCellSizePx;
                case A:
                    x += capslockKeyWidthPx;
                    if (width == 0)
                        width = keyCellSizePx;
                case CapsLock:
                    x += boundMarginSizePx;
                    y += getKeyParams(KeyCodes.Codes.Tab).y + keyCellSizePx;
                    if (width == 0)
                        width = capslockKeyWidthPx;
                    break;
                //line 5
                case MouseLeft:
                    x += keyCellSizePx;
                case Numpad3:
                    x += keyCellSizePx;
                case Numpad2:
                    x += keyCellSizePx;
                case Numpad1:
                    x += 2 * keyCellSizePx + boundMarginSizePx;
                case Up:
                    x += shiftRKeyWidthPx + boundMarginSizePx + keyCellSizePx;
                    width = keyCellSizePx;
                case RShift:
                    x += keyCellSizePx;
                    if (width == 0)
                        width = shiftRKeyWidthPx;
                case Slash:
                    x += keyCellSizePx;
                case Period:
                    x += keyCellSizePx;
                case Comma:
                    x += keyCellSizePx;
                case M:
                    x += keyCellSizePx;
                case N:
                    x += keyCellSizePx;
                case B:
                    x += keyCellSizePx;
                case V:
                    x += keyCellSizePx;
                case C:
                    x += keyCellSizePx;
                case X:
                    x += keyCellSizePx;
                case Z:
                    x += shiftLKeyWidthPx;
                    if (width == 0)
                        width = keyCellSizePx;
                case LShift:
                    x += boundMarginSizePx;
                    y += getKeyParams(KeyCodes.Codes.CapsLock).y + keyCellSizePx;
                    if (width == 0)
                        width = shiftLKeyWidthPx;
                    break;
                // line 6
                case MouseRight:
                    x += keyCellSizePx;
                case MouseMiddle:
                    x += keyCellSizePx;
                case NumpadPeriod:
                    x += keyCellSizePx;
                case Numpad0:
                    x += keyCellSizePx + boundMarginSizePx;
                case Right:
                    x += keyCellSizePx;
                case Down:
                    x += keyCellSizePx;
                case Left:
                    x += ctrlKeyWidthPx + boundMarginSizePx;
                    width = keyCellSizePx;
                case RCtrl:
                    x += ctrlKeyWidthPx;
                case Menu:
                    x += ctrlKeyWidthPx;
                case RMeta:
                    x += ctrlKeyWidthPx;
                case RAlt:
                    x += spaceKeyWidthPx;
                    if (width == 0)
                        width = ctrlKeyWidthPx;
                case Space:
                    x += ctrlKeyWidthPx;
                    if (width == 0)
                        width = spaceKeyWidthPx;
                case LAlt:
                    x += ctrlKeyWidthPx;
                case LMeta:
                    x += ctrlKeyWidthPx;
                case LCtrl:
                    x += boundMarginSizePx;
                    y += getKeyParams(KeyCodes.Codes.RShift).y + keyCellSizePx;
                    if (width == 0)
                        width = ctrlKeyWidthPx;
                    break;
                default:
                    return new KeyParams(width, height, x, y, KeyCodes.Codes.Unknown);
            }
            return new KeyParams(width, height, x, y, key);
        }
    }

    private static class KeyParams {
        public final int height;
        public final int width;
        public final int x;
        public final int y;
        public final KeyCodes.Codes key;

        public KeyParams(int width, int height, int x, int y, KeyCodes.Codes key) {
            this.height = height;
            this.width = width;
            this.x = x;
            this.y = y;
            this.key = key;
        }
    }
}

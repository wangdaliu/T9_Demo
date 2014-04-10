package com.snail.t9;

import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

public class DialpadLayout extends LinearLayout implements DialpadImageButton.OnPressedListener {

    private DialpadOnKeyDown onKeyDown;

    /**
     * Remembers the number of dialpad buttons which are pressed at this moment.
     * If it becomes 0, meaning no buttons are pressed, we'll call
     * {@link ToneGenerator#stopTone()}; the method shouldn't be called unless the last key is
     * released.
     */
    private int mDialpadPressCount;

    interface DialpadOnKeyDown {
        void dialpadOnKeyDown(int keyCode);
    }

    public DialpadLayout(Context context) {
        super(context);
        init(context);
    }

    public DialpadLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DialpadLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setOnKeyDown(DialpadOnKeyDown onKeyDown) {
        this.onKeyDown = onKeyDown;
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.calllog_dialpad_view, this);

        int[] buttonIds = new int[]{R.id.one, R.id.two, R.id.three, R.id.four, R.id.five,
                R.id.six, R.id.seven, R.id.eight, R.id.nine, R.id.zero, R.id.star, R.id.pound};
        for (int id : buttonIds) {
            ((DialpadImageButton) findViewById(id)).setOnPressedListener(this);
        }
    }

    @Override
    public void onPressed(View view, boolean pressed) {
        if (pressed) {
            switch (view.getId()) {
                case R.id.one:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_1);
                    break;
                case R.id.two:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_2);
                    break;
                case R.id.three:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_3);
                    break;
                case R.id.four:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_4);
                    break;
                case R.id.five:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_5);
                    break;
                case R.id.six:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_6);
                    break;
                case R.id.seven:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_7);
                    break;
                case R.id.eight:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_8);
                    break;
                case R.id.nine:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_9);
                    break;
                case R.id.zero:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_0);
                    break;
                case R.id.pound:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_POUND);
                    break;
                case R.id.star:
                    onKeyDown.dialpadOnKeyDown(KeyEvent.KEYCODE_STAR);
                    break;
                default:
                    break;

            }

            mDialpadPressCount++;
        } else {
            view.jumpDrawablesToCurrentState();
            mDialpadPressCount--;
            if (mDialpadPressCount < 0) {
                mDialpadPressCount = 0;
            }
        }
    }
}

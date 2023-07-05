package com.aof.flashbox.input.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

public class ToastTextView extends AppCompatTextView {

    public ToastTextView(@NonNull Context context) {
        this(context, null);
    }

    public ToastTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public ToastTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence charSequence = getText();
                if (charSequence != null && !charSequence.toString().equals(""))
                    Toast.makeText(context, charSequence.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}

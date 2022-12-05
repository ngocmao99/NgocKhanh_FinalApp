package com.example.finalproject.view.activity;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.example.finalproject.R;
import com.google.android.material.checkbox.MaterialCheckBox;

public class customCheckbox extends MaterialCheckBox {
    public customCheckbox(Context context) {
        super(context);
    }

    public customCheckbox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

    }

    @Override
    public void setChecked(boolean checked) {
        if (checked){
            this.setBackgroundResource(R.drawable.check_box_selector);
            this.setTextColor(getResources().getColor(R.color.white));
        }
        else {
            this.setBackgroundResource(R.drawable.check_box_deselect);
            this.setTextColor(getResources().getColor(R.color.dark_gray));
        }
        super.setChecked(checked);
    }

    public customCheckbox(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

}

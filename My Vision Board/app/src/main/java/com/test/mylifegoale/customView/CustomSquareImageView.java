package com.test.mylifegoale.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CustomSquareImageView extends ImageView {
    public CustomSquareImageView(Context context) {
        super(context);
    }

    public CustomSquareImageView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        setMeasuredDimension(i, i);
    }
}

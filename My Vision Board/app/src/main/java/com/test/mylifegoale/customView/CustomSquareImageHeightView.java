package com.test.mylifegoale.customView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

@SuppressLint("AppCompatCustomView")
public class CustomSquareImageHeightView extends ImageView {
    public CustomSquareImageHeightView(Context context) {
        super(context);
    }

    public CustomSquareImageHeightView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }


    public void onMeasure(int i, int i2) {
        super.onMeasure(i2, i2);
        setMeasuredDimension(i2, i2);
    }
}

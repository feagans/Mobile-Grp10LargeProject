package com.test.mylifegoale.utilities;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public ItemOffsetDecoration(int i) {
        this.mItemOffset = i;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int i) {
        this(context.getResources().getDimensionPixelSize(i));
    }

    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);
        int i = this.mItemOffset;
        rect.set(i, i, i, i);
    }
}

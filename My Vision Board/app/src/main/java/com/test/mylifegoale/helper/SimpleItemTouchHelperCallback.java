package com.test.mylifegoale.helper;

import android.graphics.Canvas;
import android.util.Log;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.adapters.BodyItemAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {
    int dragFrom = -1;
    private final ItemTouchHelperAdapter mAdapter;

    public boolean isItemViewSwipeEnabled() {
        return true;
    }

    public boolean isLongPressDragEnabled() {
        return true;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
    }

    public SimpleItemTouchHelperCallback(ItemTouchHelperAdapter itemTouchHelperAdapter) {
        this.mAdapter = itemTouchHelperAdapter;
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(3, 0);
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        if (viewHolder.getItemViewType() != viewHolder2.getItemViewType()) {
            return false;
        }
        int adapterPosition = viewHolder.getAdapterPosition();
        int adapterPosition2 = viewHolder2.getAdapterPosition();
        if (this.dragFrom == -1) {
            this.dragFrom = adapterPosition;
        }
        this.mAdapter.onItemMove(adapterPosition, adapterPosition2);
        return true;
    }

    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        if (i == 1) {
            viewHolder.itemView.setAlpha(1.0f - (Math.abs(f) / ((float) viewHolder.itemView.getWidth())));
            viewHolder.itemView.setTranslationX(f);
            return;
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
    }

    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int i) {
        if (i != 0 && (viewHolder instanceof ItemTouchHelperViewHolder)) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemSelected();
        }
        super.onSelectedChanged(viewHolder, i);
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        super.clearView(recyclerView, viewHolder);
        viewHolder.itemView.setAlpha(1.0f);
        if (this.mAdapter instanceof BodyItemAdapter) {
            Log.i("clearView", "clearView: ");
            ((BodyItemAdapter) this.mAdapter).updateBodyText();
        }
        if (viewHolder instanceof ItemTouchHelperViewHolder) {
            ((ItemTouchHelperViewHolder) viewHolder).onItemClear();
        }
    }
}

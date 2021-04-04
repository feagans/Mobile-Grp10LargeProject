package com.test.mylifegoale.utilities;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class SwipeAndDragHelper extends ItemTouchHelper.Callback {
    private ActionCompletionContract contract;
    int dragFrom = -1;
    int dragTo = -1;

    public interface ActionCompletionContract {
        void onViewMoved(int i, int i2);

        void onViewSwiped(int i);

        void reallyMoved();
    }

    public boolean isLongPressDragEnabled() {
        return false;
    }

    public SwipeAndDragHelper(ActionCompletionContract actionCompletionContract) {
        this.contract = actionCompletionContract;
    }

    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(3, 0);
    }

    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2) {
        int adapterPosition = viewHolder.getAdapterPosition();
        int adapterPosition2 = viewHolder2.getAdapterPosition();
        if (this.dragFrom == -1) {
            this.dragFrom = adapterPosition;
        }
        this.dragTo = adapterPosition2;
        this.contract.onViewMoved(viewHolder.getAdapterPosition(), viewHolder2.getAdapterPosition());
        return true;
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        this.contract.onViewSwiped(viewHolder.getAdapterPosition());
    }

    public void onChildDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float f, float f2, int i, boolean z) {
        if (i == 1) {
            viewHolder.itemView.setAlpha(1.0f - (Math.abs(f) / ((float) recyclerView.getWidth())));
        }
        super.onChildDraw(canvas, recyclerView, viewHolder, f, f2, i, z);
    }

    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int i;
        super.clearView(recyclerView, viewHolder);
        int i2 = this.dragFrom;
        if (!(i2 == -1 || (i = this.dragTo) == -1 || i2 == i)) {
            this.contract.reallyMoved();
        }
        this.dragTo = -1;
        this.dragFrom = -1;
    }
}

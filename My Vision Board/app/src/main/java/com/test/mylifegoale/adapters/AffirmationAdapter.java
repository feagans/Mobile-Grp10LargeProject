package com.test.mylifegoale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.RowAffirmationBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;

public class AffirmationAdapter extends RecyclerView.Adapter implements SwipeAndDragHelper.ActionCompletionContract {
    private ArrayList<AffirmationRowModel> arrayList;
    private Context context;

    public boolean isFolderList;

    public ItemTouchHelper itemTouchHelper;
    private FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
    private int marginCommon;
    private int marginMax;

    public RecyclerItemClick recyclerItemClick;

    public AffirmationAdapter(boolean z, Context context2, ArrayList<AffirmationRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.isFolderList = z;
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.marginCommon = AppConstants.getDPToPixel(context2, 8);
        this.marginMax = AppConstants.getDPToPixel(context2, 20);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_affirmation, viewGroup, false));
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.imgCheck.setVisibility(this.isFolderList ? View.VISIBLE : View.GONE);
            rowHolder.binding.imgDrag.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getActionMasked() != 0) {
                        return false;
                    }
                    itemTouchHelper.startDrag(viewHolder);
                    return false;
                }
            });
            if (i == 0) {
                FrameLayout.LayoutParams layoutParams2 = this.layoutParams;
                int i2 = this.marginCommon;
                layoutParams2.setMargins(i2, i2 * 2, i2, i2);
            } else if (i == this.arrayList.size() - 1) {
                FrameLayout.LayoutParams layoutParams3 = this.layoutParams;
                int i3 = this.marginCommon;
                layoutParams3.setMargins(i3, 0, i3, this.marginMax * 4);
            } else {
                FrameLayout.LayoutParams layoutParams4 = this.layoutParams;
                int i4 = this.marginCommon;
                layoutParams4.setMargins(i4, 0, i4, i4);
            }
            rowHolder.binding.mainCard.setLayoutParams(this.layoutParams);
            rowHolder.binding.setAffirmationRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowAffirmationBinding binding;

        public RowHolder(View view) {
            super(view);
            this.binding = (RowAffirmationBinding) DataBindingUtil.bind(view);
            if (isFolderList) {
                this.binding.frameCheck.setOnClickListener(this);
            }
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() != R.id.frameCheck) {
                recyclerItemClick.onClick(getAdapterPosition(), 1);
            } else {
                recyclerItemClick.onClick(getAdapterPosition(), 2);
            }
        }
    }

    public void setTouchHelper(ItemTouchHelper itemTouchHelper2) {
        this.itemTouchHelper = itemTouchHelper2;
    }

    public void onViewMoved(int i, int i2) {
        try {
            if (this.isFolderList) {
                this.arrayList.get(i).setSequenceFolder(i2);
                AppDatabase.getAppDatabase(this.context).affirmationDao().updateSequenceFolder(this.arrayList.get(i).getId(), this.arrayList.get(i).getSequenceFolder());
                this.arrayList.get(i2).setSequenceFolder(i);
                AppDatabase.getAppDatabase(this.context).affirmationDao().updateSequenceFolder(this.arrayList.get(i2).getId(), this.arrayList.get(i2).getSequenceFolder());
            } else {
                this.arrayList.get(i).setSequence(i2);
                AppDatabase.getAppDatabase(this.context).affirmationDao().updateSequence(this.arrayList.get(i).getId(), this.arrayList.get(i).getSequence());
                this.arrayList.get(i2).setSequence(i);
                AppDatabase.getAppDatabase(this.context).affirmationDao().updateSequence(this.arrayList.get(i2).getId(), this.arrayList.get(i2).getSequence());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        AffirmationRowModel affirmationRowModel = new AffirmationRowModel(this.arrayList.get(i));
        this.arrayList.remove(i);
        this.arrayList.add(i2, affirmationRowModel);
        notifyItemMoved(i, i2);
    }

    public void reallyMoved() {
        notifyDataSetChanged();
    }

    public void onViewSwiped(int i) {
        this.arrayList.remove(i);
        notifyItemRemoved(i);
    }
}

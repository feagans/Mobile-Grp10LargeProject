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
import com.test.mylifegoale.databinding.RowFolderBinding;
import com.test.mylifegoale.databinding.RowFolderHeaderBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.FolderRowModel;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.SwipeAndDragHelper;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter implements SwipeAndDragHelper.ActionCompletionContract {

    public int adapterType;

    public ArrayList<FolderRowModel> arrayList;
    private Context context;

    public ItemTouchHelper itemTouchHelper;
    private FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(-1, -2);
    private int marginCommon;

    public RecyclerItemClick recyclerItemClick;

    public FolderAdapter(int i, Context context2, ArrayList<FolderRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.adapterType = i;
        this.context = context2;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.marginCommon = AppConstants.getDPToPixel(context2, 8);
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.adapterType == 1) {
            return new RowHeaderHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_folder_header, viewGroup, false));
        }
        return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_folder, viewGroup, false));
    }

    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHeaderHolder) {
            RowHeaderHolder rowHeaderHolder = (RowHeaderHolder) viewHolder;
            rowHeaderHolder.binding.setFolderRowModel(this.arrayList.get(i));
            rowHeaderHolder.binding.executePendingBindings();
        } else if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            int i2 = 8;
            rowHolder.binding.imgCheck.setVisibility(this.adapterType == 4 ? View.VISIBLE : View.GONE);
            rowHolder.binding.textCount.setVisibility(this.adapterType == 2 ? View.VISIBLE : View.GONE);
            FrameLayout frameLayout = rowHolder.binding.frameManage;
            if (this.adapterType == 5) {
                i2 = 0;
            }
            frameLayout.setVisibility(i2);
            if (this.adapterType == 5) {
                rowHolder.binding.imgDrag.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View view, MotionEvent motionEvent) {
                        if (motionEvent.getActionMasked() != 0) {
                            return false;
                        }
                        itemTouchHelper.startDrag(viewHolder);
                        return false;
                    }
                });
            }
            if (i == 0) {
                FrameLayout.LayoutParams layoutParams2 = this.layoutParams;
                int i3 = this.marginCommon;
                layoutParams2.setMargins(i3, i3 * 2, i3, i3);
            } else if (i == this.arrayList.size() - 1) {
                FrameLayout.LayoutParams layoutParams3 = this.layoutParams;
                int i4 = this.marginCommon;
                layoutParams3.setMargins(i4, 0, i4, i4 * 2);
            } else {
                FrameLayout.LayoutParams layoutParams4 = this.layoutParams;
                int i5 = this.marginCommon;
                layoutParams4.setMargins(i5, 0, i5, i5);
            }
            rowHolder.binding.mainCard.setLayoutParams(this.layoutParams);
            rowHolder.binding.setFolderRowModelz(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowFolderBinding binding;

        public RowHolder(View view) {
            super(view);
            this.binding = (RowFolderBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
            this.binding.imgDelete.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (view.getId() == R.id.imgDelete) {
                recyclerItemClick.onClick(getAdapterPosition(), 2);
                return;
            }
            if (adapterType == 4) {
                selectionAll(false);
                ((FolderRowModel) arrayList.get(getAdapterPosition())).setSelected(!((FolderRowModel) arrayList.get(getAdapterPosition())).isSelected());
            }
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }

    private class RowHeaderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowFolderHeaderBinding binding;

        public RowHeaderHolder(View view) {
            super(view);
            this.binding = (RowFolderHeaderBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }

    public void setTouchHelper(ItemTouchHelper itemTouchHelper2) {
        this.itemTouchHelper = itemTouchHelper2;
    }

    public void onViewMoved(int i, int i2) {
        try {
            this.arrayList.get(i).setSequence(i2);
            AppDatabase.getAppDatabase(this.context).folderDao().updateSequence(this.arrayList.get(i).getId(), this.arrayList.get(i).getSequence());
            this.arrayList.get(i2).setSequence(i);
            AppDatabase.getAppDatabase(this.context).folderDao().updateSequence(this.arrayList.get(i2).getId(), this.arrayList.get(i2).getSequence());
        } catch (Exception e) {
            e.printStackTrace();
        }
        FolderRowModel folderRowModel = new FolderRowModel(this.arrayList.get(i));
        this.arrayList.remove(i);
        this.arrayList.add(i2, folderRowModel);
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

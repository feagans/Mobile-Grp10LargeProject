package com.test.mylifegoale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.databinding.RowImageIconBinding;
import com.test.mylifegoale.databinding.RowImageLargeBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.image.ImageRowModel;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter {

    public ArrayList<ImageRowModel> arrayList;
    private boolean isIcon;

    public RecyclerItemClick recyclerItemClick;

    public ImageAdapter(boolean z, ArrayList<ImageRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
        this.isIcon = z;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (this.isIcon) {
            return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_image_icon, viewGroup, false));
        }
        return new RowHolderLarge(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_image_large, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.setImageRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        } else if (viewHolder instanceof RowHolderLarge) {
            RowHolderLarge rowHolderLarge = (RowHolderLarge) viewHolder;
            rowHolderLarge.binding.setImageRowModel(this.arrayList.get(i));
            rowHolderLarge.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowImageIconBinding binding;

        public RowHolder(View view) {
            super(view);
            this.binding = (RowImageIconBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            selectionAll(false);
            ((ImageRowModel) arrayList.get(getAdapterPosition())).setSelected(!((ImageRowModel) arrayList.get(getAdapterPosition())).isSelected());
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }

    private class RowHolderLarge extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowImageLargeBinding binding;

        public RowHolderLarge(View view) {
            super(view);
            this.binding = (RowImageLargeBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            selectionAll(false);
            ((ImageRowModel) arrayList.get(getAdapterPosition())).setSelected(!((ImageRowModel) arrayList.get(getAdapterPosition())).isSelected());
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }
}

package com.test.mylifegoale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.databinding.RowSelectionItemBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.selection.SelectionRowModel;

import java.util.ArrayList;

public class SelectionAdapter extends RecyclerView.Adapter {

    public ArrayList<SelectionRowModel> arrayList;

    public boolean isSelection;

    public RecyclerItemClick recyclerItemClick;

    public SelectionAdapter(boolean z, ArrayList<SelectionRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.isSelection = z;
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_selection_item, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.setStatisticRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowSelectionItemBinding binding;

        public RowHolder(View view) {
            super(view);
            this.binding = (RowSelectionItemBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
        }

        public void onClick(View view) {
            if (isSelection) {
                selectionAll(false);
                ((SelectionRowModel) arrayList.get(getAdapterPosition())).setSelected(!((SelectionRowModel) arrayList.get(getAdapterPosition())).isSelected());
            }
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }


    public void selectionAll(boolean z) {
        for (int i = 0; i < this.arrayList.size(); i++) {
            this.arrayList.get(i).setSelected(z);
        }
    }
}

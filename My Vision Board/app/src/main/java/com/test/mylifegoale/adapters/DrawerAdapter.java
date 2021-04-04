package com.test.mylifegoale.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.databinding.RowDrawerItemBinding;
import com.test.mylifegoale.itemClick.RecyclerItemClick;
import com.test.mylifegoale.model.drawer.DrawerRowModel;

import java.util.ArrayList;

public class DrawerAdapter extends RecyclerView.Adapter {
    private ArrayList<DrawerRowModel> arrayList;

    public RecyclerItemClick recyclerItemClick;

    public DrawerAdapter(ArrayList<DrawerRowModel> arrayList2, RecyclerItemClick recyclerItemClick2) {
        this.arrayList = arrayList2;
        this.recyclerItemClick = recyclerItemClick2;
    }

    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RowHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_drawer_item, viewGroup, false));
    }

    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof RowHolder) {
            RowHolder rowHolder = (RowHolder) viewHolder;
            rowHolder.binding.setDrawerRowModel(this.arrayList.get(i));
            rowHolder.binding.executePendingBindings();
        }
    }

    public int getItemCount() {
        return this.arrayList.size();
    }

    private class RowHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowDrawerItemBinding binding;

        public RowHolder(View view) {
            super(view);
            this.binding = (RowDrawerItemBinding) DataBindingUtil.bind(view);
            view.setOnClickListener(this);
            this.binding.linMain.setOnClickListener(this);
        }

        public void onClick(View view) {
            view.getId();
            recyclerItemClick.onClick(getAdapterPosition(), 1);
        }
    }
}

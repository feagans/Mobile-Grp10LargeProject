package com.test.mylifegoale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.test.mylifegoale.R;
import com.test.mylifegoale.itemClick.RecycleItemClick;
import com.test.mylifegoale.model.DirectoryModel;
import com.test.mylifegoale.model.FolderHistory;
import com.test.mylifegoale.view.SettingFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.MyviewHolder> {
    SettingFragment activ;
    Context context;
    ArrayList<DirectoryModel> directoryModelsArrayList;
    FolderHistory fh = new FolderHistory();
    public Stack<String> folderHistory = this.fh.getHistory();
    RecycleItemClick recycleItemClick;

    public FileAdapter(Context context2, SettingFragment settingFragment, RecycleItemClick recycleItemClick2) {
        this.context = context2;
        this.activ = settingFragment;
        this.recycleItemClick = recycleItemClick2;
    }

    public void setListContent(ArrayList<DirectoryModel> arrayList) {
        this.directoryModelsArrayList = arrayList;
    }

    @NonNull
    public void onBindViewHolder(@NonNull MyviewHolder myviewHolder, int i) {
        DirectoryModel directoryModel = this.directoryModelsArrayList.get(i);
        myviewHolder.filetext.setText(directoryModel.getFilename());
        if (new File(directoryModel.getPath()).isDirectory()) {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.drawer_folder)).into(myviewHolder.imagetype);
        } else {
            Glide.with(this.context).load(Integer.valueOf(R.drawable.music)).into(myviewHolder.imagetype);
        }
    }

    public MyviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyviewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fileadapter_item, viewGroup, false));
    }

    public int getItemCount() {
        return this.directoryModelsArrayList.size();
    }

    public class MyviewHolder extends RecyclerView.ViewHolder {
        TextView filetext;
        ImageView imagetype;

        public MyviewHolder(View view) {
            super(view);
            this.filetext = (TextView) view.findViewById(R.id.filename);
            this.imagetype = (ImageView) view.findViewById(R.id.imagetype);
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    if (folderHistory.isEmpty()) {
                        folderHistory = fh.getHistory();
                    }
                    File file = new File(directoryModelsArrayList.get(MyviewHolder.this.getAdapterPosition()).getPath());
                    if (new File(directoryModelsArrayList.get(MyviewHolder.this.getAdapterPosition()).getPath()).isDirectory()) {
                        folderHistory.push(file.toString());
                        activ.populateRecyclerViewValues(file.toString());
                        return;
                    }
                    recycleItemClick.onClick(MyviewHolder.this.getAdapterPosition());
                }
            });
        }
    }

    public boolean goBack() {
        Stack<String> stack = this.folderHistory;
        if (stack == null) {
            return false;
        }
        if (stack.size() == 1) {
            return true;
        }
        this.folderHistory.pop();
        if (this.folderHistory.peek().equals("root")) {
            this.activ.populateRecyclerViewValuesFirstTime();
            return false;
        }
        this.activ.populateRecyclerViewValues(this.folderHistory.peek());
        return false;
    }
}

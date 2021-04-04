package com.test.mylifegoale.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.ToDolistActivity;
import com.test.mylifegoale.databinding.DiaryDataBodyCheckLayoutBinding;
import com.test.mylifegoale.databinding.DiaryDataRowLayoutBinding;
import com.test.mylifegoale.itemClick.RecyclerClick;
import com.test.mylifegoale.model.DiaryData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DiaryDataAdapter extends RecyclerView.Adapter<DiaryDataAdapter.DiaryDataHolder> {

    public ActionMode.Callback actionModeCallbacks = new ActionMode.Callback() {
        public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
            return false;
        }

        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
            try {
                multiSelect = true;
                actionMode.getMenuInflater().inflate(R.menu.action_menu, menu);
                selectAllMenu = menu.findItem(R.id.selectAll);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
            int itemId = menuItem.getItemId();
            if (itemId == R.id.delete) {
                recyclerClick.onItemDeleteClick(selectedItems);
                return true;
            } else if (itemId != R.id.selectAll) {
                return true;
            } else {
                selectAll();
                return true;
            }
        }

        public void onDestroyActionMode(ActionMode actionMode) {
            multiSelect = false;
            selectAll = false;
            selectedItems.clear();
            notifyDataSetChanged();
        }
    };
    Context context;

    public boolean multiSelect = false;
    RecyclerClick recyclerClick;
    Random rnd;

    public boolean selectAll = false;
    MenuItem selectAllMenu;

    public ArrayList<Integer> selectedItems = new ArrayList<>();
    ArrayList<DiaryData> tempDiaryData;

    public DiaryDataAdapter(Context context2, ArrayList<DiaryData> arrayList, RecyclerClick recyclerClick2) {
        this.tempDiaryData = arrayList;
        this.context = context2;
        this.recyclerClick = recyclerClick2;
        this.rnd = new Random();
    }

    public void setDiaryData(ArrayList<DiaryData> arrayList) {
        this.tempDiaryData = arrayList;
    }

    public ArrayList<DiaryData> getTempDiaryData() {
        return this.tempDiaryData;
    }

    public DiaryDataHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DiaryDataHolder(LayoutInflater.from(this.context).inflate(R.layout.diary_data_row_layout, viewGroup, false));
    }

    public void onBindViewHolder(DiaryDataHolder diaryDataHolder, int i) {
        DiaryData diaryData = this.tempDiaryData.get(i);
        diaryDataHolder.diaryDataRowLayoutBinding.setDiaryData(this.tempDiaryData.get(i));
        diaryDataHolder.diaryDataRowLayoutBinding.cardmain.setBackgroundColor(Color.argb(50, this.rnd.nextInt(256), this.rnd.nextInt(256), this.rnd.nextInt(256)));
        if (i > 0) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(diaryData.getTimestamp());
            Calendar instance2 = Calendar.getInstance();
            instance2.set(2, instance.get(2));
            instance2.set(1, instance.get(1));
            instance2.set(5, 0);
            instance2.set(12, 0);
            instance2.set(13, 0);
            instance2.set(14, 0);
            Calendar instance3 = Calendar.getInstance();
            instance3.setTimeInMillis(this.tempDiaryData.get(i - 1).getTimestamp());
            Calendar instance4 = Calendar.getInstance();
            instance4.set(2, instance3.get(2));
            instance4.set(1, instance3.get(1));
            instance4.set(5, 0);
            instance4.set(12, 0);
            instance4.set(13, 0);
            instance4.set(14, 0);
            if (instance2.equals(instance4)) {
                this.tempDiaryData.get(i).setStatus(8);
            } else {
                this.tempDiaryData.get(i).setStatus(0);
            }
        } else {
            this.tempDiaryData.get(i).setStatus(0);
        }
        if (ToDolistActivity.filterFlag) {
            if (this.selectedItems.contains(Integer.valueOf(this.tempDiaryData.get(i).getActualPos()))) {
                diaryDataHolder.diaryDataRowLayoutBinding.selectedItem.setVisibility(View.VISIBLE);
            } else {
                diaryDataHolder.diaryDataRowLayoutBinding.selectedItem.setVisibility(View.GONE);
            }
        } else if (this.selectedItems.contains(Integer.valueOf(i))) {
            diaryDataHolder.diaryDataRowLayoutBinding.selectedItem.setVisibility(View.VISIBLE);
        } else {
            diaryDataHolder.diaryDataRowLayoutBinding.selectedItem.setVisibility(View.GONE);
        }
        if (diaryData.getDisplay_status() == 1) {
            diaryDataHolder.diaryDataRowLayoutBinding.bodyET.setVisibility(View.GONE);
            diaryDataHolder.diaryDataRowLayoutBinding.body.setVisibility(View.VISIBLE);
            diaryDataHolder.adapter.setBodyItemAdapter(diaryData.getBodyItemArrayList());
            diaryDataHolder.adapter.notifyDataSetChanged();
        } else {
            diaryDataHolder.diaryDataRowLayoutBinding.bodyET.setVisibility(View.VISIBLE);
            diaryDataHolder.diaryDataRowLayoutBinding.body.setVisibility(View.GONE);
        }
        diaryDataHolder.diaryDataRowLayoutBinding.executePendingBindings();
    }

    public int getItemCount() {
        return this.tempDiaryData.size();
    }

    class DiaryDataHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        BodyItemAdapter adapter;
        DiaryDataRowLayoutBinding diaryDataRowLayoutBinding;

        DiaryDataHolder(View view) {
            super(view);
            this.diaryDataRowLayoutBinding = (DiaryDataRowLayoutBinding) DataBindingUtil.bind(view);
            this.diaryDataRowLayoutBinding.cardView.setOnClickListener(this);
            this.diaryDataRowLayoutBinding.itemClickLayout.setOnClickListener(this);
            this.diaryDataRowLayoutBinding.cardView.setOnLongClickListener(this);
            this.diaryDataRowLayoutBinding.itemClickLayout.setOnLongClickListener(this);
            this.diaryDataRowLayoutBinding.body.setLayoutManager(new LinearLayoutManager(context));
            this.diaryDataRowLayoutBinding.body.setNestedScrollingEnabled(false);
            this.adapter = new BodyItemAdapter();
            this.diaryDataRowLayoutBinding.body.setAdapter(this.adapter);
        }

        public void onClick(View view) {
            if (!multiSelect) {
                recyclerClick.onClick(getAdapterPosition());
            } else {
                selectItem(Integer.valueOf(getAdapterPosition()));
            }
        }


        public void selectItem(Integer num) {
            if (ToDolistActivity.filterFlag) {
                if (multiSelect) {
                    if (selectedItems.contains(Integer.valueOf(tempDiaryData.get(getAdapterPosition()).getActualPos()))) {
                        selectedItems.remove(Integer.valueOf(tempDiaryData.get(getAdapterPosition()).getActualPos()));
                    } else {
                        selectedItems.add(Integer.valueOf(tempDiaryData.get(getAdapterPosition()).getActualPos()));
                    }
                }
            } else if (multiSelect) {
                if (selectedItems.contains(num)) {
                    selectedItems.remove(num);
                } else {
                    selectedItems.add(num);
                }
            }
            notifyDataSetChanged();
        }

        public boolean onLongClick(View view) {
            if (multiSelect) {
                return true;
            }
            ((AppCompatActivity) view.getContext()).startSupportActionMode(actionModeCallbacks);
            selectItem(Integer.valueOf(getAdapterPosition()));
            return true;
        }
    }

    public void selectAll() {
        this.selectedItems.clear();
        if (!this.selectAll) {
            this.selectAllMenu.setIcon(R.drawable.ic_selectall);
            for (int i = 0; i < this.tempDiaryData.size(); i++) {
                if (ToDolistActivity.filterFlag) {
                    this.selectedItems.add(Integer.valueOf(this.tempDiaryData.get(i).getActualPos()));
                } else {
                    this.selectedItems.add(Integer.valueOf(i));
                }
            }
            this.selectAll = true;
        } else {
            this.selectAllMenu.setIcon(R.drawable.select_all_empty);
            this.selectAll = false;
        }
        notifyDataSetChanged();
    }

    public class BodyItemAdapter extends RecyclerView.Adapter<BodyItemAdapter.BodyItemViewHolder> {
        ArrayList<String> strings;

        public BodyItemAdapter() {
        }

        public void setBodyItemAdapter(ArrayList<String> arrayList) {
            this.strings = arrayList;
        }

        @NonNull
        public BodyItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            return new BodyItemViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.diary_data_body_check_layout, viewGroup, false));
        }

        public void onBindViewHolder(@NonNull BodyItemViewHolder bodyItemViewHolder, int i) {
            bodyItemViewHolder.binding.addNewForCheckList.setText(this.strings.get(i));
            bodyItemViewHolder.binding.executePendingBindings();
        }

        public int getItemCount() {
            ArrayList<String> arrayList = this.strings;
            if (arrayList == null) {
                return 0;
            }
            if (arrayList.size() > 5) {
                return 5;
            }
            return this.strings.size();
        }

        public class BodyItemViewHolder extends RecyclerView.ViewHolder {
            DiaryDataBodyCheckLayoutBinding binding;

            public BodyItemViewHolder(@NonNull View view) {
                super(view);
                this.binding = (DiaryDataBodyCheckLayoutBinding) DataBindingUtil.bind(view);
            }
        }
    }
}

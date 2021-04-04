package com.test.mylifegoale.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.test.mylifegoale.R;
import com.test.mylifegoale.activities.AddLifePurposeActivity;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.LifePurposeHolderBinding;
import com.test.mylifegoale.itemClick.DialogClick;
import com.test.mylifegoale.model.LifePurposeModel;
import com.test.mylifegoale.utilities.AllDialog;
import com.test.mylifegoale.utilities.Constants;

import java.util.ArrayList;

public class LifePurposeAdapter extends RecyclerView.Adapter<LifePurposeAdapter.LifePurposeViewHolder> {
    AppDatabase appDatabase;
    Context context;
    ArrayList<LifePurposeModel> visionModelsList;

    public LifePurposeAdapter(Context context2, ArrayList<LifePurposeModel> arrayList) {
        this.context = context2;
        this.visionModelsList = arrayList;
        this.appDatabase = AppDatabase.getAppDatabase(context2);
    }

    @NonNull
    public LifePurposeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new LifePurposeViewHolder(LayoutInflater.from(this.context).inflate(R.layout.life_purpose_holder, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull LifePurposeViewHolder lifePurposeViewHolder, int i) {
        lifePurposeViewHolder.binding.setLifePurposeModel(this.visionModelsList.get(i));
    }

    public int getItemCount() {
        ArrayList<LifePurposeModel> arrayList = this.visionModelsList;
        if (arrayList != null) {
            return arrayList.size();
        }
        return 0;
    }

    public class LifePurposeViewHolder extends RecyclerView.ViewHolder {
        LifePurposeHolderBinding binding;

        public LifePurposeViewHolder(@NonNull View view) {
            super(view);
            this.binding = (LifePurposeHolderBinding) DataBindingUtil.bind(view);
            this.binding.delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new AllDialog().callDialog("Guide", "text here", "", "", (Activity) context, new DialogClick() {
                        @Override
                        public void onNegetiveClick() {
                            //ADD
                        }

                        @Override
                        public void onPositiveClick() {
                            //ADD
                        }
                    });
                }
            });
            this.binding.delete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    new AllDialog().callDialog("", "", "", "", (Activity) context, new DialogClick() {
                        @Override
                        public void onNegetiveClick() {
                            //ADD
                        }

                        @Override
                        public void onPositiveClick() {
                            LifePurposeModel lifePurposeModel = visionModelsList.get(LifePurposeViewHolder.this.getAdapterPosition());
                            lifePurposeModel.setDescription("");
                            if (appDatabase.lifePurposeDao().update(lifePurposeModel) > 0) {
                                visionModelsList.set(LifePurposeViewHolder.this.getAdapterPosition(), lifePurposeModel);
                                notifyItemChanged(LifePurposeViewHolder.this.getAdapterPosition());
                            }
                        }
                    });
                }
            });
            this.binding.edit.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    ((Activity) context).startActivityForResult(new Intent(context, AddLifePurposeActivity.class).putExtra(Constants.EDIT_ADD_VISION_TAG, true).putExtra(Constants.VISION_DATA_TAG, visionModelsList.get(LifePurposeViewHolder.this.getAdapterPosition())), 100);
                }
            });
        }
    }
}

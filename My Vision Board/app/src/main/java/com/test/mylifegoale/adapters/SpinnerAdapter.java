package com.test.mylifegoale.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.databinding.DataBindingUtil;

import com.test.mylifegoale.R;
import com.test.mylifegoale.base.roomDb.AppDatabase;
import com.test.mylifegoale.databinding.SpinnerDropDownItemBinding;
import com.test.mylifegoale.databinding.SpinnerItemBinding;
import com.test.mylifegoale.model.CategoryModel;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter {
    ArrayList<CategoryModel> categoryDAOS;
    Context context;
    LayoutInflater inflter;

    public SpinnerAdapter(Context context2, int i, ArrayList<CategoryModel> arrayList) {
        super(context2, i, arrayList);
        this.categoryDAOS = arrayList;
        this.inflter = LayoutInflater.from(context2);
        this.context = context2;
    }

    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        return getCustomView(i, viewGroup);
    }

    public int getCount() {
        return this.categoryDAOS.size();
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        return getdropDownView(i, viewGroup);
    }

    public View getCustomView(final int i, ViewGroup viewGroup) {
        SpinnerItemBinding spinnerItemBinding = (SpinnerItemBinding) DataBindingUtil.inflate(this.inflter, R.layout.spinner_item, viewGroup, false);
        CategoryModel categoryModel = this.categoryDAOS.get(i);
        spinnerItemBinding.setCategoryModel(categoryModel);
        View root = spinnerItemBinding.getRoot();
        if (!categoryModel.isDefault()) {
            spinnerItemBinding.deleteIcon.setVisibility(View.VISIBLE);
        }
        spinnerItemBinding.deleteIcon.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (AppDatabase.getAppDatabase(context).categoryDAO().delete(categoryDAOS.get(i)) > 0) {
                    categoryDAOS.remove(i);
                    notifyDataSetChanged();
                }
            }
        });
        if (i == 0) {
            spinnerItemBinding.addIcon.setVisibility(View.VISIBLE);
        } else {
            spinnerItemBinding.addIcon.setVisibility(View.GONE);
        }
        return root;
    }

    public View getdropDownView(int i, ViewGroup viewGroup) {
        SpinnerDropDownItemBinding spinnerDropDownItemBinding = (SpinnerDropDownItemBinding) DataBindingUtil.inflate(this.inflter, R.layout.spinner_drop_down_item, viewGroup, false);
        spinnerDropDownItemBinding.setCategoryModel(this.categoryDAOS.get(i));
        return spinnerDropDownItemBinding.getRoot();
    }
}

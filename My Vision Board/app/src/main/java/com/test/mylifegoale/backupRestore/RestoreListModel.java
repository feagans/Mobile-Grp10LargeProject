package com.test.mylifegoale.backupRestore;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import java.util.ArrayList;

public class RestoreListModel extends BaseObservable {
    private ArrayList<RestoreRowModel> arrayList;
    private String noDataDetail;
    private int noDataIcon;
    private String noDataText;

    @Bindable
    public ArrayList<RestoreRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<RestoreRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    public int getNoDataIcon() {
        return this.noDataIcon;
    }

    public void setNoDataIcon(int i) {
        this.noDataIcon = i;
    }

    public String getNoDataText() {
        return this.noDataText;
    }

    public void setNoDataText(String str) {
        this.noDataText = str;
    }

    public String getNoDataDetail() {
        return this.noDataDetail;
    }

    public void setNoDataDetail(String str) {
        this.noDataDetail = str;
    }

    public boolean isListData() {
        return getArrayList() != null && getArrayList().size() > 0;
    }
}

package com.test.mylifegoale.model.affirm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.test.mylifegoale.model.FolderRowModel;
import java.util.ArrayList;

public class FolderListModel extends BaseObservable {
    private ArrayList<FolderRowModel> arrayList;
    private boolean isDragEnable;
    private String noDataDetail;
    private int noDataIcon;
    private String noDataText;

    @Bindable
    public ArrayList<FolderRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<FolderRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    @Bindable
    public boolean isDragEnable() {
        return this.isDragEnable;
    }

    public void setDragEnable(boolean z) {
        this.isDragEnable = z;
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

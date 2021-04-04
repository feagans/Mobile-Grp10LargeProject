package com.test.mylifegoale.model.affirm;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.test.mylifegoale.model.AffirmationRowModel;
import com.test.mylifegoale.model.FolderRowModel;
import java.util.ArrayList;

public class AffirmListModel extends BaseObservable {
    private ArrayList<AffirmationRowModel> arrayList;
    private ArrayList<FolderRowModel> folderList;
    private boolean isDragEnable;
    private boolean isFullSreen;
    private boolean isPause;
    private boolean isShowSeekMenu;
    private String noDataDetail;
    private int noDataIcon;
    private String noDataText;

    @Bindable
    public ArrayList<AffirmationRowModel> getArrayList() {
        return this.arrayList;
    }

    public void setArrayList(ArrayList<AffirmationRowModel> arrayList2) {
        this.arrayList = arrayList2;
        notifyChange();
    }

    @Bindable
    public ArrayList<FolderRowModel> getFolderList() {
        return this.folderList;
    }

    public void setFolderList(ArrayList<FolderRowModel> arrayList2) {
        this.folderList = arrayList2;
        notifyChange();
    }

    @Bindable
    public boolean isPause() {
        return this.isPause;
    }

    public void setPause(boolean z) {
        this.isPause = z;
        notifyChange();
    }

    @Bindable
    public boolean isShowSeekMenu() {
        return this.isShowSeekMenu;
    }

    public void setShowSeekMenu(boolean z) {
        this.isShowSeekMenu = z;
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

    @Bindable
    public boolean isFullSreen() {
        return this.isFullSreen;
    }

    public void setFullSreen(boolean z) {
        this.isFullSreen = z;
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

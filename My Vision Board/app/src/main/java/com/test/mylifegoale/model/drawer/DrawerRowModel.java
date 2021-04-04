package com.test.mylifegoale.model.drawer;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.test.mylifegoale.MyApplication;
import com.test.mylifegoale.utilities.AppConstants;

public class DrawerRowModel extends BaseObservable {
    private int consPosition;
    private int imgResId;
    private boolean isSelected;
    private String name;
    private int viewType;

    public DrawerRowModel(String str, int i, int i2, int i3, boolean z) {
        this.name = str;
        this.imgResId = i;
        this.viewType = i2;
        this.consPosition = i3;
        this.isSelected = z;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
    }

    public int getImgResId() {
        return this.imgResId;
    }

    public void setImgResId(int i) {
        this.imgResId = i;
    }

    @Bindable
    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int i) {
        this.viewType = i;
        notifyChange();
    }

    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
        notifyChange();
    }

    public int getConsPosition() {
        return this.consPosition;
    }

    public void setConsPosition(int i) {
        this.consPosition = i;
    }

    public String getImageUrl() {
        return AppConstants.getPackageResourcePath(MyApplication.getInstance().getApplicationContext()) + getImgResId();
    }
}

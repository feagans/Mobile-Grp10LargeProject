package com.test.mylifegoale.model.selection;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.test.mylifegoale.utilities.AppConstants;

public class SelectionRowModel extends BaseObservable {
    private String id;
    private String imageUrl;
    private boolean isSelected;
    private String label;
    private String value;
    private int viewType;

    public int getImageIcon() {
        return 0;
    }

    public SelectionRowModel(String str) {
        this.label = str;
    }

    public SelectionRowModel(String str, String str2) {
        this.label = str;
        this.value = str2;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String str) {
        this.label = str;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String str) {
        this.value = str;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
    }

    @Bindable
    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean z) {
        this.isSelected = z;
        notifyChange();
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int i) {
        this.viewType = i;
    }

    public String getImageIconUrl() {
        return AppConstants.getResIdUsingCategoryType(getId());
    }
}

package com.test.mylifegoale.model.image;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import com.test.mylifegoale.utilities.AppConstants;

public class ImageRowModel extends BaseObservable {
    private String id;
    private String imageUrl;
    private boolean isSelected;

    public int getImageIcon() {
        return 0;
    }

    public ImageRowModel() {
    }

    public ImageRowModel(String str) {
        this.imageUrl = str;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
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

    public String getImageUrlSmall() {
        return AppConstants.getResIdUsingCategoryType(getId());
    }

    public String getImageUrlIcon() {
        return getImageUrl();
    }
}

package com.test.mylifegoale.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "categoryData")
public class CategoryModel {
    @NonNull
    @PrimaryKey
    String id;
    boolean isDefault;
    String title;

    public CategoryModel(@NonNull String str, String str2, boolean z) {
        this.id = str;
        this.title = str2;
        this.isDefault = z;
    }

    public CategoryModel() {
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String str) {
        this.id = str;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean z) {
        this.isDefault = z;
    }
}

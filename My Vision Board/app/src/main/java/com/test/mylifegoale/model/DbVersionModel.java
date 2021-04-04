package com.test.mylifegoale.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "dbVersion")
public class DbVersionModel {

    @NonNull
    @PrimaryKey
    private int versionNumber = 0;

    public int getVersionNumber() {
        return this.versionNumber;
    }

    public void setVersionNumber(int i) {
        this.versionNumber = i;
    }

    public DbVersionModel() {
    }
    public DbVersionModel(int i) {
        this.versionNumber = i;
    }
}

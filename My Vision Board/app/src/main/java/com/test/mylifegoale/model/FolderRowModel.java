package com.test.mylifegoale.model;

import android.graphics.Color;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.test.mylifegoale.utilities.AppConstants;

@Entity(tableName = "folderList")
public class FolderRowModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<FolderRowModel> CREATOR = new Parcelable.Creator<FolderRowModel>() {
        public FolderRowModel createFromParcel(Parcel parcel) {
            return new FolderRowModel(parcel);
        }

        public FolderRowModel[] newArray(int i) {
            return new FolderRowModel[i];
        }
    };
    private String colorCode;
    private long counts;
    @Ignore
    private boolean enableDrag;
    @NonNull
    @PrimaryKey
    private String id;
    private String imageType;
    boolean isDefault;
    @Ignore
    private boolean isSelected;
    private String name;
    private int sequence;

    public int describeContents() {
        return 0;
    }

    public int getImageIcon() {
        return 0;
    }

    public FolderRowModel() {
        this.name = "";
        this.imageType = "Success";
        this.colorCode = "#9ebef1";
        this.counts = 0;
        this.sequence = -1;
        this.isDefault = false;
    }

    public FolderRowModel(@NonNull String str, String str2, String str3, String str4, int i, boolean z) {
        this.name = "";
        this.imageType = "Success";
        this.colorCode = "#9ebef1";
        this.counts = 0;
        this.sequence = -1;
        this.isDefault = false;
        this.id = str;
        this.name = str2;
        this.imageType = str3;
        this.colorCode = str4;
        this.sequence = i;
        this.isDefault = z;
    }

    public FolderRowModel(FolderRowModel folderRowModel) {
        this.name = "";
        this.imageType = "Success";
        this.colorCode = "#9ebef1";
        this.counts = 0;
        this.sequence = -1;
        this.isDefault = false;
        this.id = folderRowModel.id;
        this.name = folderRowModel.name;
        this.imageType = folderRowModel.imageType;
        this.colorCode = folderRowModel.colorCode;
        this.counts = folderRowModel.counts;
        this.sequence = folderRowModel.sequence;
        this.isSelected = folderRowModel.isSelected;
        this.enableDrag = folderRowModel.enableDrag;
        this.isDefault = folderRowModel.isDefault;
    }

    public boolean isDefault() {
        return this.isDefault;
    }

    public void setDefault(boolean z) {
        this.isDefault = z;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    @Bindable
    public String getName() {
        return this.name;
    }

    public void setName(String str) {
        this.name = str;
        notifyChange();
    }

    @Bindable
    public String getColorCode() {
        return this.colorCode;
    }

    @Bindable
    public int getColorCodes() {
        int coloar = Color.parseColor(colorCode);
        return coloar;
    }

    public void setColorCode(String str) {
        this.colorCode = str;
        notifyChange();
    }

    @Bindable
    public String getImageType() {
        return this.imageType;
    }

    public void setImageType(String str) {
        this.imageType = str;
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

    @Bindable
    public long getCounts() {
        return this.counts;
    }

    public void setCounts(long j) {
        this.counts = j;
        notifyChange();
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int i) {
        this.sequence = i;
    }

    @Bindable
    public boolean isEnableDrag() {
        return this.enableDrag;
    }

    public void setEnableDrag(boolean z) {
        this.enableDrag = z;
        notifyChange();
    }

    public boolean equals(Object obj) {
        FolderRowModel folderRowModel = (FolderRowModel) obj;
        return folderRowModel.getName().equalsIgnoreCase(getName()) && folderRowModel.getImageType().equalsIgnoreCase(getImageType()) && folderRowModel.getColorCode().equalsIgnoreCase(getColorCode());
    }

    public int getColor() {
        return Color.parseColor(this.colorCode);
    }

    public int getColorDark() {
        if (this.isDefault) {
            return Color.parseColor(AppConstants.getDarkColor(this.colorCode));
        }
        return Color.parseColor(this.colorCode);
    }

    public String getImageUrl() {
        return AppConstants.getResIdUsingCategoryType(getImageType());
    }

    protected FolderRowModel(Parcel parcel) {
        this.name = "";
        this.imageType = "Success";
        this.colorCode = "#9ebef1";
        this.counts = 0;
        this.sequence = -1;
        boolean z = false;
        this.isDefault = false;
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.imageType = parcel.readString();
        this.colorCode = parcel.readString();
        this.counts = parcel.readLong();
        this.sequence = parcel.readInt();
        this.isSelected = parcel.readByte() != 0;
        this.enableDrag = parcel.readByte() != 0;
        this.isDefault = parcel.readByte() != 0 ? true : z;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.imageType);
        parcel.writeString(this.colorCode);
        parcel.writeLong(this.counts);
        parcel.writeInt(this.sequence);
        parcel.writeByte(this.isSelected ? (byte) 1 : 0);
        parcel.writeByte(this.enableDrag ? (byte) 1 : 0);
        parcel.writeByte(this.isDefault ? (byte) 1 : 0);
    }
}

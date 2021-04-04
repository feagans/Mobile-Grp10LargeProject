package com.test.mylifegoale.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;
import java.io.File;

@Entity(tableName = "affirmationList")
public class AffirmationRowModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<AffirmationRowModel> CREATOR = new Parcelable.Creator<AffirmationRowModel>() {
        public AffirmationRowModel createFromParcel(Parcel parcel) {
            return new AffirmationRowModel(parcel);
        }

        public AffirmationRowModel[] newArray(int i) {
            return new AffirmationRowModel[i];
        }
    };
    private String folderId = "";
    @Ignore
    private FolderRowModel folderRowModel;
    @NonNull
    @PrimaryKey
    private String id = "";
    private String imageUrl = "";
    private boolean isActive;
    @Ignore
    private boolean isEnableDrag;
    @Ignore
    private boolean isPause;
    private String quoteText = "";
    private int sequence = -1;
    private int sequenceFolder = -1;
    private String voiceUrl = "";

    public int describeContents() {
        return 0;
    }

    public AffirmationRowModel() {
    }

    public AffirmationRowModel(String str, String str2, String str3) {
        this.quoteText = str;
        this.imageUrl = str2;
        this.voiceUrl = str3;
    }

    public AffirmationRowModel(@NonNull String str, String str2, String str3, String str4, boolean z, String str5, int i, int i2) {
        this.id = str;
        this.quoteText = str2;
        this.imageUrl = str3;
        this.voiceUrl = str4;
        this.folderId = str5;
        this.isActive = z;
        this.sequenceFolder = i;
        this.sequence = i2;
    }

    public AffirmationRowModel(@NonNull String str, String str2, String str3, String str4, boolean z, String str5, int i, int i2, FolderRowModel folderRowModel2) {
        this.id = str;
        this.quoteText = str2;
        this.imageUrl = str3;
        this.voiceUrl = str4;
        this.folderId = str5;
        this.isActive = z;
        this.sequenceFolder = i;
        this.sequence = i2;
        this.folderRowModel = folderRowModel2;
    }

    public AffirmationRowModel(AffirmationRowModel affirmationRowModel) {
        this.id = affirmationRowModel.id;
        this.quoteText = affirmationRowModel.quoteText;
        this.imageUrl = affirmationRowModel.imageUrl;
        this.voiceUrl = affirmationRowModel.voiceUrl;
        this.folderId = affirmationRowModel.folderId;
        this.isActive = affirmationRowModel.isActive;
        this.sequence = affirmationRowModel.sequence;
        this.sequenceFolder = affirmationRowModel.sequenceFolder;
        this.folderRowModel = affirmationRowModel.folderRowModel;
        this.isPause = affirmationRowModel.isPause;
        this.isEnableDrag = affirmationRowModel.isEnableDrag;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    @Bindable
    public String getQuoteText() {
        return this.quoteText;
    }

    public void setQuoteText(String str) {
        this.quoteText = str;
        notifyChange();
    }

    @Bindable
    public String getImageUrl() {
        return this.imageUrl;
    }

    public void setImageUrl(String str) {
        this.imageUrl = str;
        notifyChange();
    }

    @Bindable
    public String getVoiceUrl() {
        return this.voiceUrl;
    }

    public void setVoiceUrl(String str) {
        this.voiceUrl = str;
        notifyChange();
    }

    public String getFolderId() {
        return this.folderId;
    }

    public void setFolderId(String str) {
        this.folderId = str;
    }

    public int getSequence() {
        return this.sequence;
    }

    public void setSequence(int i) {
        this.sequence = i;
    }

    public int getSequenceFolder() {
        return this.sequenceFolder;
    }

    public void setSequenceFolder(int i) {
        this.sequenceFolder = i;
    }

    @Bindable
    public boolean isActive() {
        return this.isActive;
    }

    public void setActive(boolean z) {
        this.isActive = z;
        notifyChange();
    }

    @Bindable
    public FolderRowModel getFolderRowModel() {
        return this.folderRowModel;
    }

    public void setFolderRowModel(FolderRowModel folderRowModel2) {
        this.folderRowModel = folderRowModel2;
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
    public boolean isEnableDrag() {
        return this.isEnableDrag;
    }

    public void setEnableDrag(boolean z) {
        this.isEnableDrag = z;
        notifyChange();
    }

    public boolean equals(Object obj) {
        AffirmationRowModel affirmationRowModel = (AffirmationRowModel) obj;
        return affirmationRowModel.getQuoteText().equalsIgnoreCase(getQuoteText()) && affirmationRowModel.getImageUrl().equalsIgnoreCase(getImageUrl()) && affirmationRowModel.getVoiceUrl().equalsIgnoreCase(getVoiceUrl()) && affirmationRowModel.getFolderId().equalsIgnoreCase(getFolderId());
    }

    @Bindable
    public boolean isVoiceFound() {
        String str = this.voiceUrl;
        return str != null && str.trim().length() > 0 && new File(this.voiceUrl).exists();
    }

    @Bindable
    public boolean isImageFound() {
        if (getImageUrl().contains(Constants.PATH_RESOURCE)) {
            return false;
        }
        if (getImageUrl().contains(Constants.PATH_ASSET)) {
            String str = this.imageUrl;
            if (str == null || str.trim().length() <= 0) {
                return false;
            }
        } else {
            String str2 = this.imageUrl;
            if (str2 == null || str2.trim().length() <= 0 || !new File(this.imageUrl).exists()) {
                return false;
            }
        }
        return true;
    }

    public String getImageUrlPlayer() {
        if (isImageFound()) {
            return getImageUrl();
        }
        return AppConstants.getListBackgroundImage().get(AppConstants.getRandomWithBound(AppConstants.getListBackgroundImage().size())).getImageUrl();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.quoteText);
        parcel.writeString(this.imageUrl);
        parcel.writeString(this.voiceUrl);
        parcel.writeByte(this.isActive ? (byte) 1 : 0);
        parcel.writeString(this.folderId);
        parcel.writeInt(this.sequence);
        parcel.writeInt(this.sequenceFolder);
        parcel.writeParcelable(this.folderRowModel, i);
        parcel.writeByte(this.isPause ? (byte) 1 : 0);
        parcel.writeByte(this.isEnableDrag ? (byte) 1 : 0);
    }

    protected AffirmationRowModel(Parcel parcel) {
        this.id = parcel.readString();
        this.quoteText = parcel.readString();
        this.imageUrl = parcel.readString();
        this.voiceUrl = parcel.readString();
        boolean z = true;
        this.isActive = parcel.readByte() != 0;
        this.folderId = parcel.readString();
        this.sequence = parcel.readInt();
        this.sequenceFolder = parcel.readInt();
        this.folderRowModel = (FolderRowModel) parcel.readParcelable(FolderRowModel.class.getClassLoader());
        this.isPause = parcel.readByte() != 0;
        this.isEnableDrag = parcel.readByte() == 0 ? false : z;
    }
}

package com.test.mylifegoale.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.exifinterface.media.ExifInterface;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import com.test.mylifegoale.utilities.Constants;

@Entity(tableName = "lifePurposeData")
public class LifePurposeModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<LifePurposeModel> CREATOR = new Parcelable.Creator<LifePurposeModel>() {
        public LifePurposeModel createFromParcel(Parcel parcel) {
            return new LifePurposeModel(parcel);
        }

        public LifePurposeModel[] newArray(int i) {
            return new LifePurposeModel[i];
        }
    };
    String description;
    @NonNull
    @PrimaryKey
    private String id;
    String title;

    public int describeContents() {
        return 0;
    }

    public LifePurposeModel(@NonNull String str, String str2, String str3) {
        this.id = str;
        this.title = str2;
        this.description = str3;
    }

    public LifePurposeModel() {
    }

    protected LifePurposeModel(Parcel parcel) {
        this.id = parcel.readString();
        this.title = parcel.readString();
        this.description = parcel.readString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.description);
    }

    public boolean equals(Object obj) {
        if (obj == null || !LifePurposeModel.class.isAssignableFrom(obj.getClass()) || !this.id.equals(((LifePurposeModel) obj).id)) {
            return false;
        }
        return true;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
    }

    public String setDescriptionString() {
        Log.i("setDescriptionString", "setDescriptionString: " + this.description);
        if (!this.description.isEmpty()) {
            return "";
        }
        if (this.id.equalsIgnoreCase("1")) {
            return Constants.your_purpose;
        }
        if (this.id.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_2D)) {
            return Constants.my_vision;
        }
        return this.id.equalsIgnoreCase(ExifInterface.GPS_MEASUREMENT_3D) ? Constants.my_goals : "";
    }
}

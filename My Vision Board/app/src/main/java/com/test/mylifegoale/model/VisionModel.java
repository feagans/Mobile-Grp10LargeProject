package com.test.mylifegoale.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.test.mylifegoale.utilities.AppConstants;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

@Entity(tableName = "visionData")
public class VisionModel extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<VisionModel> CREATOR = new Parcelable.Creator<VisionModel>() {
        public VisionModel createFromParcel(Parcel parcel) {
            return new VisionModel(parcel);
        }

        public VisionModel[] newArray(int i) {
            return new VisionModel[i];
        }
    };
    @ColumnInfo(name = "title")
    String catTitle;
    String category;
    public long createdTime;
    String description;
    long endTime;
    @NonNull
    @PrimaryKey
    private String id;
    @Ignore
    private boolean isEnableDrag;
    boolean isPending;
    String name;
    int ord;
    String visionProfile;

    public int describeContents() {
        return 0;
    }

    public VisionModel() {
        this.description = "";
        this.visionProfile = "";
        this.endTime = 0;
        this.createdTime = 0;
        this.isPending = true;
        this.ord = 0;
        this.createdTime = System.currentTimeMillis();
    }

    public VisionModel(@NonNull String str, String str2, String str3, String str4, String str5, long j) {
        this.description = "";
        this.visionProfile = "";
        this.endTime = 0;
        this.createdTime = 0;
        this.isPending = true;
        this.ord = 0;
        this.id = str;
        this.name = str2;
        this.description = str3;
        this.visionProfile = str4;
        this.category = str5;
        this.endTime = j;
    }

    protected VisionModel(Parcel parcel) {
        this.description = "";
        this.visionProfile = "";
        this.endTime = 0;
        this.createdTime = 0;
        boolean z = true;
        this.isPending = true;
        this.ord = 0;
        this.id = parcel.readString();
        this.name = parcel.readString();
        this.description = parcel.readString();
        this.visionProfile = parcel.readString();
        this.category = parcel.readString();
        this.catTitle = parcel.readString();
        this.endTime = parcel.readLong();
        this.isPending = parcel.readByte() == 0 ? false : z;
        this.ord = parcel.readInt();
    }

    public boolean equals(Object obj) {
        if (obj == null || !VisionModel.class.isAssignableFrom(obj.getClass()) || !this.id.equals(((VisionModel) obj).id)) {
            return false;
        }
        return true;
    }

    @Bindable
    public boolean isEnableDrag() {
        return this.isEnableDrag;
    }

    public void setEnableDrag(boolean z) {
        this.isEnableDrag = z;
        notifyChange();
    }

    public String getCatTitle() {
        return this.catTitle;
    }

    public void setCatTitle(String str) {
        this.catTitle = str;
    }

    public boolean isPending() {
        return this.isPending;
    }

    public void setPending(boolean z) {
        this.isPending = z;
    }

    public long getCreatedTime() {
        return this.createdTime;
    }

    public void setCreatedTime(long j) {
        this.createdTime = j;
    }

    @NonNull
    public String getId() {
        return this.id;
    }

    public void setId(@NonNull String str) {
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
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String str) {
        this.description = str;
        notifyChange();
    }

    @Bindable
    public String getVisionProfile() {
        return this.visionProfile;
    }

    public void setVisionProfile(String str) {
        this.visionProfile = str;
        notifyChange();
    }

    @Bindable
    public String getCategory() {
        return this.category;
    }

    public void setCategory(String str) {
        this.category = str;
        notifyChange();
    }

    public int getOrd() {
        return this.ord;
    }

    public void setOrd(int i) {
        this.ord = i;
        notifyChange();
    }

    @Bindable
    public long getEndTime() {
        return this.endTime;
    }

    public void setEndTime(long j) {
        this.endTime = j;
        notifyChange();
    }

    public String getDayCount() {
        long j;
        StringBuilder sb = new StringBuilder();
        if (this.endTime > 0) {
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(System.currentTimeMillis());
            Calendar instance2 = Calendar.getInstance();
            instance2.setTimeInMillis(this.endTime);
            Calendar instance3 = Calendar.getInstance();
            boolean z = true;
            instance3.set(1, instance.get(1));
            instance3.set(2, instance.get(2));
            instance3.set(5, instance.get(5));
            instance3.set(10, 0);
            instance3.set(12, 0);
            instance3.set(13, 0);
            Calendar instance4 = Calendar.getInstance();
            instance4.set(1, instance2.get(1));
            instance4.set(2, instance2.get(2));
            instance4.set(5, instance2.get(5));
            instance4.set(10, 0);
            instance4.set(12, 0);
            instance4.set(13, 0);
            if (instance.getTimeInMillis() <= instance2.getTimeInMillis()) {
                j = Math.abs(instance4.getTimeInMillis() - instance3.getTimeInMillis());
                z = false;
            } else {
                j = Math.abs(instance3.getTimeInMillis() - instance4.getTimeInMillis());
            }
            long convert = TimeUnit.DAYS.convert(j, TimeUnit.MILLISECONDS);
            if (!this.isPending) {
                sb.append("Completed");
            } else if (!z) {
                sb.append((int) convert);
                sb.append(" day(s) left");
            } else {
                sb.append((int) convert);
                sb.append(" day(s) ago");
            }
        } else if (this.isPending) {
            sb.append("Pending");
        } else {
            sb.append("Completed");
        }
        return sb.toString();
    }

    public String getDateString() {
        StringBuilder sb = new StringBuilder();
        sb.append("End Date: ");
        long j = this.endTime;
        sb.append(j == 0 ? "(Optional)" : AppConstants.getFormattedDate(j, AppConstants.SIMPLE_DATE_FORMAT));
        return ("");
    }

    public String getDateStringEdit() {
        long j = this.endTime;
        if (j == 0) {
            return "";
        }
        return "";
    }

    public String getStatusString() {
        return this.isPending ? "Pending" : "Completed";
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.name);
        parcel.writeString(this.description);
        parcel.writeString(this.visionProfile);
        parcel.writeString(this.category);
        parcel.writeString(this.catTitle);
        parcel.writeLong(this.endTime);
        parcel.writeByte(this.isPending ? (byte) 1 : 0);
        parcel.writeInt(this.ord);
    }
}

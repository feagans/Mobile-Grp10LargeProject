package com.test.mylifegoale.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import com.test.mylifegoale.utilities.AppConstants;
import com.test.mylifegoale.utilities.Constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;

@Entity(tableName = "diaryData")
public class DiaryData extends BaseObservable implements Parcelable {
    public static final Parcelable.Creator<DiaryData> CREATOR = new Parcelable.Creator<DiaryData>() {
        public DiaryData createFromParcel(Parcel parcel) {
            return new DiaryData(parcel);
        }

        public DiaryData[] newArray(int i) {
            return new DiaryData[i];
        }
    };
    @Ignore
    int actualPos = -1;
    String body = "";
    @Ignore
    ArrayList<String> bodyItemArrayList = new ArrayList<>();
    @Ignore
    int checkItemCount = 0;
    long createtTimestamp = -1;
    @Ignore
    public final Comparator<DiaryData> diaryDataComparatorByActualPos = new Comparator<DiaryData>() {
        @RequiresApi(api = 19)
        public int compare(DiaryData diaryData, DiaryData diaryData2) {
            return Integer.compare(diaryData2.getActualPos(), diaryData.getActualPos());
        }
    };
    @Ignore
    public final Comparator<DiaryData> diaryDataComparatorCreatedFirst = new Comparator<DiaryData>() {
        @RequiresApi(api = 19)
        public int compare(DiaryData diaryData, DiaryData diaryData2) {
            return Long.compare(diaryData2.getCreatetTimestamp(), diaryData.getCreatetTimestamp());
        }
    };
    @Ignore
    public final Comparator<DiaryData> diaryDataComparatorModifiedFirst = new Comparator<DiaryData>() {
        @RequiresApi(api = 19)
        public int compare(DiaryData diaryData, DiaryData diaryData2) {
            return Long.compare(diaryData2.getTimestamp(), diaryData.getTimestamp());
        }
    };
    int display_status = 0;
    @NonNull
    @PrimaryKey
    String id;
    @Ignore
    int imageCount;
    @Ignore
    int imageCurrentPos = 0;
    @Ignore
    int status;
    long timestamp = -1;
    String title = "";

    public int describeContents() {
        return 0;
    }

    public DiaryData() {
    }

    public DiaryData(String str, String str2, String str3, String str4, long j, long j2) {
        this.id = str;
        this.title = str2;
        this.body = str3;
        this.timestamp = j;
        this.createtTimestamp = j2;
        this.status = this.status;
    }

    protected DiaryData(Parcel parcel) {
        this.id = parcel.readString();
        this.title = parcel.readString();
        this.body = parcel.readString();
        this.timestamp = parcel.readLong();
        this.createtTimestamp = parcel.readLong();
        this.display_status = parcel.readInt();
        this.imageCount = parcel.readInt();
        this.checkItemCount = parcel.readInt();
        this.imageCurrentPos = parcel.readInt();
        this.status = parcel.readInt();
        this.actualPos = parcel.readInt();
        this.bodyItemArrayList = parcel.createStringArrayList();
    }

    @Bindable
    public String getId() {
        return this.id;
    }

    public void setId(String str) {
        this.id = str;
    }

    @Bindable
    public int getImageCount() {
        return this.imageCount;
    }

    public void setImageCount(int i) {
        this.imageCount = i;
        notifyChange();
    }

    @Bindable
    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
        notifyChange();
    }

    @Bindable
    public String getBody() {
        return this.body;
    }

    public void setBody(String str) {
        this.body = str;
        notifyChange();
    }

    public int getActualPos() {
        return this.actualPos;
    }

    public void setActualPos(int i) {
        this.actualPos = i;
    }

    @Bindable
    public int getStatus() {
        return this.status;
    }

    public void setStatus(int i) {
        this.status = i;
        notifyChange();
    }

    @Bindable
    public long getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(long j) {
        this.timestamp = j;
        notifyChange();
    }

    public long getCreatetTimestamp() {
        return this.createtTimestamp;
    }

    public void setCreatetTimestamp(long j) {
        this.createtTimestamp = j;
    }

    public boolean equals(Object obj) {
        if (obj == null || !DiaryData.class.isAssignableFrom(obj.getClass()) || !this.id.equals(((DiaryData) obj).id)) {
            return false;
        }
        return true;
    }

    @Bindable
    public int getDisplay_status() {
        return this.display_status;
    }

    public void setDisplay_status(int i) {
        this.display_status = i;
        notifyChange();
    }

    public int getCheckItemCount() {
        return this.checkItemCount;
    }

    public void setCheckItemCount(int i) {
        this.checkItemCount = i;
    }

    @Bindable
    public int getImageCurrentPos() {
        return this.imageCurrentPos;
    }

    public void setImageCurrentPos(int i) {
        this.imageCurrentPos = i;
        notifyChange();
    }

    public ArrayList<String> getBodyItemArrayList() {
        return this.bodyItemArrayList;
    }

    public void setBodyItemArrayList(ArrayList<String> arrayList) {
        this.bodyItemArrayList = arrayList;
    }

    public String getImageCountLabel() {
        return "Image-" + this.imageCount;
    }

    public String getImageCountLabelPager() {
        return (this.imageCurrentPos + 1) +"/" + this.imageCount;
    }

    @Bindable
    public SimpleDateFormat getSIMPLE_MONTH_YEAR() {
        return Constants.SIMPLE_MONTH_YEAR;
    }

    @Bindable
    public SimpleDateFormat getSIMPLE_DATE_FORMAT_ALL() {
        return Constants.SIMPLE_DATE_FORMAT_ALL;
    }

    @Bindable
    public SimpleDateFormat getSIMPLE_TIME_OF_MONTH() {
        return Constants.SIMPLE_TIME_OF_MONTH;
    }

    public String getFormattedDate(SimpleDateFormat simpleDateFormat) {
        return AppConstants.getFormattedDate(this.timestamp, simpleDateFormat);
    }

    public String getCreatedDate(SimpleDateFormat simpleDateFormat) {
        return AppConstants.getFormattedDate(this.timestamp, simpleDateFormat);
    }

    public String lastModifiedDate() {
        StringBuilder sb = new StringBuilder();
        sb.append("Last Modified : ");
        long j = this.timestamp;
        if (j <= 0 || this.createtTimestamp <= 0) {
            sb.append(AppConstants.getFormattedDate(this.timestamp, Constants.SIMPLE_Month_year_DATE));
            return sb.toString();
        }
        Calendar calendar = getCalendar(j);
        Calendar calendar2 = getCalendar(System.currentTimeMillis());
        int i = calendar2.get(5) - calendar.get(5);
        Log.i("lastModifiedDate", "lastModifiedDate: " + i);
        if (i == 0) {
            sb.append("Today at " + AppConstants.getFormattedDate(this.timestamp, Constants.SIMPLE_TIME_OF_MONTH));
            return sb.toString();
        } else if (i == 1) {
            sb.append("Yesterday at" + AppConstants.getFormattedDate(this.timestamp, Constants.SIMPLE_TIME_OF_MONTH));
            return sb.toString();
        } else if (calendar.get(1) != calendar2.get(1)) {
            sb.append(AppConstants.getFormattedDate(this.timestamp, Constants.SIMPLE_DATE));
            return sb.toString();
        } else {
            sb.append(AppConstants.getFormattedDate(this.timestamp, Constants.SIMPLE_Month_year_DATE));
            return sb.toString();
        }
    }

    public static Calendar getCalendar(long j) {
        Calendar instance = Calendar.getInstance();
        instance.setTimeInMillis(j);
        return instance;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.id);
        parcel.writeString(this.title);
        parcel.writeString(this.body);
        parcel.writeLong(this.timestamp);
        parcel.writeLong(this.createtTimestamp);
        parcel.writeInt(this.display_status);
        parcel.writeInt(this.imageCount);
        parcel.writeInt(this.checkItemCount);
        parcel.writeInt(this.imageCurrentPos);
        parcel.writeInt(this.status);
        parcel.writeInt(this.actualPos);
        parcel.writeStringList(this.bodyItemArrayList);
    }
}

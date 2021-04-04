package com.test.mylifegoale.model;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import java.util.Comparator;

public class BodyItemModel extends BaseObservable {
    public static final int addItemViewType = 2;
    public static final int checkedItemViewType = 3;
    public static final int editedDateViewType = 5;
    public static final int itemViewType = 1;
    public static final int labelViewType = 4;
    public static final int titleViewType = 0;
    private boolean addNewItemVisible = true;
    public final Comparator<BodyItemModel> comparator = new Comparator<BodyItemModel>() {
        public int compare(BodyItemModel bodyItemModel, BodyItemModel bodyItemModel2) {
            int compare = Long.compare(bodyItemModel.id, bodyItemModel2.id);
            return compare == 0 ? Integer.compare(bodyItemModel.rank, bodyItemModel2.rank) : compare;
        }
    };
    private boolean cursorVisible = false;
    private int displayStatus = 0;
    long id = -1;
    String item;
    long note_id;

    public int rank = -1;
    private int status = 0;
    private int viewType;

    public BodyItemModel(long j, long j2, String str, int i, int i2, int i3, boolean z, int i4) {
        this.id = j;
        this.note_id = j2;
        this.item = str;
        this.status = i;
        this.viewType = i2;
        this.rank = i3;
        this.cursorVisible = z;
        this.displayStatus = i4;
    }

    public BodyItemModel() {
    }

    @Bindable
    public long getId() {
        return this.id;
    }

    public void setId(long j) {
        this.id = j;
        notifyChange();
    }

    @Bindable
    public long getNote_id() {
        return this.note_id;
    }

    public void setNote_id(long j) {
        this.note_id = j;
        notifyChange();
    }

    @Bindable
    public String getItem() {
        return this.item;
    }

    public void setItem(String str) {
        this.item = str;
        notifyChange();
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
    public boolean isAddNewItemVisible() {
        return this.addNewItemVisible;
    }

    public void setAddNewItemVisible(boolean z) {
        this.addNewItemVisible = z;
        notifyChange();
    }

    public int getViewType() {
        return this.viewType;
    }

    public void setViewType(int i) {
        this.viewType = i;
    }

    @Bindable
    public boolean isCursorVisible() {
        return this.cursorVisible;
    }

    public void setCursorVisible(boolean z) {
        this.cursorVisible = z;
        notifyChange();
    }

    @Bindable
    public int getRank() {
        return this.rank;
    }

    public void setRank(int i) {
        this.rank = i;
        notifyChange();
    }

    public int getDisplayStatus() {
        return this.displayStatus;
    }

    public void setDisplayStatus(int i) {
        this.displayStatus = i;
    }

    public boolean equals(Object obj) {
        return ((BodyItemModel) obj).getId() == getId();
    }
}

package com.test.mylifegoale.data.model;

public class BucketComponents {
    public String _id;
    public String userID;
    public String itemTitle;
    public String caption;
    public Boolean completed;

    public String getID() { return _id; }
    public void setID() { this._id = _id; }
    public String getUserID() { return userID; }
    public void setUserID(String userID) { this.userID = userID; }
    public String getItemTitle() { return itemTitle; }
    public void setItemTitle(String itemTitle) { this.itemTitle = itemTitle; }
    public String getCaption() { return caption; }
    public void setCaption(String caption) { this.caption = caption; }
    public boolean getCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }
}

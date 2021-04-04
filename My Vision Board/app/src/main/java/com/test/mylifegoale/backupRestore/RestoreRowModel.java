package com.test.mylifegoale.backupRestore;

public class RestoreRowModel {
    String DateModified;
    String path;
    String size;
    long timestamp;
    String title;

    public RestoreRowModel() {
    }

    public RestoreRowModel(String str, String str2, String str3, String str4, long j) {
        this.title = str;
        this.path = str2;
        this.DateModified = str3;
        this.size = str4;
        this.timestamp = j;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getSize() {
        return this.size;
    }

    public void setSize(String str) {
        this.size = str;
    }

    public String getDateModified() {
        return this.DateModified;
    }

    public void setDateModified(String str) {
        this.DateModified = str;
    }

    public Long getTimestamp() {
        return Long.valueOf(this.timestamp);
    }

    public void setTimestamp(Long l) {
        this.timestamp = l.longValue();
    }
}

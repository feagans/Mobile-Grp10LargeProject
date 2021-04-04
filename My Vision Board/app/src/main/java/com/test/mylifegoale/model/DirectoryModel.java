package com.test.mylifegoale.model;

public class DirectoryModel {
    private String filename;
    private int howmanyfiles = -1;
    private boolean isChecked;
    private boolean isDirectory;
    private String path;
    private long totalSize = -1;

    public DirectoryModel(String str, String str2, boolean z, Long l, int i, boolean z2) {
        this.path = str;
        this.filename = str2;
        this.isChecked = z2;
        this.isDirectory = z;
        this.totalSize = l.longValue();
        this.howmanyfiles = i;
    }

    public DirectoryModel(String str, String str2) {
        this.filename = str2;
        this.path = str;
    }

    public String getPath() {
        return this.path;
    }

    public void setPath(String str) {
        this.path = str;
    }

    public String getFilename() {
        return this.filename;
    }

    public void setFilename(String str) {
        this.filename = str;
    }

    public int getHowManyFiles() {
        return this.howmanyfiles;
    }

    public void setHowmanyfiles(int i) {
        this.howmanyfiles = i;
    }

    public boolean isChecked() {
        return this.isChecked;
    }

    public void setChecked(boolean z) {
        this.isChecked = z;
    }

    public boolean isDirectory() {
        return this.isDirectory;
    }

    public void setDirectory(boolean z) {
        this.isDirectory = z;
    }

    public Long getTotalSize() {
        return Long.valueOf(this.totalSize);
    }

    public void setTotalSize(Long l) {
        this.totalSize = l.longValue();
    }
}

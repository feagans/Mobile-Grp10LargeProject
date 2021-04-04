package com.test.mylifegoale.model;

import java.util.Stack;

public class FolderHistory {
    Stack<String> folderHistory = new Stack<>();

    public Stack<String> getHistory() {
        this.folderHistory.add("root");
        return this.folderHistory;
    }
}

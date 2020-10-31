package com.dwestaway.satisfyingtodolist;

import android.widget.ImageButton;

public class ListItemModel {

    String taskText;
    ImageButton doneButton;


    public ListItemModel(String todoText) {

        this.taskText = todoText;
    }

    public String getTaskText() {
        return taskText;
    }

    public void setTaskText(String todoText) {
        this.taskText = todoText;
    }

    public ImageButton getDoneButton() {
        return doneButton;
    }
    public void taskDone() {

    }
}

package com.dwestaway.satisfyingtodolist;

import android.widget.ImageButton;

public class ListItemModel {

    String taskText;
    Boolean taskDone;


    public ListItemModel(String todoText, Boolean taskDone) {

        this.taskText = todoText;
        this.taskDone = taskDone;
    }

    public String getTaskText() {
        return taskText;
    }

    public Boolean getTaskDone() {
        return taskDone;
    }

    public void setTaskDone(Boolean taskDone) {
        this.taskDone = taskDone;
    }
}

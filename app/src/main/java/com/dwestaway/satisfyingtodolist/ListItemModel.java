package com.dwestaway.satisfyingtodolist;

import android.widget.ImageButton;

public class ListItemModel {

    String taskText;
    Boolean taskDone;
    Boolean everyday;


    public ListItemModel(String todoText, Boolean taskDone, Boolean everyday) {

        this.taskText = todoText;
        this.taskDone = taskDone;
        this.everyday = everyday;
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

    public Boolean getEveryday() {
        return everyday;
    }
}

package com.dwestaway.satisfyingtodolist;

import android.widget.ImageButton;

public class ListItemModel {

    String taskText;
    Boolean taskDone;
    Boolean everyday;
    int dayOfYear, year;


    public ListItemModel(String todoText, Boolean taskDone, Boolean everyday, int dayOfYear, int year) {

        this.taskText = todoText;
        this.taskDone = taskDone;
        this.everyday = everyday;

        this.dayOfYear = dayOfYear;
        this.year = year;

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


    public int getDayOfYear() {
        return dayOfYear;
    }

    public void setDayOfYear(int dayOfYear) {
        this.dayOfYear = dayOfYear;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

package com.stuxo.notify.model;

import java.util.Date;

public class ToDoItem{

    private int Id;
    private String text;
    private boolean isComplete;
    private Date alarmTime;

    public ToDoItem(String text){
        Id = NotificationId.createId();
        this.text = text;
        this.isComplete = false;
        alarmTime = null;
    }

}
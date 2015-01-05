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

    public void setIsComplete(boolean isComplete){
        this.isComplete = isComplete;
    }

    public boolean getIsComplete(){
        return isComplete;
    }

    public int getId(){
        return Id;
    }

    public String getText(){
        return text;
    }
//
//    public void createNotification(){
//                        Intent intent = new Intent();
//                intent.setAction(KEY_DONE);
//
//                //on press go to activity with list of activities
//        //notifications only appear when due
//
//        //Added activities go on the main screen as list view
//                intent.setClass(getApplicationContext(), NotificationActionReceiver.class);
//
//                PendingIntent doneIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//
//                Notification notification = createBasicNotification("Don't forget!", txtNotificationDescription.getText().toString(), doneIntent);
//
//                notification.extras.putInt("Id", temp);
//                displayNotification(notification, temp);
//    }
}
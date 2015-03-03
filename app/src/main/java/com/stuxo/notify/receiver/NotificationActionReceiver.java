package com.stuxo.notify.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by stu on 1/11/14.
 */
class NotificationActionReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        int notificationId = intent.getExtras().getInt("Id");



//        if (intent.getAction().equals(MainActivity.KEY_DONE)) {
            clearNotification(context, intent.getIntExtra("Id", 0));
//        } else if (intent.getAction().equals(MainActivity.KEY_DONE)) {
//        }

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.cancel(notificationId);
    }


    public void clearNotification(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(id);
    }
}


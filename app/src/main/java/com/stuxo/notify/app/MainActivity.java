package com.stuxo.notify.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    public static final String KEY_DONE = "com.stuxo.notify.MainActivity.clearNotification";
    public ArrayList<ToDoItem> ToDoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ToDoItems = new ArrayList<ToDoItem>();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle the menu item
                        return true;
                    }
                });

        // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);

        btnCreateNotification = (Button) findViewById(R.id.btnCreateReminder);
        txtNotificationDescription = (EditText) findViewById(R.id.txtReminderDesc);
        btnCreateNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //
                ToDoItems.add(new ToDoItem(txtNotificationDescription.getText().toString()));

//                int temp = NotificationId.createId();
//                Intent intent = new Intent();
//                intent.setAction(KEY_DONE);
//                intent.setClass(getApplicationContext(), NotificationActionReceiver.class);
//
//                PendingIntent doneIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//
//                Notification notification = createBasicNotification("Don't forget!", txtNotificationDescription.getText().toString(), doneIntent);
//
//                notification.extras.putInt("Id", temp);
//                displayNotification(notification, temp);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Notification createBasicNotification(String title, String text, PendingIntent pIntent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext());


        Notification notification = builder
                .setSmallIcon(R.drawable.ic_action_done)
                .setContentIntent(pIntent)
                .setContentTitle(title)
                .setContentText(text)
                .addAction(R.drawable.ic_action_done, "Done", pIntent)
                .addAction(R.drawable.ic_action_time, "10min", pIntent)
                .addAction(R.drawable.ic_action_alarms, "1hr", pIntent)
                .build();

        return notification;
    }

    private void displayNotification(Notification notification, int id) {
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification.extras.putInt("Id",id);
        notificationManager.notify(id, notification);
    }
}

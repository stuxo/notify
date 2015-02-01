package com.stuxo.notify.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.stuxo.notify.controller.ToDoListAdapter;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    public ArrayList<ToDoItem> ToDoItems;
    private ListView toDoLV;
    Firebase myFirebaseRef;
    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://blazing-heat-2528.firebaseio.com/");

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);


        ToDoItems = new ArrayList<ToDoItem>();
        adapter = new ToDoListAdapter(getApplicationContext(), ToDoItems);

        toDoLV = (ListView) findViewById(R.id.listview);
        toDoLV.setAdapter(adapter);

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

               addToDoTitemToList();
            }
        });
    }

    private void addToDoTitemToList(){
        if (txtNotificationDescription.getText().toString().trim().length() != 0){
            ToDoItem newItem = new ToDoItem(txtNotificationDescription.getText().toString());

            ToDoItems.add(newItem);

            addFirebaseListener(newItem);
            txtNotificationDescription.setText("");

            adapter.notifyDataSetChanged();
        }
        else {
            Toast.makeText(getApplicationContext(), "I'm not going to remind you that...", Toast.LENGTH_LONG).show();
        }
    }

    private void addFirebaseListener(ToDoItem item){
        myFirebaseRef.child("Notification" + item.getId()).setValue(item);

        myFirebaseRef.child("message").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot snapshot) {
                ToDoItems.add((ToDoItem)snapshot.getValue());
//                adapter.notifyDataSetChanged();

            }

            @Override public void onCancelled(FirebaseError error) { }

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

    public void onResume() {
        super.onResume();


        }


//    private void displayNotification(Notification notification, int id) {
//        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
//        notification.extras.putInt("Id",id);
//        notificationManager.notify(id, notification);
//    }
}

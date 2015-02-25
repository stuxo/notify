package com.stuxo.notify.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.stuxo.notify.controller.ToDoListAdapter;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    public ArrayList<ToDoItem> ToDoItems;
    private ListView toDoLV;
    private boolean isAuthed;
    private String userName;
    SQLiteDatabase db;
    public static final String PREFS_NAME = "NotifyPrefs";
    public static final String dbName = "notify";


    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (settings.getBoolean("isFirstLaunch", false)){

        }
        editor.putBoolean("isLoggedIn", false);


        //db = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

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

                addToDoItemToList();
            }
        });
    }



    private void addToDoItemToList() {
        if (txtNotificationDescription.getText().toString().trim().length() != 0) {
            ToDoItem newItem = new ToDoItem();
            newItem.setText(txtNotificationDescription.getText().toString().trim());

            if(ToDoItems != null){
                ToDoItems.add(newItem);
            }else{
                ToDoItems = new ArrayList<>();
                ToDoItems.add(newItem);
            }

            try {
                newItem.save();
            }catch(Exception e){
                Log.d("dbsave", e.toString());
            }

            txtNotificationDescription.setText("");

            adapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "I'm not going to remind you that...", Toast.LENGTH_LONG).show();
        }
    }

    //call from xml, need to set the object boolean too
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.toDoItemDoneCheckBox:
                if (checked) {

                }
        }
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

        getExistingItems();

    }

    private void getExistingItems(){
        List<ToDoItem> toDoItems = ToDoItem.listAll(ToDoItem.class);
        ToDoItems.addAll(toDoItems);
        //show today's "Done" items
        adapter.notifyDataSetChanged();

    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
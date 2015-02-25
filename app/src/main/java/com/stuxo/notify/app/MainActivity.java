package com.stuxo.notify.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.firebase.client.*;
import com.stuxo.notify.controller.ToDoListAdapter;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    public ArrayList<ToDoItem> ToDoItems;
    private ListView toDoLV;
    Firebase myFirebaseRef;
    Firebase toDoRef;
    Firebase userRef;
    private boolean isAuthed;
    private String userName;
    public static final String PREFS_NAME = "NotifyPrefs";

    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();

        if (settings.getBoolean("isFirstLaunch", false)){

        }
        editor.putBoolean("isLoggedIn", false);


        myFirebaseRef = new Firebase("https://blazing-heat-2528.firebaseio.com/");;
        AuthData authData = myFirebaseRef.getAuth();

        myFirebaseRef.authWithPassword("stuart.simmons0@gmail.com", "password",
                new Firebase.AuthResultHandler() {
                    @Override
                    public void onAuthenticated(AuthData authData) {
                        // Authentication just completed successfully :)
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("provider", authData.getProvider());
                        if(authData.getProviderData().containsKey("id")) {
                            map.put("provider_id", authData.getProviderData().get("id").toString());
                        }
                        if(authData.getProviderData().containsKey("displayName")) {
                            map.put("displayName", authData.getProviderData().get("displayName").toString());
                        }

                        myFirebaseRef.child("users").child(authData.getUid()).setValue(map);

                    }

                    @Override
                    public void onAuthenticationError(FirebaseError error) {
                        // Something went wrong :(
                    }
                });

        //handler is initialised, but auth never takes place. Need to handle user creation thru menu, also the key pairs for user

        //toDoRef = myFirebaseRef.child("users").child(userName).child("ToDoItems");

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
        //getExistingItems();
    }



    private void addToDoItemToList() {
        if (txtNotificationDescription.getText().toString().trim().length() != 0) {
            ToDoItem newItem = new ToDoItem(txtNotificationDescription.getText().toString());

            ToDoItems.add(newItem);

            toDoRef.setValue(ToDoItems);

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

        //getExistingItems();

    }

    private void getExistingItems(){
        toDoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChild) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                for (DataSnapshot item : dataSnapshot.getChildren()) {

                    ToDoItem todo = new ToDoItem(item.child("text").getValue(String.class));
                    todo.setId((item.child("id").getValue(int.class)));
                    todo.setIsComplete((item.child("isComplete").getValue(Boolean.class)));
                    ToDoItems.add(todo);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
            // ....
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        ToDoItems = null;
        if (myFirebaseRef != null){
            myFirebaseRef.unauth();
        }
        Toast.makeText(getApplicationContext(), "User logged out", Toast.LENGTH_LONG).show();
    }
}
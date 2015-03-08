package com.stuxo.notify.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.stuxo.notify.controller.ToDoListAdapter;
import com.stuxo.notify.listeners.SwipeableRecyclerViewTouchListener;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    private ArrayList<ToDoItem> ToDoItems = new ArrayList<>();

    private ListView toDoLV;
    private boolean isAuthed;
    private String userName;
    SQLiteDatabase db;
    public static final String dbName = "notify";

    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //db = this.openOrCreateDatabase(dbName, MODE_PRIVATE, null);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

        recList.setHasFixedSize(true);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recList.setLayoutManager(mLayoutManager);


        adapter = new ToDoListAdapter(ToDoItems);

        recList.setAdapter(adapter);

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recList,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    ToDoItems.get(position).delete();
                                    ToDoItems.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    ToDoItems.get(position).delete();
                                    ToDoItems.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                adapter.notifyDataSetChanged();
                            }
                        });

        recList.addOnItemTouchListener(swipeTouchListener);

        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch (item.getItemId()) {
                            case R.id.action_clear_all:
                                ToDoItem.deleteAll(ToDoItem.class);
                                ToDoItems.clear();
                                adapter.notifyDataSetChanged();
                                return true;
                            default:
                                return true;
                        }
                    }
                });

           // Inflate a menu to be displayed in the toolbar
        toolbar.inflateMenu(R.menu.menu_main);

        btnCreateNotification = (Button) findViewById(R.id.btnCreateReminder);
        txtNotificationDescription = (EditText) findViewById(R.id.txtReminderDesc);

        txtNotificationDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    btnCreateNotification.performClick();
                }
                return false;
            }
        });

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

        return builder
                .setSmallIcon(R.drawable.ic_action_done)
                .setContentIntent(pIntent)
                .setContentTitle(title)
                .setContentText(text)
                .addAction(R.drawable.ic_action_done, "Done", pIntent)
                .addAction(R.drawable.ic_action_time, "10min", pIntent)
                .addAction(R.drawable.ic_action_alarms, "1hr", pIntent)
                .build();
    }

    public void onResume() {
        super.onResume();

        getExistingItems();

    }

    private void getExistingItems(){

        try {
            List<ToDoItem> toDoItems = ToDoItem.listAll(ToDoItem.class);
            if (ToDoItems.size() == 0) {
                ToDoItems.addAll(toDoItems);
                adapter.notifyDataSetChanged();
            }
            //show today's "Done" items, settings menu
        }
        catch (SQLiteException e){
            if (e.getMessage().toString().contains("no such table")){

            }
        }
    }

}
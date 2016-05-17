package com.stuxo.notify.ui;

import android.app.Notification;
import android.app.PendingIntent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.stuxo.ToDoItemModel;
import com.stuxo.notify.app.R;
import com.stuxo.notify.controller.ToDoListAdapter;
import com.stuxo.notify.data.DatabaseOpenHelper;
import com.stuxo.notify.listeners.SwipeableRecyclerViewTouchListener;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private Button btnCreateNotification;
    private EditText txtNotificationDescription;
    private ArrayList<ToDoItem> toDoItems = new ArrayList<>();

    private ToDoListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recList.setLayoutManager(mLayoutManager);

        adapter = new ToDoListAdapter(toDoItems);
        recList.setAdapter(adapter);

        final SQLiteDatabase db = DatabaseOpenHelper.getInstance(this).getWritableDatabase();

        SwipeableRecyclerViewTouchListener swipeTouchListener =
                new SwipeableRecyclerViewTouchListener(recList,
                        new SwipeableRecyclerViewTouchListener.SwipeListener() {
                            @Override
                            public boolean canSwipe(int position) {
                                return true;
                            }

                            Cursor toDoCursor;

                            @Override
                            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    toDoCursor = db.rawQuery(ToDoItemModel.DELETE_BY_TEXT, new String[]{String.valueOf(toDoItems.get(position).getText())});
                                    toDoItems.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                toDoCursor.close();
                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    toDoCursor = db.rawQuery(ToDoItemModel.DELETE_BY_TEXT, new String[]{String.valueOf(toDoItems.get(position).getText())});
                                    toDoItems.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                                toDoCursor.close();
                                adapter.notifyDataSetChanged();
                            }
                        });

        recList.addOnItemTouchListener(swipeTouchListener);

        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setOnMenuItemClickListener(
                    new Toolbar.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.action_clear_all:
                                    db.rawQuery(ToDoItemModel.DELETE_ALL, new String[0]);
                                    toDoItems.clear();
                                    adapter.notifyDataSetChanged();
                                    return true;
                                default:
                                    return true;
                            }
                        }
                    });
            toolbar.inflateMenu(R.menu.menu_main);
        }

        // Inflate a menu to be displayed in the toolbar

        btnCreateNotification = (Button) findViewById(R.id.btnCreateReminder);
        txtNotificationDescription = (EditText) findViewById(R.id.txtReminderDesc);

        txtNotificationDescription.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    btnCreateNotification.performClick();
                    txtNotificationDescription.setSelection(0);
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

            if (toDoItems != null) {
                toDoItems.add(newItem);
            } else {
                toDoItems = new ArrayList<>();
                toDoItems.add(newItem);
            }

            SQLiteDatabase db = DatabaseOpenHelper.getInstance(this).getWritableDatabase();
            db.insert(ToDoItemModel.TABLE_NAME, null, new ToDoItemModel.ToDoItemMarshal<>()
                    .text(newItem.getText())
                    .isComplete(0)
                    .asContentValues());

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

    private void getExistingItems() {

        try {
            SQLiteDatabase db = DatabaseOpenHelper.getInstance(this).getWritableDatabase();
            Cursor result = db.rawQuery(ToDoItemModel.SELECT_ALL, new String[0]);

            List<ToDoItem> toDoItems = new ArrayList<>();
            while (result.moveToNext()) {
                toDoItems.add(new ToDoItem(result.getInt(2) == 1, result.getString(1), result.getInt(0)));
            }
            if (this.toDoItems.size() == 0) {
                this.toDoItems.addAll(toDoItems);
                adapter.notifyDataSetChanged();
            }
            //show today's "Done" items, settings menu
        } catch (SQLiteException e) {
            if (e.getMessage().toString().contains("no such table")) {

            }
        } catch (NullPointerException e) {
            //Database does not exist, this is first run
        }
    }
}
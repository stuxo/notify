package com.stuxo.notify.controller;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import com.stuxo.notify.app.R;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;

/**
 * Created by stu on 13/11/14.
 */
@SuppressWarnings("ALL")
public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder>{

    private static ArrayList<ToDoItem> items;

    public ToDoListAdapter(ArrayList<ToDoItem> results) {
        items = results;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.to_do_item, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        ToDoItem item = items.get(i);
        viewHolder.desc.setText(item.getText());
        viewHolder.done.setChecked(item.getIsComplete());
        viewHolder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToDoItem item = items.get(i);
                CheckBox done = (CheckBox) v.findViewById(R.id.toDoItemDoneCheckBox);
                item.setIsComplete(done.isChecked());

            }
        });
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView desc;
        CheckBox done;

        public ViewHolder(View v) {
            super(v);
            desc = (TextView) v.findViewById(R.id.toDoItemDescEditText);
            done = (CheckBox) v.findViewById(R.id.toDoItemDoneCheckBox);
        }
    }
}

package com.stuxo.notify.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import com.stuxo.notify.app.R;
import com.stuxo.notify.model.ToDoItem;

import java.util.ArrayList;

/**
 * Created by stu on 13/11/14.
 */
public class ToDoListAdapter extends BaseAdapter {

    private static ArrayList<ToDoItem> items;

    private LayoutInflater mInflater;

    public ToDoListAdapter(Context context, ArrayList<ToDoItem> results) {
        items = results;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return items.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return items.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.to_do_item, parent, false);
            holder = new ViewHolder();
            holder.desc = (TextView) convertView.findViewById(R.id.toDoItemDescEditText);
            holder.done = (CheckBox) convertView.findViewById(R.id.toDoItemDoneCheckBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.desc.setText(items.get(position).getText());
        holder.done.setChecked(items.get(position).getIsComplete());

        return convertView;
    }

    static class ViewHolder {
        TextView desc;
        CheckBox done;
    }
}

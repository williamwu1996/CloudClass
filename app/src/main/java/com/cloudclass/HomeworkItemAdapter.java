package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class HomeworkItemAdapter extends ArrayAdapter<HomeworkItem> {
    private int resourceId;

    public HomeworkItemAdapter(Context context, int textViewResourceId, List<HomeworkItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        HomeworkItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);


        TextView title = (TextView)view.findViewById(R.id.homework_item_title);
        TextView id = (TextView)view.findViewById(R.id.homework_item_id);

        title.setText(cm.getTitle());
        id.setText(cm.getId());

        return view;
    }
}

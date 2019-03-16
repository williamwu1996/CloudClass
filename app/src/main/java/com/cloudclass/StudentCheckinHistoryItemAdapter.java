package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StudentCheckinHistoryItemAdapter extends ArrayAdapter<StudentCheckinHistoryItem> {

    private int resourceId;

    public StudentCheckinHistoryItemAdapter(Context context,int textViewResourceId, List<StudentCheckinHistoryItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StudentCheckinHistoryItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);


        TextView time = (TextView)view.findViewById(R.id.student_checkin_history_time);
        TextView status = (TextView)view.findViewById(R.id.student_checkin_history_status);
        TextView id = (TextView)view.findViewById(R.id.student_checkin_history_id);


        status.setText(cm.getStatus());
        time.setText(cm.getTime());
        id.setText(cm.getId());

        return view;
    }

}

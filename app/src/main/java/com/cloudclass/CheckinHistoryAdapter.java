package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CheckinHistoryAdapter extends ArrayAdapter<CheckinHistoryItem> {
    private int resourceId;

    public CheckinHistoryAdapter(Context context, int textViewResourceId, List<CheckinHistoryItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CheckinHistoryItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);


        TextView name = (TextView)view.findViewById(R.id.checkin_history_name);
        TextView status = (TextView)view.findViewById(R.id.checkin_history_status);

        name.setText(cm.getName());
        status.setText(cm.getStatus());

        return view;
    }

}

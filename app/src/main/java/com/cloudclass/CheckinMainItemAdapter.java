package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CheckinMainItemAdapter extends ArrayAdapter<CheckinMainItem> {

    private int resourceId;

    public CheckinMainItemAdapter(Context context, int textViewResourceId, List<CheckinMainItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        CheckinMainItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);


        TextView chid = (TextView)view.findViewById(R.id.checkin_main_item_chid);
        TextView time = (TextView)view.findViewById(R.id.checkin_main_item_time);

        chid.setText(cm.getChid());
        time.setText(cm.getTime());

        return view;
    }
}

package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StudentResourceItemAdapter extends ArrayAdapter<StudentResourceItem> {
    private int resourceId;

    public StudentResourceItemAdapter(Context context, int textViewResourceId, List<StudentResourceItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StudentResourceItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        ImageView resourceImage = (ImageView) view.findViewById(R.id.student_resource_item_img);
        TextView resourcename = (TextView)view.findViewById(R.id.student_resource_item_name);
        TextView opentime = (TextView)view.findViewById(R.id.student_resource_item_time);

        resourcename.setText(cm.getName());
        resourceImage.setImageResource(cm.getImageid());
        opentime.setText(cm.getOpentime());

        return view;
    }
}

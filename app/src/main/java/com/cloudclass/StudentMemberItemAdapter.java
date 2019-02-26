package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class StudentMemberItemAdapter extends ArrayAdapter<StudentMemberItem> {

    private int resourceId;

    public StudentMemberItemAdapter(Context context, int textViewResourceId, List<StudentMemberItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        StudentMemberItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        ImageView resourceImage = (ImageView) view.findViewById(R.id.student_main_members_item_img);
        TextView name = (TextView)view.findViewById(R.id.student_main_members_name);

        name.setText(cm.getName());
        resourceImage.setImageResource(cm.getImageid());

        return view;
    }
}

package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class ClassAdapter extends ArrayAdapter<ClassMain> {

    private int resourceId;

    public ClassAdapter(Context context, int textViewResourceId, List<ClassMain> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ClassMain cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        ImageView classImage = (ImageView) view.findViewById(R.id.class_image);
        TextView classname = (TextView)view.findViewById(R.id.class_name);
        TextView course = (TextView)view.findViewById(R.id.course);
        TextView teacher = (TextView)view.findViewById(R.id.teacher);
        TextView code = (TextView)view.findViewById(R.id.code);

        classname.setText(cm.getClassname());
        classImage.setImageResource(cm.getImageid());
        course.setText(cm.getCourse());
        teacher.setText(cm.getTeacher());
        code.setText(cm.getCode());

        return view;
    }
}

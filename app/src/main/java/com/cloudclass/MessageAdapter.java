package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MessageAdapter extends ArrayAdapter<MessageMain> {

    private int resourceId;

    public MessageAdapter(Context context, int textViewResourceId, List<MessageMain> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        MessageMain cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        ImageView personImage = (ImageView) view.findViewById(R.id.message_person_image);
        TextView fromperson = (TextView)view.findViewById(R.id.message_personname);
        TextView fromclass = (TextView)view.findViewById(R.id.message_fromclass);
        TextView firstcontent = (TextView)view.findViewById(R.id.message_firstcontent);
        TextView time = (TextView)view.findViewById(R.id.message_time);


        personImage.setImageResource(cm.getImageid());
        fromperson.setText(cm.getName());
        fromclass.setText(cm.getFromclass());
        firstcontent.setText(cm.getFirstcontent());
        time.setText(cm.getTime());


        return view;
    }
}

package com.cloudclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ResourceItemAdapter extends ArrayAdapter<ResourceItem> {

    private int resourceId;

    public ResourceItemAdapter(Context context, int textViewResourceId, List<ResourceItem> objs){
        super(context, textViewResourceId, objs);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ResourceItem cm = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        TextView title = (TextView)view.findViewById(R.id.resource_item_title);
        TextView filename = (TextView)view.findViewById(R.id.resource_item_filename);

        title.setText(cm.getTitle());
        filename.setText(cm.getFilename());

        return view;
    }
}

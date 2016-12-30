package com.example.homework8;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by revati on 16-04-2016.
 */
public class MessageListAdapter extends ArrayAdapter<Message>
{
    List<Message> mData;
    Context mContext;
    int mResource;
    String msender;
    ImageButton delete;

    MessageListAdapter(Context context, int resource, List<Message> objects,String sender)
    {

        super(context, resource, objects);
        mContext=context;
        mResource=resource;
        mData=objects;
        msender=sender;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Message msg = mData.get(position);

        TextView tv1,tv2,tv3;
        tv1 = (TextView) convertView.findViewById(R.id.tv_cname);
        tv2 = (TextView) convertView.findViewById(R.id.tv_message);
        tv3 = (TextView) convertView.findViewById(R.id.tv_date);
        delete = (ImageButton) convertView.findViewById(R.id.imageButton_delete);

        tv1.setText(msg.getSender());
        tv2.setText(msg.getMessage_text());
        tv3.setText(msg.getTime_Stamp());
        if(msg.getSender().equals(msender))
        {
            delete.setTag(msg);
            delete.setVisibility(View.VISIBLE);
            convertView.setBackgroundColor(Color.GRAY);
        }
        else
        {
            delete.setVisibility(View.INVISIBLE);
            convertView.setBackgroundColor(Color.WHITE);
        }
        convertView.setBottom(25);

        return convertView;
    }

}

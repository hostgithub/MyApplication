package com.example.sc.myapplication.ui.mainpage;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.sc.myapplication.R;

import java.util.ArrayList;

/**
 * Created by wangjiawei on 2017-9-19.
 */

public class SubAdapter extends android.widget.BaseAdapter {

    private ArrayList<String> sublist;
    private Context context;

    public SubAdapter(ArrayList<String> sublist,Context context){
        this.sublist=sublist;
        this.context=context;
    }
    @Override
    public int getCount() {
        return sublist.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder vh;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_sublist, null);
            vh = new MyViewHolder(convertView);
            convertView.setTag(vh);
        } else {
            vh = (MyViewHolder) convertView.getTag();
        }
        vh.tv_test.setText(sublist.get(position));
        return convertView;
    }

    class MyViewHolder {
        View itemView;
        TextView tv_test;
        public MyViewHolder(View itemView) {
            this.itemView = itemView;
            tv_test = (TextView) itemView.findViewById(R.id.tv_test);
        }
    }
}

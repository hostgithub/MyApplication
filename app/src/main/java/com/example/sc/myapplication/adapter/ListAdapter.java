package com.example.sc.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sc.myapplication.R;

/**
 * Created by wangjiawei on 2017-11-28.
 */

public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private int mLastPosition = -1;
    private MyClickListener mListener;
    private MyClickListener2 mListener2;
    private MyClickListener3 mListener3;

    public ListAdapter(Context context, MyClickListener mListener,
                       MyClickListener2 mListener2, MyClickListener3 mListener3) {
        this.mContext = context;
        this.mListener = mListener;
        this.mListener2 = mListener2;
        this.mListener3 = mListener3;
    }

    @Override
    public int getCount() {
        return 100;
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
        Holder holder = new Holder();
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.list_item_sublist, null);
            holder.textView = (TextView) convertView
                    .findViewById(R.id.textView);
            holder.UEFAView = (ImageView) convertView
                    .findViewById(R.id.image_uefa);
            holder.mascotView = (ImageView) convertView
                    .findViewById(R.id.image_mascot);
            holder.hint = convertView.findViewById(R.id.hint_image);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        holder.textView.setText("Hello,It is " + position);
        if (position == mLastPosition) {
            holder.hint.setVisibility(View.VISIBLE);
        } else {
            holder.hint.setVisibility(View.GONE);
        }

        holder.UEFAView.setOnClickListener(mListener);
        holder.UEFAView.setTag(position);
        //
        holder.mascotView.setOnClickListener(mListener2);
        holder.mascotView.setTag(position);

        holder.textView.setOnClickListener(mListener3);
        holder.textView.setTag(position);

        return convertView;
    }

    public static abstract class MyClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            myOnClick((Integer) v.getTag(), v);
        }

        public abstract void myOnClick(int position, View v);
    }

    public static abstract class MyClickListener2 implements View.OnClickListener {
        public void onClick(View v) {
            myOnClick2((Integer) v.getTag(), v);
        }

        public abstract void myOnClick2(int position, View v);
    }

    public static abstract class MyClickListener3 implements View.OnClickListener {
        public void onClick(View v) {
            myOnClick3((Integer) v.getTag(), v);
        }
        public abstract void myOnClick3(int position, View v);
    }

    class Holder {
        TextView textView;
        ImageView UEFAView;
        ImageView mascotView;
        View hint;
    }

    public void changeImageVisable(int position) {
        if (position != mLastPosition) {
            mLastPosition = position;
        } else {
            mLastPosition = -1;
        }
        notifyDataSetChanged();
    }
}

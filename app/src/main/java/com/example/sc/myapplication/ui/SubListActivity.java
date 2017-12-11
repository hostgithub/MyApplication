package com.example.sc.myapplication.ui;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sc.myapplication.R;
import com.example.sc.myapplication.adapter.ListAdapter;
import com.llf.basemodel.base.BaseActivity;

public class SubListActivity extends BaseActivity {

    private ListView mListView;
    private ListAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sub_list;
    }

    @Override
    protected void initView() {
        mListView = (ListView) findViewById(R.id.list);
        mAdapter = new ListAdapter(this, listener, listener2, listener3);
        mListView.setAdapter(mAdapter);
    }

    ListAdapter.MyClickListener listener = new ListAdapter.MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            Toast.makeText(SubListActivity.this, "修改", Toast.LENGTH_SHORT).show();
        }
    };

    ListAdapter.MyClickListener2 listener2 = new ListAdapter.MyClickListener2() {
        public void myOnClick2(int position, View v) {

            Toast.makeText(SubListActivity.this, "重置",  Toast.LENGTH_SHORT).show();
        }
    };

    ListAdapter.MyClickListener3 listener3 = new ListAdapter.MyClickListener3() {

        @Override
        public void myOnClick3(int position, View v) {
            mAdapter.changeImageVisable(position);
        }
    };
}

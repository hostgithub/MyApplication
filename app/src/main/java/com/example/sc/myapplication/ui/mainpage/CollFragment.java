package com.example.sc.myapplication.ui.mainpage;


import android.support.v4.app.Fragment;

import com.example.sc.myapplication.R;
import com.llf.basemodel.base.BaseFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class CollFragment extends BaseFragment {

    public static CollFragment getInstance() {
        CollFragment collFragment = new CollFragment();
        return collFragment;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void lazyFetchData() {

    }

}

package com.example.sc.myapplication.ui.girl;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.sc.myapplication.R;
import com.example.sc.myapplication.entity.JcodeEntity;
import com.example.sc.myapplication.ui.girl.adapter.GirlAdapter;
import com.example.sc.myapplication.ui.girl.contract.GirlContract;
import com.example.sc.myapplication.ui.girl.presenter.GirlPresenter;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonactivity.WebViewActivity;
import com.llf.basemodel.recycleview.DefaultItemDecoration;
import com.llf.basemodel.recycleview.EndLessOnScrollListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by llf on 2017/3/15.
 * 发现
 */

public class GirlFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener, GirlContract.View {
    public static GirlFragment getInstance() {
        GirlFragment girlFragment = new GirlFragment();//单例模式
        return girlFragment;
    }

    @Bind(R.id.recyclerView) //列表控件
    RecyclerView mRecyclerView;
    @Bind(R.id.refreshLayout)//刷新控件
    SwipeRefreshLayout mRefreshLayout;

    private GirlAdapter mAdapter; //适配器
    private List<JcodeEntity> jcodes = new ArrayList<>();//数据源 集合
    private GirlPresenter mPresenter;//MVP 中的P
    private int pageIndex = 1;//第一页

    private static final String HOST = "http://www.jcodecraeer.com";//baseUrl

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_girl;
    }//重写base方法   初始化布局

    @Override
    protected void initView() {//初始化
        mPresenter = new GirlPresenter(this);
        //设置刷新时 进度条不同颜色 变换
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        //刷新 监听
        mRefreshLayout.setOnRefreshListener(this);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DefaultItemDecoration(getActivity()));
        mAdapter = new GirlAdapter(jcodes, getActivity());
        //条目点击事件
        mAdapter.setOnItemClickLitener(new GirlAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mPresenter.addRecord(getActivity(), jcodes.get(position));
                WebViewActivity.lanuch(getActivity(), HOST + jcodes.get(position).getDetailUrl());
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addFooterView(R.layout.layout_footer);//添加脚布局
        mRecyclerView.addOnScrollListener(new EndLessOnScrollListener(manager) {//滑动到底部 加载更多
            @Override
            public void onLoadMore() {
                pageIndex++;
                mAdapter.setFooterVisible(View.VISIBLE);
                mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
            }
        });
    }

    @OnClick(R.id.floatBtn)
    public void onViewClicked() {
        mRecyclerView.smoothScrollToPosition(0);
    }//点击floatbutton 回到顶部

    @Override
    protected void lazyFetchData() {//预加载
        mRefreshLayout.setRefreshing(true);
        mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
    }

    @Override
    public void onRefresh() {//刷新 的回调
        pageIndex = 1;
        jcodes.clear();
        mPresenter.loadData("http://www.jcodecraeer.com/plus/list.php?tid=18&TotalResult=1801&PageNo=" + pageIndex);
    }

    @Override
    public void showLoading() {
        startProgressDialog();
    }

    @Override
    public void stopLoading() {
        stopProgressDialog();
    }

    @Override
    public void showErrorTip(String msg) {
        showErrorHint(msg);
        mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void returnData(List<JcodeEntity> datas) {
        if (pageIndex == 1) {
            mAdapter.replaceAll(datas);
            mRefreshLayout.setRefreshing(false);
        } else {
            mAdapter.addAll(datas);
            mAdapter.setFooterVisible(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jcodes.clear();
        jcodes = null;
    }
}

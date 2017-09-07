package com.example.sc.myapplication;

import com.example.sc.myapplication.entity.ApplicationEntity;
import com.llf.basemodel.utils.DownloadUtil;
import com.llf.basemodel.utils.JsonUtils;
import com.llf.basemodel.utils.LogUtil;
import com.llf.basemodel.utils.OkHttpUtils;

/**
 * Created by wangjiawei on 2017/7/18.
 */

public class MainPresenter implements MainContract.Presenter {
    private MainContract.View mView;

    public MainPresenter(MainContract.View view) {
        this.mView = view;
    }

    @Override
    public void start() {

    }

    @Override
    public void checkUpdate(String url) {
        OkHttpUtils.get(url, new OkHttpUtils.ResultCallback<String>() {
            @Override
            public void onSuccess(String response) {
                LogUtil.d("应用更新:" + response);
                ApplicationEntity entity = JsonUtils.deserialize(response, ApplicationEntity.class);
                mView.retureUpdateResult(entity);
            }

            @Override
            public void onFailure(Exception e) {
                mView.retureResult(e.getMessage());
            }
        });
    }

    @Override
    public void update(ApplicationEntity entity) {
        DownloadUtil.downloadApk(App.instance, entity.getInstall_url(), entity.getName(), entity.getChangelog(), "xiuqu.apk");
    }
}

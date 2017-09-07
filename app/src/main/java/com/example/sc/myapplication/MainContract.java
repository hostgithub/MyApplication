package com.example.sc.myapplication;

import com.example.sc.myapplication.entity.ApplicationEntity;
import com.llf.basemodel.base.BasePresenter;
import com.llf.basemodel.base.BaseView;

/**
 * Created by wangjiawei on 2017/7/18.
 */

public interface MainContract {
    interface View extends BaseView {
        void retureResult(String result);
        void retureUpdateResult(ApplicationEntity entity);
    }

    interface Presenter extends BasePresenter {
        void checkUpdate(String url);
        void update(ApplicationEntity entity);
    }
}

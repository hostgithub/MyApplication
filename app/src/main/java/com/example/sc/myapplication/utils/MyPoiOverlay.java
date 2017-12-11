package com.example.sc.myapplication.utils;

import com.baidu.mapapi.map.BaiduMap;

/**
 * Created by wangjiawei on 2017-11-30.
 */

public class MyPoiOverlay extends PoiOverlay {

    public MyPoiOverlay(BaiduMap baiduMap) {
        super(baiduMap);
    }

    @Override

    public boolean onPoiClick(int index) {
        super.onPoiClick(index);
        return true;
    }
}

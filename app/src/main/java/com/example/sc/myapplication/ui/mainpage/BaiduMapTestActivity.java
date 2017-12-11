package com.example.sc.myapplication.ui.mainpage;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiNearbySearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.baidu.mapapi.search.poi.PoiSortType;
import com.example.sc.myapplication.R;
import com.example.sc.myapplication.utils.MyPoiOverlay;
import com.example.sc.myapplication.utils.PoiOverlay;
import com.llf.basemodel.base.BaseActivity;

import butterknife.Bind;

public class BaiduMapTestActivity extends BaseActivity implements OnGetPoiSearchResultListener {

    @Bind(R.id.bmapView)
    MapView mMapView ;

    // 定位相关
    LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    BaiduMap mBaiduMap;
    boolean isFirstLoc = true; // 是否首次定位
    @Bind(R.id.et_text)
     EditText et_text;
    @Bind(R.id.bt_button)
    Button bt_button;

    private PoiSearch mPoiSearch = null;
    LatLng center = null;
    int radius = 50000;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_baidu_map_test;
    }

    @Override
    protected void initView() {
        mBaiduMap = mMapView.getMap();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();


        // 初始化搜索模块，注册搜索事件监听
        mPoiSearch = PoiSearch.newInstance();
        mPoiSearch.setOnGetPoiSearchResultListener(this);

        //点击搜索
        bt_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //关闭输入框
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                PoiNearbySearchOption nearbySearchOption = new PoiNearbySearchOption().keyword(et_text.getText()
                        .toString()).sortType(PoiSortType.distance_from_near_to_far).location(center)
                        .radius(radius).pageNum(10);//显示数据个数
                mPoiSearch.searchNearby(nearbySearchOption);

            }
        });
    }

    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }

            center=new LatLng(location.getLatitude(), location.getLongitude());

            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        super.onDestroy();
    }


    /**
     * 获取POI详情搜索结果，得到searchPoiDetail返回的搜索结果
     * @param
     */
    @Override
    public void onGetPoiDetailResult(PoiDetailResult arg0) {
        if (arg0.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(BaiduMapTestActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(BaiduMapTestActivity.this, arg0.getName() + ": " + arg0.getAddress(), Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

    }

    /**
     * 获取POI搜索结果，包括searchInCity，searchNearby，searchInBound返回的搜索结果
     * @param
     */
    @Override
    public void onGetPoiResult(PoiResult arg0) {
        if (arg0 == null || arg0.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(BaiduMapTestActivity.this, "未找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (arg0.error == SearchResult.ERRORNO.NO_ERROR) {
            mBaiduMap.clear();
            PoiOverlay overlay = new MyPoiOverlay(mBaiduMap); //自定义 点击事件 和显示
//            PoiOverlay overlay = new PoiOverlay(mBaiduMap);
            mBaiduMap.setOnMarkerClickListener(overlay);
            overlay.setData(arg0);
            overlay.addToMap();
            overlay.zoomToSpan();

            //showNearbyArea(center, radius);

            return;
        }

    }
}

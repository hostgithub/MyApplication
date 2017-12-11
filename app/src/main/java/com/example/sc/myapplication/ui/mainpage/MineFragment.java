package com.example.sc.myapplication.ui.mainpage;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sc.myapplication.R;
import com.example.sc.myapplication.ui.BaiduMapActivity;
import com.example.sc.myapplication.ui.LoginTestActivity;
import com.example.sc.myapplication.ui.record.RecordActivity;
import com.example.sc.myapplication.ui.SubListActivity;
import com.example.sc.myapplication.widget.GlideImageLoader;
import com.llf.basemodel.base.BaseFragment;
import com.llf.basemodel.commonactivity.TakePhotoActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

/**
 * A simple {@link Fragment} subclass.
 */
public class MineFragment extends BaseFragment {

    @Bind(R.id.banner)
    Banner banner;
    @Bind(R.id.tv_click)
    TextView tv_click;
    @Bind(R.id.tv_pic)
    TextView tv_pic;
    @Bind(R.id.tv_map)
    TextView tv_map;
    @Bind(R.id.tv_login_test)
    TextView tv_login_test;
    @Bind(R.id.tv_map_poi)
    TextView tv_map_poi;
    @Bind(R.id.tv_record)
    TextView tv_record;

    public static final String[] BANNER_IMGS =
            {
                    "http://upload.jianshu.io/admin_banners/app_images/2542/5b04b8ccb01432425cb13dab1aed399da607b454.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2547/ae57560a074ccbf0213c1ac004e6c8e27e9e4094.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2383/126ffdb2dacadba08292bc2bf326cbeddb9e13f6.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2295/bf91c806e3b88e6f38f58c1f20367605f17cd797.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2474/82506437d78663c5ca0bee823bd4b7bed04b9b96.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2523/7207d4150a150f14efbdfc4411bf05b0199ceffa.jpg",
                    "http://upload.jianshu.io/admin_banners/app_images/2540/9a7011f88c7fa0f471824111011ec8ffd08860f2.jpg"
            };
    public static final String[] BANNER_TITLES =
            {
                    "AAAAAAAAAAAAAA",
                    "BBBBBBBBBBBBBB",
                    "CCCCCCCCCCCCCC",
                    "DDDDDDDDDDDDDD",
                    "EEEEEEEEEEEEEE",
                    "FFFFFFFFFFFFFF",
                    "GGGGGGGGGGGGGG"
            };
    public static final String[] BANNER_URL =
            {
                    "http://www.jianshu.com/p/69a6da71e499",
                    "http://www.jianshu.com/p/9f0f4597cf3c",
                    "http://www.jianshu.com/p/61907890ab0d",
                    "http://www.jianshu.com/p/9348cbf32aad",
                    "http://www.jianshu.com/p/087bd0713477",
                    "http://www.jianshu.com/p/7c0a80c6e8a8",
                    "http://www.jianshu.com/p/fef9771435a0"

            };

    private List<String> banner_img;
    private List<String> banner_url;
    private List<String> banner_titles;

    public static MineFragment getInstance() {
        MineFragment mineFragment = new MineFragment();
        return mineFragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void initView() {

        banner_img = new ArrayList<>();
        banner_url = new ArrayList<>();
        banner_titles = new ArrayList<>();
        banner_img = Arrays.asList(BANNER_IMGS);
        banner_url = Arrays.asList(BANNER_URL);
        banner_titles = Arrays.asList(BANNER_TITLES);

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(banner_img);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(banner_titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(getActivity(),"点击了第"+position+"个位置!",Toast.LENGTH_SHORT).show();
            }
        });


        tv_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), SubListActivity.class));
            }
        });

        tv_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), TakePhotoActivity.class));
            }
        });
        tv_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), BaiduMapActivity.class));
            }
        });
        tv_login_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), LoginTestActivity.class));
            }
        });
        tv_map_poi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), BaiduMapTestActivity.class));
            }
        });
        tv_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().startActivity(new Intent(getActivity(), RecordActivity.class));
            }
        });
    }

    @Override
    protected void lazyFetchData() {

    }
}

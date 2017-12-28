package com.example.sc.myapplication.ui.jpush;

import android.content.Intent;
import android.view.View;

import com.example.sc.myapplication.R;
import com.llf.basemodel.base.BaseActivity;

import cn.jpush.sms.SMSSDK;

public class JpushActivity extends BaseActivity implements View.OnClickListener {



    @Override
    protected int getLayoutId() {
        return R.layout.activity_jpush;
    }

    @Override
    protected void initView() {

        SMSSDK.getInstance().setIntervalTime(30*1000);
        findViewById(R.id.sms_btn).setOnClickListener(this);
        findViewById(R.id.voice_btn).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sms_btn:
                startActivity(new Intent(this,MainSmsActivity.class));
                break;
            case R.id.voice_btn:
                startActivity(new Intent(this,MainVoiceActivity.class));
                break;
        }
    }
}

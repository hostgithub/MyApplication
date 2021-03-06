package com.example.sc.myapplication.ui.jpush;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.sc.myapplication.R;

import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.sms.SMSSDK;
import cn.jpush.sms.listener.SmscheckListener;
import cn.jpush.sms.listener.SmscodeListener;

public class MainVoiceActivity extends Activity {
    private static final String TAG = "MainVoiceActivity";
    private EditText editText;
    private Button btn;
    private EditText codeText;
    private Button signBtn;
    private TimerTask timerTask;
    private Timer timer;
    private int timess;
    private ProgressDialog progressDialog;
    private RadioButton radioChinese;
    private RadioButton radioEnglish;
    private RadioButton radioChineseEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jpush_sms_voice);
        progressDialog = new ProgressDialog(this);
        editText = (EditText) findViewById(R.id.edt_code);
        codeText = (EditText) findViewById(R.id.edt_write_code);
        btn = (Button) findViewById(R.id.btn);
        signBtn = (Button) findViewById(R.id.btn_sign);

        radioChinese = (RadioButton) findViewById(R.id.radio_chinese);
        radioEnglish = (RadioButton) findViewById(R.id.radio_english);
        radioChineseEnglish = (RadioButton) findViewById(R.id.radio_chinese_english);
        //获取验证码
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = editText.getText().toString();
                if(TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(MainVoiceActivity.this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                int lang = 0;
                if(radioEnglish.isChecked()){
                    lang = 1;
                }else if(radioChineseEnglish.isChecked()){
                    lang = 2;
                }
                Log.e(TAG,"lang:"+lang);
                btn.setClickable(false);
                //开始倒计时
                startTimer();
               SMSSDK.getInstance().getVoiceCodeAsyn(phoneNum, lang,new SmscodeListener() {
                    @Override
                    public void getCodeSuccess(final String uuid) {
                                Toast.makeText(MainVoiceActivity.this,uuid,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void getCodeFail(int errCode, final String errmsg) {
                                //失败后停止计时
                                stopTimer();
                                Toast.makeText(MainVoiceActivity.this,errmsg,Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        //开始验证
        signBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = codeText.getText().toString();
                String phoneNum = editText.getText().toString();
                if(TextUtils.isEmpty(code)){
                    Toast.makeText(MainVoiceActivity.this,"请输入验证码",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(phoneNum)){
                    Toast.makeText(MainVoiceActivity.this,"请输入手机号码",Toast.LENGTH_SHORT).show();
                    return;
                }
                progressDialog.setTitle("正在验证...");
                progressDialog.show();
                SMSSDK.getInstance().checkSmsCodeAsyn(phoneNum, code, new SmscheckListener() {
                    @Override
                    public void checkCodeSuccess(final String code) {
                                if(progressDialog!=null&&progressDialog.isShowing()){
                                    progressDialog.dismiss();
                                }
                                Toast.makeText(MainVoiceActivity.this,code,Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void checkCodeFail(int errCode, final String errmsg) {
                                    if(progressDialog!=null&&progressDialog.isShowing()){
                                        progressDialog.dismiss();
                                    }
                                    Toast.makeText(MainVoiceActivity.this,errmsg,Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }


    private void startTimer(){
        timess = (int) (SMSSDK.getInstance().getIntervalTime()/1000);
        btn.setText(timess+"s");
        if(timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            timess--;
                            if(timess<=0){
                                stopTimer();
                                return;
                            }
                            btn.setText(timess+"s");
                        }
                    });
                }
            };
        }
        if(timer==null){
            timer = new Timer();
        }
        timer.schedule(timerTask, 1000, 1000);
    }
    private void stopTimer(){
        if(timer!=null){
            timer.cancel();
            timer=null;
        }
        if(timerTask!=null){
            timerTask.cancel();
            timerTask=null;
        }
        btn.setText("重新获取");
        btn.setClickable(true);
    }
}

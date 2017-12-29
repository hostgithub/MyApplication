package com.example.sc.myapplication.ui.record;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.example.sc.myapplication.R;
import com.llf.basemodel.base.BaseActivity;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;

public class RecordActivity extends BaseActivity {

    @Bind(R.id.main_btn_record_sound)
     Button mBtnRecordAudio;
    @Bind(R.id.main_btn_play_sound)
     Button mBtnPlayAudio;

    private ViewFlipper viewFlipper;
    private TextView txtContent;


    //数据源
    private List<String> dataList = Arrays.asList(
            "没有你的世界，就像没有暑假的八月 \n" +
                    "没有你的世界，就像没有笑容的圣诞老人",
            "我还可以默默地喜欢你多久、你还可以在我心里呆多久、、、是你自己走出去、还是我放了你、、、",
            "好好珍惜那个人带给你的伤痛，好好享受爱情留下的痕迹。多年以后，当激情归于平淡，当生活变得麻木，这种伤痛就成了得不到的奢侈品。回头想起那个人的时候，我会说，谢谢你，带给我伤痛。痛，是因为爱过!",
            "幸福不是获得多了，而是在乎少了;活得糊涂的人，容易幸福;活得清醒的人，容易烦恼。清醒的人看得太真切，一较真，生活中便烦恼遍地;糊涂的人，计较得少，虽然活得简单粗糙，却因此觅得了人生的大滋味。"
    );

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {

        initFlipper();

        mBtnRecordAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final RecordAudioDialogFragment fragment = RecordAudioDialogFragment.newInstance();
                fragment.show(getSupportFragmentManager(), RecordAudioDialogFragment.class.getSimpleName());
                fragment.setOnCancelListener(new RecordAudioDialogFragment.OnAudioCancelListener() {
                    @Override
                    public void onCancel() {
                        fragment.dismiss();
                    }
                });
            }
        });

        mBtnPlayAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordingItem recordingItem = new RecordingItem();
                SharedPreferences sharePreferences = getSharedPreferences("sp_name_audio", MODE_PRIVATE);
                final String filePath = sharePreferences.getString("audio_path", "");
                long elpased = sharePreferences.getLong("elpased", 0);
                recordingItem.setFilePath(filePath);
                recordingItem.setLength((int) elpased);
                PlaybackDialogFragment fragmentPlay = PlaybackDialogFragment.newInstance(recordingItem);
                fragmentPlay.show(getSupportFragmentManager(), PlaybackDialogFragment.class.getSimpleName());
            }
        });
    }

    //Android 垂直滚动广告条，仿淘宝头条垂直滚动展示最新消息

    public void initFlipper() {
        viewFlipper = (ViewFlipper) findViewById(R.id.vf);
        for (int i = 0; i < dataList.size(); i++) {
            View view = LayoutInflater.from(this).inflate(R.layout.childviewflipper, null);
            txtContent = (TextView) view.findViewById(R.id.txt_content);
            txtContent.setText(dataList.get(i).toString());
            final int finalI = i;
            txtContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(RecordActivity.this, dataList.get(finalI).toString(), Toast.LENGTH_SHORT).show();
                }
            });
            viewFlipper.addView(view);
        }
        viewFlipper.startFlipping();
    }

}

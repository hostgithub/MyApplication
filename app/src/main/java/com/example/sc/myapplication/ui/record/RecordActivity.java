package com.example.sc.myapplication.ui.record;

import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;

import com.example.sc.myapplication.R;
import com.llf.basemodel.base.BaseActivity;

import butterknife.Bind;

public class RecordActivity extends BaseActivity {

    @Bind(R.id.main_btn_record_sound)
     Button mBtnRecordAudio;
    @Bind(R.id.main_btn_play_sound)
     Button mBtnPlayAudio;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_record;
    }

    @Override
    protected void initView() {
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
}

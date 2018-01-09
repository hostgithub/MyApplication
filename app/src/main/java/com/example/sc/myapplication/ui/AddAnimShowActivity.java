package com.example.sc.myapplication.ui;

import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.sc.myapplication.R;
import com.llf.basemodel.base.BaseActivity;

public class AddAnimShowActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener{

    private View mPanelView;
    private View mCloseButton;
    private View mIdeaButton;
    private View mPhotoButton;
    private View mWeiboButton;
    private View mLbsButton;
    private View mReviewButton;
    private View mMoreButton;

    private Animation mButtonInAnimation;
    private Animation mButtonOutAnimation;
    private Animation mButtonScaleLargeAnimation;
    private Animation mButtonScaleSmallAnimation;
    private Animation mCloseRotateAnimation;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_anim_show;
    }

    @Override
    protected void initView() {

        View addButton = findViewById(R.id.add);
        mPanelView = findViewById(R.id.panel);
        mCloseButton = findViewById(R.id.close);
        mIdeaButton = findViewById(R.id.idea_btn);
        mPhotoButton = findViewById(R.id.photo_btn);
        mWeiboButton = findViewById(R.id.weibo_btn);
        mLbsButton = findViewById(R.id.lbs_btn);
        mReviewButton = findViewById(R.id.review_btn);
        mMoreButton = findViewById(R.id.more_btn);

        addButton.setOnClickListener(this);
        mCloseButton.setOnClickListener(this);

        mIdeaButton.setOnTouchListener(this);
        mPhotoButton.setOnTouchListener(this);
        mWeiboButton.setOnTouchListener(this);
        mLbsButton.setOnTouchListener(this);
        mReviewButton.setOnTouchListener(this);
        mMoreButton.setOnTouchListener(this);

        initAnimation();
    }


    // 初始化动画
    private void initAnimation() {
        mButtonInAnimation = AnimationUtils.loadAnimation(this, R.anim.button_in);
        mButtonOutAnimation = AnimationUtils.loadAnimation(this, R.anim.button_out);
        mButtonScaleLargeAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_large);
        mButtonScaleSmallAnimation = AnimationUtils.loadAnimation(this, R.anim.button_scale_to_small);
        mCloseRotateAnimation = AnimationUtils.loadAnimation(this, R.anim.close_rotate);

        mButtonOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 6个按钮的退出动画执行完毕后，将面板隐藏
                mPanelView.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    // 打开面板视图
    private void openPanelView() {
        mPanelView.setVisibility(View.VISIBLE);

        mIdeaButton.startAnimation(mButtonInAnimation);
        mPhotoButton.startAnimation(mButtonInAnimation);
        mWeiboButton.startAnimation(mButtonInAnimation);
        mLbsButton.startAnimation(mButtonInAnimation);
        mReviewButton.startAnimation(mButtonInAnimation);
        mMoreButton.startAnimation(mButtonInAnimation);

        mCloseButton.startAnimation(mCloseRotateAnimation);
    }

    // 关闭面板视图
    private void closePanelView() {
        // 给6个按钮添加退出动画
        mIdeaButton.startAnimation(mButtonOutAnimation);
        mPhotoButton.startAnimation(mButtonOutAnimation);
        mWeiboButton.startAnimation(mButtonOutAnimation);
        mLbsButton.startAnimation(mButtonOutAnimation);
        mReviewButton.startAnimation(mButtonOutAnimation);
        mMoreButton.startAnimation(mButtonOutAnimation);
    }

    @Override
    public void onBackPressed() {
        if (mPanelView.getVisibility() == View.VISIBLE) {
            closePanelView();
            return;
        }
        super.onBackPressed();
    }

    @Override
    public boolean onTouch(final View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下，按钮执行放大动画
                v.startAnimation(mButtonScaleLargeAnimation);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                // 手指移开，按钮执行缩小动画
                v.startAnimation(mButtonScaleSmallAnimation);
                v.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 缩小动画执行完毕后，将按钮的动画清除。这里的150毫秒是缩小动画的执行时间。
                        v.clearAnimation();
                    }
                }, 150);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:// 添加按钮
                openPanelView();
                break;
            case R.id.close:// 关闭按钮
                closePanelView();
                break;
        }
    }
}

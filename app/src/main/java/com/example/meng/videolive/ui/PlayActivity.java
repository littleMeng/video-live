package com.example.meng.videolive.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.example.meng.videolive.R;
import com.example.meng.videolive.db.RoomIdDatabaseHelper;
import com.example.meng.videolive.utils.DanmuProcess;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.vov.vitamio.Vitamio;
import master.flame.danmaku.controller.IDanmakuView;

public class PlayActivity extends Activity {
    private static final String TAG = "PLAY_ACTIVITY";
    private static final int CONTROL_STAY_TIME = 4000;

    @BindView(R.id.vtm_vv) io.vov.vitamio.widget.VideoView videoView;
    @BindView(R.id.view_control) RelativeLayout mViewControl;
    @BindView(R.id.danmakuView) IDanmakuView mDanmakuView;
    @BindView(R.id.ib_heart) ImageButton mBtnHeart;

    private DanmuProcess mDanmuProcess;
    private RoomIdDatabaseHelper mRoomIdDB;
    private int mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_play);
        ButterKnife.bind(this);

        hideSystemUI();
        String path = getIntent().getStringExtra("PATH");
        mRoomId = getIntent().getIntExtra("ROOM_ID", -1);
        init();
        playVideo(path);
        playDanmu();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_UP:
                if (mViewControl.getVisibility() == View.VISIBLE) {
                    mViewControl.setVisibility(View.INVISIBLE);
                    mHandler.removeCallbacks(mRunnable);
                } else {
                    mViewControl.setVisibility(View.VISIBLE);
                    mHandler.postDelayed(mRunnable, CONTROL_STAY_TIME);
                }
        }
        return super.onTouchEvent(event);
    }

    Handler mHandler = new Handler();

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            mViewControl.setVisibility(View.INVISIBLE);
        }
    };

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void init() {
        Button mBtnBack;
        Switch mDanmuSwitch;

        mRoomIdDB = new RoomIdDatabaseHelper(getApplicationContext(),
                RoomIdDatabaseHelper.HEART_DB_NAME, null, 1);
        mBtnBack = (Button) findViewById(R.id.btn_back);
        mDanmuSwitch = (Switch) findViewById(R.id.swch_danmu);

        mViewControl.setVisibility(View.INVISIBLE);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mDanmuSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDanmakuView.hide();
                } else {
                    mDanmakuView.show();
                }
                restartHideViewDelay();
            }
        });

        if (mRoomIdDB.getRoomIds().contains(mRoomId)) {
            mBtnHeart.setImageResource(R.mipmap.ic_heart_press);
        } else {
            mBtnHeart.setImageResource(R.mipmap.ic_heart);
        }
    }

    @OnClick(R.id.ib_heart)
    public void heartClickListener() {
        if (mRoomIdDB.getRoomIds().contains(mRoomId)) {
            mRoomIdDB.deleteRoomId(mRoomId);
            mBtnHeart.setImageResource(R.mipmap.ic_heart);
        } else {
            mRoomIdDB.addRoomId(mRoomId);
            mBtnHeart.setImageResource(R.mipmap.ic_heart_press);
        }
    }

    private void restartHideViewDelay() {
        mHandler.removeCallbacks(mRunnable);
        mHandler.postDelayed(mRunnable, CONTROL_STAY_TIME);
    }

    private void playDanmu() {
        mDanmuProcess = new DanmuProcess(this, mDanmakuView, mRoomId);
        mDanmuProcess.start();
    }

    private void playVideo(String path) {
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDanmuProcess.finish();
        mDanmakuView.release();
    }
}

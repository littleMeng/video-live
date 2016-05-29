package com.example.meng.videolive.ui;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.meng.videolive.R;
import com.example.meng.videolive.douyuDanmu.client.DyBulletScreenClient;
import com.example.meng.videolive.douyuDanmu.utils.KeepAlive;
import com.example.meng.videolive.douyuDanmu.utils.KeepGetMsg;

import java.io.InputStream;
import java.util.HashMap;

import io.vov.vitamio.Vitamio;
import master.flame.danmaku.controller.DrawHandler;
import master.flame.danmaku.controller.IDanmakuView;
import master.flame.danmaku.danmaku.loader.ILoader;
import master.flame.danmaku.danmaku.loader.IllegalDataException;
import master.flame.danmaku.danmaku.loader.android.DanmakuLoaderFactory;
import master.flame.danmaku.danmaku.model.BaseDanmaku;
import master.flame.danmaku.danmaku.model.DanmakuTimer;
import master.flame.danmaku.danmaku.model.IDisplayer;
import master.flame.danmaku.danmaku.model.android.DanmakuContext;
import master.flame.danmaku.danmaku.model.android.Danmakus;
import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
import master.flame.danmaku.danmaku.parser.IDataSource;
import master.flame.danmaku.danmaku.parser.android.BiliDanmukuParser;

public class PlayActivity extends Activity {
    private static final String TAG = "PLAY_ACTIVITY";
    private io.vov.vitamio.widget.VideoView videoView;

    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mParser;
    DyBulletScreenClient mDanmuClient;
    private int mRoomId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Vitamio.isInitialized(this);
        setContentView(R.layout.activity_play);
        hideSystemUI();
        init();
        String path = getIntent().getStringExtra("PATH");
        int roomId = getIntent().getIntExtra("ROOM_ID", -1);
        playThePath(path);
        playTheDanmu(roomId);
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    private void init() {
        videoView = (io.vov.vitamio.widget.VideoView) findViewById(R.id.vtm_vv);
        mDanmakuView = (IDanmakuView) findViewById(R.id.danmakuView);
        initDanmaku();
    }

    private void initDanmaku() {
        mDanmakuContext = DanmakuContext.create();
        try {
            mParser = createParser(null);
        } catch (IllegalDataException e) {
            e.printStackTrace();
        }
        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 3);
        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);

        mDanmakuContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3)
                .setDuplicateMergingEnabled(false)
                .setScrollSpeedFactor(1.2f)
                .setScaleTextSize(1.2f)
                .setMaximumLines(maxLinesPair)
                .preventOverlapping(overlappingEnablePair);

        if (mDanmakuView != null) {
            mDanmakuView.setCallback(new DrawHandler.Callback() {
                @Override
                public void prepared() {
                    mDanmakuView.start();
                }

                @Override
                public void updateTimer(DanmakuTimer timer) {

                }

                @Override
                public void danmakuShown(BaseDanmaku danmaku) {

                }

                @Override
                public void drawingFinished() {

                }
            });
            mDanmakuView.prepare(mParser, mDanmakuContext);
            mDanmakuView.enableDanmakuDrawingCache(true);
        }
    }

    private BaseDanmakuParser createParser(InputStream stream) throws IllegalDataException {

        if (stream == null) {
            return new BaseDanmakuParser() {

                @Override
                protected Danmakus parse() {
                    return new Danmakus();
                }
            };
        }

        ILoader loader = DanmakuLoaderFactory.create(DanmakuLoaderFactory.TAG_BILI);

        loader.load(stream);

        BaseDanmakuParser parser = new BiliDanmukuParser();
        IDataSource<?> dataSource = loader.getDataSource();
        parser.load(dataSource);
        return parser;
    }

    private void playThePath(String path) {
        Uri uri = Uri.parse(path);
        videoView.setVideoURI(uri);
        videoView.start();
    }

    private void playTheDanmu(int roomId) {
        mRoomId = roomId;

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int groupId = -9999;
                mDanmuClient = DyBulletScreenClient.getInstance();
                //设置需要连接和访问的房间ID，以及弹幕池分组号
                mDanmuClient.init(mRoomId, groupId);

                KeepAlive keepAlive = new KeepAlive();
                keepAlive.start();

                KeepGetMsg keepGetMsg = new KeepGetMsg();
                keepGetMsg.start();

                mDanmuClient.setmHandleMsgListener(new DyBulletScreenClient.HandleMsgListener() {
                    @Override
                    public void handleMessage(String txt) {
                        addDanmaku(true, txt);
                        Log.i(TAG, "handleMessage: txt=" + txt);
                    }
                });
            }
        });
        thread.start();
    }

    private void addDanmaku(boolean islive, String txt) {
        BaseDanmaku danmaku = mDanmakuContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
        if (danmaku == null || mDanmakuView == null) {
            return;
        }
        danmaku.text = txt;
        danmaku.padding = 5;
        danmaku.priority = 0;
        danmaku.isLive = islive;
        danmaku.textSize = 50f * (mParser.getDisplayer().getDensity() - 0.6f);
        danmaku.textColor = Color.WHITE;
        danmaku.time = mDanmakuView.getCurrentTime();
        mDanmakuView.addDanmaku(danmaku);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDanmuClient.unInit();
    }
}

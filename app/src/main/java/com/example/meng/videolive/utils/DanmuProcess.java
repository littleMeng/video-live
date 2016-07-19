package com.example.meng.videolive.utils;

import android.content.Context;
import android.graphics.Color;

import com.example.meng.videolive.douyuDanmu.client.DyBulletScreenClient;

import java.io.InputStream;
import java.util.HashMap;

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

/**
 * Created by 小萌神_0 on 2016/5/31.
 */
public class DanmuProcess {
    private Context mContext;
    private IDanmakuView mDanmakuView;
    private DanmakuContext mDanmakuContext;
    private BaseDanmakuParser mParser;
    private DyBulletScreenClient mDanmuClient;
    private int mRoomId;

    public DanmuProcess(Context context, IDanmakuView danmakuView, int roomId) {
        this.mContext = context;
        this.mDanmakuView = danmakuView;
        this.mRoomId = roomId;
    }

    public void start() {
        initDanmaku();
        getAndAddDanmu();
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

    private void getAndAddDanmu() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                int groupId = -9999;
                mDanmuClient = DyBulletScreenClient.getInstance();
                //设置需要连接和访问的房间ID，以及弹幕池分组号
                mDanmuClient.start(mRoomId, groupId);
//                mDanmuClient.init(mRoomId, groupId);
//
//                KeepAlive keepAlive = new KeepAlive();
//                keepAlive.start();
//
//                KeepGetMsg keepGetMsg = new KeepGetMsg();
//                keepGetMsg.start();

                mDanmuClient.setmHandleMsgListener(new DyBulletScreenClient.HandleMsgListener() {
                    @Override
                    public void handleMessage(String txt) {
                        addDanmaku(true, txt);
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
        danmaku.textSize = 32;
        danmaku.textColor = Color.WHITE;
        danmaku.time = mDanmakuView.getCurrentTime();
        mDanmakuView.addDanmaku(danmaku);
    }

    public void finish() {
        //停止从服务器获取弹幕
        mDanmuClient.stop();
    }
}

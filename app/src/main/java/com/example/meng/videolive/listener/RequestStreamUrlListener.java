package com.example.meng.videolive.listener;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public interface RequestStreamUrlListener {
    void onSuccess(int roomId, String url);

    void onError();
}

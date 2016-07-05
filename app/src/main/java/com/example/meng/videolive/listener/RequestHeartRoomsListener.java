package com.example.meng.videolive.listener;

import com.example.meng.videolive.bean.RoomInfo;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public interface RequestHeartRoomsListener {
    void onSuccess(RoomInfo roomInfo);

    void onError();
}

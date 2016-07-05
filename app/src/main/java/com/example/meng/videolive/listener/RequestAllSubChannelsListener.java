package com.example.meng.videolive.listener;

import com.example.meng.videolive.bean.SubChannelInfo;

import java.util.List;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public interface RequestAllSubChannelsListener {
    void onSuccess(List<SubChannelInfo> subChannelInfos);

    void onError();
}

package com.example.meng.videolive.listener;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public interface NetworkRequest {
    void getSubChannel(String url, RequestSubChannelListener listener);

    void getStreamUrl(int roomId, RequestStreamUrlListener listener);

    void getAllSubChannels(RequestAllSubChannelsListener listener);

    void getHeartRooms(RequestHeartRoomsListener listener);
}

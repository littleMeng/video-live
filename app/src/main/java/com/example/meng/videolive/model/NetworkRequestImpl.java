package com.example.meng.videolive.model;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meng.videolive.bean.GsonAllSubChannels;
import com.example.meng.videolive.bean.GsonDouyuRoom;
import com.example.meng.videolive.bean.GsonDouyuRoomInfo;
import com.example.meng.videolive.bean.GsonSubChannel;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.bean.SubChannelInfo;
import com.example.meng.videolive.db.RoomIdDatabaseHelper;
import com.example.meng.videolive.listener.NetworkRequest;
import com.example.meng.videolive.listener.RequestAllSubChannelsListener;
import com.example.meng.videolive.listener.RequestHeartRoomsListener;
import com.example.meng.videolive.listener.RequestStreamUrlListener;
import com.example.meng.videolive.listener.RequestSubChannelListener;
import com.example.meng.videolive.utils.BuildUrl;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by uspai.taobao.com on 2016/7/5.
 */
public class NetworkRequestImpl implements NetworkRequest {
    private static final String TAG = "NetworkRequestImpl";
    private Context mContext;
    private RequestQueue mRequestQueue;
    private RoomIdDatabaseHelper mRoomIdDB;

    public NetworkRequestImpl(Context context) {
        this.mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
        mRoomIdDB = new RoomIdDatabaseHelper(context, RoomIdDatabaseHelper.HEART_DB_NAME, null, 1);
    }

    private Boolean isSearchUrl(String url) {
        return url.contains("mobileSearch");
    }

    @Override
    public void getSubChannel(String url, final RequestSubChannelListener listener) {
        final String urlTemp = url;
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<RoomInfo> roomInfos;
                        if (isSearchUrl(urlTemp)) {
                            roomInfos = handleSearchResponse(response);
                        } else {
                            roomInfos = handlerSubChannelResponse(response);
                        }
                        listener.onSuccess(roomInfos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        });
        mRequestQueue.add(request);
    }

    private List<RoomInfo> handleSearchResponse(String response) {
        Gson gson = new Gson();
        List<RoomInfo> roomInfos = new ArrayList<>();
        try {
            GsonDouyuRoomInfo subChannel = gson.fromJson(response, GsonDouyuRoomInfo.class);
            for (GsonSubChannel.Room room : subChannel.getData().getRoom()) {
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setRoomId(room.getRoom_id());
                roomInfo.setRoomSrc(room.getRoomSrc());
                roomInfo.setRoomName(room.getRoom_name());
                roomInfo.setNickname(room.getNickname());
                roomInfo.setOnline(room.getOnline());
                roomInfos.add(roomInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerSunChannelResponse: subChannel is null", e);
        }
        return roomInfos;
    }

    private List<RoomInfo> handlerSubChannelResponse(String response){
        Gson gson = new Gson();
        List<RoomInfo> roomInfos = new ArrayList<>();
        try {
            GsonSubChannel subChannel = gson.fromJson(response, GsonSubChannel.class);
            for (GsonSubChannel.Room room : subChannel.getData()) {
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setRoomId(room.getRoom_id());
                roomInfo.setRoomSrc(room.getRoom_src());
                roomInfo.setRoomName(room.getRoom_name());
                roomInfo.setNickname(room.getNickname());
                roomInfo.setOnline(room.getOnline());
                roomInfos.add(roomInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerSunChannelResponse: subChannel is null", e);
        }
        return roomInfos;
    }

    @Override
    public void getStreamUrl(final int roomId, final RequestStreamUrlListener listener) {
        String url = BuildUrl.getDouyuRoomUrl(roomId);
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        Gson gson = new Gson();
                        try {
                            GsonDouyuRoom roomInfo = gson.fromJson(response, GsonDouyuRoom.class);
                            String url = roomInfo.getData().getLive_url();
                            listener.onSuccess(roomId, url);
                        } catch (Exception e) {
                            Log.e(TAG, "onResponse: roomInfo is null", e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return BuildUrl.getDouyuRoomParams(roomId);
            }
        };

        mRequestQueue.add(request);
    }

    @Override
    public void getAllSubChannels(final RequestAllSubChannelsListener listener) {
        String url = BuildUrl.getDouyuAllSubChannels();
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        List<SubChannelInfo> subChannelInfos = handlerAllSubChannelsResponse(response);
                        listener.onSuccess(subChannelInfos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        });
        mRequestQueue.add(request);
    }

    private List<SubChannelInfo> handlerAllSubChannelsResponse(String response){
        List<SubChannelInfo> subChannelInfos = new ArrayList<>();
        Gson gson = new Gson();
        try {
            GsonAllSubChannels allSubChannel = gson.fromJson(response, GsonAllSubChannels.class);
            for (GsonAllSubChannels.Data gsonData : allSubChannel.getData()) {
                SubChannelInfo subChannelInfo = new SubChannelInfo();
                subChannelInfo.setTagId(gsonData.getTag_id());
                subChannelInfo.setTagName(gsonData.getTag_name());
                subChannelInfo.setIconUrl(gsonData.getIcon_url());

                subChannelInfos.add(subChannelInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerAllSubChannelsResponse: allSubChannel is null", e);
        }
        return subChannelInfos;
    }

    @Override
    public void getHeartRooms(final RequestHeartRoomsListener listener) {
        mRequestQueue.cancelAll(null);
        List<Integer> roomIds = mRoomIdDB.getRoomIds();
        for (int roomId : roomIds) {
            String url = BuildUrl.getDouyuRoom(roomId);
            StringRequest request = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            RoomInfo roomInfo = handlerHeartRoomsResponse(response);
                            if (roomInfo != null) {
                                listener.onSuccess(roomInfo);
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    listener.onError();
                }
            });
            mRequestQueue.add(request);
        }
    }

    private RoomInfo handlerHeartRoomsResponse(String response){
        Gson gson = new Gson();
        try {
            GsonDouyuRoomInfo subChannel = gson.fromJson(response, GsonDouyuRoomInfo.class);
            GsonSubChannel.Room room = subChannel.getData().getRoom().get(0);
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomId(room.getRoom_id());
            roomInfo.setRoomSrc(room.getRoom_src());
            roomInfo.setRoomName(room.getRoom_name());
            roomInfo.setNickname(room.getNickname());
            roomInfo.setOnline(room.getOnline());

            return roomInfo;
        } catch (Exception e) {
            Log.e(TAG, "handlerHeartRoomsResponse: gsonRoomInfo is error", e);
        }
        return null;
    }
}

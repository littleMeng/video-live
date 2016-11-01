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
    private List<RoomInfo> mRoomInfos;
    private List<SubChannelInfo> mSubChannelInfos;
    private RequestQueue mRequestQueue;
    private RoomIdDatabaseHelper mRoomIdDB;

    public NetworkRequestImpl(Context context) {
        this.mContext = context;
        mRoomInfos = new ArrayList<>();
        mSubChannelInfos = new ArrayList<>();
        mRequestQueue = Volley.newRequestQueue(context);
        mRoomIdDB = new RoomIdDatabaseHelper(context, RoomIdDatabaseHelper.HEART_DB_NAME, null, 1);
    }

    @Override
    public void getSubChannel(String url, final RequestSubChannelListener listener) {
        StringRequest request = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        handlerSunChannelResponse(response);
                        listener.onSuccess(mRoomInfos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        });
        mRequestQueue.add(request);
    }

    private void handlerSunChannelResponse(String response){
        Gson gson = new Gson();
        mRoomInfos.clear();
        try {
            GsonSubChannel subChannel = gson.fromJson(response, GsonSubChannel.class);
            for (GsonSubChannel.Room room : subChannel.getData()) {
                RoomInfo roomInfo = new RoomInfo();
                roomInfo.setRoomId(room.getRoom_id());
                roomInfo.setRoomSrc(room.getRoom_src());
                roomInfo.setRoomName(room.getRoom_name());
                roomInfo.setNickname(room.getNickname());
                roomInfo.setOnline(room.getOnline());
                mRoomInfos.add(roomInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerSunChannelResponse: subChannel is null", e);
        }
    }

    @Override
    public void getStreamUrl(final int roomId, final RequestStreamUrlListener listener) {
        String url = BuildUrl.getDouyuRoomUrl(roomId);
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, response);
                        Gson gson = new Gson();
                        try {
                            GsonDouyuRoom roomInfo = gson.fromJson(response, GsonDouyuRoom.class);
                            String url = roomInfo.getData().getRtmp_url() + "/" + roomInfo.getData().getRtmp_live();
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
            protected Map<String, String> getParams() throws AuthFailureError {
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
                        handlerAllSubChannelsResponse(response);
                        listener.onSuccess(mSubChannelInfos);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError();
            }
        });
        mRequestQueue.add(request);
    }

    private void handlerAllSubChannelsResponse(String response){
        Gson gson = new Gson();
        mSubChannelInfos.clear();
        try {
            GsonAllSubChannels allSubChannel = gson.fromJson(response, GsonAllSubChannels.class);
            for (GsonAllSubChannels.Data gsonData : allSubChannel.getData()) {
                SubChannelInfo subChannelInfo = new SubChannelInfo();
                subChannelInfo.setTagId(gsonData.getTag_id());
                subChannelInfo.setTagName(gsonData.getTag_name());
                subChannelInfo.setIconUrl(gsonData.getIcon_url());

                mSubChannelInfos.add(subChannelInfo);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerAllSubChannelsResponse: allSubChannel is null", e);
        }
    }

    @Override
    public void getHeartRooms(final RequestHeartRoomsListener listener) {
        mRequestQueue.cancelAll(null);
        List<Integer> roomIds = mRoomIdDB.getRoomIds();
//        for (int roomId : roomIds) {
//            String path = BuildUrl.getDouyuRoom(roomId);
//            StringRequest request = new StringRequest(path,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            RoomInfo roomInfo = handlerHeartRoomsResponse(response);
//                            if (roomInfo != null) {
//                                listener.onSuccess(roomInfo);
//                            }
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    listener.onError();
//                }
//            });
//            mRequestQueue.add(request);
//        }
    }

    private RoomInfo handlerHeartRoomsResponse(String response){
        Gson gson = new Gson();
        try {
            GsonDouyuRoomInfo gsonRoomInfo = gson.fromJson(response, GsonDouyuRoomInfo.class);
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomId(gsonRoomInfo.getData().getRoom_id());
            roomInfo.setNickname(gsonRoomInfo.getData().getNickname());
            roomInfo.setOnline(gsonRoomInfo.getData().getOnline());
            roomInfo.setRoomSrc(gsonRoomInfo.getData().getRoom_src());
            return roomInfo;
        } catch (Exception e) {
            Log.e(TAG, "handlerHeartRoomsResponse: gsonRoomInfo is error", e);
        }
        return null;
    }
}

package com.example.meng.videolive.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.RoomInfoAdapter;
import com.example.meng.videolive.bean.BuildUrl;
import com.example.meng.videolive.bean.GsonDouyuRoomInfo;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.db.RoomIdDatabaseHelper;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by uspai.taobao.com on 2016/6/23.
 */
public class HeartFragment extends Fragment {
    private static final String TAG = "HeartFragment";
    private View mView;
    private PtrClassicFrameLayout mptrClassicFrameLayout;
    private RecyclerView mRecyclerView;
    private List<RoomInfo> mRoomInfos;
    private RoomInfoAdapter mAdapter;
    private RequestQueue mRequestQueue;
    private int mDeletePosition;
    private RoomIdDatabaseHelper mRoomIdDB;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live, container, false);
            init(mView);
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    private void init(View view) {
        mRoomIdDB = new RoomIdDatabaseHelper(getContext(), RoomIdDatabaseHelper.HEART_DB_NAME, null, 1);
        mptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.store_house_ptr_rv);
        mRoomInfos = new ArrayList<>();
        mAdapter = new RoomInfoAdapter(getContext(), mRoomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRequestQueue = Volley.newRequestQueue(getContext());
        mAdapter.setOnItemClickListener(new RoomInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String path = BuildUrl.getDouyuRoom(mRoomInfos.get(position).getRoomId());
                requestStreamPath(path);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                mDeletePosition = position;
                new AlertDialog.Builder(getContext()).setTitle("收藏")
                        .setMessage("确认收藏")
                        .setNegativeButton("否", null)
                        .setPositiveButton("是", mPositiveClickListener)
                        .show();
            }
        });
        setAdapter();
        setPtrHandler();
        mptrClassicFrameLayout.autoRefresh(true);
    }

    DialogInterface.OnClickListener mPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int roomId = mRoomInfos.get(mDeletePosition).getRoomId();
            mRoomIdDB.deleteRoomId(roomId);
            mRoomInfos.remove(mRoomInfos.get(mDeletePosition));
            mAdapter.notifyDataSetChanged();
        }
    };

    private void requestStreamPath(final String path) {
        StringRequest request = new StringRequest(path,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        GsonDouyuRoomInfo roomInfo = gson.fromJson(response, GsonDouyuRoomInfo.class);
                        String path = roomInfo.getData().getRtmp_url() + "/" + roomInfo.getData().getRtmp_live();
                        Intent intent = new Intent(getActivity(), PlayActivity.class);
                        intent.putExtra("PATH", path);
                        intent.putExtra("ROOM_ID", roomInfo.getData().getRoom_id());
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: requestStreamPath fail");
            }
        });
        mRequestQueue.add(request);
    }

    private void setAdapter() {
        mRecyclerView.setAdapter(mAdapter);
    }

    private void setPtrHandler() {
        mptrClassicFrameLayout.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mptrClassicFrameLayout.post(runnable);
            }
        });
    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mRoomInfos.clear();
            List<Integer> roomIds = mRoomIdDB.getRoomIds();
            for (int roomId : roomIds) {
                String path = BuildUrl.getDouyuRoom(roomId);
                StringRequest request = new StringRequest(path,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                handlerResponse(response);
                                mAdapter.setData(mRoomInfos);
                                mAdapter.notifyDataSetChanged();
                                mptrClassicFrameLayout.refreshComplete();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        mptrClassicFrameLayout.refreshComplete();
                    }
                });
                mRequestQueue.add(request);
            }
        }
    };

    private void handlerResponse(String response){
        Gson gson = new Gson();
        GsonDouyuRoomInfo gsonRoomInfo = gson.fromJson(response, GsonDouyuRoomInfo.class);
        RoomInfo roomInfo = new RoomInfo();
        roomInfo.setRoomId(gsonRoomInfo.getData().getRoom_id());
        roomInfo.setNickname(gsonRoomInfo.getData().getNickname());
        roomInfo.setOnline(gsonRoomInfo.getData().getOnline());
        roomInfo.setRoomSrc(gsonRoomInfo.getData().getRoom_src());

        mRoomInfos.add(roomInfo);
    }
}

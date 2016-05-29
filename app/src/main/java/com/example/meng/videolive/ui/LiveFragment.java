package com.example.meng.videolive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.RoomInfoAdapter;
import com.example.meng.videolive.bean.BuildUrl;
import com.example.meng.videolive.bean.GsonDouyuRoomInfo;
import com.example.meng.videolive.bean.GsonSubChannel;
import com.example.meng.videolive.bean.RoomInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class LiveFragment extends Fragment {
    private static final String TAG = "LIVE_FRAGMENT";
    private PtrClassicFrameLayout mptrClassicFrameLayout;
    private RecyclerView mRecyclerView;
    private List<RoomInfo> mRoomInfos;
    private RoomInfoAdapter mAdapter;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_live, container, false);

        init(view);
        setAdapter();
        setPtrHandler();
        return view;
    }

    private void init(View view) {
        mptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.store_house_ptr_rv);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        requestQueue = Volley.newRequestQueue(getContext());
        mRoomInfos = new ArrayList<>();
        mAdapter = new RoomInfoAdapter(getContext(), mRoomInfos);
        mAdapter.setOnItemClickListener(new RoomInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String path = BuildUrl.getDouyuDota2Room(mRoomInfos.get(position).getRoomId());
                requestStreamPath(path);
            }
        });
    }

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
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i(TAG, "onErrorResponse: requestStreamPath fail");
            }
        });
        requestQueue.add(request);
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
            String url = BuildUrl.getDouyuDota2SubChannel();
            StringRequest request = new StringRequest(url,
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
                    Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                    mptrClassicFrameLayout.refreshComplete();
                }
            });
//            requestQueue.add(new ClearCacheRequest(requestQueue.getCache(), null));
            requestQueue.add(request);
        }
    };

    private void handlerResponse(String response){
        Gson gson = new Gson();
        mRoomInfos.clear();
        GsonSubChannel subChannel = gson.fromJson(response, GsonSubChannel.class);
        for (GsonSubChannel.Room room : subChannel.getData()) {
            RoomInfo roomInfo = new RoomInfo();
            roomInfo.setRoomId(room.getRoom_id());
            roomInfo.setRoomSrc(room.getRoom_src());
            roomInfo.setRoomName(room.getRoom_name());
            roomInfo.setOnline(room.getOnline());
            mRoomInfos.add(roomInfo);
        }
    }
}

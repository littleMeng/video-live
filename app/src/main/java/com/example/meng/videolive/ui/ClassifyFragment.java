package com.example.meng.videolive.ui;

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
import com.example.meng.videolive.adapter.SubChannelAdapter;
import com.example.meng.videolive.bean.BuildUrl;
import com.example.meng.videolive.bean.GsonAllSubChannels;
import com.example.meng.videolive.bean.GsonSubChannel;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.bean.SubChannelInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;

/**
 * Created by uspai.taobao.com on 2016/6/22.
 */
public class ClassifyFragment extends Fragment {
    private static final String TAG = "CLASSIFY_FRAGMENT";
    private View mView;
    private PtrClassicFrameLayout mptrClassicFrameLayout;
    private RecyclerView mRecyclerView;
    private List<SubChannelInfo> mSubChannelInfos;
    private SubChannelAdapter mAdapter;
    private RequestQueue mRequestQueue;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live, container, false);
            init(mView);
            mptrClassicFrameLayout.autoRefresh(true);
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    private void init(View view) {
        mptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.store_house_ptr_rv);
        mSubChannelInfos = new ArrayList<>();
        mAdapter = new SubChannelAdapter(getContext(), mSubChannelInfos);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mRequestQueue = Volley.newRequestQueue(getContext());
        setAdapter();
        setPtrHandler();
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
            String url = BuildUrl.getDouyuAllSubChannels();
            StringRequest request = new StringRequest(url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            handlerResponse(response);
                            mAdapter.setmSubChannelInfos(mSubChannelInfos);
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
            mRequestQueue.add(request);
        }
    };

    private void handlerResponse(String response){
        Gson gson = new Gson();
        mSubChannelInfos.clear();
        GsonAllSubChannels allSubChannel = gson.fromJson(response, GsonAllSubChannels.class);
        for (GsonAllSubChannels.Data gsonData : allSubChannel.getData()) {
            SubChannelInfo subChannelInfo = new SubChannelInfo();
            subChannelInfo.setTagId(gsonData.getTag_id());
            subChannelInfo.setTagName(gsonData.getTag_name());
            subChannelInfo.setIconUrl(gsonData.getIcon_url());

            mSubChannelInfos.add(subChannelInfo);
        }
    }
}

package com.example.meng.videolive.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.SubChannelAdapter;
import com.example.meng.videolive.bean.SubChannelInfo;
import com.example.meng.videolive.listener.NetworkRequest;
import com.example.meng.videolive.listener.RequestAllSubChannelsListener;
import com.example.meng.videolive.model.NetworkRequestImpl;

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

    private NetworkRequest mNetworkRequest;

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
        mNetworkRequest = new NetworkRequestImpl(getContext());
        mptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.store_house_ptr_rv);
        mSubChannelInfos = new ArrayList<>();
        mAdapter = new SubChannelAdapter(getContext(), mSubChannelInfos);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        mAdapter.setOnItemClickListener(new SubChannelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                intent.putExtra(ChannelActivity.CHANNEL_TAG, mSubChannelInfos.get(position).getTagId());
                intent.putExtra(ChannelActivity.CHANNEL_NAME, mSubChannelInfos.get(position).getTagName());
                startActivity(intent);
            }
        });
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
            mNetworkRequest.getAllSubChannels(mAllSubChannelsListener);
        }
    };

    private RequestAllSubChannelsListener mAllSubChannelsListener = new RequestAllSubChannelsListener() {
        @Override
        public void onSuccess(List<SubChannelInfo> subChannelInfos) {
            mSubChannelInfos.clear();
            mSubChannelInfos.addAll(subChannelInfos);
            mAdapter.notifyDataSetChanged();
            mptrClassicFrameLayout.refreshComplete();
        }

        @Override
        public void onError() {
            Toast.makeText(getContext(), "请求失败", Toast.LENGTH_SHORT).show();
            mptrClassicFrameLayout.refreshComplete();
        }
    };
}

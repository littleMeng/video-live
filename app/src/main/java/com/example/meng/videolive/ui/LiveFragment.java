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

import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.RoomInfoAdapter;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.listener.NetworkRequest;
import com.example.meng.videolive.listener.RequestStreamUrlListener;
import com.example.meng.videolive.listener.RequestSubChannelListener;
import com.example.meng.videolive.model.NetworkRequestImpl;

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
    public static final String ARGUMENT = "argument";
    private View mView;
    private PtrClassicFrameLayout mptrClassicFrameLayout;
    private RecyclerView mRecyclerView;
    private List<RoomInfo> mRoomInfos;
    private RoomInfoAdapter mAdapter;
    private String mRequestUrl;
    private int mOffset = 0;

    private NetworkRequest mNetworkRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            mRequestUrl = bundle.getString(ARGUMENT);
        }
    }

    public static LiveFragment newInstance(String argument) {
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENT, argument);
        LiveFragment liveFragment = new LiveFragment();
        liveFragment.setArguments(bundle);
        return liveFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_live, container, false);
            init(mView);
            setAdapter();
            setPtrHandler();
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
        mRoomInfos = new ArrayList<>();
        mAdapter = new RoomInfoAdapter(getContext(), mRoomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        //第一列单独占一行
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position == 0) ? gridLayoutManager.getSpanCount() : 1;
            }
        });
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setOnItemClickListener(new RoomInfoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mNetworkRequest.getStreamUrl(mRoomInfos.get(position).getRoomId(), mStreamUrlListener);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
    }

    private RequestStreamUrlListener mStreamUrlListener = new RequestStreamUrlListener() {
        @Override
        public void onSuccess(int roomId, String url) {
            Intent intent = new Intent(getActivity(), PlayActivity.class);
            intent.putExtra("PATH", url);
            intent.putExtra("ROOM_ID", roomId);
            startActivity(intent);
        }

        @Override
        public void onError() {
            Log.i(TAG, "onErrorResponse: requestStreamPath fail");
        }
    };


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
            String url = mRequestUrl + "&offset=0";
            mNetworkRequest.getSubChannel(url, mSubChannelListener);
        }
    };

    private RequestSubChannelListener mSubChannelListener = new RequestSubChannelListener() {
        @Override
        public void onSuccess(List<RoomInfo> roomInfos) {
            mRoomInfos.clear();
            mRoomInfos.addAll(roomInfos);
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

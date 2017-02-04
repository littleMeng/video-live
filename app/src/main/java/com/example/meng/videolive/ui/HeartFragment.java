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

import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.RoomInfoAdapter;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.db.RoomIdDatabaseHelper;
import com.example.meng.videolive.listener.NetworkRequest;
import com.example.meng.videolive.listener.RequestHeartRoomsListener;
import com.example.meng.videolive.listener.RequestStreamUrlListener;
import com.example.meng.videolive.model.NetworkRequestImpl;
import com.example.meng.videolive.utils.AdapterCallback;

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
    private int mDeletePosition;
    private RoomIdDatabaseHelper mRoomIdDB;

    private NetworkRequest mNetworkRequest;

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
        mNetworkRequest = new NetworkRequestImpl(getContext());
        mRoomIdDB = new RoomIdDatabaseHelper(getContext(), RoomIdDatabaseHelper.HEART_DB_NAME, null, 1);
        mptrClassicFrameLayout = (PtrClassicFrameLayout) view.findViewById(R.id.store_house_ptr_frame);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.store_house_ptr_rv);
        mRoomInfos = new ArrayList<>();
        mAdapter = new RoomInfoAdapter(getContext(), mRoomInfos);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mAdapter.setAdapterCallback(new AdapterCallback() {
            @Override
            public void onItemClick(View view, int position) {
                mNetworkRequest.getStreamUrl(mRoomInfos.get(position).getRoomId(), mStreamUrlListener);
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

            @Override
            public void onPositionChanged(int position) {

            }
        });
        setAdapter();
        setPtrHandler();
        mptrClassicFrameLayout.autoRefresh(true);
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

    DialogInterface.OnClickListener mPositiveClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            int roomId = mRoomInfos.get(mDeletePosition).getRoomId();
            mRoomIdDB.deleteRoomId(roomId);
            mRoomInfos.remove(mRoomInfos.get(mDeletePosition));
            mAdapter.notifyDataSetChanged();
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
            mRoomInfos.clear();
            mNetworkRequest.getHeartRooms(mHeartRoomsListener);
            mptrClassicFrameLayout.refreshComplete();
        }
    };

    private RequestHeartRoomsListener mHeartRoomsListener = new RequestHeartRoomsListener() {
        @Override
        public void onSuccess(RoomInfo roomInfo) {
            mRoomInfos.add(roomInfo);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onError() {

        }
    };
}

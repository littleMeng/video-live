package com.example.meng.videolive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.meng.videolive.R;
import com.example.meng.videolive.bean.RoomInfo;
import com.example.meng.videolive.utils.AdapterCallback;

import java.util.List;

/**
 * Created by 小萌神_0 on 2016/5/27.
 *
 */
public class RoomInfoAdapter extends RecyclerView.Adapter<RoomInfoAdapter.MyViewHolder> {
    private Context context;
    private List<RoomInfo> roomInfos;

    public RoomInfoAdapter(Context context, List<RoomInfo> roomInfos) {
        this.context = context;
        this.roomInfos = roomInfos;
    }

    private AdapterCallback mAdapterCallback = null;

    public void setAdapterCallback(AdapterCallback adapterCallback) {
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).
                inflate(R.layout.room_info_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Glide.with(context).
                load(roomInfos.get(position).getRoomSrc()).
                fitCenter().
                error(R.mipmap.default_dota2).
                into(holder.roomSrc);
        String nickname = roomInfos.get(position).getNickname();
        holder.nickname.setText(nickname);
        int online = roomInfos.get(position).getOnline();
        String onlineStr = String.valueOf(online) + "位观众";
        holder.online.setText(onlineStr);

        handleCallback(holder);
    }

    private void handleCallback(final MyViewHolder holder) {
        final int position = holder.getLayoutPosition();
        if (mAdapterCallback != null) {
            mAdapterCallback.onPositionChanged(position);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAdapterCallback.onItemClick(holder.itemView, position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mAdapterCallback.onItemLongClick(holder.itemView, position);
                    return true;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return roomInfos.size();
    }

    public void setData(List<RoomInfo> roomInfos) {
        this.roomInfos = roomInfos;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView roomSrc;
        TextView nickname;
        TextView online;

        public MyViewHolder(View itemView) {
            super(itemView);
            roomSrc = (ImageView) itemView.findViewById(R.id.iv_room);
            nickname = (TextView) itemView.findViewById(R.id.nickname);
            online = (TextView) itemView.findViewById(R.id.online);
        }
    }
}

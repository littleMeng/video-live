package com.example.meng.videolive.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.meng.videolive.R;
import com.example.meng.videolive.bean.SubChannelInfo;
import com.example.meng.videolive.utils.BitmapCache;

import java.util.List;

/**
 * Created by uspai.taobao.com on 2016/6/24.
 */
public class SubChannelAdapter extends RecyclerView.Adapter<SubChannelAdapter.MyViewHolder> {
    private Context mContext;
    private List<SubChannelInfo> mSubChannelInfos;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    public SubChannelAdapter(Context context, List<SubChannelInfo> subChannelInfos) {
        this.mContext = context;
        this.mSubChannelInfos = subChannelInfos;
        requestQueue = Volley.newRequestQueue(context);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
    }

    public void setmSubChannelInfos(List<SubChannelInfo> mSubChannelInfos) {
        this.mSubChannelInfos = mSubChannelInfos;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MyViewHolder myViewHolder = new MyViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.sub_channel_item, parent, false));
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.gameIcon,
                R.mipmap.default_dota2, R.mipmap.default_dota2);
        imageLoader.get(mSubChannelInfos.get(position).getIconUrl(), listener);
        String gameName = mSubChannelInfos.get(position).getTagName();
        holder.gameName.setText(gameName);

        handleClick(holder);
    }

    private void handleClick(final MyViewHolder holder) {
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(holder.itemView, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSubChannelInfos.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView gameIcon;
        TextView gameName;

        public MyViewHolder(View itemView) {
            super(itemView);
            gameIcon = (ImageView) itemView.findViewById(R.id.iv_game_classify_icon);
            gameName = (TextView) itemView.findViewById(R.id.tv_game_classify_name);
        }
    }
}

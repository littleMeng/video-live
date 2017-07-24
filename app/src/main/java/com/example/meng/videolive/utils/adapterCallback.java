package com.example.meng.videolive.utils;

import android.view.View;

/**
 * Created by mengshen on 2017/2/5.
 *
 */
public interface AdapterCallback {
    void onItemClick(View view, int position);
    void onItemLongClick(View view, int position);
    void onPositionChanged(int position);
}

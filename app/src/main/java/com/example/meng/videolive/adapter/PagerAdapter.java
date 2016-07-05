package com.example.meng.videolive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.meng.videolive.ui.LiveFragment;
import com.example.meng.videolive.utils.BuildUrl;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private LiveFragment mDota2Live = null;
    private LiveFragment mLolLive = null;
    private LiveFragment mFurnaceStoneLive = null;
    private LiveFragment mLive = null;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final String[] titles = {"推荐", "DOTA2", "LOL", "炉石"};

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (mLive == null){
                    String url = BuildUrl.getDouyuLiveChannel();
                    mLive = LiveFragment.newInstance(url);
                }
                return mLive;
            case 1:
                if (mDota2Live == null){
                    String url = BuildUrl.getDouyuDota2SubChannel();
                    mDota2Live = LiveFragment.newInstance(url);
                }
                return mDota2Live;
            case 2:
                if (mLolLive == null){
                    String url = BuildUrl.getDouyuLOLSubChannel();
                    mLolLive = LiveFragment.newInstance(url);
                }
                return mLolLive;
            case 3:
                if (mFurnaceStoneLive == null){
                    String url = BuildUrl.getDouyuFurnaceStoneSubChannel();
                    mFurnaceStoneLive = LiveFragment.newInstance(url);
                }
                return mFurnaceStoneLive;
            default:
                return null;
        }
    }
}

package com.example.meng.videolive.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.meng.videolive.ui.LiveFragment;

/**
 * Created by 小萌神_0 on 2016/5/27.
 */
public class PagerAdapter extends FragmentPagerAdapter {
    private LiveFragment mLiveFragment = null;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    private final String[] titles = { "直播"};

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
                if (mLiveFragment == null){
                    mLiveFragment = new LiveFragment();
                }
                return mLiveFragment;
            default:
                return null;
        }
    }
}

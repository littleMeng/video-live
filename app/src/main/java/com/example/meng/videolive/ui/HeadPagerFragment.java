package com.example.meng.videolive.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.PagerAdapter;

/**
 * Created by uspai.taobao.com on 2016/6/22.
 */
public class HeadPagerFragment extends Fragment {
    private View mView;
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private DisplayMetrics dm;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_head_pager, container, false);
            mPagerSlidingTabStrip = (PagerSlidingTabStrip) mView.findViewById(R.id.tab_host);
            ViewPager viewPager = (ViewPager) mView.findViewById(R.id.view_pager);
            if (viewPager != null) {
                viewPager.setAdapter(new PagerAdapter(getFragmentManager()));
                viewPager.setOffscreenPageLimit(1);
                mPagerSlidingTabStrip.setViewPager(viewPager);
            }
            setTabsValue();
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    private void setTabsValue() {
        dm = getResources().getDisplayMetrics();

        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 2, dm));
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 12, dm));
        mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#45c01a"));
        mPagerSlidingTabStrip.setTabBackground(0);
    }
}

package com.example.meng.videolive.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.astuetz.PagerSlidingTabStrip;
import com.example.meng.videolive.R;
import com.example.meng.videolive.adapter.PagerAdapter;

public class MainActivity extends AppCompatActivity {
    private PagerSlidingTabStrip mPagerSlidingTabStrip;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        if (viewPager != null) {
            viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager()));
            viewPager.setOffscreenPageLimit(4);
            mPagerSlidingTabStrip.setViewPager(viewPager);
        }
        setTabsValue();
    }

    private void setTabsValue() {
        dm = getResources().getDisplayMetrics();

        mPagerSlidingTabStrip.setDividerColor(Color.TRANSPARENT);
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, dm));
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 20, dm));
        mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#45c01a"));
        mPagerSlidingTabStrip.setTabBackground(0);
    }
}

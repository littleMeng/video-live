package com.example.meng.videolive.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;

import com.example.meng.videolive.R;

public class MainActivity extends FragmentActivity {

    private final String mTabSpec[] = {"head", "classify"};

    private final String mIndicator[] = {"首页", "分类"};

    private final Class mFragmentsClass[] = {HeadPagerFragment.class, ClassifyFragment.class};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTabHost tabHost = (FragmentTabHost) findViewById(R.id.tab_host);
        if (tabHost == null) {
            return;
        }
        tabHost.setup(this, getSupportFragmentManager(), R.id.real_tab_content);
        for (int i = 0; i < mTabSpec.length; i++) {
            tabHost.addTab(tabHost.newTabSpec(mTabSpec[i]).setIndicator(mIndicator[i]),
                    mFragmentsClass[i], null);
        }
    }
}

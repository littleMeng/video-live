package com.example.meng.videolive.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.meng.videolive.R;

public class MainActivity extends BaseActivity {

    private final String mTabSpec[] = {"head", "classify", "heart", "search"};

    private final String mIndicator[] = {"首页", "分类", "喜欢", "搜索"};

    private final Class mFragmentsClass[] = {HeadPagerFragment.class,
                                ClassifyFragment.class,
                                HeartFragment.class,
                                SearchFragment.class};

    private final int mTabImage[] = {R.drawable.tab_home_item,
                                R.drawable.tab_classify_item,
                                R.drawable.tab_heart_item,
                                R.drawable.tab_user_item};

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
            tabHost.addTab(tabHost.newTabSpec(mTabSpec[i]).setIndicator(getTabView(i)),
                    mFragmentsClass[i], null);
        }
    }

    private View getTabView(int index) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.tab_item, null);
        ImageView image = (ImageView) view.findViewById(R.id.tab_image);
        TextView text = (TextView) view.findViewById(R.id.tab_title);
        image.setImageResource(mTabImage[index]);
        text.setText(mIndicator[index]);

        return view;
    }
}

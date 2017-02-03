package com.example.meng.videolive.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.example.meng.videolive.R;
import com.example.meng.videolive.utils.BuildUrl;

public class ChannelActivity extends BaseActivity {
    private static final String TAG = "ChannelActivity";
    public static final String CHANNEL_NAME = "CHANNEL_NAME";
    public static final String CHANNEL_TAG = "CHANNEL_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        String channelName = getIntent().getStringExtra(CHANNEL_NAME);
        int channelTag = getIntent().getIntExtra(CHANNEL_TAG, 0);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(channelName);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        String url = BuildUrl.getDouyuSubChannelBaseTag(channelTag);
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.classify_fragment, LiveFragment.newInstance(url));
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

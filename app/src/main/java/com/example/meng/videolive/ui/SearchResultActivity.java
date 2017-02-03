package com.example.meng.videolive.ui;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.meng.videolive.R;
import com.example.meng.videolive.utils.BuildUrl;

import io.vov.vitamio.utils.Log;

public class SearchResultActivity extends BaseActivity {
    private static final String TAG = "SearchResultActivity";
    public static final String KEY_WORD = "KEY_WORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);

        String keyWord = getIntent().getStringExtra(KEY_WORD);
        Log.i(TAG, "keyWord:" + keyWord);
        ActionBar bar = getSupportActionBar();
        if (bar != null) {
            bar.setTitle(keyWord);
            bar.setDisplayHomeAsUpEnabled(true);
        }
        String url = BuildUrl.getDouyuSearchUrl(keyWord);
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

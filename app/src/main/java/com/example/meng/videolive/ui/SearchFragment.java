package com.example.meng.videolive.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.meng.videolive.R;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by uspai.taobao.com on 2016/6/23.
 */
public class SearchFragment extends Fragment {
    private static final String TAG = "SearchFragment";
    private View mView;
    private Button mSearchBtn;
    private MaterialEditText mEditText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_search, container, false);
            init(mView);
        }

        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    View.OnClickListener onSearchClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (TextUtils.isEmpty(mEditText.getText())) {
                Toast.makeText(getContext(), "搜索关键字不能为空", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(getContext(), SearchResultActivity.class);
            Log.i(TAG, "mEditText:" + mEditText.getText());
            intent.putExtra(SearchResultActivity.KEY_WORD, mEditText.getText().toString());
            startActivity(intent);
        }
    };

    private void init(View view) {
        mSearchBtn = (Button) view.findViewById(R.id.search_btn);
        mEditText = (MaterialEditText) view.findViewById(R.id.search_edit_view);

        mSearchBtn.setOnClickListener(onSearchClick);
    }
}

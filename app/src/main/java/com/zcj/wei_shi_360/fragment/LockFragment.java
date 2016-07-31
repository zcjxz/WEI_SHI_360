package com.zcj.wei_shi_360.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.zcj.wei_shi_360.R;


public class LockFragment extends Fragment {

    private WebView webView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lock, container, false);
        webView = (WebView) rootView.findViewById(R.id.webView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("3g.baidu.com");

    }
}

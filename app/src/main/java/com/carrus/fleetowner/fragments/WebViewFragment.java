package com.carrus.fleetowner.fragments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.carrus.fleetowner.R;
import com.carrus.fleetowner.utils.Utils;

/**
 * Created by Sunny on 1/14/16 for Fleet Owner.
 */
public class WebViewFragment extends Fragment {

    private WebView webView;
    private ProgressBar pBar;
    private String url="http://52.25.204.93/carrus_dev/carrus-web/index.html#/welcomeScreen";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        initView(rootView);
        loadUrl();
        return rootView;
    }

    private void initView(View view) {
        webView = (WebView) view.findViewById(R.id.webView);
        pBar = (ProgressBar) view.findViewById(R.id.pBar);
        pBar.getIndeterminateDrawable().setColorFilter(
                Utils.getColor(getActivity(), R.color.colorPrimary),
                android.graphics.PorterDuff.Mode.SRC_IN);
    }

    private void loadUrl() {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                pBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                pBar.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(url);

    }
}

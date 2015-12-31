package com.carrus.fleetowner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by Sunny on 11/17/15.
 */
public class ShowPODActivity extends BaseActivity {

    private ImageView closeButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pod);

        init();
        initializeClickListner();

    }

    private void init() {
        closeButton = (ImageView) findViewById(R.id.imageView_close);
        ImageView imageView = (ImageView) findViewById(R.id.image);
        WebView mWebView = (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setPluginsEnabled(true);

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");


        String extension = url.substring(url.lastIndexOf("."));

        if (extension.equals(".pdf")) {
            mWebView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.GONE);
            mWebView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=" + url);
        } else {
            mWebView.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);

            Picasso.with(this).
                    load(url).
                    placeholder(R.mipmap.ic_launcher)
                    .into(imageView);
        }
    }

    private void initializeClickListner() {
        View.OnClickListener handler = new View.OnClickListener() {
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.imageView_close:
                        finish();
                        overridePendingTransition(0, 0);
                        break;
                }
            }
        };

        findViewById(R.id.imageView_close).setOnClickListener(handler);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, 0);
    }
}
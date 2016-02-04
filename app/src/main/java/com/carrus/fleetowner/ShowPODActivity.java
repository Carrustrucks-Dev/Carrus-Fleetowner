package com.carrus.fleetowner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.Constants;
import com.squareup.picasso.Picasso;

/**
 * Created by Sunny on 11/17/15 for Fleet Owner for Fleet Owner for Fleet Owner.
 */
public class ShowPODActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pod);
        if (new ConnectionDetector(this).isConnectingToInternet())
            init();
        else
            noInternetDialog();
        initializeClickListner();

    }

    @SuppressLint("SetJavaScriptEnabled")
    private void init() {
//        ImageView closeButton = (ImageView) findViewById(R.id.imageView_close);
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

    private void noInternetDialog(){
        if(ShowPODActivity.this!=null)
            CommonNoInternetDialog.WithActivity(ShowPODActivity.this).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit),getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
                @Override
                public void OnOkButtonPressed() {
                    if (new ConnectionDetector(ShowPODActivity.this).isConnectingToInternet())
                        init();
                    else
                        noInternetDialog();
                }

                @Override
                public void OnCancelButtonPressed() {
                    finish();
                }

                @Override
                public void OnNutralButtonPressed() {
                    try {
                        Intent callIntent = new Intent(Intent.ACTION_DIAL);
                        callIntent.setData(Uri.parse("tel:" + Constants.CONTACT_CARRUS));
                        startActivity(callIntent);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });
    }
}
package com.carrus.fleetowner.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
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
import com.carrus.fleetowner.utils.CommonNoInternetDialog;
import com.carrus.fleetowner.utils.ConnectionDetector;
import com.carrus.fleetowner.utils.Constants;
import com.carrus.fleetowner.utils.Utils;

/**
 * Created by Sunny on 1/14/16 for Fleet Owner.
 */
public class WebViewFragment extends Fragment {

    private WebView webView;
    private ProgressBar pBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_webview, container, false);
        initView(rootView);
        if (new ConnectionDetector(getActivity()).isConnectingToInternet())
            loadUrl();
        else
            noInternetDialog();

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

        String url = "http://52.25.204.93/carrus_dev/carrus-web/index.html#/welcomeScreen";
        webView.loadUrl(url);

    }

    private void noInternetDialog() {
        if(getActivity()!=null && isAdded())
            CommonNoInternetDialog.WithActivity(getActivity()).Show(getResources().getString(R.string.nointernetconnection), getResources().getString(R.string.tryagain), getResources().getString(R.string.exit), getResources().getString(R.string.callcarrus), new CommonNoInternetDialog.ConfirmationDialogEventsListener() {
                @Override
                public void OnOkButtonPressed() {
                    if (new ConnectionDetector(getActivity()).isConnectingToInternet())
                        loadUrl();
                    else
                        noInternetDialog();
                }

                @Override
                public void OnCancelButtonPressed() {
                    getActivity().finish();
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

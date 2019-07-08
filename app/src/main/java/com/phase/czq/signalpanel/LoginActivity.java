package com.phase.czq.signalpanel;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final WebView webView = new WebView(this);
        webView.requestFocus();
        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.getSettings().setBlockNetworkImage(true);
        webView.setWebViewClient(new WebClient());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://github.com/login/oauth/authorize?client_id="+ WebClient.getClient_ID());
        setContentView(webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

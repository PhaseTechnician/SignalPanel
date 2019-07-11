package com.phase.czq.signalpanel;

import android.app.Activity;
import android.content.Intent;
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
        WebClient client = new WebClient();
        client.setAccountMessage(new WebClient.AccountMessage() {
            @Override
            public void onGetMessage(String login, String url) {
                Intent intent = new Intent();
                intent.putExtra("login",login);
                intent.putExtra("avatar",url);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
        webView.setWebViewClient(client);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl("https://github.com/login/oauth/authorize?client_id="+ WebClient.getClient_ID());
        setContentView(webView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

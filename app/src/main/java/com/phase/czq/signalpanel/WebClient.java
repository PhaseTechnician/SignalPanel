package com.phase.czq.signalpanel;

import android.net.http.HttpResponseCache;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;
import java.util.regex.Pattern;

public class WebClient extends WebViewClient {

    private String userCode=null;
    static private String client_ID="be38424d8e3416b7b0e3";
    static private String client_secret="03936a8d63e5c5a0f8367bc142fd77ace2fab49e";
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        Log.i("urlRES",url);
        if(Pattern.matches("https://github.com/PhaseTechnician/SignalPanel/wiki[?]code=[0123456789abcdef]{0,}", url)){
            userCode = url.substring(57);
            Log.i("urlRES","code:"+userCode);
            try {
                URL urll = new URL("https://github.com/login/oauth/access_token?client_id="+client_ID+"&client_secret="+client_secret+"&code="+userCode);
                HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setConnectTimeout(2000);
                connection.connect();
                if(connection.getResponseCode()== HttpURLConnection.HTTP_OK){
                    InputStream inputStream = connection.getInputStream();
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    String str = new String(bytes);
                    Log.i("urlRES","str:"+str);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //access_token=948685f3c19fceca500608e24277701575591048&scope=&token_type=bearer
        }
    }

    static public String getClient_ID() {
        return client_ID;
    }

    static public String getClient_secret() {
        return client_secret;
    }
}

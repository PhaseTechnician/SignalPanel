package com.phase.czq.signalpanel.Login;

import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.phase.czq.signalpanel.R;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Pattern;

public class WebClient extends WebViewClient {

    private String userCode=null;
    static private String client_ID="be38424d8e3416b7b0e3";
    static private String client_secret="03936a8d63e5c5a0f8367bc142fd77ace2fab49e";

    public void setAccountMessage(AccountMessage accountMessage) {
        this.accountMessage = accountMessage;
    }

    public interface AccountMessage{
        void onGetMessage(String login,String url);
    }

    private AccountMessage accountMessage = null;

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(accountMessage==null){
            return;
        }
        Log.i("urlRES",url);
        if(Pattern.matches("https://github.com/PhaseTechnician/SignalPanel/wiki[?]code=[0123456789abcdef]{0,}", url)){
            userCode = url.substring(57);
            Log.i("urlRES","code:"+userCode);
            try {
                URL urll = new URL("https://github.com/login/oauth/access_token");
                HttpURLConnection connection = (HttpURLConnection) urll.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setUseCaches(false);
                connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                connection.setConnectTimeout(2000);
                connection.setReadTimeout(2000);
                connection.connect();
                DataOutputStream outputStream = new DataOutputStream(connection
                        .getOutputStream());
                String content = "client_id=be38424d8e3416b7b0e3&client_secret=03936a8d63e5c5a0f8367bc142fd77ace2fab49e&code="+userCode;
                outputStream.writeBytes(content);
                outputStream.flush();
                outputStream.close();
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                if(connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    String line = reader.readLine();
                    reader.close();
                    connection.disconnect();
                    if(line!=null){
                        String token = line.substring(13,53);
                        Log.i("urlRES","accesstoken:"+token);
                        URL url_token = new URL("https://api.github.com/user?access_token="+token);
                        HttpURLConnection token_connection = (HttpURLConnection) url_token.openConnection();
                        token_connection.setRequestMethod("GET");
                        token_connection.setDoInput(true);
                        token_connection.setUseCaches(false);
                        token_connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
                        token_connection.setConnectTimeout(2000);
                        token_connection.setReadTimeout(2000);
                        token_connection.connect();
                        BufferedReader token_reader = new BufferedReader(new InputStreamReader(token_connection.getInputStream()));
                        if(token_connection.getResponseCode()==HttpURLConnection.HTTP_OK){
                            String mess = token_reader.readLine();
                            if(mess!=null){
                                Log.i("urlRES","mess:"+mess);
                                int index_login_start = mess.indexOf("login")+8;
                                int index_login_end = mess.indexOf("id")-3;
                                int index_avatar_start = mess.indexOf("avatar_url")+13;
                                int index_avatar_end = mess.indexOf("gravatar_id")-3;
                                String login = mess.substring(index_login_start,index_login_end);
                                String avatar = mess.substring(index_avatar_start,index_avatar_end);
                                accountMessage.onGetMessage(login,avatar);
                                Log.i("urlRES","login:"+login);
                                Log.i("urlRES","avatar:"+avatar);
                                Toast.makeText(view.getContext(), R.string.login_success_message,Toast.LENGTH_LONG).show();
                            }
                        }
                        token_reader.close();
                        token_connection.disconnect();
                    }

                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static public String getClient_ID() {
        return client_ID;
    }

    static public String getClient_secret() {
        return client_secret;
    }
}

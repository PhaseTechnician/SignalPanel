package com.phase.czq.signalpanel;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountFile {
    private static String accountDir;

    static void setDir(String path){
        accountDir = path;
    }

    public static boolean add(String login,String url){
        if(accountDir==null){
            Log.i("account","nulldir");
            return false;
        }
        File file = new File(accountDir+File.separator+login+".log");
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(url);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String getLogin(){
        File file = new File(accountDir);
        File[] files;
        if ((files=file.listFiles()).length!=0){
            String name = files[0].getName();
            return name.substring(0,name.length()-4);
        }
        else {
            return "unlogin";
        }
    }

    @Nullable
    public static Bitmap getIcon(){
        //尝试从本地获取图片
        String filePath = accountDir+File.separator+getLogin()+".png";
        File file = new File(filePath);
        if(file.exists()){
            return BitmapFactory.decodeFile(filePath);
        }
        //从网络加载图片
        else {
            try {
                URL url1 = new URL(getUrl());
                Bitmap bitmap = BitmapFactory.decodeStream(url1.openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, ((OutputStream)fileOutputStream));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String getUrl(){
        File file = new File(accountDir+File.separator+getLogin()+".log");
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            return bufferedReader.readLine();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "null";
    }
}

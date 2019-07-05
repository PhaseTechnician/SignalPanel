package com.phase.czq.signalpanel;

import android.content.Context;
import android.preference.Preference;
import android.preference.PreferenceManager;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class ValuePool {
    static Context context;

    static Boolean blueToothPipeConnect = false;
    static Boolean usbOTGPipeConnect = false;
    static Boolean wifiPipeConnect = false;

    static TCPClient tcpClient = null;
    //static BlueToothPipe blueToothPipe;
    static BluetoothSPP spp = null;

    static Serial serial = new Serial();

    static PlugParams defaultParam = new PlugParams("undefine",300,200,0,0,-1);
    //初始化PV
    static void init(Context mContext){
         context = mContext;
    }
    static boolean getBoolean(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,false);
    }
    static String getString(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,"default");
    }
    static int getInt(String key){
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(key,"0")) ;
    }
}

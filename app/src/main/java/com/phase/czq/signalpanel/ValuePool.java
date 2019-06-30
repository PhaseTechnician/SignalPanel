package com.phase.czq.signalpanel;

import android.content.Context;
import android.preference.PreferenceManager;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class ValuePool {
    static Context context;

    static Boolean blueToothPipeConnect = true;
    static Boolean usbOTGPipeConnect = false;
    static Boolean wifiPipeConnect = false;

    static BlueToothPipe blueToothPipe;

    static BluetoothSPP spp = null;

    static PlugParams defaultParam = new PlugParams("undefine",300,200,0,0,-1);
    //初始化PV
    static void init(Context mContext){
         context = mContext;
    }
    static boolean getBoolean(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,false);
    }

    static boolean getString(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,false);
    }
}

package com.phase.czq.signalpanel;

import android.content.Context;
import android.preference.PreferenceManager;

public class ValuePool {
    static Context context;

    static Boolean blueToothPipeConnect = true;
    static Boolean usbOTGPipeConnect = false;
    static Boolean wifiPipeConnect = false;

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

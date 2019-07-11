package com.phase.czq.signalpanel.tools_value;

import android.content.Context;
import android.preference.PreferenceManager;

import com.phase.czq.signalpanel.plugs.PlugParams;
import com.phase.czq.signalpanel.Pipe.PipeLine.PipeLine;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class ValuePool {
    static public Context context;

    static public Boolean blueToothPipeConnect = false;
    static public Boolean usbOTGPipeConnect = false;
    static public Boolean wifiPipeConnect = false;

    static public PipeLine pipeLine = null;
    //static BlueToothPipe blueToothPipe;
    static public BluetoothSPP spp = null;

    static public PlugParams defaultParam = new PlugParams("undefine",300,200,0,0,-1);
    //初始化PV
    static public void init(Context mContext){
         context = mContext;
    }
    static public boolean getBoolean(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getBoolean(key,false);
    }
    static public String getString(String key){
        return PreferenceManager.getDefaultSharedPreferences(context).getString(key,"default");
    }
    static public int getInt(String key){
        return Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(context).getString(key,"0")) ;
    }
}

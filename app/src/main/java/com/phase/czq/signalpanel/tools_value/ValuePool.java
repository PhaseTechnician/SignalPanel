package com.phase.czq.signalpanel.tools_value;

import android.content.Context;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.preference.PreferenceManager;

import com.phase.czq.signalpanel.plugs.PlugParams;
import com.phase.czq.signalpanel.Pipe.PipeLine.PipeLine;

import java.util.UUID;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;

public class ValuePool {
    static public Context context;

    static public Boolean blueToothPipeConnect = false;
    static public Boolean usbOTGPipeConnect = false;
    static public Boolean wifiPipeConnect = false;
    static public Boolean debugPipeConnect = false;

    static public PipeLine pipeLine = null;
    //static BlueToothPipeLine blueToothPipe;
    static public BluetoothSPP spp = null;

    static public int Byte_Buffer_Size = 512;
    static public final UUID UUID_SPP = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
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

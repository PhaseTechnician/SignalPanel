package com.phase.czq.signalpanel;

import android.support.annotation.Nullable;

import java.io.Serializable;

//用于传输控件的布局信息
public class PlugParams implements Serializable {
    public String mainString,spareString = "null";
    public String positiveKey,negativeKey = "null";
    public boolean positiveEnable,negativeEnable = true;
    public String modes = "null";
    public String srcs = "null";
    public int width,height;
    public int X,Y;
    public int ID=-1;

    public PlugParams(String mainString,int width,int height,int X,int Y,int ID){
        this.mainString = mainString;
        this.width =width;
        this.height = height;
        this.X=X;
        this.Y=Y;
        this.ID=ID;
    }

    public PlugParams changeID(int newID){
        return  new PlugParams(mainString,width,height,X,Y,newID);
    }
    //NULL
    public PlugParams(){

    }


}
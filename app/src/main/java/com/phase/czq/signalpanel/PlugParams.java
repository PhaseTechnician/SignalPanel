package com.phase.czq.signalpanel;

import android.support.annotation.Nullable;

//用于传输控件的布局信息
public class PlugParams{
    public String mainString,spareString;
    public String positiveKey,negativeKey;
    public boolean positiveEnable,negativeEnable;
    public String modes;
    public String srcs;
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
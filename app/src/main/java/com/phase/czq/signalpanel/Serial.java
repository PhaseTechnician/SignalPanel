package com.phase.czq.signalpanel;

//使用这个类收集从控件事件中获得的字符流或字节流,交由pipe发送和接收
//这里可能会跨线程，注意线程安全

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Serial {
    private List<ValueChangMessage> valueChangMessages = new ArrayList();

    interface UploadValueChange{
        void onValueChanged(String key,SupportValueKinds valueKind,String value);
    }
    private UploadValueChange uploadValueChange = null;

    void send(String message){
        Log.i("SerialMessage",message);
        ValuePool.tcpClient.sendMessage(message);
    }

    boolean isReceive(){
        if(ValuePool.tcpClient.isReceived()){
            return true;
        }else {
            return false;
        }
    }

    public void setUploadValueChange(UploadValueChange uploadValueChange) {
        this.uploadValueChange = uploadValueChange;
    }
}

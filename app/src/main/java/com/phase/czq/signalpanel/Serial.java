package com.phase.czq.signalpanel;

//使用这个类收集从控件事件中获得的字符流或字节流,交由pipe发送和接收
//这里可能会跨线程，注意线程安全

import android.util.Log;

public class Serial {

    void send(String message){
        Log.i("SerialMessage",message);
    }

}

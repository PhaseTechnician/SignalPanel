package com.phase.czq.signalpanel;

//使用这个类收集从控件事件中获得的字符流或字节流,交由pipe发送和接收
//这里可能会跨线程，注意线程安全

import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Serial {
    static PipeLineKinds pipeline = PipeLineKinds.pipe_undefine;
    static AdapterKinds adapter = AdapterKinds.packet;
    private StringBuffer tempReport = null;
    private List<ValueChangMessage> valueChangMessages = new ArrayList();

    public List<ValueChangMessage> getValueChangMessages() {
        return valueChangMessages;
    }

    interface UploadValueChange{
        void onValueChanged(String key,SupportValueKinds valueKind,String value);
    }
    private UploadValueChange uploadValueChange = null;

    void send(String message){
        Log.i("SerialMessage",message);
        switch (pipeline){
            case pipe_usbotg:
                break;
            case pipe_undefine:
                break;
            case pipe_bluetooth:
                break;
            case pipe_wifi_tcpclient:
                ValuePool.tcpClient.sendMessage(message);
                break;
            case pipe_wifi_tcpserver:
                break;
            default:
                break;
        }
    }

    boolean isReceive(){
        if(ValuePool.tcpClient.isReceived()){
            //尝试读取流来获得一些信息
            tempReport.append(ValuePool.tcpClient.getMessage("utf-8"));
            //尝试截取报文
            int index;
            if((index=tempReport.indexOf("end"))!=-1){
                String newReport = tempReport.substring(0,index);
                tempReport.delete(0,index);
            }
            //将报文交付给adapter解析为ValueChangeMessage
            switch (adapter){
                case packet:

            }
            //解析完成后尝试返回值
            return true;
        }
        return false;
    }

    public void setUploadValueChange(UploadValueChange uploadValueChange) {
        this.uploadValueChange = uploadValueChange;
    }
}

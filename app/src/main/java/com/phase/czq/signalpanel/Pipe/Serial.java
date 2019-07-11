package com.phase.czq.signalpanel.Pipe;

import com.phase.czq.signalpanel.Pipe.Adapter.ReportAdapter;
import com.phase.czq.signalpanel.Pipe.PipeLine.PipeLine;
import com.phase.czq.signalpanel.Pipe.message.ValueChangMessage;

import java.util.List;

public class Serial {
    static private PipeLine pipeLine;
    static private ReportAdapter reportAdapter;
    static private ApplyChange applyChange;

    public interface ApplyChange{
        public void apply(ValueChangMessage changeMessage);
    }

    static public void send(String string){
        pipeLine.sendMessage(string);
    }

    static  public void update(){
        if(applyChange==null){
            return;
        }
        if(pipeLine.isReceived()){
            List<ValueChangMessage> changes = reportAdapter.opMessage(convertBytes(pipeLine.getReceive()));
            for(int i=0;i<changes.size();i++){
                applyChange.apply(changes.get(i));
            }
        }
    }

    static private String convertBytes(byte[] bytes){
        return new String(bytes);
    }

    public static void setPipeLine(PipeLine pipeLine) {
        if(Serial.pipeLine!=null){
            Serial.pipeLine.close();
            Serial.pipeLine = pipeLine;
            //Serial.pipeLine.open();
        }else {
            Serial.pipeLine = pipeLine;
            //Serial.pipeLine.open();
        }
    }
    public static void setReportAdapter(ReportAdapter reportAdapter) {
        Serial.reportAdapter = reportAdapter;
    }
    public static void setApplyChange(ApplyChange applyChange) {
        Serial.applyChange = applyChange;
    }

}

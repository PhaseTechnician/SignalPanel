package com.phase.czq.signalpanel.Pipe.Adapter;

import com.phase.czq.signalpanel.Pipe.Phrasing.Phrasing;
import com.phase.czq.signalpanel.Pipe.message.ValueChangMessage;

import java.util.ArrayList;
import java.util.List;

public abstract class ReportAdapter {
    protected Phrasing phrasing;

    public List<ValueChangMessage> opMessage(String message){
        List<String> reports = phrasing.phraseMessage(message);
        List<ValueChangMessage> valueChangMessages = new ArrayList<>();
        for (int i = 0; i < reports.size(); i++) {
            List<ValueChangMessage> tempValueChangeMessage= opReport(reports.get(i));
            for(int k=0;k<tempValueChangeMessage.size();k++){
                valueChangMessages.add(tempValueChangeMessage.get(k));
            }
        }
        return valueChangMessages;
    }

    abstract protected List<ValueChangMessage> opReport(String report);

}

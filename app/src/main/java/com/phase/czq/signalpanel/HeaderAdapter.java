package com.phase.czq.signalpanel;

import com.phase.czq.signalpanel.Phrasing;
import com.phase.czq.signalpanel.ReportAdapter;
import com.phase.czq.signalpanel.SinglePhrasing;
import com.phase.czq.signalpanel.ValueChangMessage;

import java.util.ArrayList;
import java.util.List;

public class HeaderAdapter extends ReportAdapter {

    public HeaderAdapter(){
        phrasing = new SinglePhrasing(';');
    }

    @Override
    protected List<ValueChangMessage> opReport(String report) {
        List<ValueChangMessage> newMessage = new ArrayList<>();
        ValueExpression expression = ValueExpression.analzeSignalString(report,":");
        newMessage.add(new ValueChangMessage(expression.head,SupportValueKinds.type_undefine,expression.foot));
        return newMessage;
    }
}

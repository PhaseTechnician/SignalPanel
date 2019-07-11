package com.phase.czq.signalpanel.Pipe.Adapter;

import com.phase.czq.signalpanel.Pipe.Phrasing.SinglePhrasing;
import com.phase.czq.signalpanel.Pipe.message.SupportValueKinds;
import com.phase.czq.signalpanel.Pipe.message.ValueChangMessage;
import com.phase.czq.signalpanel.tools_value.ValueExpression;

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
        newMessage.add(new ValueChangMessage(expression.head, SupportValueKinds.type_undefine,expression.foot));
        return newMessage;
    }
}

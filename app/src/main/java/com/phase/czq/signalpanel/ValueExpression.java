package com.phase.czq.signalpanel;

public class ValueExpression {
    public String head,foot;

    public ValueExpression(String Head,String Foot){
        head = Head;
        foot = Foot;
    }

    static public ValueExpression analzeSignalString(String signalString,String holder){
        String head,foot;
        int numIndex = signalString.indexOf(holder);
        if(numIndex!=-1){
            head = signalString.substring(0,numIndex);
            foot = signalString.substring(numIndex+holder.length());
        }
        else {
            head = "";
            foot = "";
        }
        return new ValueExpression(head , foot);
    }

}

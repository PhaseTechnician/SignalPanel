package com.phase.czq.signalpanel.Pipe.Phrasing;

import java.util.ArrayList;
import java.util.List;

public class SinglePhrasing extends Phrasing {

    private  char end;
    public SinglePhrasing(char endChar){
        end = endChar;
    }

    @Override
    public List<String> phraseMessage(String message) {
        List<String> strings = new ArrayList<>();
        buffer = new StringBuffer();
        for (int i = 0; i <message.length() ; i++) {
            char c;
            if((c=message.charAt(i))==end){
                strings.add(buffer.toString());
                buffer.setLength(0);
            }else {
                buffer.append(c);
            }
        }
        return strings;
    }
}

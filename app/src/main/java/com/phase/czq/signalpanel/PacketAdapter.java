package com.phase.czq.signalpanel;

import com.phase.czq.signalpanel.ValueChangMessage;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

//从临时的字符串加载或者从文件加载

public class PacketAdapter {
    private String filePath;

    PacketAdapter(){

    }

    public List<ValueChangMessage> getValues(String message){
        List<ValueChangMessage> messages = new ArrayList<>();

        return  messages;
    }
}

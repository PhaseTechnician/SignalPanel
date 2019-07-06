package com.phase.czq.signalpanel;

import com.phase.czq.signalpanel.ValueChangMessage;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

//从临时的字符串加载或者从文件加载

public class PacketAdapter {
    List<ValueDecodeMessage> decodeMessages = new ArrayList<>();

    PacketAdapter(StringBuffer adapt){
        decodeMessages.clear();
        StringBuffer key=new StringBuffer();
        StringBuffer kind=new StringBuffer();
        StringBuffer length=new StringBuffer();
        int lable =0;
        for (int i=0;i<adapt.length();i++) {
            char c = adapt.charAt(i);
            switch (c){
                case '[':
                    lable =1;
                    break;
                case ',':
                    lable++;
                    break;
                case ']':
                    int valueLength = 0;
                    if(lable==3){
                        valueLength = Integer.valueOf(length.toString());
                    }
                    decodeMessages.add(new ValueDecodeMessage(key.toString(),kindIs(kind.toString()),valueLength));
                    lable=0;
                    break;
                default:
                    switch (lable){
                        case 1:
                            key.append(c);
                            break;
                        case 2:
                            kind.append(c);
                            break;
                        case 3:
                            length.append(c);
                    }
                    break;
            }
        }
    }

    private DecodeValueKinds kindIs(String kind){
        switch (kind){
            case "empty":
                return DecodeValueKinds.kind_empty;
            case "string":
                return DecodeValueKinds.kind_string;
            case "int":
                return DecodeValueKinds.kind_int;
            case "float":
                return DecodeValueKinds.kind_float;
            case "bool":
                return DecodeValueKinds.kind_bool;
            default:
                return DecodeValueKinds.kind_undefine;
        }
    }

    public List<ValueChangMessage> getValues(Byte[] message){
        List<ValueChangMessage> messages = new ArrayList<>();
        int byteIndex = 0;
        for(int i=0;i<decodeMessages.size();i++){
            ValueDecodeMessage decodeMessage = decodeMessages.get(i);
            Byte bytes[] = new Byte[decodeMessage.getByteSize()];
            for(int k=0;k<decodeMessage.getByteSize();k++){
                bytes[k] = message[k+byteIndex];
            }
            messages.add(new ValueChangMessage(decodeMessage.getKey(),decodeMessage.getSupporValueKind(),getValue(decodeMessage,bytes)));
            byteIndex+=decodeMessage.getByteSize();
        }
        return  messages;
    }

    static private String getValue(ValueDecodeMessage decode,Byte[] bytes){
        switch (decode.getSupporValueKind()){
            case type_string:
                return bytes.toString();
            case type_int:
            case type_bool:
            case type_float:
            case type_unsupport:
            default:
        }
        return null;
    }
}

package com.phase.czq.signalpanel;

import android.util.Size;

//用于保存用于解析报文的引导结构节点
@Deprecated
public class ValueDecodeMessage {
    private String key;
    private DecodeValueKinds kind;
    private int length;

    ValueDecodeMessage(String Key,DecodeValueKinds Kind,int Length){
        this.key = Key;
        this.kind=Kind;
        this.length = Length;
    }

    public int getByteSize(){
        switch (kind){
            case kind_bool:
                return 1;
            case kind_int:
                return 4;
            case kind_float:
                return 4;
            case kind_string:
                return length;
            case kind_empty:
                return length;
            case kind_undefine:
                return 0;
            default:
                return 0;
        }
    }
    public SupportValueKinds getSupporValueKind(){
        switch (kind){
            case kind_empty:
                return SupportValueKinds.type_unsupport;
            case kind_undefine:
                return SupportValueKinds.type_unsupport;
            case kind_bool:
                return SupportValueKinds.type_bool;
            case kind_float:
                return SupportValueKinds.type_float;
            case kind_int:
                return SupportValueKinds.type_int;
            case kind_string:
                return SupportValueKinds.type_string;
            default:
                return SupportValueKinds.type_unsupport;
        }
    }

    public String getKey() {
        return key;
    }
    public DecodeValueKinds getKind() {
        return kind;
    }
}

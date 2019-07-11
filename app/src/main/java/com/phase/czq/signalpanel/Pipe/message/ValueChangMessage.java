package com.phase.czq.signalpanel.Pipe.message;

public class ValueChangMessage {
    private String key;
    private SupportValueKinds kind;
    private String value;

    public ValueChangMessage(String Key,SupportValueKinds Kind,String Value){
        key = Key;
        kind = Kind;
        value = Value;
    }

    public int getInt(){
        if(kind == SupportValueKinds.type_int)
            return Integer.valueOf(value);
        else
            return 0;
    }

    public float getFloat(){
        if(kind == SupportValueKinds.type_float)
            return Float.valueOf(value);
        else
            return 0.0f;
    }

    public boolean getBoolean(){
        if(kind == SupportValueKinds.type_bool)
            return Boolean.valueOf(value);
        else
            return false;
    }
    public String getString(){
        if(kind == SupportValueKinds.type_string)
            return value;
        else
            return "";
    }
    public String getDefault(){
        return value;
    }

    public String getKey() {
        return key;
    }
}

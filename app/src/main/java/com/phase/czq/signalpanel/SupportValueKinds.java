package com.phase.czq.signalpanel;

public enum SupportValueKinds {
    type_bool,
    type_int,
    type_float,
    type_string,
    type_unsupport;
    public DecodeValueKinds getDecodeKind(){
        switch (this){
            case type_unsupport:
                return DecodeValueKinds.kind_undefine;
            case type_float:
                return DecodeValueKinds.kind_float;
            case type_bool:
                return DecodeValueKinds.kind_bool;
            case type_int:
                return DecodeValueKinds.kind_int;
            case type_string:
                return DecodeValueKinds.kind_string;
            default:
                return DecodeValueKinds.kind_undefine;
        }
    }
}

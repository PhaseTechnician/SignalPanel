package com.phase.czq.signalpanel.Pipe.message;

public enum DecodeValueKinds {
    kind_string,
    kind_empty,
    kind_int,
    kind_float,
    kind_bool,
    kind_undefine;
    public SupportValueKinds getSupportKind(){
        switch (this){
            case kind_string:
                return SupportValueKinds.type_string;
            case kind_int:
                return SupportValueKinds.type_int;
            case kind_float:
                return SupportValueKinds.type_float;
            case kind_bool:
                return SupportValueKinds.type_bool;
            case kind_undefine:
                return SupportValueKinds.type_unsupport;
            case kind_empty:
                return SupportValueKinds.type_unsupport;
            default:
                return SupportValueKinds.type_unsupport;
        }
    }
}

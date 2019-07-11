package com.phase.czq.signalpanel.Pipe.Adapter;

public enum AdapterKinds {
    adapter_header,
    adapter_undefien;
//
    static public AdapterKinds getAdapterKind(String adapter){
        switch (adapter)
        {
            case "adapter_header":
                return AdapterKinds.adapter_header;
            default:
                return AdapterKinds.adapter_undefien;
        }
    }
}

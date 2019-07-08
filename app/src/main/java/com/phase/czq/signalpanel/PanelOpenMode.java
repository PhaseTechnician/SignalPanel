package com.phase.czq.signalpanel;

public enum PanelOpenMode {
    CreatNewPanelToConfig,
    LoadFileToConfig,
    LoadFileToUse,
    undefine;

    public int getIndex(){
        switch (this){
            case LoadFileToUse:
                return 1;
            case undefine:
                return 0;
            case LoadFileToConfig:
                return 2;
            case CreatNewPanelToConfig:
                return 3;
                default:
                    return -1;
        }
    }

    public boolean equal(int index){
        if(index == this.getIndex()){
            return true;
        }
        else{
            return false;
        }
    }
}

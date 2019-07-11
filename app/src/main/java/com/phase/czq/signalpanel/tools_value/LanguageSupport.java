package com.phase.czq.signalpanel.tools_value;

public enum LanguageSupport {
    zh,
    en;
    public int getIndex(){
        switch (this){
            case zh:
                return 0;
            case en:
                return 1;
            default:
                return -1;
        }
    }
    static public LanguageSupport getLanguage(int Index){
        switch (Index){
            case 0:
                return zh;
            case 1:
                return en;
            default:
                return zh;
        }
    }
}

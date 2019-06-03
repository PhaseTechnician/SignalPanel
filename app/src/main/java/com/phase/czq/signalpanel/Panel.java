package com.phase.czq.signalpanel;

public class Panel {
    private String XMLpath;
    private String panelName;
    private String author;
    private String desc;
    //对应的图像
    private String IMAGE;

    public Panel(String path){
        this.XMLpath=path;
        decodeXML();
    }

    public void setXMLpath(String XMLpath) {
        this.XMLpath = XMLpath;
        decodeXML();
    }

    //从XML文件获取概述信息
    private void decodeXML(){


        panelName="RTC";
        author="CZQ";
        desc="233";
    }
    //保存Panel信息到XML
    public void save(){

    }

    public String getPanelName() {
        return panelName;
    }

    public String getAuthor() {
        return author;
    }

    public String getDesc() {
        return desc;
    }
}

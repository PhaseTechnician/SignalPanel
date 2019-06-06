package com.phase.czq.signalpanel;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class PanelXmlDom {

    public enum DomMode{
        ReadFromFile,
        WriteToFile
    }
    public enum Pipeline{
        BlueToothSerial,
        USBOTGSerial,
        TCP,
        UDP
    }
    DomMode mode;
    DocumentBuilderFactory documentBuilderFactory;
    DocumentBuilder documentBuilder;
    Document document;
    Element root,header,layout,setings,panelName,author,desc;

    public PanelXmlDom(DomMode domMode){
        mode=domMode;
        InitDom();
    }

    private void InitDom(){
        //实例化对象
        try{
            documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(mode==DomMode.ReadFromFile){

        }
        else if(mode==DomMode.WriteToFile){
            document.setXmlStandalone(true);
            document.setXmlVersion("1.0");
            //编写文件头
            root = document.createElement("panelName");
            document.appendChild(root);
            //header 部分
            header = document.createElement("header");
            root.appendChild(header);
            panelName = document.createElement("name");
            header.appendChild(panelName);
            author = document.createElement("author");
            header.appendChild(author);
            desc = document.createElement("description");
            header.appendChild(desc);
            //layout 部分
            layout = document.createElement("layout");
            root.appendChild(layout);
            //seting 部分
            setings = document.createElement("setings");
            root.appendChild(setings);
        }
        else{
            //undefine
        }
    }
    /*添加控件信息*/
    public void AddButtun(Button bt){

    }
    public void AddSwitch(Switch sw){

    }
    public void AddProgressBar(ProgressBar progressBar){

    }
    /*获取header信息*/
    public String getHeaderPanelName(){

        return "l";
    }
    public String getHeaderAuthor(){

        return "l";
    }
    public String getHeaderDescription(){
        return "l";
    }
    /*获取layout 信息*/

    /*获取seting 信息*/
    public Pipeline getSetingPipeLine(){

        return Pipeline.BlueToothSerial;
    }
}

package com.phase.czq.signalpanel;

import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class PanelXmlDom {

    public enum DomMode{
        ReadFromFile,
        WriteToFile
    }
    public enum Pipeline{
        BlueToothSerial,
        USBOTGSerial,
        TCP,
        UDP,
        Undefine
    }
    private String filePath;
    private DomMode mode;
    private Pipeline pipeline;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Element root,header,layout,setings,panelName,author,desc;

    public PanelXmlDom(DomMode domMode){
        mode=domMode;
        pipeline=Pipeline.Undefine;
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
    public void XmlAddButtun(Button bt){
        Element newButtunElement = document.createElement("buttun");
        newButtunElement.setTextContent(bt.getText().toString());
        newButtunElement.setAttribute("width",Integer.toString(bt.getWidth()));
        newButtunElement.setAttribute("height",Integer.toString(bt.getHeight()));
        newButtunElement.setAttribute("X",Integer.toString(bt.getTop()));
        newButtunElement.setAttribute("Y",Integer.toString(bt.getLeft()));
        layout.appendChild(newButtunElement);
    }
    public void XmlAddSwitch(Switch sw){
        Element newSwitchElement = document.createElement("switch");
        newSwitchElement.setAttribute("width",Integer.toString(sw.getWidth()));
        newSwitchElement.setAttribute("height",Integer.toString(sw.getHeight()));
        newSwitchElement.setAttribute("X",Integer.toString(sw.getTop()));
        newSwitchElement.setAttribute("Y",Integer.toString(sw.getLeft()));
        layout.appendChild(newSwitchElement);
    }
    public void XmlAddProgressBar(ProgressBar progressBar){

    }
    /*获取header信息*/
    public String getHeaderPanelName(){
        if(panelName!=null)
            return panelName.getTextContent();
        else
            return "undefine";
    }
    public String getHeaderAuthor(){

        return author.getTextContent();
    }
    public String getHeaderDescription(){
        return desc.getTextContent();
    }
    /*获取layout 信息*/

    /*获取seting 信息*/
    public Pipeline getSetingPipeLine(){
        return Pipeline.BlueToothSerial;
    }

    /*保存XML文档*/
    public boolean saveXml(){
        if(!isLegalPanelXml()){
            return false;
        }
        try{
            //创建转换器
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty("encoding","utf-8");
            //转换
            StringWriter stringWriter = new StringWriter();
            transformer.transform(new DOMSource(document),new StreamResult(stringWriter));
            //创建文件流并输出
            FileOutputStream os = new FileOutputStream(new File(" "));
            os.write(stringWriter.toString().getBytes());
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
            return false;
        } catch (TransformerException e) {
            e.printStackTrace();
            return false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /* 判断是否是合法的XML文档 */
    private boolean isLegalPanelXml(){
        return false;
    }

    public Pipeline getPipeline() {
        return pipeline;
    }

    public void setPipeline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public void setMode(DomMode mode) {
        this.mode = mode;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

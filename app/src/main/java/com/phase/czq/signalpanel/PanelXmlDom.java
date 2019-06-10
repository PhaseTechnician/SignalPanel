package com.phase.czq.signalpanel;

import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

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
    private Element root,header,layout,setings,panelNameE, authorE, descE;

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
        }
        catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        if(mode==DomMode.ReadFromFile){
            //从文件中初始化XML
            try{
                document = documentBuilder.parse(filePath);
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if(mode==DomMode.WriteToFile){
            document = documentBuilder.newDocument();
            document.setXmlStandalone(true);
            document.setXmlVersion("1.0");
            //编写文件头
            root = document.createElement("panelName");
            document.appendChild(root);
            //header 部分
            header = document.createElement("header");
            root.appendChild(header);
            panelNameE = document.createElement("name");
            header.appendChild(panelNameE);
            authorE = document.createElement("authorE");
            header.appendChild(authorE);
            descE = document.createElement("description");
            header.appendChild(descE);
            //layout 部分
            layout = document.createElement("layout");
            root.appendChild(layout);
            //seting 部分
            setings = document.createElement("setings");
            root.appendChild(setings);
        }else{
            //undefine
            Log.e("SignalPanel","XMLDOM undefine Mode");
        }
    }

    /*添加控件信息*/
    //Buttun
    public void XmlAddButtun(Button bt){
        Element newButtunElement = document.createElement("buttun");
        newButtunElement.setTextContent(bt.getText().toString());
        newButtunElement.setAttribute("width",Integer.toString(bt.getWidth()));
        newButtunElement.setAttribute("height",Integer.toString(bt.getHeight()));
        newButtunElement.setAttribute("X",Integer.toString(bt.getTop()));
        newButtunElement.setAttribute("Y",Integer.toString(bt.getLeft()));
        newButtunElement.setIdAttribute(Integer.toString(bt.getId()),true);
        layout.appendChild(newButtunElement);
    }
    public void XmlFlushButtun(Button bt){
        //当出现重复ID，可能会出现奇怪的事情
        Element buttun = document.getElementById(Integer.toString(bt.getId()));
        if(buttun!=null){
            buttun.setAttribute("width",Integer.toString(bt.getWidth()));
            buttun.setAttribute("height",Integer.toString(bt.getHeight()));
            buttun.setAttribute("X",Integer.toString(bt.getTop()));
            buttun.setAttribute("Y",Integer.toString(bt.getLeft()));
        }
    }

    public List<PlugParams> getPlugsParams(PlugKinds plugkind){
        NodeList plugs = layout.getElementsByTagName(plugkind.toString());
        if(plugs==null){
            return null;
        }
        List<PlugParams> plugParams = new ArrayList<>();
        for (int i = 0; i <plugs.getLength() ; i++) {
            PlugParams params = new PlugParams();
            params.width=Integer.getInteger(plugs.item(i).getAttributes().getNamedItem("width").getNodeValue()) ;
            params.height=Integer.getInteger(plugs.item(i).getAttributes().getNamedItem("height").getNodeValue()) ;
            params.X=Integer.getInteger(plugs.item(i).getAttributes().getNamedItem("X").getNodeValue()) ;
            params.Y=Integer.getInteger(plugs.item(i).getAttributes().getNamedItem("Y").getNodeValue()) ;
            params.ID=Integer.getInteger(plugs.item(i).getAttributes().getNamedItem("id").getNodeValue()) ;
            plugParams.add(params);
        }
        return plugParams;
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
    /*设置header信息*/
    public void setHeaderPanelName(String panelName){
        panelNameE.setTextContent(panelName);
    }
    public void setHeaderAuthor(String author){
        authorE.setTextContent(author);
    }
    public void setHeaderDescription(String description){
        descE.setTextContent(description);
    }

    /*获取header信息*/
    public String getHeaderPanelName(){
        if(panelNameE!=null)
            return panelNameE.getTextContent();
        else
            return "undefine";
    }
    public String getHeaderAuthor(){

        return authorE.getTextContent();
    }
    public String getHeaderDescription(){
        return descE.getTextContent();
    }
    /*获取layout 信息*/

    /*获取seting 信息*/
    @Deprecated
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
            os.flush();
            os.close();
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
    @Deprecated
    private boolean isLegalPanelXml(){
        return false;
    }

    public void setMode(DomMode mode) {
        this.mode = mode;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

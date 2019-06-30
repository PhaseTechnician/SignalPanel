package com.phase.czq.signalpanel;

import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
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

    public PanelXmlDom(DomMode domMode, String xmlFilepath){
        mode=domMode;
        filePath=xmlFilepath;
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
                FileInputStream fileInputStream = new FileInputStream(filePath.substring(0,filePath.length()-4));
                document = documentBuilder.parse(fileInputStream);
                root = getElement("panel");
                header = getElement("header");
                panelNameE = getElement("panelName");
                authorE = getElement("author");
                descE = getElement("description");
                layout = getElement("layout");
                setings = getElement("setings");
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
            root = document.createElement("panel");
            document.appendChild(root);
            //header 部分
            header = document.createElement("header");
            root.appendChild(header);
            panelNameE = document.createElement("panelName");
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

    //只能查找到第一个Element,所以用来查找单例元素
    private Element getElement(String tagName){
        NodeList nodeList = document.getElementsByTagName(tagName);
        if(nodeList==null||nodeList.getLength()==0){
            return null;
        }
        Node node = nodeList.item(0);
        return (Element) node;
    }
    //获取控件参数
    public List<PlugParams> getPlugsParams(PlugKinds plugkind){
        NodeList plugs = layout.getElementsByTagName(plugkind.toString());
        if(plugs==null){
            return null;
        }
        List<PlugParams> plugParams = new ArrayList<>();
        for (int i = 0; i <plugs.getLength() ; i++) {
            PlugParams params = new PlugParams();
            params.mainString = plugs.item(i).getTextContent();
            params.width=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("width").getNodeValue()) ;
            params.height=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("height").getNodeValue()) ;
            params.X=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("X").getNodeValue()) ;
            params.Y=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("Y").getNodeValue()) ;
            params.ID=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("id").getNodeValue()) ;
            plugParams.add(params);
        }
        return plugParams;
    }
    /*添加控件信息*/
    //Buttun
    @Deprecated
    public void XmlAddButtun(PlugParams params){
        Element newButtunElement = document.createElement("buttun");
        newButtunElement.setTextContent(params.mainString);
        newButtunElement.setAttribute("width",Integer.toString(params.width));
        newButtunElement.setAttribute("height",Integer.toString(params.height));
        newButtunElement.setAttribute("X",Integer.toString(params.X));
        newButtunElement.setAttribute("Y",Integer.toString(params.Y));
        newButtunElement.setAttribute("id",Integer.toString(params.ID));
        layout.appendChild(newButtunElement);
    }
    @Deprecated
    public void XmlFlushButtun(PlugParams params){
        //当出现重复ID，可能会出现奇怪的事情
        Element buttun = document.getElementById(Integer.toString(params.ID));
        if(buttun!=null){
            buttun.setAttribute("width",Integer.toString(params.width));
            buttun.setAttribute("height",Integer.toString(params.height));
            buttun.setAttribute("X",Integer.toString(params.X));
            buttun.setAttribute("Y",Integer.toString(params.Y));
        }
    }
    //Switch
    @Deprecated
    public void XmlAddSwitch(PlugParams params){
        Element newSwitchElement = document.createElement("switche");
        newSwitchElement.setTextContent(params.mainString);
        newSwitchElement.setAttribute("width",Integer.toString(params.width));
        newSwitchElement.setAttribute("height",Integer.toString(params.height));
        newSwitchElement.setAttribute("X",Integer.toString(params.X));
        newSwitchElement.setAttribute("Y",Integer.toString(params.Y));
        newSwitchElement.setAttribute("id",Integer.toString(params.ID));
        layout.appendChild(newSwitchElement);
    }
    @Deprecated
    public void XmlFlushSwitch(PlugParams params){
        //当出现重复ID，可能会出现奇怪的事情
        Element newSwitchElement = document.getElementById(Integer.toString(params.ID));
        if(newSwitchElement!=null){
            newSwitchElement.setAttribute("width",Integer.toString(params.width));
            newSwitchElement.setAttribute("height",Integer.toString(params.height));
            newSwitchElement.setAttribute("X",Integer.toString(params.X));
            newSwitchElement.setAttribute("Y",Integer.toString(params.Y));
        }
    }
    @Deprecated
    public void XmlAddProgressBar(ProgressBar progressBar){

    }

    //Extract
    public void XmlFlushPlug(PlugParams params){
        Element plugElement = document.getElementById(Integer.toString(params.ID));
        if(plugElement!=null){
            plugElement.setAttribute("width",Integer.toString(params.width));
            plugElement.setAttribute("height",Integer.toString(params.height));
            plugElement.setAttribute("X",Integer.toString(params.X));
            plugElement.setAttribute("Y",Integer.toString(params.Y));
        }
    }
    //EXTRACT
    public void XmlAddPlug(PlugKinds kinds,PlugParams params){
        Element pluElement =  document.createElement(kinds.toString());
        pluElement.setTextContent(params.mainString);
        pluElement.setAttribute("width",Integer.toString(params.width));
        pluElement.setAttribute("height",Integer.toString(params.height));
        pluElement.setAttribute("X",Integer.toString(params.X));
        pluElement.setAttribute("Y",Integer.toString(params.Y));
        pluElement.setAttribute("id",Integer.toString(params.ID));
        layout.appendChild(pluElement);
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
    //获取控件中最大的ID
    public int getMaxID(){
        NodeList allPlugs = layout.getChildNodes();
        if(allPlugs!=null){
            int maxID=0;
            for(int i=0;i<allPlugs.getLength();i++){
                int idNum =Integer.getInteger(allPlugs.item(i).getAttributes().getNamedItem("id").getNodeValue());
                if(idNum>maxID){
                    maxID=idNum;
                }
            }
            return maxID;
        }else {
            return 0;
        }
    }
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
            FileOutputStream os = new FileOutputStream(new File(filePath));
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
        return true;
    }

    @Deprecated
    public void setMode(DomMode mode) {
        this.mode = mode;
    }
    @Deprecated
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}

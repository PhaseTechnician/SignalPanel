package com.phase.czq.signalpanel.Panel;

import android.util.Log;

import com.phase.czq.signalpanel.Pipe.Adapter.AdapterKinds;
import com.phase.czq.signalpanel.plugs.PlugKinds;
import com.phase.czq.signalpanel.plugs.PlugParams;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
    private String filePath;
    private DomMode mode;
    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;
    private Document document;
    private Element root,header,layout,setings,panelNameE, authorE, descE;

    public PanelXmlDom(DomMode domMode, String xmlFilepath){
        mode=domMode;
        filePath=xmlFilepath;
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
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SAXException e) {
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
            authorE = document.createElement("author");
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
            plugParams.add(getPluParamsFromNodeList(i,plugs));
        }
        return plugParams;
    }

    public PlugParams getPlugParamsByID(int ID){
        NodeList nodeList = layout.getChildNodes();
        for (int i=0;i<nodeList.getLength();i++) {
            if(nodeList.item(i).getAttributes().getNamedItem("id").getNodeValue()==Integer.toString(ID)){
                return getPluParamsFromNodeList(i,nodeList);
            }
        }
        return null;
    }

    private PlugParams getPluParamsFromNodeList(int i,NodeList plugs){
        PlugParams params = new PlugParams();
        params.mainString = plugs.item(i).getTextContent();
        params.spareString = plugs.item(i).getAttributes().getNamedItem("sparestring").getNodeValue();
        params.width=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("width").getNodeValue()) ;
        params.height=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("height").getNodeValue()) ;
        params.X=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("X").getNodeValue()) ;
        params.Y=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("Y").getNodeValue()) ;
        params.ID=Integer.valueOf(plugs.item(i).getAttributes().getNamedItem("id").getNodeValue()) ;
        params.srcs = plugs.item(i).getAttributes().getNamedItem("src").getNodeValue();
        params.modes = plugs.item(i).getAttributes().getNamedItem("mode").getNodeValue();
        params.positiveKey = plugs.item(i).getAttributes().getNamedItem("positivekey").getNodeValue();
        params.positiveEnable = Boolean.valueOf(plugs.item(i).getAttributes().getNamedItem("positiveenable").getNodeValue());
        params.negativeKey = plugs.item(i).getAttributes().getNamedItem("negativekey").getNodeValue();
        params.negativeEnable = Boolean.valueOf(plugs.item(i).getAttributes().getNamedItem("negativeenable").getNodeValue());
        return  params;
    }
    /*添加控件信息*/

    //Extract
    public void XmlFlushPlugBasic(PlugParams params){
        Element plugElement = document.getElementById(Integer.toString(params.ID));
        if(plugElement!=null){
            plugElement.setAttribute("width",Integer.toString(params.width));
            plugElement.setAttribute("height",Integer.toString(params.height));
            plugElement.setAttribute("X",Integer.toString(params.X));
            plugElement.setAttribute("Y",Integer.toString(params.Y));
        }
    }
    public void XmlFlushPlugReact(PlugParams params){
        Element plugElement = document.getElementById(Integer.toString(params.ID));
        if(plugElement!=null){
            plugElement.setTextContent(params.mainString);
            plugElement.setAttribute("sparestring",params.spareString);
            plugElement.setAttribute("positivekey",params.positiveKey);
            plugElement.setAttribute("positiveenable",Boolean.toString(params.positiveEnable));
            plugElement.setAttribute("negativekey",params.negativeKey);
            plugElement.setAttribute("negativeenable",Boolean.toString(params.negativeEnable));
            plugElement.setAttribute("src",params.srcs);
            plugElement.setAttribute("mode",params.modes);
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
        pluElement.setAttribute("sparestring",params.spareString);
        pluElement.setAttribute("positivekey",params.positiveKey);
        pluElement.setAttribute("positiveenable",Boolean.toString(params.positiveEnable));
        pluElement.setAttribute("negativekey",params.negativeKey);
        pluElement.setAttribute("negativeenable",Boolean.toString(params.negativeEnable));
        pluElement.setAttribute("src",params.srcs);
        pluElement.setAttribute("mode",params.modes);
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
                int idNum =Integer.valueOf(allPlugs.item(i).getAttributes().getNamedItem("id").getNodeValue());
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
    /*管理Adapt*/
    public boolean hasAdapt(){
        NodeList setingEs = setings.getElementsByTagName("adapt");
        return setingEs.getLength() != 0;
    }
    public void setAdapt(String adapt, AdapterKinds kinds){
        Element adaptE = document.createElement("adapt");
        adaptE.setTextContent(adapt);
        adaptE.setAttribute("adapter",kinds.toString());
        setings.appendChild(adaptE);
    }
    public String getApapt(){
        NodeList setingEs = setings.getElementsByTagName("adapt");
        if(setingEs.getLength()!=0){
            return setingEs.item(0).getTextContent();
        }
        return null;
    }
    public AdapterKinds getAdapter(){
        NodeList setingEs = setings.getElementsByTagName("adapt");
        if(setingEs.getLength()!=0){
            return AdapterKinds.getAdapterKind(setingEs.item(0).getAttributes().getNamedItem("adapter").getNodeValue());
        }
        return null;
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

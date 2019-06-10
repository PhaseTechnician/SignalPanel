package com.phase.czq.signalpanel;

import android.content.Context;
import android.provider.DocumentsContract;
import android.util.Log;
import android.util.Xml;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
//仅仅解析文件的header
public class Panel {
    private Context context;
    private String XMLpath;
    private String panelName="undefine";
    private String author="undefine";
    private String desc="undefine";
    //对应的图像
    private String IMAGE;

    public Panel(String panelName,Context context){
        if(panelName==""){
            //合适的文件名
            this.XMLpath = "NULL";
        }
        this.XMLpath=context.getFilesDir().getAbsolutePath()+File.separator+"panelXMLs"+File.separator+panelName+".xml";
        this.context=context;
        decodeXMLHeader();
    }

    //从XML文件获取概述信息
    private void decodeXMLHeader(){
        try{
            FileInputStream fileInputStream = new FileInputStream(XMLpath.substring(0,XMLpath.length()-4));
            DocumentBuilderFactory documentBuilderFactory=DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder=documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fileInputStream);

            NodeList nl=document.getElementsByTagName("header");
            if(nl.getLength()==0){
                Toast.makeText(context,"Header0",Toast.LENGTH_SHORT).show();
                return;
            }

            NodeList namel=document.getElementsByTagName("panelName");
            if(namel.getLength()==0)
            {
                Toast.makeText(context,"name0",Toast.LENGTH_SHORT).show();
               return;
            }
            Node nameN = namel.item(0);
            panelName = nameN.getTextContent();

            NodeList authorl=document.getElementsByTagName("author");
            if(authorl.getLength()==0)
            {
                Toast.makeText(context,"author0",Toast.LENGTH_SHORT).show();
                return;
            }
            Node authorN = authorl.item(0);
            author = authorN.getTextContent();

            NodeList descl=document.getElementsByTagName("description");
            if(descl.getLength()==0)
            {
                Toast.makeText(context,"desc0",Toast.LENGTH_SHORT).show();
                return;
            }
            Node descN = descl.item(0);
            desc = descN.getTextContent();
            //释放资源
            fileInputStream.close();
        }
        catch (FileNotFoundException ex) {
            Log.e("panelDecoder",ex.getMessage());
        }catch (ParserConfigurationException ex){
            Log.e("panelDecoder",ex.getMessage());
        }catch (SAXException ex){
            Log.e("panelDecoder",ex.getMessage());
        }catch(IOException ex){
            Log.e("panelDecoder",ex.getMessage());
        }
    }
    //保存Panel信息到XML
    public void save(){
        try {
            File file = new File(XMLpath);
            if(!file.exists()) {
                if(file.createNewFile())
                    Log.e("panelCreat","creat new xml file success");
                else
                    Log.e("panelCreat","creat xml file fail");
            }
            else{
                Log.e("panelCreat","file has exist");
            }
            FileOutputStream fileOutputStream=new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream,"utf-8");
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"panel");
            serializer.startTag(null,"header");

            serializer.startTag(null,"panelName");
            serializer.text(panelName);
            serializer.endTag(null,"panelName");

            serializer.startTag(null,"author");
            serializer.text("unknow author");
            serializer.endTag(null,"author");

            serializer.startTag(null,"description");
            serializer.text("This is an empty panel");
            serializer.endTag(null,"description");
            serializer.endTag(null,"header");

            serializer.startTag(null,"layout");
            serializer.endTag(null,"layout");

            serializer.startTag(null,"seting");
            serializer.endTag(null,"seting");

            serializer.endTag(null,"panel");
            serializer.endDocument();

            fileOutputStream.flush();
            fileOutputStream.close();

        }catch (IllegalArgumentException e) {
            Log.e("panelCreat",e.getMessage()+e);
        } catch (IllegalStateException e) {
            Log.e("panelCreat",e.getMessage()+e);
        } catch (IOException e) {
            Log.e("panelCreat",e.getMessage()+e);
        }
        Log.e("panelCreat", "save: FINISH");
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

    public String getXMLpath() {
        return XMLpath;
    }
}

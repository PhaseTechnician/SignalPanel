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

public class Panel {
    private Context context;
    private String XMLpath;
    private String panelName;
    private String author;
    private String desc;
    //对应的图像
    private String IMAGE;

    public Panel(String path,Context context){
        this.XMLpath=path;
        this.context=context;
        decodeXML();
    }

    public void setXMLpath(String XMLpath) {
        this.XMLpath = XMLpath;
        decodeXML();
    }

    //从XML文件获取概述信息
    private void decodeXML(){
        //默认值
        panelName="No name";
        author="dont know";
        desc="empty";
        try{
            FileInputStream fileInputStream = new FileInputStream(XMLpath);
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
        }
        catch (FileNotFoundException ex)
        {
            ex.printStackTrace();
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }catch (ParserConfigurationException ex){
            ex.printStackTrace();
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }catch (SAXException ex){
            ex.printStackTrace();
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }catch(IOException ex){
            ex.printStackTrace();
            Toast.makeText(context,ex.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    //保存Panel信息到XML
    public void save(){
        String appFilePath = context.getFilesDir().getAbsolutePath();
        String filePath = appFilePath+File.separator+"EXAMPLE.xml";
        XMLpath = filePath;
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(filePath);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOutputStream,"utf-8");
            serializer.startDocument("utf-8",true);
            serializer.startTag(null,"panel");
            serializer.startTag(null,"header");

            serializer.startTag(null,"panelName");
            serializer.text("EXAMPLE");
            serializer.endTag(null,"panelName");

            serializer.startTag(null,"author");
            serializer.text("czq");
            serializer.endTag(null,"author");

            serializer.startTag(null,"description");
            serializer.text("This is an example panel");
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
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
        }
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

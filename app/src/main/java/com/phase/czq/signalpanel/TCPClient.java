package com.phase.czq.signalpanel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
//考虑当前并没有大量的数据传输，考虑放弃多线程的操作
public class TCPClient {

    private String address;
    private int port;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    TCPClient(String Address, int Port){
        address = Address;
        port = Port;
    }

    public boolean initStream(){
        try {
            socket = new Socket(address,port);
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return  false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void sendMessage(String message){
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public StringBuffer getMessage(String charset){
        byte[] buf = new byte[1024];
        StringBuffer sb = new StringBuffer();
        int len = 0;
        try {
            while ((len=inputStream.read(buf)) != -1) {
                sb.append(new String(buf, 0, len, charset));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb;
    }

    public boolean isReceived(){
        if(inputStream==null){
            return false;
        }
        try {
            if(inputStream.available()!=0){
                return true;
            }
            else {
                return  false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void close(){
        if (socket!=null){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(inputStream!=null){
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(outputStream!=null){
            try {
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

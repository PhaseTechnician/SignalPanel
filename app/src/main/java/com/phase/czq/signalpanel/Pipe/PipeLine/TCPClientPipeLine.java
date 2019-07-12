package com.phase.czq.signalpanel.Pipe.PipeLine;

import com.phase.czq.signalpanel.tools_value.ValuePool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

//考虑当前并没有大量的数据传输，考虑放弃多线程的操作
public class TCPClientPipeLine extends PipeLine {

    private String address;
    private int port;
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;

    public TCPClientPipeLine(String Address, int Port){
        address = Address;
        port = Port;
    }

    @Override
    public boolean open(){
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

    @Override
    public void sendMessage(String message){
        try {
            outputStream.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isReceived() {
        try {
            if(inputStream.available()!=0)
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public byte[] getReceive() {
        byte[] buf = new byte[ValuePool.Byte_Buffer_Size];
        try {
            inputStream.read(buf);
            return buf;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
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

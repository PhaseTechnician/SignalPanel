package com.phase.czq.signalpanel.Pipe.PipeLine;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class TCPServer {

    private int port;
    private Socket socket;
    private Context context;
    private InputStream inputStream;
    private OutputStream outputStream;

    TCPServer(int Port,Context mcontext){
        port = Port;
        context = mcontext;
    }

    public boolean initStream(){
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            Toast.makeText(context,"等待接入，IP:"+ InetAddress.getLocalHost().getHostAddress() +" Port:"+Integer.toString(port),Toast.LENGTH_LONG);
            socket = serverSocket.accept();
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return  true;
    }
}

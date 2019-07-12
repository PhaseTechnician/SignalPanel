package com.phase.czq.signalpanel.Pipe.PipeLine;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.phase.czq.signalpanel.R;
import com.phase.czq.signalpanel.tools_value.ValuePool;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static app.akexorcist.bluetotohspp.library.BluetoothState.REQUEST_ENABLE_BT;

//通过该类进行蓝牙串口通讯
public class BlueToothPipeLine extends PipeLine{
    private BluetoothDevice choseDevice = null;
    private BluetoothSocket socket = null;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;

    public BlueToothPipeLine(final Context context){
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            Toast.makeText(context,"unsupport",Toast.LENGTH_LONG).show();
            return;
        }
        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            context.startActivity(enableBtIntent);
            Toast.makeText(context,"you dont accept,please retry",Toast.LENGTH_LONG).show();
        }
        final Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        if (pairedDevices.size() > 0) {
            final String[] deviceNames = new String[pairedDevices.size()];
            int index = 0;
            for (BluetoothDevice device : pairedDevices) {
                deviceNames[index] = device.getName();
                index++;
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("BT C");
            builder.setIcon(R.drawable.ic_bluetooth_normal);
            builder.setSingleChoiceItems(deviceNames, 0, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (BluetoothDevice device : pairedDevices) {
                        if(device.getName().equals(deviceNames[which])){
                            choseDevice = device;
                            break;
                        }
                    }
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(R.string.canel_buttun,null);
            builder.show();
        }else {
            Toast.makeText(context,"no paired device",Toast.LENGTH_LONG).show();
            return;
        }
    }

    @Override
    public void sendMessage(String message) {
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
    public void close() {
        try {
            inputStream.close();
            outputStream.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean open() {
        if(choseDevice==null){
            return false;
        }
        try {
            socket = choseDevice.createRfcommSocketToServiceRecord(ValuePool.UUID_SPP);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        try {
            socket.connect();
        } catch (IOException connectException) {
            try {
                socket.close();
            } catch (IOException closeException) {
                return false;
            }
            return false;
        }
        try {
            outputStream = socket.getOutputStream();
            inputStream = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}

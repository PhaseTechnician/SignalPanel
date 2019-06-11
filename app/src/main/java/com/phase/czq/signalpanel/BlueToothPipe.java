package com.phase.czq.signalpanel;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

//通过该类进行蓝牙串口通讯
public class BlueToothPipe {
    //
    Context context;
    //BlueTooth
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;
    //
    boolean isPair =false;

    public void Send(char c){

    }

    public void Send(String st){

    }

    @Deprecated
    public void Send(ByteBuffer bf){

    }

    public BlueToothPipe(Context context){
        this.context =context;
        BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothManager==null)
        {
            Log.e("BlueTooth","BlueToothManager init fail");
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(bluetoothAdapter==null){
            Log.e("BlueTooth","BlueToothAdadpter init fail");
        }
        //开始扫描设备
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                //super.onBatchScanResults(results);
                //显示列表
                //results.get(1).getDevice()

                //isPair=true;
            }

            @Override
            public void onScanFailed(int errorCode) {
                //super.onScanFailed(errorCode);
                Log.i("BlueTooth","scale fail error :"+errorCode);
            }
        });
        android.os.Handler stopScanHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        //超时操作
                        break;
                }
            }
        };
        stopScanHandler.sendEmptyMessageDelayed(0,10000);
        //尝试连接
        bluetoothGatt =bluetoothDevice.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if(status== BluetoothProfile.STATE_CONNECTED){
                    //
                }else if(status == BluetoothProfile.STATE_DISCONNECTED){
                    //
                }
            }
        });
        if(bluetoothGatt.connect()){
            Log.i("BlueTooth","Success connect device"+bluetoothDevice.getName());
        }
    }

    static public boolean enableBluetooth(@NonNull Context appContext){
        BluetoothManager bluetoothManager = (BluetoothManager)appContext.getSystemService(Context.BLUETOOTH_SERVICE);
        if(bluetoothManager!=null){
            BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
            if(bluetoothAdapter!=null){
                if(!bluetoothAdapter.isEnabled()){
                    bluetoothAdapter.enable();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isLeagel(){

        return false;
    }

}

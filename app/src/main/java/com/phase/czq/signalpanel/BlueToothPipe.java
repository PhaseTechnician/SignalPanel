package com.phase.czq.signalpanel;

import android.Manifest;
import android.app.Activity;
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
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;
@Deprecated
//通过该类进行蓝牙串口通讯
public class BlueToothPipe{
    //
    Context context;
    //BlueTooth
    BluetoothAdapter bluetoothAdapter;
    BluetoothLeScanner bluetoothLeScanner;
    BluetoothDevice bluetoothDevice;
    BluetoothGatt bluetoothGatt;
    //
    boolean isPair =false;
    //
    BlueToothUIInterface uiInterface;

    public BlueToothPipe(@NonNull Context contextx){
        this.context =contextx;
        BluetoothManager bluetoothManager = (BluetoothManager)context.getSystemService(Context.BLUETOOTH_SERVICE);
        requestPermission();
        if(bluetoothManager==null)
        {
            Log.e("BlueTooth","BlueToothManager init fail");
        }
        bluetoothAdapter = bluetoothManager.getAdapter();
        if(bluetoothAdapter==null){
            Log.e("BlueTooth","BlueToothAdadpter init fail");
        }

    }

    public void Send(char c){

    }

    public void Send(String st){

    }

    @Deprecated
    public void Send(ByteBuffer bf){

    }

    interface BlueToothUIInterface{
        public void choseDevice(BluetoothDevice[] bluetoothDevices);
        public void timeOut();
    }

    //设置UI接口
    public void setUiInterface(BlueToothUIInterface blueToothUIInterface){
        this.uiInterface = blueToothUIInterface;
    }
    //初始化设备
    public boolean init(){
        return true;
    }

    //扫描设备
    public void scan(){
        //开始扫描设备
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();
        bluetoothLeScanner.startScan(new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                super.onScanResult(callbackType, result);
                Toast.makeText(context,result.getDevice().getName(),Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                //super.onBatchScanResults(results);
                //显示列表
                //results.get(1).getDevice()
                //isPair=true;
                if(results!=null){
                    BluetoothDevice[] devices = new BluetoothDevice[results.size()];
                    for(int i=0;i<results.size();i++){
                        devices[i]=results.get(i).getDevice();
                        Toast.makeText(context,"123",Toast.LENGTH_SHORT).show();
                    }
                    uiInterface.choseDevice(devices);
                }
            }

            @Override
            public void onScanFailed(int errorCode) {
                //super.onScanFailed(errorCode);
                Log.i("BlueTooth","scale fail error :"+errorCode);
            }
        });
        //超时操作
        android.os.Handler stopScanHandler = new android.os.Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        //超时操作
                        uiInterface.timeOut();
                        Toast.makeText(context,"time out",Toast.LENGTH_SHORT).show();
                        bluetoothLeScanner.stopScan(new ScanCallback() {
                            @Override
                            public void onScanResult(int callbackType, ScanResult result) {
                                super.onScanResult(callbackType, result);
                            }

                            @Override
                            public void onBatchScanResults(List<ScanResult> results) {
                                super.onBatchScanResults(results);
                            }

                            @Override
                            public void onScanFailed(int errorCode) {
                                super.onScanFailed(errorCode);
                            }
                        });
                        break;
                }
            }
        };
        stopScanHandler.sendEmptyMessageDelayed(0,10000);
    }

    //连接指定设备
    public boolean connect(BluetoothDevice bluetoothDevice){
        this.bluetoothDevice = bluetoothDevice;
        //尝试连接
        bluetoothGatt =bluetoothDevice.connectGatt(context, false, new BluetoothGattCallback() {
            @Override
            public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                if(status== BluetoothProfile.STATE_CONNECTED){
                    gatt.discoverServices(); //执行到这里其实蓝牙已经连接成功了

                }else if(status == BluetoothProfile.STATE_DISCONNECTED){
                    //尝试重连
                }
            }
        });
        if(bluetoothGatt == null){
            return false;
        }
        if(bluetoothGatt.connect()){
            Log.i("BlueTooth","Success connect device"+bluetoothDevice.getName());
            return  true;
        }else{
            return false;
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

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                Log.w("BlueTooth", "没有权限，请求权限");
                return;
            }
            Log.w("BlueTooth", "已有定位权限");
        }
        //做下面该做的事
    }
}

//link https://www.cnblogs.com/xxzjyf/p/x_x_z_j_y_f.html
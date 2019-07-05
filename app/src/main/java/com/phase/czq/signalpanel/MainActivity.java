package com.phase.czq.signalpanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.phase.czq.signalpanel.ui.login.LoginActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private RecycleViewAdapter RVA;
    private Menu mMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        setContentView(R.layout.activity_main);
        //尝试创建XML文档路径
        tryMakeDirAndList(this.getFilesDir().getAbsolutePath()+File.separator+"panelXMLs");
        //尝试创建IMG文档路径
        tryMakeDirAndList(this.getFilesDir().getAbsolutePath()+File.separator+"panelImgs");
        //设置RecycleView
        RecyclerView RV=(RecyclerView)findViewById(R.id.recycleView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        RV.setLayoutManager(layoutManager);
        layoutManager.setOrientation(OrientationHelper.VERTICAL);

        RVA = new RecycleViewAdapter(this);
        RV.setAdapter(RVA);
        RVA.reloadItems();

        //设置工具栏
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //浮动按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加新的Panel 并进入编辑Config
                newPanelSetingDialog("czq");
            }
        });
        //
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ValuePool.init(this);
    }
//213
    @Override
    protected void onRestart() {
        super.onRestart();
        //更新菜单
        upLoadOptionsMenu();
        //更新items
        RVA.reloadItems();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ValuePool.spp!=null)
        ValuePool.spp.stopService();
        if(ValuePool.tcpClient!=null){
            ValuePool.tcpClient.close();
        }
    }

    //尝试添加一个路径
    private void tryMakeDir(String Path){
        File file = new File(Path);
        if(file.exists()){
            //路径存在
            Log.e("FilePath",Path+" Exist");
        }
        else{
            //路径不存在
            Log.e("FilePath",Path+" Not found");
            file.mkdirs();
            Log.e("FilePath",Path+" Creat");
        }
    }
    //尝试添加路径并进行列举
    private void tryMakeDirAndList(String path){
        File file = new File(path);
        if(file.exists()){
            //路径存在
            Log.e("FilePath","path exist");
            File[] files = file.listFiles();
            if(files!=null) {
                Log.e("FilePath",files.length+" files found");
                for(int i=0;i<files.length;i++){
                    Log.e("FilePath",files[i].getName().toString());
                }
            }
            else {
                Log.e("FilePath","0 files found");
            }
        }
        else{
            //路径不存在
            Log.e("FilePath","path not found");
            file.mkdirs();
            Log.e("FilePath","path has creat");
        }
    }

    //创建一个新面板打开一个对话框
    private void newPanelSetingDialog(String defaultAuthor){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("add new plane");
        builder.setIcon(R.drawable.ic_add);
        View view = LayoutInflater.from(this).inflate(R.layout.new_panel_dialog,null);
        builder.setView(view);
        final EditText eName = view.findViewById(R.id.input_panel_name);
        final EditText eAuthor = view.findViewById(R.id.input_panel_author);
        final EditText eDesc = view.findViewById(R.id.input_panel_desc);
        eAuthor.setText(defaultAuthor);
        builder.setPositiveButton("creat", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(MainActivity.this,ConfigPanelActivity.class);
                intent.putExtra("SignalPanel.panelName",eName.getText().toString());
                intent.putExtra("SignalPanel.author",eAuthor.getText().toString());
                intent.putExtra("SignalPanel.description",eDesc.getText().toString());
                intent.putExtra("SignalPanel.Creat_Change","Creat");
                startActivity(intent);
            }
        });
        builder.setNegativeButton("cancel",null);
        builder.show();
    }

    //创建一个临时的WIFI连接窗口
    private void newWifiConnectDialog(){
        ValuePool.tcpClient = new TCPClient(ValuePool.getString("pipe_wifi_address"),ValuePool.getInt("pipe_wifi_port"));
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_wifi_normal);
        builder.setTitle("connecting...");
        builder.setMessage("IP:"+ValuePool.getString("pipe_wifi_address")+"Port:"+ValuePool.getInt("pipe_wifi_port"));
        builder.setNegativeButton("canel",null);
        builder.show();
        if(ValuePool.tcpClient == null){
            builder.setMessage("unable00");
            builder.show();
            return;
        }
        if(ValuePool.tcpClient.initStream()){
            builder.setMessage("success");
            builder.show();
            ValuePool.wifiPipeConnect = true;
        }
    }

    //创建一个新的蓝牙连接设置对话框
    private void newBlueToothConnectDialog() {
        ValuePool.spp = new BluetoothSPP(this);
        if(!ValuePool.spp.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(),"Bluetooth is not available",Toast.LENGTH_SHORT).show();
            finish();
        }
        ValuePool.spp.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();

            }

            public void onDeviceDisconnected() {
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() {
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });
        Intent intent = new Intent(this, DeviceList.class);
        intent.putExtra("bluetooth_devices", "Bluetooth devices");
        intent.putExtra("no_devices_found", "No device");
        intent.putExtra("scanning", "Scanning");
        intent.putExtra("scan_for_devices", "Search");
        intent.putExtra("select_device", "Select");
        intent.putExtra("layout_list", R.layout.device_layout_list);
        intent.putExtra("layout_text", R.layout.device_layout_text);
        startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case BluetoothState.REQUEST_CONNECT_DEVICE:
                if(resultCode == Activity.RESULT_OK)
                    ValuePool.spp.connect(data);
                break;
            case BluetoothState.REQUEST_ENABLE_BT:
                if(resultCode == Activity.RESULT_OK) {
                    ValuePool.spp.setupService();
                } else {
                    Toast.makeText(getApplicationContext()
                            , "Bluetooth was not enabled."
                            , Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            case RequestCodes.ImportFile:
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri=data.getData();
                    Toast.makeText(this,uri.getPath(),Toast.LENGTH_LONG);
                    break;
                }
                else {
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        mMenu = menu;
        upLoadOptionsMenu();
        return true;
    }

    //更新菜单上的pipe按钮
    private void upLoadOptionsMenu(){
        MenuItem menuItemBT = mMenu.findItem(R.id.pipe_BT);
        if(ValuePool.getBoolean("pipe_bluetooth")){
            menuItemBT.setVisible(true);
        }else{
            menuItemBT.setVisible(false);
        }
        MenuItem menuItemOTG = mMenu.findItem(R.id.pipe_OTG);
        if(ValuePool.getBoolean("pipe_otg")){
            menuItemOTG.setVisible(true);
        }else{
            menuItemOTG.setVisible(false);
        }
        MenuItem menuItemWIFI = mMenu.findItem(R.id.pipe_WIFI);
        if(ValuePool.getBoolean("pipe_wifi")){
            menuItemWIFI.setVisible(true);
        }else{
            menuItemWIFI.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.pipe_BT) {
            //ValuePool.blueToothPipe = new BlueToothPipe(this);
            newBlueToothConnectDialog();
            return true;
        }else if(id==R.id.pipe_OTG){

            return true;
        }else if(id==R.id.pipe_WIFI){
            newWifiConnectDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Deprecated
    private void chosefile(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("file/*.txt");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_import) {
            chosefile();
        } else if (id == R.id.nav_export) {

        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_panel) {

        } else if (id == R.id.nav_seting) {
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if(id==R.id.nav_login){
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

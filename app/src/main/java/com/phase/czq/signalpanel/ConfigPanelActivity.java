package com.phase.czq.signalpanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.phase.czq.signalpanel.plugs.Joystick;

import java.io.File;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConfigPanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //用于确定是否有新的改变，从而判断是否需要保存
    private boolean newChanges = false;
    //保留一个Panel的DOM实例，使用它读取和保存XML文档
    private PanelXmlDom panelXmlDom;
    //主要布局
    private ConstraintLayout mainLayout;
    //ID计数 不应该出现重复的ID
    private int IDcount=0;

    View.OnTouchListener dragListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return false;
                case MotionEvent.ACTION_MOVE:
                    newChanges = true;
                    v.setTranslationX(event.getRawX());
                    v.setTranslationY(event.getRawY());
                    flushPlug(v);
                    return false;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    break;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        setContentView(R.layout.activity_config_panel);
        mainLayout=(ConstraintLayout)findViewById(R.id.config_layout_main);

        //隐藏控制栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //设置侧边栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_config);
        navigationView.setNavigationItemSelectedListener(this);
        //从Intent中获取基本信息填充到nav_drawer中
        if(navigationView.getHeaderCount() > 0) {//From 雪兰灵莹 https://blog.csdn.net/xuelanlingying/article/details/79884592
            View header = navigationView.getHeaderView(0);
            TextView panelNameTextView = header.findViewById(R.id.nav_config_panelname);
            TextView descTextView = header.findViewById(R.id.nav_config_desc);
            panelNameTextView.setText(getIntent().getStringExtra("SignalPanel.panelName"));
            descTextView.setText(getIntent().getStringExtra("SignalPanel.description"));
        }
        //准备对应的XML对象
        if(getIntent().getStringExtra("SignalPanel.Creat_Change").equals("Creat")){
            //新建流程
            panelXmlDom = new PanelXmlDom(PanelXmlDom.DomMode.WriteToFile,
                    this.getFilesDir().getAbsolutePath()+ File.separator+"panelXMLs"+File.separator+getIntent().getStringExtra("SignalPanel.panelName"));
            panelXmlDom.setHeaderPanelName(getIntent().getStringExtra("SignalPanel.panelName"));
            panelXmlDom.setHeaderAuthor(getIntent().getStringExtra("SignalPanel.author"));
            panelXmlDom.setHeaderDescription(getIntent().getStringExtra("SignalPanel.description"));
            IDcount = 0;
        }
        else {
//ERRORs
            //修改流程
            panelXmlDom = new PanelXmlDom(PanelXmlDom.DomMode.ReadFromFile,
                    this.getFilesDir().getAbsolutePath()+ File.separator+"panelXMLs"+File.separator+getIntent().getStringExtra("SignalPanel.panelName"));
            //更新最大ID，避免重复
            IDcount=panelXmlDom.getMaxID();
            //创建控件
            List<PlugParams> buttuns = panelXmlDom.getPlugsParams(PlugKinds.buttun);
            if(buttuns!=null){
                for (PlugParams buttun:buttuns) {

                }
            }
            /*当创建完成之后*/
        }
    }


    //EXTRACT
    @NewPlugAttation
    private void addPlug(PlugKinds plugKinds, PlugParams plugParams)
    {
        View view = null;
        switch(plugKinds.toString())
        {
            case "buttun":
                final Button newButtun = new Button(this);
                newButtun.setText(plugParams.mainString);
                view = newButtun;
                break;
            case "switche":
                Switch newSwitch=new Switch(this);
                newSwitch.setText(plugParams.mainString);
                view = newSwitch;
                break;
            case "progressbar":
                ProgressBar newPB = new ProgressBar(this);
                //newPB.setTooltipText(plugParams.mainString);
                view = newPB;
                break;
            case "seekbar":
                SeekBar newSB = new SeekBar(this);
                //newSB.setTooltipText(plugParams.mainString);
                newSB.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        plugSetingDialog(seekBar.getId());
                    }
                });
                view = newSB;
                break;
            case "imageview":
                ImageView newImgView = new ImageView(this);
                newImgView.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
                view = newImgView;
                break;
            case "joystick":
                Joystick newJoystick = new Joystick(this);
                newJoystick.setLocked(true);
                view = newJoystick;
                break;
            case "textview":
                TextView newTV = new TextView(this);
                newTV.setText(plugParams.mainString);
                view = newTV;
                break;
        }
        if(view==null)
        {
            return;
        }
        mainLayout.addView(view);
        view.setClickable(true);
        view.setTag(plugKinds);
        applyBasicPlugParam(view,plugParams);
        view.setOnTouchListener(dragListener);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                plugSetingDialog(view.getId());
            }
        });
        panelXmlDom.XmlAddPlug(plugKinds,plugParams.changeID(view.getId()));
        Toast.makeText(this,"NEW "+plugKinds.toString()+" Add",Toast.LENGTH_SHORT).show();
    }
    //EXTRACT
    //当控件被拖动时或修改大小时，更新控件信息
    private void flushPlug(View view){
        panelXmlDom.XmlFlushPlugBasic(new PlugParams("unuse",view.getWidth(),view.getHeight(),(int)view.getTranslationX(),(int)view.getTranslationY(),view.getId()));
    }


    private boolean savePanel(){
        newChanges=false;
        if(panelXmlDom.saveXml()){
            return true;
        }
        return false;
    }

    @Override@NewPlugAttation
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            //input
            case R.id.nav_buttun:
                addPlug(PlugKinds.buttun,ValuePool.defaultParam);
                break;
            case R.id.nav_switch:
                addPlug(PlugKinds.switche,ValuePool.defaultParam);
                break;
            case R.id.nav_sbr:
                addPlug(PlugKinds.seekbar,ValuePool.defaultParam);
                break;
            case R.id.nav_joystick:
                addPlug(PlugKinds.joystick,ValuePool.defaultParam);
                break;
            //display
            case R.id.nav_textview:
                addPlug(PlugKinds.textview,ValuePool.defaultParam);
                break;
            case R.id.nav_imageview:
                addPlug(PlugKinds.imageview,ValuePool.defaultParam);
                break;
            case R.id.nav_pbr:
                addPlug(PlugKinds.progressbar,ValuePool.defaultParam);
                break;
            //pipe
            case R.id.nav_otg_pipe:
                break;
            //File
            case R.id.nav_save:
                Log.e("XMLSAVE","START SAVE");
                if(savePanel()){
                    Toast.makeText(this,"Success save file",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"Some errors happened",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_upload:
                break;
        }
        return false;
    }

    @Deprecated
    private Bitmap savePanelImg(){
        View mainView = findViewById(R.id.config_layout_main);
        mainView.setDrawingCacheEnabled(true);
        mainView.buildDrawingCache();
        Rect rect = new Rect();
        mainView.getWindowVisibleDisplayFrame(rect);
        int stateBarHeight = rect.top;

        WindowManager windowManager = this.getWindowManager();

        DisplayMetrics outMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(outMetrics);
        int width = outMetrics.widthPixels;
        int height = outMetrics.heightPixels;

        Bitmap bitmap = Bitmap.createBitmap(mainView.getDrawingCache(), 0, stateBarHeight, width,
                height - stateBarHeight);
        //销毁缓存信息
        mainView.destroyDrawingCache();
        mainView.setDrawingCacheEnabled(false);

        //图形压缩

        return  bitmap;
    }

    private void plugSetingDialog(int ID){
        Intent intent = new Intent(this,ParamsSetingActivity.class);
        //intent.putExtra("plugID",ID);
        intent.putExtra("params",panelXmlDom.getPlugParamsByID(ID));
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode==1){
            if(resultCode == Activity.RESULT_OK){ //
                PlugParams params = new PlugParams();
                params.mainString = intent.getStringExtra("mainstring");
                params.spareString = intent.getStringExtra("sparestring");
                params.modes = intent.getStringExtra("mode");
                params.srcs = intent.getStringExtra("src");
                params.positiveKey = intent.getStringExtra("positivekey");
                params.negativeKey = intent.getStringExtra("negativekey");
                params.positiveEnable = intent.getBooleanExtra("positiveenable",true);
                params.negativeEnable = intent.getBooleanExtra("negativeenable",true);
                params.ID = intent.getIntExtra("id",-1);
                panelXmlDom.XmlFlushPlugReact(params);
                flushPlug(params);
            }
        }
    }

    private void applyBasicPlugParam(@NonNull View view, @NonNull PlugParams plugParams){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = plugParams.width;
        params.height = plugParams.height;
        view.setLayoutParams(params);
        view.setX(plugParams.X);
        view.setY(plugParams.Y);
        if(plugParams.ID!=-1) {
            view.setId(plugParams.ID);
        }else {
            IDcount++;
            view.setId(IDcount);
        }
    }

    @NewPlugAttation
    private void flushPlug(@NonNull PlugParams params){
        View v = findViewById(params.ID);
        if(v==null){
            return;
        }
        if(v.getTag()==PlugKinds.buttun) {
            Button buttun = (Button)v;
            buttun.setText(params.mainString);
        }
        else if(v.getTag()==PlugKinds.switche){
            Switch sw = (Switch)v;
            sw.setText(params.mainString);
        }
        else if(v.getTag()==PlugKinds.textview){
            TextView tv = (TextView) v;
            tv.setText(params.mainString);
        }
    }

}

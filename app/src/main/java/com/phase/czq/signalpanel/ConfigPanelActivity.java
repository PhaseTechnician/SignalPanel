package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsoluteLayout;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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
            List<PlugParams> buttuns = panelXmlDom.getPlugsParams(PlugKinds.Buttun);
            if(buttuns!=null){
                for (PlugParams buttun:buttuns) {
                    addButtun(buttun.mainString,buttun.width,buttun.height,buttun.X,buttun.Y,buttun.ID);
                }
            }
            /*当创建完成之后*/
        }
    }

    //根据现有的参数添加一个按钮，其中ID如果为-1，就自动使用自增ID
    private void addButtun(String buttunName,int width,int height, int X,int Y,int ID){
        Button newButtun=new Button(this);
        mainLayout.addView(newButtun);
        newButtun.setClickable(true);
        //// 获取要改变view的父布局的布局参数
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newButtun.getLayoutParams();
        params.width = width;
        params.height = height;
        newButtun.setLayoutParams(params);
        newButtun.setX(X);
        newButtun.setY(Y);
        newButtun.setText(buttunName);
        newButtun.setOnTouchListener(dragListener);
        if(ID==-1) {
            newButtun.setId(ID);
        }else {
            IDcount++;
            newButtun.setId(IDcount);
        }
        //同步修改Xml文件
        panelXmlDom.XmlAddButtun(newButtun);
    }

    private void addSwitch(String switchName){
        Switch newSwitch=new Switch(this);
        mainLayout.addView(newSwitch);
        newSwitch.setClickable(true);
        ConstraintLayout.LayoutParams params =(ConstraintLayout.LayoutParams) newSwitch.getLayoutParams();
        params.width = 400;
        params.height = 200;
        newSwitch.setLayoutParams(params);
        newSwitch.setText(switchName);
        newSwitch.setOnTouchListener(dragListener);
    }
    private void addProgressBar(String barName){
        ProgressBar npb = new ProgressBar(this);
        mainLayout.addView(npb);
        npb.setOnTouchListener(dragListener);
        npb.setClickable(true);
        ConstraintLayout.LayoutParams params =(ConstraintLayout.LayoutParams) npb.getLayoutParams();
        params.width = 400;
        params.height = 400;
        npb.setLayoutParams(params);
    }
    private void addSeekBar(String barname){
        SeekBar nsb=new SeekBar(this);
        mainLayout.addView(nsb);
        nsb.setOnTouchListener(dragListener);
        nsb.setClickable(true);
        ConstraintLayout.LayoutParams params =(ConstraintLayout.LayoutParams) nsb.getLayoutParams();
        params.width = 400;
        params.height = 400;
        nsb.setLayoutParams(params);
    }

    private boolean savePanel(){
        newChanges=false;
        if(panelXmlDom.saveXml()){
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_buttun:
                addButtun("undefine",300,200,0,0,-1);
                break;
            case R.id.nav_pbr:
                //addProgressBar("undefien");
                break;
            case R.id.nav_switch:
                //addSwitch("undefien");
                break;
            case R.id.nav_sbr:
                //addSeekBar("undefien");
                break;
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

}

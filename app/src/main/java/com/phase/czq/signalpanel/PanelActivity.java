package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.Button;

import java.util.List;

public class PanelActivity extends AppCompatActivity {

    PanelXmlDom panelXmlDom;
    //主要布局
    private ConstraintLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        String path = getIntent().getExtras().getString("SignalPanel.XMLPath");

        //读取XML文档
        panelXmlDom = new PanelXmlDom(PanelXmlDom.DomMode.ReadFromFile,path);
        //主布局
        mainLayout = findViewById(R.id.panel_layout_main);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //添加控件
        addPlugsFromXml();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button.
            NavUtils.navigateUpFromSameTask(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //从XML中获取信息添加控件
    private void addPlugsFromXml(){
        //addbuttuns
        List<PlugParams> buttunParams = panelXmlDom.getPlugsParams(PlugKinds.buttun);
        if(buttunParams==null){
            return;
        }
        for(int i=0;i<buttunParams.size();i++){
            addButtun(buttunParams.get(i).mainString,buttunParams.get(i).width,buttunParams.get(i).height,buttunParams.get(i).X,buttunParams.get(i).Y,buttunParams.get(i).ID);
        }

    }

    //ERROR
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
        if(ID!=-1) {
            newButtun.setId(ID);
        }else {
            Log.e("ID","ERROR ID");
        }
    }
}

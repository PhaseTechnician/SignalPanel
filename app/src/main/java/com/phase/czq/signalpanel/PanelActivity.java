package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import java.lang.reflect.Type;
import java.util.List;

import javax.xml.transform.Templates;

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
        panelXmlDom = new PanelXmlDom(PanelXmlDom.DomMode.ReadFromFile, path);
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
    private void addPlugsFromXml() {
        //addbuttuns
        List<PlugParams> buttunParams = panelXmlDom.getPlugsParams(PlugKinds.buttun);
        if (buttunParams == null) {
            return;
        }
        for (int i = 0; i < buttunParams.size(); i++) {
            addButtun(buttunParams.get(i));
        }
        //addswitch
        List<PlugParams> switchParams = panelXmlDom.getPlugsParams(PlugKinds.switche);
        if (switchParams == null) {
            return;
        }
        for (int i = 0; i < switchParams.size(); i++) {
            addSwitch(switchParams.get(i));
        }
        //addseeksbar
        List<PlugParams> seekbarParams = panelXmlDom.getPlugsParams(PlugKinds.seekbar);
        if (seekbarParams == null) {
            return;
        }
        for (int i = 0; i < seekbarParams.size(); i++) {
            addSeekBar(seekbarParams.get(i));
        }
        //addimageview
        List<PlugParams> imageviewParams = panelXmlDom.getPlugsParams(PlugKinds.imageview);
        if(imageviewParams==null){
            return;
        }
        for (int i = 0; i < imageviewParams.size(); i++) {
            addImageView(imageviewParams.get(i));
        }
        //addprogressbar
        List<PlugParams> progressbarParams = panelXmlDom.getPlugsParams(PlugKinds.progressbar);
        if (progressbarParams == null) {
            return;
        }
        for (int i = 0; i < progressbarParams.size(); i++) {
            addProgressBar(progressbarParams.get(i));
        }

    }


    //ERROR
    @Deprecated
    private void addButtun(String buttunName, int width, int height, int X, int Y, int ID) {
        Button newButtun = new Button(this);
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
        if (ID != -1) {
            newButtun.setId(ID);
        } else {
            Log.e("ID", "ERROR ID");
        }
    }
    @Deprecated
    private void addSwitch(String switchName, int width, int height, int X, int Y, int ID) {
        Switch newSwitch = new Switch(this);
        mainLayout.addView(newSwitch);
        newSwitch.setClickable(true);
        //// 获取要改变view的父布局的布局参数
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newSwitch.getLayoutParams();
        params.width = width;
        params.height = height;
        newSwitch.setLayoutParams(params);
        newSwitch.setX(X);
        newSwitch.setY(Y);
        newSwitch.setText(switchName);
        if (ID != -1) {
            newSwitch.setId(ID);
        } else {
            Log.e("ID", "ERROR ID");
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addButtun(final PlugParams plugParams) {
        Button newButtun = new Button(this);
        mainLayout.addView(newButtun);
        newButtun.setClickable(true);
        //// 获取要改变view的父布局的布局参数
        applyBasicParam(newButtun,plugParams);
        newButtun.setText(plugParams.mainString);
        newButtun.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    //未添加mode解释和spareString
                    case MotionEvent.ACTION_DOWN:
                        if(plugParams.positiveEnable) {
                            ValuePool.serial.send(plugParams.positiveKey);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if(plugParams.negativeEnable){
                            ValuePool.serial.send(plugParams.negativeKey);
                        }
                        break;
                }
                return false;
            }
        });
    }
    private void addSwitch(PlugParams plugParams) {
        Switch newSwitch = new Switch(this);
        mainLayout.addView(newSwitch);
        newSwitch.setClickable(true);
        //// 获取要改变view的父布局的布局参数
        applyBasicParam(newSwitch,plugParams);
        newSwitch.setText(plugParams.mainString);
    }
    private void addSeekBar(PlugParams plugParams){
        SeekBar sb = new SeekBar(this);
        mainLayout.addView(sb);
        sb.setClickable(true);
        applyBasicParam(sb,plugParams);
        //sb.setTooltipText(plugParams.mainString);
    }
    private void addImageView(PlugParams plugParams){
        ImageView niv = new ImageView(this);
        mainLayout.addView(niv);
        if(plugParams.srcs==null) {
            niv.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        }
        else{
            //niv.setImageBitmap();
        }
        applyBasicParam(niv,plugParams);
    }
    private void addProgressBar(PlugParams plugParams) {
        ProgressBar npb = new ProgressBar(this);
        mainLayout.addView(npb);
        npb.setClickable(true);
        applyBasicParam(npb,plugParams);
        //npb.setTooltipText(plugParams.mainString);
    }


    //XYWHI
    static void applyBasicParam(View view, PlugParams plugParams) {
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = plugParams.width;
        params.height = plugParams.height;
        view.setLayoutParams(params);
        view.setX(plugParams.X);
        view.setY(plugParams.Y);
        if (plugParams.ID != -1) {
            view.setId(plugParams.ID);
        } else {
            Log.e("ID", "ERROR ID");
        }
    }

    @Deprecated
    static void applyReactor(View view,PlugParams plugParams){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

}
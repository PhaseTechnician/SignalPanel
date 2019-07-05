package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.phase.czq.signalpanel.plugs.Joystick;

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
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
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
        //addJoystick
        List<PlugParams> joystickParams = panelXmlDom.getPlugsParams(PlugKinds.joystick);
        if (joystickParams == null) {
            return;
        }
        for (int i = 0; i < joystickParams.size(); i++) {
            addJoystick(joystickParams.get(i));
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
        //addtextview
        List<PlugParams> textviewParams = panelXmlDom.getPlugsParams(PlugKinds.textview);
        if (textviewParams == null) {
            return;
        }
        for (int i = 0; i < textviewParams.size(); i++) {
            addTextView(textviewParams.get(i));
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
    private void addSwitch(final PlugParams plugParams) {
        Switch newSwitch = new Switch(this);
        mainLayout.addView(newSwitch);
        newSwitch.setClickable(true);
        applyBasicParam(newSwitch,plugParams);
        newSwitch.setText(plugParams.mainString);
        newSwitch.setChecked(plugParams.positiveEnable);
        newSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Switch sw = (Switch) v;
                if(sw.isChecked()){
                    ValuePool.serial.send(plugParams.positiveKey);
                    if(plugParams.positiveEnable){
                        sw.setText(plugParams.mainString);
                    }
                }
                else if(!sw.isChecked()){
                    ValuePool.serial.send(plugParams.negativeKey);
                    if(plugParams.positiveEnable){
                        sw.setText(plugParams.spareString);
                    }
                }
            }
        });
    }
    @SuppressLint("NewApi")
    private void addSeekBar(final PlugParams plugParams){
        SeekBar sb = new SeekBar(this);
        mainLayout.addView(sb);
        sb.setClickable(true);
        applyBasicParam(sb,plugParams);
        sb.setMax(Integer.valueOf(plugParams.negativeKey));
        sb.setMin(Integer.valueOf(plugParams.positiveKey));
        final ValueExpression expression = ValueExpression.analzeSignalString(plugParams.spareString,"%");
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress=Integer.valueOf(plugParams.positiveKey);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress-lastProgress>0&&plugParams.positiveEnable){
                    ValuePool.serial.send(expression.head + Integer.toString(progress)+ expression.foot);
                }
                else if(progress-lastProgress<0&&plugParams.negativeEnable){
                    ValuePool.serial.send(expression.head + Integer.toString(progress)+ expression.foot);
                }
                lastProgress = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    private void addJoystick(final PlugParams plugParams){
        Joystick joystick = new Joystick(this);
        mainLayout.addView(joystick);
        joystick.setClickable(true);
        applyBasicParam(joystick,plugParams);
        joystick.setAutoCentral(true);
        joystick.setStickName(plugParams.mainString);
        joystick.setRange(Integer.valueOf(plugParams.positiveKey),Integer.valueOf(plugParams.negativeKey));
        ValueExpression expression = ValueExpression.analzeSignalString(plugParams.spareString,",");
        final ValueExpression Xexpression,Yexpression;
        if(expression.head.indexOf("%X")!=-1){
            Xexpression = ValueExpression.analzeSignalString(expression.head,"%X");
            Yexpression = ValueExpression.analzeSignalString(expression.foot,"%Y");
        }else{
            Xexpression = ValueExpression.analzeSignalString(expression.foot,"%X");
            Yexpression = ValueExpression.analzeSignalString(expression.head,"%Y");
        }

        joystick.setOnValueChange(new Joystick.OnValueChange() {
            @Override
            public void onAxeXValueChange(int X) {
                if(plugParams.positiveEnable){
                    ValuePool.serial.send(Xexpression.head+X+Xexpression.foot);
                    //Log.i("Joystick","X: "+Integer.toString(X));
                }
            }

            @Override
            public void onAxeYValueChange(int Y) {
                if(plugParams.negativeEnable){
                    ValuePool.serial.send(Yexpression.head+Y+Yexpression.foot);
                    //Log.i("Joystick","Y: "+Integer.toString(Y));
                }
            }

            @Override
            public void onAutoCentral() {
                ValuePool.serial.send(Xexpression.head+0+Xexpression.foot);
                ValuePool.serial.send(Yexpression.head+0+Yexpression.foot);
            }
        });
    }
    private void addImageView(PlugParams plugParams){
        ImageView niv = new ImageView(this);
        mainLayout.addView(niv);
        if(plugParams.srcs=="null") {
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
    private void addTextView(PlugParams plugParams){
        TextView TV= new TextView(this);
        mainLayout.addView(TV);
        TV.setText(plugParams.mainString);
        applyBasicParam(TV,plugParams);
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
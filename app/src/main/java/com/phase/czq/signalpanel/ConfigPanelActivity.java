package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ConfigPanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    float Lastx,Lasty;
    private ConstraintLayout mainLayout;
    View.OnTouchListener dragListener=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Lastx=event.getRawX();
                    Lasty=event.getRawY();
                    return false;
                case MotionEvent.ACTION_MOVE:
                    v.setTranslationX(event.getRawX());
                    v.setTranslationY(event.getRawY());
                    return false;
                case MotionEvent.ACTION_UP:

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_config);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void addButtun(String buttunName){
        Button newButtun=new Button(this);
        mainLayout.addView(newButtun);
        newButtun.setClickable(true);
        //// 获取要改变view的父布局的布局参数
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) newButtun.getLayoutParams();
        params.width = 400;
        params.height = 200;
        newButtun.setLayoutParams(params);
        newButtun.setText(buttunName);
        newButtun.setOnTouchListener(dragListener);
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




        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_buttun:
                addButtun("undefine");
                break;
            case R.id.nav_pbr:
                addProgressBar("undefien");
                break;
            case R.id.nav_switch:
                addSwitch("undefien");
                break;
            case R.id.nav_sbr:
                addSeekBar("undefien");
                break;
            case R.id.nav_save:
                savePanel();
                break;
            case R.id.nav_otg_pipe:
                break;
        }
        return false;
    }


}

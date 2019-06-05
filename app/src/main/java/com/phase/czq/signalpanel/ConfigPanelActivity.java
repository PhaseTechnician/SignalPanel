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
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

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
        addButtun(getResources().getString(R.string.default_buttun_name)+"1");
        //addButtun(getResources().getString(R.string.default_buttun_name)+"2");
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
        //newButtun.setLayoutParams(params);
        newButtun.setText(buttunName);
        //// 设置宽为100dp
        params.width = 400;
        //// 设置高为100dp
        params.height = 200;
        newButtun.setLayoutParams(params);
        newButtun.setOnTouchListener(dragListener);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        return false;
    }


}

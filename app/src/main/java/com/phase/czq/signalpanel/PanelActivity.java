package com.phase.czq.signalpanel;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.NavigationView;
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

import com.phase.czq.signalpanel.Panel.PanelXmlDom;
import com.phase.czq.signalpanel.plugs.PlugParams;
import com.phase.czq.signalpanel.Pipe.Adapter.HeaderAdapter;
import com.phase.czq.signalpanel.Pipe.Serial;
import com.phase.czq.signalpanel.Pipe.message.ValueChangMessage;
import com.phase.czq.signalpanel.plugs.Joystick;
import com.phase.czq.signalpanel.plugs.PlugKinds;
import com.phase.czq.signalpanel.tools_value.ValueExpression;
import com.phase.czq.signalpanel.tools_value.ValuePool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

//考虑何时停止TIMER

public class PanelActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    PanelXmlDom panelXmlDom;
    //kepmap 在panel加载的同时完成初始化，自动建立mainkey和ID映射。方便之后查找。
    private Map<String,Integer> keyMap = new HashMap<String,Integer>();
    Context context=this;
    //主要布局
    private ConstraintLayout mainLayout;
    private Timer timer;
    private Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                Bundle bundle = msg.getData();
                String key = bundle.getString("key");
                if(keyMap.containsKey(key)){
                    View view = findViewById(keyMap.get(key));
                    applyValueChangeMessage(view,bundle.getString("value"));
                }else {
                    Toast.makeText(context,"key("+bundle.getString("key")+")undefine",Toast.LENGTH_SHORT).show();
                }
                //Toast.makeText(context,bundle.getString("key")+";"+bundle.getString("value"),Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_config:
                finish();
                break;

        }
        return false;
    }

    class checkReceiveTask extends TimerTask {
        protected checkReceiveTask() {
            Serial.setPipeLine(ValuePool.pipeLine);
            Serial.setReportAdapter(new HeaderAdapter());
            Serial.setApplyChange(new Serial.ApplyChange() {
                @Override
                public void apply(ValueChangMessage changeMessage) {
                    Message message = Message.obtain();
                    message.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("key",changeMessage.getKey());
                    bundle.putString("value",changeMessage.getDefault());
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
        }

        public void run() {
            Serial.update();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏标题栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //设置UI
        setContentView(R.layout.activity_panel);
        //读取XML文档
        String path = getIntent().getExtras().getString("SignalPanel.XMLPath");
        panelXmlDom = new PanelXmlDom(PanelXmlDom.DomMode.ReadFromFile, path);
        mainLayout = findViewById(R.id.panel_layout_main);
        //添加控件
        addPlugsFromXml();
        //设置定时器
        timer = new Timer();
        //频率设置
        timer.schedule(new checkReceiveTask(),0,100);
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
                            Serial.send(plugParams.positiveKey);
                        }
                        break;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        if(plugParams.negativeEnable){
                            Serial.send(plugParams.negativeKey);
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
                    Serial.send(plugParams.positiveKey);
                    if(plugParams.positiveEnable){
                        sw.setText(plugParams.mainString);
                    }
                }
                else if(!sw.isChecked()){
                    Serial.send(plugParams.negativeKey);
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
        final int min=0;
        int max=100;
        if(!plugParams.negativeKey.equals("null"))
            sb.setMax(Integer.valueOf(plugParams.negativeKey));
        if(!plugParams.positiveKey.equals("null"))
            sb.setMin(Integer.valueOf(plugParams.positiveKey));
        final ValueExpression expression = ValueExpression.analzeSignalString(plugParams.spareString,"%");
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress=min;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(progress-lastProgress>0&&plugParams.positiveEnable){
                    Serial.send(expression.head + Integer.toString(progress)+ expression.foot);
                }
                else if(progress-lastProgress<0&&plugParams.negativeEnable){
                    Serial.send(expression.head + Integer.toString(progress)+ expression.foot);
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
        int Xr=100;
        int Yr=100;
        if(!plugParams.positiveKey.equals("null")){
            Xr=Integer.valueOf(plugParams.positiveKey);
        }
        if(!plugParams.negativeKey.equals("null")){
            Yr=Integer.valueOf(plugParams.negativeKey);
        }
        joystick.setRange(Xr,Yr);
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
                    Serial.send(Xexpression.head+X+Xexpression.foot);
                    //Log.i("Joystick","X: "+Integer.toString(X));
                }
            }

            @Override
            public void onAxeYValueChange(int Y) {
                if(plugParams.negativeEnable){
                    Serial.send(Yexpression.head+Y+Yexpression.foot);
                    //Log.i("Joystick","Y: "+Integer.toString(Y));
                }
            }

            @Override
            public void onAutoCentral() {
                Serial.send(Xexpression.head+0+Xexpression.foot);
                Serial.send(Yexpression.head+0+Yexpression.foot);
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
        keyMap.put(plugParams.spareString,plugParams.ID);
    }
    private void addTextView(PlugParams plugParams){
        TextView TV= new TextView(this);
        mainLayout.addView(TV);
        TV.setText(plugParams.mainString);
        applyBasicParam(TV,plugParams);
        keyMap.put(plugParams.spareString,plugParams.ID);
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

    @NewPlugAttation
    private void applyValueChangeMessage(View view ,String Value){
        if(view instanceof TextView){
            TextView textView = (TextView) view;
            textView.setText(Value);
        }else if(view instanceof ProgressBar){
            ProgressBar progressBar = (ProgressBar) view;
            progressBar.setProgress(Integer.valueOf(Value));
        }else{
            Toast.makeText(context,"Plug don`t receive input",Toast.LENGTH_SHORT).show();
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
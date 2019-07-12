package com.phase.czq.signalpanel;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.phase.czq.signalpanel.Panel.PanelOpenMode;
import com.phase.czq.signalpanel.Panel.PanelXmlDom;
import com.phase.czq.signalpanel.Pipe.Adapter.AdapterKinds;
import com.phase.czq.signalpanel.plugs.PlugParams;
import com.phase.czq.signalpanel.plugs.Joystick;
import com.phase.czq.signalpanel.plugs.PlugKinds;
import com.phase.czq.signalpanel.tools_value.RequestCodes;
import com.phase.czq.signalpanel.tools_value.ValuePool;

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
    //编辑模式
    boolean layoutMode = true;


    Context context = this;
    View.OnTouchListener dragListener=new View.OnTouchListener() {
        private int firsrID=-1;
        private int secondID=-1;
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(!layoutMode){
                return false;
            }
            switch (event.getActionMasked()) {
                case MotionEvent.ACTION_DOWN:
                    firsrID = event.getPointerId(event.getActionIndex());
                    break;
                case MotionEvent.ACTION_MOVE:
                    newChanges = true;
                    if(event.getPointerCount()==1){
                        v.setRight(v.getRight()+(int) event.getX(firsrID));
                        v.setBottom(v.getBottom()+(int) event.getY(firsrID));
                        v.setLeft(v.getLeft()+(int) event.getX(firsrID));
                        v.setTop(v.getTop()+(int) event.getY(firsrID));
                        v.setTranslationX(event.getRawX());
                        v.setTranslationY(event.getRawY());
                    }
                    else if(event.getPointerCount()==2){
                        v.setRight(v.getLeft()+(int) event.getX(secondID));
                        v.setBottom(v.getTop()+(int) event.getY(secondID));
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_POINTER_UP:
                    reload(v);
                    flushPlug(v);
                    break;
                case MotionEvent.ACTION_POINTER_DOWN:
                    secondID = event.getPointerId(event.getActionIndex());
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
        mainLayout= findViewById(R.id.config_layout_main);

        //隐藏控制栏
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        //设置侧边栏
        NavigationView navigationView = findViewById(R.id.nav_config);
        navigationView.setNavigationItemSelectedListener(this);


        findViewById(R.id.EditModeButtun).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(layoutMode){
                    ((FloatingActionButton) v).setImageDrawable(getDrawable(R.drawable.ic_function));
                }else {
                    ((FloatingActionButton) v).setImageDrawable(getDrawable(R.drawable.ic_layout));
                }
                layoutMode =!layoutMode;
            }
        });
        //从Intent中获取基本信息填充到nav_drawer中
        if(navigationView.getHeaderCount() > 0) {
            View header = navigationView.getHeaderView(0);
            TextView panelNameTextView = header.findViewById(R.id.nav_config_panelname);
            TextView descTextView = header.findViewById(R.id.nav_config_desc);
            panelNameTextView.setText(getIntent().getStringExtra("SignalPanel.panelName"));
            descTextView.setText(getIntent().getStringExtra("SignalPanel.description"));
        }
        //准备对应的XML对象
        if(PanelOpenMode.CreatNewPanelToConfig.equal(getIntent().getIntExtra("SignalPanel.openMode",PanelOpenMode.undefine.getIndex()))){
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
                    this.getFilesDir().getAbsolutePath()+ File.separator+"panelXMLs"+File.separator+getIntent().getStringExtra("SignalPanel.panelName")+".xml");
            //更新最大ID，避免重复
            IDcount=panelXmlDom.getMaxID();
            //创建控件
            addPlugsFromXml();
            /*当创建完成之后*/
        }
    }

    private void addPlugsFromXml() {
        //addbuttuns
        List<PlugParams> buttunParams = panelXmlDom.getPlugsParams(PlugKinds.buttun);
        if (buttunParams == null) {
            return;
        }
        for (int i = 0; i < buttunParams.size(); i++) {
            addPlug(PlugKinds.buttun,buttunParams.get(i));
        }
        //addswitch
        List<PlugParams> switchParams = panelXmlDom.getPlugsParams(PlugKinds.switche);
        if (switchParams == null) {
            return;
        }
        for (int i = 0; i < switchParams.size(); i++) {
            addPlug(PlugKinds.switche,switchParams.get(i));
        }
        //addseeksbar
        List<PlugParams> seekbarParams = panelXmlDom.getPlugsParams(PlugKinds.seekbar);
        if (seekbarParams == null) {
            return;
        }
        for (int i = 0; i < seekbarParams.size(); i++) {
            addPlug(PlugKinds.seekbar,seekbarParams.get(i));
        }
        //addJoystick
        List<PlugParams> joystickParams = panelXmlDom.getPlugsParams(PlugKinds.joystick);
        if (joystickParams == null) {
            return;
        }
        for (int i = 0; i < joystickParams.size(); i++) {
            addPlug(PlugKinds.joystick,joystickParams.get(i));
        }
        //addimageview
        List<PlugParams> imageviewParams = panelXmlDom.getPlugsParams(PlugKinds.imageview);
        if(imageviewParams==null){
            return;
        }
        for (int i = 0; i < imageviewParams.size(); i++) {
            addPlug(PlugKinds.imageview,imageviewParams.get(i));
        }
        //addprogressbar
        List<PlugParams> progressbarParams = panelXmlDom.getPlugsParams(PlugKinds.progressbar);
        if (progressbarParams == null) {
            return;
        }
        for (int i = 0; i < progressbarParams.size(); i++) {
            addPlug(PlugKinds.progressbar,progressbarParams.get(i));
        }
        //addtextview
        List<PlugParams> textviewParams = panelXmlDom.getPlugsParams(PlugKinds.textview);
        if (textviewParams == null) {
            return;
        }
        for (int i = 0; i < textviewParams.size(); i++) {
            addPlug(PlugKinds.textview,textviewParams.get(i));
        }

    }

    //EXTRACT
    @NewPlugAttation
    private void addPlug(PlugKinds plugKinds, PlugParams plugParams) {
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
        //以默认值进行加载，应该是手动添加的控件
        if(plugParams== ValuePool.defaultParam)
            Toast.makeText(this,"NEW "+plugKinds.toString()+" Add",Toast.LENGTH_SHORT).show();
    }
    //EXTRACT
    //当控件被拖动时或修改大小时，更新控件信息
    private void flushPlug(@NonNull View view){
        panelXmlDom.XmlFlushPlugBasic(new PlugParams("unuse",view.getWidth(),view.getHeight(),(int)view.getTranslationX(),(int)view.getTranslationY(),view.getId()));
    }


    private boolean savePanel(){
        newChanges=false;
        return panelXmlDom.saveXml();
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
            //File
            case R.id.nav_config:
                Log.e("XMLSAVE","START SAVE");
                if(savePanel()){
                    Toast.makeText(this,R.string.config_save_success,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,R.string.config_save_fail,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_upload:
                break;
            //Feature
            case R.id.nav_notification:
                break;
            case R.id.nav_frequency:
                break;
            case R.id.nav_Adapter:
                adapterEditDialog();
                break;
        }
        return false;
    }

    private void adapterEditDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final MultiAutoCompleteTextView textInputEditText = new MultiAutoCompleteTextView(this);
        if(panelXmlDom.hasAdapt()){
            textInputEditText.setText(panelXmlDom.getApapt());
        }
        builder.setIcon(R.drawable.ic_receive);
        builder.setTitle(R.string.config_adapter_dialog_title);
        builder.setView(textInputEditText);
        builder.setPositiveButton(R.string.accept_buttun, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                panelXmlDom.setAdapt(textInputEditText.getText().toString(), AdapterKinds.adapter_header);
            }
        });
        builder.setNegativeButton(R.string.canel_buttun,null);
        builder.show();
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
        if(layoutMode){
            return;
        }
        Intent intent = new Intent(this,ParamsSetingActivity.class);
        //intent.putExtra("plugID",ID);
        intent.putExtra("params",panelXmlDom.getPlugParamsByID(ID));
        startActivityForResult(intent, RequestCodes.ParamSetingActivity);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent){
        if(requestCode==RequestCodes.ParamSetingActivity){
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
    private void reload(View view){
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) view.getLayoutParams();
        params.width = view.getWidth();
        params.height = view.getHeight();
        view.setLayoutParams(params);
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

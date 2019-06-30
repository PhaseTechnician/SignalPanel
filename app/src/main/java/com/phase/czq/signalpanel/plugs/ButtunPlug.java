package com.phase.czq.signalpanel.plugs;

import android.app.AlertDialog;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.phase.czq.signalpanel.PlugParams;
import com.phase.czq.signalpanel.R;

public class ButtunPlug extends android.support.v7.widget.AppCompatButton {
    @Deprecated
    public ButtunPlug(Context context) {
        super(context);
    }


    public ButtunPlug(Context context, PlugParams Params)
    {
        super(context);
        setClickable(true);
        //// 获取要改变view的父布局的布局参数
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) getLayoutParams();
        params.width = Params.width;
        params.height = Params.height;
        setLayoutParams(params);
        setX(Params.X);
        setY(Params.Y);
        setText(Params.mainString);

        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newButtunSetingDialog();
            }
        });
        //同步修改Xml文件
        //panelXmlDom.XmlAddButtun(new PlugParams(buttunName,width,height,X,Y,newButtun.getId()));
        //作出提示
        //Toast.makeText(this,"NEW buttun add",Toast.LENGTH_SHORT).show();
    }

    private void newButtunSetingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("config buttun");
        builder.setIcon(R.drawable.ic_config);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.buttun_set_dialog,null);
        builder.setView(view);
        builder.setPositiveButton("save", null);
        builder.setNegativeButton("cancel",null);
        builder.show();
    }
}

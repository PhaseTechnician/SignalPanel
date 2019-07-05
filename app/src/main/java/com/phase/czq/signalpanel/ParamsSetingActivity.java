package com.phase.czq.signalpanel;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

public class ParamsSetingActivity extends AppCompatActivity {

    PlugParams params = new  PlugParams();
    private Button modeButtun,srcButtun,descriptionButtun,acceptButtun;
    private Switch posSwitch,negSwitch;
    private TextInputEditText mainText,spareText,posText,negText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params_seting);

        //params.ID = getIntent().getIntExtra("plugID",-1);
        params=(PlugParams)getIntent().getSerializableExtra("params");

        modeButtun = findViewById(R.id.paramset_mode);
        srcButtun = findViewById(R.id.paramset_src);
        descriptionButtun = findViewById(R.id.paramset_description);
        acceptButtun = findViewById(R.id.paramset_accept);
        posSwitch = findViewById(R.id.paramset_positiveEnable);
        negSwitch = findViewById(R.id.paramset_negativeEnable);
        mainText = findViewById(R.id.paramset_mainString);
        spareText = findViewById(R.id.paramset_spareString);
        posText = findViewById(R.id.paramset_positiveKey);
        negText = findViewById(R.id.paramset_negativeKey);

        mainText.setText(params.mainString);
        spareText.setText(params.spareString);
        posText.setText(params.positiveKey);
        negText.setText(params.negativeKey);
        posSwitch.setChecked(params.positiveEnable);
        negSwitch.setChecked(params.negativeEnable);

        modeButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDialogForMode();
            }
        });
        srcButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDialogForSrc();
            }
        });
        descriptionButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newDialogForDescription();
            }
        });
        acceptButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                params.mainString = mainText.getText().toString();
                params.spareString = spareText.getText().toString();
                params.positiveKey = posText.getText().toString();
                params.negativeKey = negText.getText().toString();
                params.positiveEnable = posSwitch.isChecked();
                params.negativeEnable = negSwitch.isChecked();
                Intent intent = new Intent();
                intent.putExtra("id",params.ID);
                intent.putExtra("mainstring", params.mainString);
                intent.putExtra("sparestring",params.spareString);
                intent.putExtra("mode",params.modes);
                intent.putExtra("src",params.srcs);
                intent.putExtra("positivekey",params.positiveKey);
                intent.putExtra("negativekey",params.negativeKey);
                intent.putExtra("positiveenable",params.positiveEnable);
                intent.putExtra("negativeenable",params.negativeEnable);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }
    private void newDialogForMode(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_mode_edit);
        builder.setTitle(R.string.paramset_act_mode_dialog_title);
        //builder.setSingleChoiceItems();
        builder.setNegativeButton(R.string.canel_buttun,null);
        builder.setPositiveButton(R.string.accept_buttun,null);
        builder.show();
    }

    private void newDialogForSrc(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_src_set);
        builder.setTitle(R.string.paramset_act_src_dialog_title);
        builder.setMessage("NULL");
        builder.setNegativeButton(R.string.canel_buttun,null);
        builder.setPositiveButton(R.string.accept_buttun,null);
        builder.show();
    }

    private void newDialogForDescription(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_info_outline);
        builder.setTitle(R.string.paramset_act_des_dialog_title);
        builder.setMessage(R.string.paramset_act_des_buttun);
        builder.setPositiveButton(R.string.accept_buttun,null);
        builder.show();
    }

}

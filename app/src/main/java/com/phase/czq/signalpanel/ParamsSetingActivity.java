package com.phase.czq.signalpanel;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

public class ParamsSetingActivity extends AppCompatActivity {

    private Button modeButtun,srcButtun,descriptionButtun;
    private Switch posSwitch,negSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_params_seting);

        modeButtun = findViewById(R.id.paramset_mode);
        srcButtun = findViewById(R.id.paramset_src);
        descriptionButtun = findViewById(R.id.paramset_description);

        modeButtun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               newda();
            }
        });
    }
    private void newda(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //builder.setIcon();
        //builder.setTitle();
        //builder.setSingleChoiceItems();
        //builder.setNegativeButton();
        //builder.setPositiveButton();
        builder.show();
    }
}

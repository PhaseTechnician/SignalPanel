package com.phase.czq.signalpanel;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.List;

/*使用这个类来管理所有的panels资源*/
@Deprecated
public class PanelsManager {

    public PanelsManager(Context c){
        this.context=c;
        if(initPanelResource()) {

        }
        else{

        }
    }
    private Context context;
    private List<Panel> panels;

    private boolean initPanelResource(){
        //试图读取本地panels列表
        String path = context.getFilesDir().getAbsolutePath()+File.separator+"panelXMLs";
        File floder = new File(path);
        File[] files = floder.listFiles();
        if(files!=null){
            for (File file : files) Log.e("eee", "文件名 ： " + file.getName());
        }
        else {
            Log.e("eee","NON");
        }
        //解析Panels生成List

        return true;
    }
}

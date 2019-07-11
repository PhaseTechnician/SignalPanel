package com.phase.czq.signalpanel;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.PanelViewHolder> {

    private List<Panel> datas;
    private Context mcontext;

    public RecycleViewAdapter(Context context, List<Panel> data){
        this.mcontext=context;
        datas=data;
    }

    public RecycleViewAdapter(Context context){
        this.mcontext=context;
        datas=new ArrayList<>();
    }

    @NonNull
    @Override
    public PanelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_main, viewGroup, false);
        return new PanelViewHolder(view,this);
    }

    @Override
    public void onBindViewHolder(@NonNull final PanelViewHolder viewHolder, int i) {
        viewHolder.panel_name.setText(datas.get(i).getPanelName());
        viewHolder.panel_author.setText(datas.get(i).getAuthor());
        viewHolder.panel_descrip.setText(datas.get(i).getDesc());
        viewHolder.XmlFilePath=datas.get(i).getXMLpath();
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPanel(viewHolder,PanelOpenMode.LoadFileToUse);
            }
        });
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public List<Panel> getDatas() {
        return datas;
    }

    public static class PanelViewHolder extends RecyclerView.ViewHolder{
        public final TextView panel_name,panel_author,panel_descrip;
        public String XmlFilePath;
        public RecycleViewAdapter RVadapter;

        public PanelViewHolder(View v,RecycleViewAdapter adapter) {
            super(v);
            RVadapter = adapter;
            panel_name = v.findViewById(R.id.panel_main_name);
            panel_author =  v.findViewById(R.id.panel_main_author);
            panel_descrip = v.findViewById(R.id.panel_main_desc);
        }

        public void openConfig(){
            RVadapter.openPanel(this,PanelOpenMode.LoadFileToConfig);
        }

        public void delet(){
            AlertDialog.Builder builder = new AlertDialog.Builder(RVadapter.mcontext);
            builder.setIcon(R.drawable.ic_notifications);
            builder.setMessage("File will be delete .accept?");
            builder.setTitle("warning");
            builder.setNegativeButton(R.string.canel_buttun,null);
            final int pos = this.getAdapterPosition();
            builder.setPositiveButton(R.string.accept_buttun, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    RVadapter.deletePanel(RVadapter.datas.get(pos).getXMLpath());
                    RVadapter.reloadItems();
                    RVadapter.notifyDataSetChanged();
                }
            });
            builder.show();
        }
    }

    private void addItem(Panel newPanel){
        datas.add(newPanel);
    }
    private void deleteItem(String panelName){
        for (Panel panel:datas) {
            if(panel.getPanelName().equals(panelName)){
                datas.remove(panel);
                return;
            }
        }
    }
    private void clearAllItem(){
        datas.clear();
    }
    public void reloadItems(){
        clearAllItem();
        File file = new File(mcontext.getFilesDir().getAbsolutePath()+File.separator+"panelXMLs");
        File[] files=file.listFiles();
        if(files!=null){
            for(int i=0;i<files.length;i++){
                addItem(new Panel(files[i].getName(),mcontext));
            }
        }
    }

    //管线状态判断
    private boolean criticalPipe(){
        if(ValuePool.blueToothPipeConnect||ValuePool.usbOTGPipeConnect||ValuePool.wifiPipeConnect){
            return true;
        }
        else{
            return false;
        }
    }

    //打开一个Panel
    private void openPanel(PanelViewHolder viewHolder, PanelOpenMode mode){
        if(mode==PanelOpenMode.LoadFileToUse){
            if(!criticalPipe()){
                Toast.makeText(mcontext,"No Pipe Contect",Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(mcontext,PanelActivity.class);
            intent.putExtra("SignalPanel.XMLPath",viewHolder.XmlFilePath);
            mcontext.startActivity(intent);
        }else  if(mode==PanelOpenMode.LoadFileToConfig){
            openConfigActivity(viewHolder.panel_name.getText().toString(),viewHolder.panel_author.getText().toString(),viewHolder.panel_descrip.getText().toString(),
                    PanelOpenMode.LoadFileToConfig);
        }
    }

    private void openConfigActivity(String panelName,String author,String description,PanelOpenMode mode){
        Intent intent = new Intent(mcontext,ConfigPanelActivity.class);
        intent.putExtra("SignalPanel.panelName",panelName);
        intent.putExtra("SignalPanel.author",author);
        intent.putExtra("SignalPanel.description",description);
        intent.putExtra("SignalPanel.openMode",mode.getIndex());
        mcontext.startActivity(intent);
    }

    private boolean deletePanel(String path){
        File file = new File(path.substring(0,path.length()-4));
            if (file.exists() && file.isFile()) {
                if (file.delete()) {
                    Log.i("File", "Delete "+path);
                    return true;
                } else {
                    Log.i("File", "Delete "+path+" Fail");
                    return false;
                }
            } else {
                Log.i("File", "Delete "+path+" not exist");
                return false;
            }
    }
}

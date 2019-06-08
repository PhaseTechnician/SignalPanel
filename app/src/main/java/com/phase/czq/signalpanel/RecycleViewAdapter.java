package com.phase.czq.signalpanel;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.phase.czq.signalpanel.Panel;

import java.util.ArrayList;
import java.util.List;

public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.panelViewHolder> {

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
    public RecycleViewAdapter.panelViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.panel_main, viewGroup, false);
        return new panelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecycleViewAdapter.panelViewHolder viewHolder, int i) {
        viewHolder.panel_name.setText(datas.get(i).getPanelName());
        viewHolder.panel_author.setText(datas.get(i).getAuthor());
        viewHolder.panel_descrip.setText(datas.get(i).getDesc());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mcontext,"open",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.itemView.setOnTouchListener(new View.OnTouchListener(){


            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch(event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        v.scrollTo(-(int)event.getX(),0);
                        return false;
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP:
                        v.scrollTo(0,0);
                        return false;
                }
                return false;
            }
        });

    }
    @Override
    public int getItemCount() {
        return datas.size();
    }

    public static class panelViewHolder extends RecyclerView.ViewHolder{
        public final TextView panel_name,panel_author,panel_descrip;
        public panelViewHolder(View v) {
            super(v);
            panel_name = v.findViewById(R.id.panel_main_name);
            panel_author =  v.findViewById(R.id.panel_main_author);
            panel_descrip = v.findViewById(R.id.panel_main_desc);
        }

    }

    public void addItem(Panel newPanel){
        datas.add(newPanel);
    }
}

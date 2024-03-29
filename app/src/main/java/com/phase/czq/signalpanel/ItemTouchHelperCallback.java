package com.phase.czq.signalpanel;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import java.util.Collections;

public class ItemTouchHelperCallback extends ItemTouchHelper.Callback {

    public ItemTouchHelperCallback(){

    }

    public interface OnSwipeLR{
        void onLeft(RecycleViewAdapter.PanelViewHolder viewHolder);
        void onRight(RecycleViewAdapter.PanelViewHolder viewHolder);
    }
    OnSwipeLR onSwipeLR;
    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT|ItemTouchHelper.LEFT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        if (recyclerView == null)
            return false;
        RecycleViewAdapter adapter = (RecycleViewAdapter) recyclerView.getAdapter();
        if (adapter == null)
            return false;
        if (adapter.getItemCount() != 0) {
            int from = viewHolder.getAdapterPosition();
            int endPosition = target.getAdapterPosition();//在这里我一直在刷新最后移动到的位置，以便接下来做其他操作
            Collections.swap(adapter.getDatas(), from, endPosition);//数据交换位置
            // 使用notifyItemMoved可以表现得更平滑，问题是 from ~ endPosition 间的item position 不会更新，并引发一系列角标混乱的问题，
            //这个问题可以在后面的 onSelectedChanged()方法中解决。
            // 在此做notifyItemMoved操作就足够了，notifyDataSetChanged() 和 notifyItemRangeChanged() 会打断 drag 操作。
            adapter.notifyItemMoved(from, endPosition);
        }
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(direction == ItemTouchHelper.LEFT){
            onSwipeLR.onLeft((RecycleViewAdapter.PanelViewHolder) viewHolder);
        }else if(direction == ItemTouchHelper.RIGHT){
            onSwipeLR.onRight((RecycleViewAdapter.PanelViewHolder) viewHolder);
        }
    }
    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            viewHolder.itemView.scrollTo(-(int)dX,0);
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
    }

    public void setOnSwipeLR(OnSwipeLR lr){
        this.onSwipeLR = lr;
    }
}

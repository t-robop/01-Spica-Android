package com.t_robop.yuusuke.a01_spica_android.util;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.t_robop.yuusuke.a01_spica_android.RecyclerAdapter;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.Callback {

    private RecyclerAdapter adapter;
    private Context context;

    public SimpleItemTouchHelperCallback(RecyclerAdapter adapter, Context context) {

        this.adapter = adapter;
        this.context = context;
    }

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        final int fromPos = viewHolder.getAdapterPosition();
        final int toPos = target.getAdapterPosition();
        adapter.itemMoved(fromPos, toPos);
        adapter.notifyItemMoved(fromPos, toPos);
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        final int fromPos = viewHolder.getAdapterPosition();
        adapter.removeItem(fromPos);
        adapter.notifyItemRemoved(fromPos);
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        super.onSelectedChanged(viewHolder, actionState);

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
        }
    }
}

package com.t_robop.yuusuke.a01_spica_android;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
    private OnRecyclerListener recyclerListener;

    private ArrayList<ItemDataModel> ItemDataArray;

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        TextView speedRight;
        TextView speedLeft;
        TextView time;
        ImageView image;

        FrameLayout containerMove;
        FrameLayout containerLoop;
        LinearLayout containerLoopNum;
        TextView textLoopNum;
        ImageView imgLoopBack;

        ViewHolder(View view){
            super(view);
            this.linearLayout = view.findViewById(R.id.item_frame);
            this.speedRight = view.findViewById(R.id.text_speed_right);
            this.speedLeft = view.findViewById(R.id.text_speed_left);
            this.time = view.findViewById(R.id.text_time);
            this.image = view.findViewById(R.id.direction_item_image);

            this.containerMove=view.findViewById(R.id.container_move);
            this.containerLoop=view.findViewById(R.id.container_loop);
            this.containerLoopNum=view.findViewById(R.id.container_loop_text);
            this.textLoopNum=view.findViewById(R.id.cnt_loop);
            this.imgLoopBack=view.findViewById(R.id.img_loop_back);
        }
    }

    RecyclerAdapter(ArrayList<ItemDataModel> itemDataList, OnRecyclerListener listener){
        ItemDataArray = itemDataList;
        recyclerListener = listener;
    }

    public ItemDataModel getItem(int position) {
        return ItemDataArray.get(position);
    }
    public ArrayList<ItemDataModel> getAllItem(){
        return ItemDataArray;
    }

    public void setItem(int position, ItemDataModel dataModel) {
        ItemDataArray.set(position,dataModel);
    }

    public void addItem(ItemDataModel dataModel) {
        ItemDataArray.add(dataModel);
    }
    public void removeItem(int position) {
        ItemDataArray.remove(position);
    }

    public void itemMoved(int fromPos, int toPos){

        ItemDataModel fromItem = ItemDataArray.get(fromPos);
        ItemDataModel toItem = ItemDataArray.get(toPos);
        ItemDataArray.set(toPos,fromItem);
        ItemDataArray.set(fromPos,toItem);

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final ViewHolder viewHolder = holder;

        if(ItemDataArray.get(position).getOrderName().equals("loopStart") || ItemDataArray.get(position).getOrderName().equals("loopEnd")){
            holder.containerLoop.setVisibility(View.VISIBLE);
            holder.containerMove.setVisibility(View.GONE);

            switch (ItemDataArray.get(position).getOrderName()) {
                case "loopStart":
                    holder.containerLoopNum.setOnClickListener(new View.OnClickListener(){
                        @Override
                        public void onClick(View view){
                            recyclerListener.onRecyclerClicked(view, viewHolder.getLayoutPosition());
                        }
                    });
                    holder.containerLoopNum.setVisibility(View.VISIBLE);
                    holder.textLoopNum.setText(String.valueOf(ItemDataArray.get(position).getLoopCount()));
                    holder.imgLoopBack.setImageResource(R.drawable.loop_start_block);
                    break;

                case "loopEnd":
                    holder.containerLoopNum.setVisibility(View.GONE);
                    holder.imgLoopBack.setImageResource(R.drawable.loop_end_block);
                    break;
            }
        }else {

            holder.speedRight.setText("右パワー : " + ItemDataArray.get(position).getRightSpeed());
            holder.speedLeft.setText("左パワー : " + ItemDataArray.get(position).getLeftSpeed());
            holder.time.setText(ItemDataArray.get(position).getTime() + "秒");

            holder.linearLayout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    recyclerListener.onRecyclerClicked(view, viewHolder.getLayoutPosition());
                }
            });

            holder.containerLoop.setVisibility(View.GONE);
            holder.containerMove.setVisibility(View.VISIBLE);
            holder.containerLoopNum.setVisibility(View.GONE);

            switch (ItemDataArray.get(position).getOrderName()) {
                case "forward":
                    holder.image.setImageResource(R.drawable.move_front);
                    break;

                case "back":
                    holder.image.setImageResource(R.drawable.move_back);
                    break;

                case "left":
                    holder.image.setImageResource(R.drawable.move_left);
                    break;

                case "right":
                    holder.image.setImageResource(R.drawable.move_right);
                    break;

                default:
                    //holder.image.setImageResource(R.drawable.edit_icon);
                    break;
            }
        }
    }

    @Override
    public int getItemCount(){
        return ItemDataArray.size();
    }

    public interface OnRecyclerListener {
        void onRecyclerClicked(View view, int position);
    }
}

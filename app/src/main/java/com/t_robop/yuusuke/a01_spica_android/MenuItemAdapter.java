package com.t_robop.yuusuke.a01_spica_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t_robop.yuusuke.a01_spica_android.model.MenuItemModel;

import java.util.ArrayList;

public class MenuItemAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private ArrayList<MenuItemModel> itemList = new ArrayList<>();

    MenuItemAdapter(Context context){
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    static class ViewHolder {

        ImageView imageBlock;
        ImageView imageDirection;
        TextView cmdName;
        TextView cmdDetail;

        ViewHolder(View view){
            super();
            imageBlock = view.findViewById(R.id.menu_item_bg);
            imageDirection = view.findViewById(R.id.direction_image);
            cmdName = view.findViewById(R.id.text_title);
            cmdDetail = view.findViewById(R.id.text_sub);
        }
    }

    public void add(MenuItemModel item){
        this.itemList.add(item);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public MenuItemModel getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder;

        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.menu_list_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageDirection.setImageResource(itemList.get(position).getItemImage());
        holder.cmdName.setText(itemList.get(position).getCmdName());
        holder.cmdDetail.setText(itemList.get(position).getCmdDetail());

        if (position == 4 || position == 5){
            holder.imageBlock.setImageResource(R.drawable.back_loop);
        }else{
            holder.imageBlock.setImageResource(R.drawable.back_move);
        }

        return convertView;
    }

}

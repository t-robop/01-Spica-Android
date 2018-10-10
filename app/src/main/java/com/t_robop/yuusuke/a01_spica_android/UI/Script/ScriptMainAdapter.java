package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t_robop.yuusuke.a01_spica_android.databinding.ItemContainerScriptMainBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainAdapter extends RecyclerView.Adapter<ScriptMainAdapter.BindingHolder> {
    private ArrayList<ScriptSet> mScriptList;
    private Context mContext;

    public static class BindingHolder extends RecyclerView.ViewHolder {
        private ItemContainerScriptMainBinding mBinding;

        public BindingHolder(ItemContainerScriptMainBinding binding) {
                super(binding.getRoot());
                mBinding = binding;
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }
    }

    public static class ScriptSet{
        private ScriptModel scriptDefault;
        private ScriptModel scriptSpecial;

        public ScriptModel getScriptDefault() {
            return scriptDefault;
        }
        public void setScriptDefault(ScriptModel scriptDefault) {
            this.scriptDefault = scriptDefault;
        }

        public ScriptModel getScriptSpecial() {
            return scriptSpecial;
        }
        public void setScriptSpecial(ScriptModel scriptSpecial) {
            this.scriptSpecial = scriptSpecial;
        }
    }

    public ScriptMainAdapter(Context context) {
        mScriptList = new ArrayList<>();
        this.mContext = context;
    }

    public void addDefault(int index,ScriptModel script) {
        if(mScriptList.size()<index) return;

        //普通に追加
        if(mScriptList.size()==index){
            ScriptSet set=new ScriptSet();
            set.setScriptDefault(script);
            mScriptList.add(set);
        }else{
            ScriptSet set=mScriptList.get(index);
            set.setScriptDefault(script);
            mScriptList.set(index,set);
        }
    }

    public void addSpecial(int index,ScriptModel script) {
        if(mScriptList.size()<index) return;

        //普通に追加
        if(mScriptList.size()==index){
            ScriptSet set=new ScriptSet();
            set.setScriptSpecial(script);
            mScriptList.add(set);
        }else{
            ScriptSet set=mScriptList.get(index);
            set.setScriptSpecial(script);
            mScriptList.set(index,set);
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler_itemレイアウト
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemContainerScriptMainBinding binding = ItemContainerScriptMainBinding.inflate(layoutInflater, parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(final BindingHolder holder, int position) {
        ScriptSet set = mScriptList.get(position);

        holder.mBinding.setScript(set.getScriptDefault());
        holder.mBinding.conductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeScaleUpAnimation(
                            holder.mBinding.conductorAdd,
                            (int)holder.mBinding.conductorAdd.getX(),
                            (int)holder.mBinding.conductorAdd.getY(),
                            holder.mBinding.conductorAdd.getWidth(),
                            holder.mBinding.conductorAdd.getHeight());
                    mContext.startActivity(new Intent(mContext,BlockSelectActivity.class), options.toBundle());
                }
            }
        });

        holder.mBinding.setScriptOther(set.scriptSpecial);

        holder.mBinding.conductorIf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    options = ActivityOptions.makeScaleUpAnimation(
                            holder.mBinding.conductorAdd,
                            (int)holder.mBinding.conductorAdd.getX(),
                            (int)holder.mBinding.conductorAdd.getY(),
                            holder.mBinding.conductorAdd.getWidth(),
                            holder.mBinding.conductorAdd.getHeight());
                    mContext.startActivity(new Intent(mContext,BlockSelectActivity.class), options.toBundle());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }
}

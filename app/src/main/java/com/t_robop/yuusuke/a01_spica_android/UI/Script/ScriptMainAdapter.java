package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t_robop.yuusuke.a01_spica_android.BR;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainAdapter extends RecyclerView.Adapter<ScriptMainAdapter.ItemViewHolder>{
    private ArrayList<ScriptModel> mScriptList;
    private Context context;

    // ViewHolder
    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        private ViewDataBinding mBinding;

        public ItemViewHolder(View v) {
            super(v);
            // Bind処理
            mBinding = DataBindingUtil.bind(v);
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }
    }

    /**
     * コンストラクタ
     */
    public ScriptMainAdapter(Context context,ArrayList<ScriptModel> scriptList) {
        mScriptList = scriptList;
        this.context=context;
    }
    public ScriptMainAdapter(Context context) {
        mScriptList=new ArrayList<>();
        this.context=context;
    }

    public void add(ScriptModel script){
        mScriptList.add(script);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler_itemレイアウト
        View v = LayoutInflater.from(this.context).inflate(R.layout.item_container_script_main, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        ScriptModel script = mScriptList.get(position);

        // Userデータをセット。BR.userはxmlのvariableの名前
        holder.getBinding().setVariable(BR.script, script);
        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }
}

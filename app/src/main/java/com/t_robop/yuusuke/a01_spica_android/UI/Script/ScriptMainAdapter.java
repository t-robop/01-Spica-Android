package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.Activity;
import android.app.ActivityOptions;

import android.content.Context;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ItemContainerScriptMainBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

    public class ScriptMainAdapter extends RecyclerView.Adapter<ScriptMainAdapter.BindingHolder> {
    private ArrayList<ScriptModel> mScriptList;
    private Context mContext;


    ScriptMainActivity scriptMainActivity;

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

    public ScriptMainAdapter(Context context, ArrayList<ScriptModel> scriptList) {
        mScriptList = scriptList;
        this.mContext = context;
    }

    public ScriptMainAdapter(Context context) {
        mScriptList = new ArrayList<>();
        this.mContext = context;
    }


    //class保持クラスで置き換える
    public void setActivity(ScriptMainActivity activity) {
        this.scriptMainActivity = activity;
    }

    public void add(ScriptModel script) {
        mScriptList.add(script);
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
        ScriptModel script = mScriptList.get(position);

        holder.mBinding.setScript(script);
        holder.mBinding.conductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ActivityOptions options = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    options = ActivityOptions.makeScaleUpAnimation(
//                            holder.mBinding.conductorAdd,
//                            (int)holder.mBinding.conductorAdd.getX(),
//                            (int)holder.mBinding.conductorAdd.getY(),
//                            holder.mBinding.conductorAdd.getWidth(),
//                            holder.mBinding.conductorAdd.getHeight());
//                    mContext.startActivity(new Intent(mContext,BlockSelectActivity.class), options.toBundle());
//                }
            scriptMainActivity.inflateFragment();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }
}

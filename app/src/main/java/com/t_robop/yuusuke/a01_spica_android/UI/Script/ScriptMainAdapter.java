package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.Activity;
import android.app.ActivityOptions;

import android.content.Context;
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
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainAdapter extends RecyclerView.Adapter<ScriptMainAdapter.BindingHolder> {
    private ArrayList<ScriptSet> mScriptList;
    private Context mContext;
    private onItemClickListener clickConductor;
    private onItemClickListener clickConductorIf;
    private onItemClickListener clickBlock;
    private onItemClickListener clickBlockIf;
    private onItemLongClickListener longClickBlock;
    private onItemLongClickListener longClickBlockIf;

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

    public static class ScriptSet {
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

    public void clear() {
        mScriptList.clear();
    }

    public void addDefault(int index, ScriptModel script) {
        if (mScriptList.size() < index) return;

        //普通に追加
        if (mScriptList.size() == index) {
            ScriptSet set = new ScriptSet();
            set.setScriptDefault(script);
            mScriptList.add(set);
        } else {
            ScriptSet set = mScriptList.get(index);
            set.setScriptDefault(script);
            mScriptList.set(index, set);
        }
    }

    public void addSpecial(int index, ScriptModel script) {
        if (mScriptList.size() < index) return;

        //普通に追加
        if (mScriptList.size() == index) {
            ScriptSet set = new ScriptSet();
            set.setScriptSpecial(script);
            mScriptList.add(set);
        } else {
            ScriptSet set = mScriptList.get(index);
            set.setScriptSpecial(script);
            mScriptList.set(index, set);
        }
    }

    @Override
    public BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // recycler_itemレイアウト
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemContainerScriptMainBinding binding = ItemContainerScriptMainBinding.inflate(layoutInflater, parent, false);
        return new BindingHolder(binding);
    }

    /**
     * if文のTrueの終点Indexを返すメソッド
     */
    private int getTrueEndIndex(int posIfStart) {
        for (int i = posIfStart + 1; i < mScriptList.size(); i++) {
            if (mScriptList.get(i).scriptSpecial == null) {
                ScriptModel scriptLastTrue = mScriptList.get(i - 1).scriptSpecial;
                if (scriptLastTrue != null) {
                    return scriptLastTrue.getPos();
                } else {
                    return mScriptList.get(i - 1).scriptDefault.getPos();
                }
            }
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ScriptSet set = mScriptList.get(position);

        /**
         * 通常レーンの描画・ハンドラ設定
         */
        final ScriptModel scriptDefault = set.scriptDefault;
        holder.mBinding.setScript(scriptDefault);
        holder.mBinding.conductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
                if (scriptDefault.getBlock().getBlock() == BlockModel.SpicaBlock.IF_START) {
                    //if_startの直後のFalseレーンの場合はTrueレーンのブロック数を数えてから配置する
                    clickConductor.onClick(view, getTrueEndIndex(position), 2);
                } else {
                    clickConductor.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
                }
            }
        });
        if (scriptDefault != null) {
            holder.mBinding.blockImage.setImageResource(scriptDefault.getBlock().getBlock().getIcResource());
        }
        holder.mBinding.blockContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (scriptDefault.getBlock().getBlock() == BlockModel.SpicaBlock.START) {
                    clickBlock.onClick(view, -1, 0);
                } else if (scriptDefault.getBlock().getBlock() == BlockModel.SpicaBlock.END) {
                    clickBlock.onClick(view, -2, 0);
                } else {
                    clickBlock.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
                }
            }
        });
        holder.mBinding.blockContainer.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickBlock.onLongClick(view, scriptDefault.getPos());
                return false;
            }
        });

        /**
         * ifレーンの描画・ハンドラ設定
         */
        final ScriptModel scriptSpecial = set.scriptSpecial;
        holder.mBinding.setScriptOther(scriptSpecial);
        holder.mBinding.conductorIf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
                if (scriptSpecial != null) {
                    clickConductorIf.onClick(view, scriptSpecial.getPos(), 1);
                } else {
                    //if_startの直後のTrueレーンの場合は通常レーンのIF_STARTブロックのposを送る
                    clickConductorIf.onClick(view, scriptDefault.getPos(), 1);
                }
            }
        });
        if (scriptSpecial != null) {
            holder.mBinding.blockImageIf.setImageResource(scriptSpecial.getBlock().getBlock().getIcResource());
        }
        holder.mBinding.blockContainerIf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickBlockIf.onClick(view, scriptSpecial.getPos(), scriptSpecial.getIfState());
            }
        });
        holder.mBinding.blockContainerIf.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                longClickBlock.onLongClick(view, scriptSpecial.getPos());
                return false;
            }
        });

    }

    public interface onItemClickListener {
        void onClick(View view, int pos, int ifState);
    }

    public interface onItemLongClickListener {
        void onLongClick(View view, int pos);
    }

    public void setOnConductorClickListener(onItemClickListener listener) {
        this.clickConductor = listener;
    }

    public void setOnConductorIfClickListener(onItemClickListener listener) {
        this.clickConductorIf = listener;
    }

    public void setOnBlockClickListener(onItemClickListener listener) {
        this.clickBlock = listener;
    }

    public void setOnBlockIfClickListener(onItemClickListener listener) {
        this.clickBlockIf = listener;
    }

    public void setOnBlockLongClickListener(onItemLongClickListener listener) {
        this.longClickBlock = listener;
    }

    public void setOnBlockIfLongClickListener(onItemLongClickListener listener) {
        this.longClickBlockIf = listener;
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }
}

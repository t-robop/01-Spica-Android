package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockFrontBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockBackBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockLeftBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockRightBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockIfStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockIfEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockForStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockForEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockBreakBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorHorizontalBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorIfStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorIfEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ItemContainerScriptMainBinding;
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
            LayoutInflater layoutInflater = LayoutInflater.from(mBinding.laneDefault.getContext());
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

    public ScriptSet getItem(int index){
        return this.mScriptList.get(index);
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
        final ScriptModel scriptSpecial = set.scriptSpecial;
        final ScriptModel scriptDefault = set.scriptDefault;

        /**
         * ifレーンの描画
         */
        holder.mBinding.laneIf.removeAllViews();
        if (scriptSpecial != null) {
            drawCommandBlock(position, holder.mBinding.laneIf, scriptSpecial);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(holder.mBinding.laneIf.getContext());
            if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                ConductorIfStartBinding bindingConIfStart = ConductorIfStartBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                bindingConIfStart.setAdapter(this);
                bindingConIfStart.setPosition(position);
                bindingConIfStart.setIfState(1);
                holder.mBinding.laneIf.addView(bindingConIfStart.getRoot());
            } else if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.IF_END) {
                ConductorIfEndBinding bindingConIfEnd = ConductorIfEndBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                holder.mBinding.laneIf.addView(bindingConIfEnd.getRoot());
            } else if (scriptDefault.getIfState() == 2) {
                ConductorHorizontalBinding bindingConHorizontal = ConductorHorizontalBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                holder.mBinding.laneIf.addView(bindingConHorizontal.getRoot());
            }
        }

        /**
         * 通常レーンの描画
         */
        holder.mBinding.laneDefault.removeAllViews();
        if (scriptDefault != null) {
            drawCommandBlock(position, holder.mBinding.laneDefault, scriptDefault);
        } else if (scriptSpecial.getIfState() == 1) {
            LayoutInflater layoutInflater = LayoutInflater.from(holder.mBinding.laneDefault.getContext());
            ConductorHorizontalBinding bindingConHorizontal = ConductorHorizontalBinding.inflate(layoutInflater, holder.mBinding.laneDefault, false);
            holder.mBinding.laneDefault.addView(bindingConHorizontal.getRoot());
        }
    }

    @SuppressLint("SetTextI18n")
    public void drawCommandBlock(int position, LinearLayout lane, ScriptModel script) {
        LayoutInflater layoutInflater = LayoutInflater.from(lane.getContext());
        switch (script.getBlock()) {
            case START:
                BlockStartBinding bindingStart = BlockStartBinding.inflate(layoutInflater, lane, false);
                bindingStart.setAdapter(this);
                bindingStart.setPosition(position);
                bindingStart.setScript(script);
                bindingStart.setIfState(script.getIfState());
                lane.addView(bindingStart.getRoot());
                break;
            case END:
                BlockEndBinding bindingEnd = BlockEndBinding.inflate(layoutInflater, lane, false);
                bindingEnd.setPosition(position);
                bindingEnd.setScript(script);
                lane.addView(BlockEndBinding.class.cast(bindingEnd).getRoot());
                break;
            case FRONT:
                BlockFrontBinding bindingFront = BlockFrontBinding.inflate(layoutInflater, lane, false);
                bindingFront.setAdapter(this);
                bindingFront.setPosition(position);
                bindingFront.setScript(script);
                bindingFront.setValue(script.getValue());
                bindingFront.setIfState(script.getIfState());
                lane.addView(bindingFront.getRoot());
                break;
            case BACK:
                BlockBackBinding bindingBack = BlockBackBinding.inflate(layoutInflater, lane, false);
                bindingBack.setAdapter(this);
                bindingBack.setPosition(position);
                bindingBack.setScript(script);
                bindingBack.setValue(script.getValue());
                bindingBack.setIfState(script.getIfState());
                lane.addView(BlockBackBinding.class.cast(bindingBack).getRoot());
                break;
            case LEFT:
                BlockLeftBinding bindingLeft = BlockLeftBinding.inflate(layoutInflater, lane, false);
                bindingLeft.setAdapter(this);
                bindingLeft.setPosition(position);
                bindingLeft.setScript(script);
                bindingLeft.setValue(script.getValue());
                bindingLeft.setIfState(script.getIfState());
                lane.addView(BlockLeftBinding.class.cast(bindingLeft).getRoot());
                break;
            case RIGHT:
                BlockRightBinding bindingRight = BlockRightBinding.inflate(layoutInflater, lane, false);
                bindingRight.setAdapter(this);
                bindingRight.setPosition(position);
                bindingRight.setScript(script);
                bindingRight.setValue(script.getValue());
                bindingRight.setIfState(script.getIfState());
                lane.addView(BlockRightBinding.class.cast(bindingRight).getRoot());
                break;
            case IF_START:
                BlockIfStartBinding bindingIfStart = BlockIfStartBinding.inflate(layoutInflater, lane, false);
                bindingIfStart.setAdapter(this);
                bindingIfStart.setPosition(position);
                bindingIfStart.setScript(script);
                if(script.getIfOperator() == script.getSensorAboveNum()){
                    bindingIfStart.idText.setText((int)Math.floor(script.getValue()) + mContext.getString(R.string.script_main_adapter_block_if_above));
                }else if(script.getIfOperator() == script.getSensorBelowNum()){
                    bindingIfStart.idText.setText((int)Math.floor(script.getValue()) + mContext.getString(R.string.script_main_adapter_block_if_below));
                }
                bindingIfStart.setIfState(script.getIfState());
                lane.addView(BlockIfStartBinding.class.cast(bindingIfStart).getRoot());
                break;
            case IF_END:
                BlockIfEndBinding bindingIfEnd = BlockIfEndBinding.inflate(layoutInflater, lane, false);
                bindingIfEnd.setAdapter(this);
                bindingIfEnd.setPosition(position);
                bindingIfEnd.setScript(script);
                bindingIfEnd.setIfState(script.getIfState());
                lane.addView(BlockIfEndBinding.class.cast(bindingIfEnd).getRoot());
                break;
            case FOR_START:
                BlockForStartBinding bindingForStart = BlockForStartBinding.inflate(layoutInflater, lane, false);
                bindingForStart.setAdapter(this);
                bindingForStart.setPosition(position);
                bindingForStart.setScript(script);
                bindingForStart.setValue((int)Math.floor(script.getValue()));
                bindingForStart.setIfState(script.getIfState());
                lane.addView(BlockForStartBinding.class.cast(bindingForStart).getRoot());
                break;
            case FOR_END:
                BlockForEndBinding bindingForEnd = BlockForEndBinding.inflate(layoutInflater, lane, false);
                bindingForEnd.setAdapter(this);
                bindingForEnd.setPosition(position);
                bindingForEnd.setScript(script);
                bindingForEnd.setIfState(script.getIfState());
                lane.addView(BlockForEndBinding.class.cast(bindingForEnd).getRoot());
                break;
            case BREAK:
                BlockBreakBinding bindingBreak = BlockBreakBinding.inflate(layoutInflater, lane, false);
                bindingBreak.setAdapter(this);
                bindingBreak.setPosition(position);
                bindingBreak.setScript(script);
                bindingBreak.setIfState(script.getIfState());
                lane.addView(BlockBreakBinding.class.cast(bindingBreak).getRoot());
                break;
        }
    }

    private boolean isInLoop(ScriptModel script) {
        if (script.getBlock() == ScriptModel.SpicaBlock.FOR_END) {
            return false;
        } else if (script.getBlock() == ScriptModel.SpicaBlock.FOR_START) {
            return true;
        } else if (script.isInLoop()) {
            return true;
        } else if (!script.isInLoop()) {
            return false;
        }
        return false;
    }

    public void clickConductor(View view, int position, int ifState) {
        ScriptModel scriptDefault = mScriptList.get(position).getScriptDefault();
        ScriptModel scriptSpecial = mScriptList.get(position).getScriptSpecial();
        if (ifState == 1) {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (scriptSpecial != null) {
                clickConductorIf.onClick(view, scriptSpecial.getPos(), 1, isInLoop(scriptSpecial));
            } else {
                //if_startの直後のTrueレーンの場合は通常レーンのIF_STARTブロックのposを送る
                clickConductorIf.onClick(view, scriptDefault.getPos(), 1, isInLoop(scriptDefault));
            }
        } else {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                //if_startの直後のFalseレーンの場合はTrueレーンのブロック数を数えてから配置する
                clickConductor.onClick(view, getTrueEndIndex(position), 2, isInLoop(scriptDefault));
            } else {
                clickConductor.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState(), isInLoop(scriptDefault));
            }
        }
    }

    public void clickBlock(View view, int position, int ifState) {
        ScriptModel scriptDefault = mScriptList.get(position).getScriptDefault();
        ScriptModel scriptSpecial = mScriptList.get(position).getScriptSpecial();
        if (ifState == 1) {
            clickBlockIf.onClick(view, scriptSpecial.getPos(), scriptSpecial.getIfState(), scriptSpecial.isInLoop());
        } else {
            if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.START) {
                clickBlock.onClick(view, -1, 0, scriptDefault.isInLoop());
            } else if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.END) {
                clickBlock.onClick(view, -2, 0, scriptDefault.isInLoop());
            } else {
                clickBlock.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState(), scriptDefault.isInLoop());
            }
        }
    }

    public void longClickBlock(View view, int position) {
        ScriptModel scriptDefault = mScriptList.get(position).getScriptDefault();
        longClickBlock.onLongClick(view, scriptDefault.getPos());
    }

    public void longClickBlockIf(View view, int position) {
        ScriptModel scriptSpecial = mScriptList.get(position).getScriptDefault();
        longClickBlockIf.onLongClick(view, scriptSpecial.getPos());
    }

    public interface onItemClickListener {
        void onClick(View view, int pos, int ifState, boolean isInLoop);
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

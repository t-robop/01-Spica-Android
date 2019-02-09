package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.annotation.SuppressLint;
import android.content.Context;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.t_robop.yuusuke.a01_spica_android.Block;
import com.t_robop.yuusuke.a01_spica_android.Config;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockBackBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockBreakBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockForEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockForStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockFrontBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockIfEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockIfStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockLeftBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockRightBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.BlockStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorHorizontalBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorIfEndBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ConductorIfStartBinding;
import com.t_robop.yuusuke.a01_spica_android.databinding.ItemContainerScriptMainBinding;
import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;

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

        BindingHolder(ItemContainerScriptMainBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ViewDataBinding getBinding() {
            return mBinding;
        }
    }

    public static class ScriptSet {
        private UIBlockModel uiBlockUnderLane;
        private UIBlockModel uiBlockTopLane;

        public UIBlockModel getUiBlockUnderLane() {
            return uiBlockUnderLane;
        }

        void setUiBlockUnderLane(UIBlockModel uiBlockUnderLane) {
            this.uiBlockUnderLane = uiBlockUnderLane;
        }

        public UIBlockModel getUiBlockTopLane() {
            return uiBlockTopLane;
        }

        void setUiBlockTopLane(UIBlockModel uiBlockTopLane) {
            this.uiBlockTopLane = uiBlockTopLane;
        }
    }

    public ScriptMainAdapter(Context context) {
        mScriptList = new ArrayList<>();
        this.mContext = context;
    }

    void clear() {
        mScriptList.clear();
    }

    public ScriptSet getItem(int index) {
        return this.mScriptList.get(index);
    }

    void addDefault(int index, UIBlockModel uiBlockModel) {
        if (mScriptList.size() < index) return;

        //普通に追加
        if (mScriptList.size() == index) {
            ScriptSet set = new ScriptSet();
            set.setUiBlockUnderLane(uiBlockModel);
            mScriptList.add(set);
        } else {
            ScriptSet set = mScriptList.get(index);
            set.setUiBlockUnderLane(uiBlockModel);
            mScriptList.set(index, set);
        }
    }

    void addSpecial(int index, UIBlockModel uiBlockModel) {
        if (mScriptList.size() < index) return;

        //普通に追加
        if (mScriptList.size() == index) {
            ScriptSet set = new ScriptSet();
            set.setUiBlockTopLane(uiBlockModel);
            mScriptList.add(set);
        } else {
            ScriptSet set = mScriptList.get(index);
            set.setUiBlockTopLane(uiBlockModel);
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
            if (mScriptList.get(i).uiBlockTopLane == null) {
                UIBlockModel scriptLastTrue = mScriptList.get(i - 1).uiBlockTopLane;
                if (scriptLastTrue != null) {
                    return scriptLastTrue.getPos();
                } else {
                    return mScriptList.get(i - 1).uiBlockUnderLane.getPos();
                }
            }
        }
        return -1;
    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ScriptSet set = mScriptList.get(position);
        final UIBlockModel uiBlockTopLane = set.uiBlockTopLane;
        final UIBlockModel uiBlockUnderLane = set.uiBlockUnderLane;

        /*
         * ifレーンの描画
         */
        holder.mBinding.laneIf.removeAllViews();
        if (uiBlockTopLane != null) {
            drawCommandBlock(position, holder.mBinding.laneIf, uiBlockTopLane);
        } else {
            LayoutInflater layoutInflater = LayoutInflater.from(holder.mBinding.laneIf.getContext());

            if (uiBlockUnderLane.getId().equals(Block.IfStartBlock.id)) {
                ConductorIfStartBinding bindingConIfStart = ConductorIfStartBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                bindingConIfStart.setAdapter(this);
                bindingConIfStart.setPosition(position);
                bindingConIfStart.setIfState(Config.IN_TRUE_LANE);
                holder.mBinding.laneIf.addView(bindingConIfStart.getRoot());
            } else if (uiBlockUnderLane.getId().equals(Block.IfEndBlock.id)) {
                ConductorIfEndBinding bindingConIfEnd = ConductorIfEndBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                holder.mBinding.laneIf.addView(bindingConIfEnd.getRoot());
            } else if (uiBlockUnderLane.getIfState() == Config.IN_FALSE_LANE) {
                ConductorHorizontalBinding bindingConHorizontal = ConductorHorizontalBinding.inflate(layoutInflater, holder.mBinding.laneIf, false);
                holder.mBinding.laneIf.addView(bindingConHorizontal.getRoot());
            }
        }

        /*
         * 通常レーンの描画
         */
        holder.mBinding.laneDefault.removeAllViews();
        if (uiBlockUnderLane != null) {
            drawCommandBlock(position, holder.mBinding.laneDefault, uiBlockUnderLane);
        } else if (uiBlockTopLane.getIfState() == Config.IN_TRUE_LANE) {
            LayoutInflater layoutInflater = LayoutInflater.from(holder.mBinding.laneDefault.getContext());
            ConductorHorizontalBinding bindingConHorizontal = ConductorHorizontalBinding.inflate(layoutInflater, holder.mBinding.laneDefault, false);
            holder.mBinding.laneDefault.addView(bindingConHorizontal.getRoot());
        }
    }

    //FIXME 長すぎなのでまとめたい
    @SuppressLint("SetTextI18n")
    private void drawCommandBlock(int position, LinearLayout lane, UIBlockModel uiBlockModel) {
        LayoutInflater layoutInflater = LayoutInflater.from(lane.getContext());
        switch (uiBlockModel.getId()) {
            case Block.StartBlock.id:
                BlockStartBinding bindingStart = BlockStartBinding.inflate(layoutInflater, lane, false);
                bindingStart.setAdapter(this);
                bindingStart.setPosition(position);
                bindingStart.setScript(uiBlockModel);
                bindingStart.setIfState(uiBlockModel.getIfState());
                lane.addView(bindingStart.getRoot());
                break;

            case Block.EndBlock.id:
                BlockEndBinding bindingEnd = BlockEndBinding.inflate(layoutInflater, lane, false);
                bindingEnd.setPosition(position);
                bindingEnd.setScript(uiBlockModel);
                lane.addView((bindingEnd).getRoot());
                break;

            case Block.FrontBlock.id:
                BlockFrontBinding bindingFront = BlockFrontBinding.inflate(layoutInflater, lane, false);
                bindingFront.setAdapter(this);
                bindingFront.setPosition(position);
                bindingFront.setScript(uiBlockModel);
                bindingFront.setValue(uiBlockModel.getTime());
                bindingFront.setIfState(uiBlockModel.getIfState());
                lane.addView(bindingFront.getRoot());
                break;

            case Block.BackBlock.id:
                BlockBackBinding bindingBack = BlockBackBinding.inflate(layoutInflater, lane, false);
                bindingBack.setAdapter(this);
                bindingBack.setPosition(position);
                bindingBack.setScript(uiBlockModel);
                bindingBack.setValue(uiBlockModel.getTime());
                bindingBack.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingBack).getRoot());
                break;

            case Block.LeftBlock.id:
                BlockLeftBinding bindingLeft = BlockLeftBinding.inflate(layoutInflater, lane, false);
                bindingLeft.setAdapter(this);
                bindingLeft.setPosition(position);
                bindingLeft.setScript(uiBlockModel);
                bindingLeft.setValue(uiBlockModel.getTime());
                bindingLeft.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingLeft).getRoot());
                break;

            case Block.RightBlock.id:
                BlockRightBinding bindingRight = BlockRightBinding.inflate(layoutInflater, lane, false);
                bindingRight.setAdapter(this);
                bindingRight.setPosition(position);
                bindingRight.setScript(uiBlockModel);
                bindingRight.setValue(uiBlockModel.getTime());
                bindingRight.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingRight).getRoot());
                break;

            case Block.IfStartBlock.id:
                BlockIfStartBinding bindingIfStart = BlockIfStartBinding.inflate(layoutInflater, lane, false);
                bindingIfStart.setAdapter(this);
                bindingIfStart.setPosition(position);
                bindingIfStart.setScript(uiBlockModel);
                if (uiBlockModel.getIfOperator() == Config.SENSOR_ABOVE) {
                    bindingIfStart.idText.setText((int) Math.floor(uiBlockModel.getThreshold()) + mContext.getString(R.string.script_main_adapter_block_if_above));
                } else if (uiBlockModel.getIfOperator() == Config.SENSOR_BELOW) {
                    bindingIfStart.idText.setText((int) Math.floor(uiBlockModel.getThreshold()) + mContext.getString(R.string.script_main_adapter_block_if_below));
                }
                bindingIfStart.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingIfStart).getRoot());
                break;

            case Block.IfEndBlock.id:
                BlockIfEndBinding bindingIfEnd = BlockIfEndBinding.inflate(layoutInflater, lane, false);
                bindingIfEnd.setAdapter(this);
                bindingIfEnd.setPosition(position);
                bindingIfEnd.setScript(uiBlockModel);
                bindingIfEnd.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingIfEnd).getRoot());
                break;

            case Block.ForStartBlock.id:
                BlockForStartBinding bindingForStart = BlockForStartBinding.inflate(layoutInflater, lane, false);
                bindingForStart.setAdapter(this);
                bindingForStart.setPosition(position);
                bindingForStart.setScript(uiBlockModel);
                bindingForStart.setValue((int) Math.floor(uiBlockModel.getLoopNum()));
                bindingForStart.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingForStart).getRoot());
                break;

            case Block.ForEndBlock.id:
                BlockForEndBinding bindingForEnd = BlockForEndBinding.inflate(layoutInflater, lane, false);
                bindingForEnd.setAdapter(this);
                bindingForEnd.setPosition(position);
                bindingForEnd.setScript(uiBlockModel);
                bindingForEnd.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingForEnd).getRoot());
                break;

            case Block.BreakBlock.id:
                BlockBreakBinding bindingBreak = BlockBreakBinding.inflate(layoutInflater, lane, false);
                bindingBreak.setAdapter(this);
                bindingBreak.setPosition(position);
                bindingBreak.setScript(uiBlockModel);
                bindingBreak.setIfState(uiBlockModel.getIfState());
                lane.addView((bindingBreak).getRoot());
                break;
        }
    }

    private boolean isInLoop(UIBlockModel uiBlockModel) {
        if (uiBlockModel.getId().equals(Block.ForEndBlock.id)) {
            return false;
        } else if (uiBlockModel.getId().equals(Block.ForStartBlock.id)) {
            return true;
        } else if (uiBlockModel.isInLoop()) {
            return true;
        } else if (!uiBlockModel.isInLoop()) {
            return false;
        }
        return false;
    }

    public void clickConductor(View view, int position, int ifState) {
        UIBlockModel uiBlockUnderLane = mScriptList.get(position).getUiBlockUnderLane();
        UIBlockModel uiBlockTopLane = mScriptList.get(position).getUiBlockTopLane();
        if (ifState == Config.IN_TRUE_LANE) {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (uiBlockTopLane != null) {
                clickConductorIf.onClick(view, uiBlockTopLane.getPos(), Config.IN_TRUE_LANE, isInLoop(uiBlockTopLane));
            } else {
                //if_startの直後のTrueレーンの場合は通常レーンのIF_STARTブロックのposを送る
                clickConductorIf.onClick(view, uiBlockUnderLane.getPos(), Config.IN_TRUE_LANE, isInLoop(uiBlockUnderLane));
            }
        } else {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (uiBlockUnderLane.getId().equals(Block.IfStartBlock.id)) {
                //if_startの直後のFalseレーンの場合はTrueレーンのブロック数を数えてから配置する
                clickConductor.onClick(view, getTrueEndIndex(position), Config.IN_FALSE_LANE, isInLoop(uiBlockUnderLane));
            } else {
                clickConductor.onClick(view, uiBlockUnderLane.getPos(), uiBlockUnderLane.getIfState(), isInLoop(uiBlockUnderLane));
            }
        }
    }

    public void clickBlock(View view, int position, int ifState) {
        UIBlockModel uiBlockTopLane = mScriptList.get(position).getUiBlockTopLane();
        UIBlockModel uiBlockUnderLane = mScriptList.get(position).getUiBlockUnderLane();
        if (ifState == Config.IN_TRUE_LANE) {
            clickBlockIf.onClick(view, uiBlockTopLane.getPos(), uiBlockTopLane.getIfState(), uiBlockTopLane.isInLoop());
        } else {
            clickBlock.onClick(view, uiBlockUnderLane.getPos(), uiBlockUnderLane.getIfState(), uiBlockUnderLane.isInLoop());
        }
    }

    @Deprecated
    public void longClickBlock(View view, int position) {
        UIBlockModel uiBlockUnderLane = mScriptList.get(position).getUiBlockUnderLane();
        longClickBlock.onLongClick(view, uiBlockUnderLane.getPos());
    }

    @Deprecated
    public void longClickBlockIf(View view, int position) {
        UIBlockModel uiBlockUnderLane = mScriptList.get(position).getUiBlockUnderLane();
        longClickBlockIf.onLongClick(view, uiBlockUnderLane.getPos());
    }

    public interface onItemClickListener {
        void onClick(View view, int pos, int ifState, boolean isInLoop);
    }

    public interface onItemLongClickListener {
        void onLongClick(View view, int pos);
    }

    void setOnConductorClickListener(onItemClickListener listener) {
        this.clickConductor = listener;
    }

    void setOnConductorIfClickListener(onItemClickListener listener) {
        this.clickConductorIf = listener;
    }

    void setOnBlockClickListener(onItemClickListener listener) {
        this.clickBlock = listener;
    }

    void setOnBlockIfClickListener(onItemClickListener listener) {
        this.clickBlockIf = listener;
    }

    void setOnBlockLongClickListener(onItemLongClickListener listener) {
        this.longClickBlock = listener;
    }

    void setOnBlockIfLongClickListener(onItemLongClickListener listener) {
        this.longClickBlockIf = listener;
    }

    @Override
    public int getItemCount() {
        return mScriptList.size();
    }
}

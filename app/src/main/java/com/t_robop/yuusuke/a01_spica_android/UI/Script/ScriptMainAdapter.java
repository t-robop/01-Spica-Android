package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

//    @Override
//    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view;
//        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_script_main, parent, false);
//        LayoutInflater inflater;
//        LinearLayout blockLayout;
//        LinearLayout mainLayout;
//        switch (viewType) {
//            case FRONT:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_front, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_null, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//                break;
//            case BACK:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_back, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_null, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//
//                break;
//            case LEFT:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_left, parent, false);
//                break;
//            case RIGHT:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_right, parent, false);
//                break;
//            case IF_START:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_if_start, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_if_start, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//                break;
//            case IF_END:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_if_end, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_if_end, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//                break;
//            case FOR_START:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_for_start, parent, false);
//                break;
//            case FOR_END:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_for_end, parent, false);
//                break;
//            case BREAK:
////                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_break, parent, false);
//                break;
//            case START:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_start, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_null, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//
//
//                break;
//            case END:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_end, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_null, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//
//                break;
//
//
//            case FRONT + 100:
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_null, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_default);
//                mainLayout.addView(blockLayout);
//
//                inflater = LayoutInflater.from(parent.getContext());
//                blockLayout = (LinearLayout) inflater.inflate(R.layout.block_front, null);
//                mainLayout = (LinearLayout) view.findViewById(R.id.lane_if);
//                mainLayout.addView(blockLayout);
//                break;
//
//
//        }
//
//
//        final ScriptMainAdapter.viewHolder viewHold = new viewHolder(view);
//        return viewHold;
//
//    }

    @Override
    public void onBindViewHolder(BindingHolder holder, final int position) {
        ScriptSet set = mScriptList.get(position);

        /**
         * ifレーンの描画
         */
        final ScriptModel scriptSpecial = set.scriptSpecial;
        if (scriptSpecial != null) {
            holder.mBinding.laneIf.removeAllViews();
            drawCommandBlock(position,holder.mBinding.laneIf,scriptSpecial);
        }

        /**
         * 通常レーンの描画
         */
        final ScriptModel scriptDefault = set.scriptDefault;
        if (scriptDefault != null) {
            holder.mBinding.laneDefault.removeAllViews();
            drawCommandBlock(position,holder.mBinding.laneDefault,scriptDefault);
        }
    }

    public void drawCommandBlock(int position,LinearLayout lane,ScriptModel script){
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
                bindingFront.setIfState(script.getIfState());
                lane.addView(bindingFront.getRoot());
                break;
            case BACK:
                BlockBackBinding bindingBack = BlockBackBinding.inflate(layoutInflater, lane, false);
                bindingBack.setAdapter(this);
                bindingBack.setPosition(position);
                bindingBack.setScript(script);
                bindingBack.setIfState(script.getIfState());
                lane.addView(BlockBackBinding.class.cast(bindingBack).getRoot());
                break;
            case LEFT:
                BlockLeftBinding bindingLeft = BlockLeftBinding.inflate(layoutInflater, lane, false);
                bindingLeft.setAdapter(this);
                bindingLeft.setPosition(position);
                bindingLeft.setScript(script);
                bindingLeft.setIfState(script.getIfState());
                lane.addView(BlockLeftBinding.class.cast(bindingLeft).getRoot());
                break;
            case RIGHT:
                BlockRightBinding bindingRight = BlockRightBinding.inflate(layoutInflater, lane, false);
                bindingRight.setAdapter(this);
                bindingRight.setPosition(position);
                bindingRight.setScript(script);
                bindingRight.setIfState(script.getIfState());
                lane.addView(BlockRightBinding.class.cast(bindingRight).getRoot());
                break;
            case IF_START:
                BlockIfStartBinding bindingIfStart = BlockIfStartBinding.inflate(layoutInflater, lane, false);
                bindingIfStart.setAdapter(this);
                bindingIfStart.setPosition(position);
                bindingIfStart.setScript(script);
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

    public void clickConductor(View view, int position, int ifState) {
        ScriptModel scriptDefault = mScriptList.get(position).getScriptDefault();
        ScriptModel scriptSpecial = mScriptList.get(position).getScriptDefault();
        if (ifState == 1) {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (scriptSpecial != null) {
                clickConductorIf.onClick(view, scriptSpecial.getPos(), 1);
            } else {
                //if_startの直後のTrueレーンの場合は通常レーンのIF_STARTブロックのposを送る
                clickConductorIf.onClick(view, scriptDefault.getPos(), 1);
            }
        } else {
            //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
            if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                //if_startの直後のFalseレーンの場合はTrueレーンのブロック数を数えてから配置する
                clickConductor.onClick(view, getTrueEndIndex(position), 2);
            } else {
                clickConductor.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
            }
        }
    }

    public void clickBlock(View view, int position, int ifState) {
        ScriptModel scriptDefault = mScriptList.get(position).getScriptDefault();
        ScriptModel scriptSpecial = mScriptList.get(position).getScriptDefault();
        if (ifState == 1) {
            clickBlockIf.onClick(view, scriptSpecial.getPos(), scriptSpecial.getIfState());
        } else {
            if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.START) {
                clickBlock.onClick(view, -1, 0);
            } else if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.END) {
                clickBlock.onClick(view, -2, 0);
            } else {
                clickBlock.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
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

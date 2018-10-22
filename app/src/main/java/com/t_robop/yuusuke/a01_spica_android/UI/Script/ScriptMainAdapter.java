package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
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
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainAdapter extends RecyclerView.Adapter<ScriptMainAdapter.viewHolder> {
    private ArrayList<ScriptSet> mScriptList;
    private Context mContext;
    private onItemClickListener clickConductor;
    private onItemClickListener clickConductorIf;
    private onItemClickListener clickBlock;
    private onItemClickListener clickBlockIf;
    private onItemLongClickListener longClickBlock;
    private onItemLongClickListener longClickBlockIf;


    private final int FRONT = 1;
    private final int BACK = 2;
    private final int LEFT = 3;
    private final int RIGHT = 4;
    private final int IF_START = 5;
    private final int IF_END = 6;
    private final int FOR_START = 7;
    private final int FOR_END = 8;
    private final int BREAK = 9;
    private final int START = 10;
    private final int END = 11;


    public static class viewHolder extends RecyclerView.ViewHolder {
        TextView pramText;
        LinearLayout laneIfLayout;
        LinearLayout laneDefaultLayout;
        LinearLayout blockContainerLayout;
        LinearLayout conductorAddLayout;

        public viewHolder(View view) {
            super(view);
            laneIfLayout = view.findViewById(R.id.lane_if);
            laneDefaultLayout = view.findViewById(R.id.lane_default);
            pramText = view.findViewById(R.id.id_text);
            blockContainerLayout = view.findViewById(R.id.block_container);
            conductorAddLayout = view.findViewById(R.id.conductor_add);
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

//    @Override
//    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // recycler_itemレイアウト
//        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
//        ItemContainerScriptMainBinding binding = ItemContainerScriptMainBinding.inflate(layoutInflater, parent, false);
//        return new BindingHolder(binding);
//    }


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
    public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        LinearLayout test = (LinearLayout) parent.getChildAt(1);
        Log.d("aaaaaaaa",String.valueOf(parent.getTag()));
        switch (viewType) {
            case FRONT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_front, parent, false);
                break;
            case BACK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_back, parent, false);
                break;
            case LEFT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_left, parent, false);
                break;
            case RIGHT:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_right, parent, false);
                break;
            case IF_START:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_if_start, parent, false);
                break;
            case IF_END:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_if_end, parent, false);
                break;
            case FOR_START:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_for_start, parent, false);
                break;
            case FOR_END:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_for_end, parent, false);
                break;
            case BREAK:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_break, parent, false);
                break;
            case START:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_start, test, false);
                break;
            case END:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.block_end, parent, false);
                break;

        }
        final ScriptMainAdapter.viewHolder viewHold = new viewHolder(view);
        return viewHold;

    }

    @Override
    public void onBindViewHolder(viewHolder holder, final int position) {
        ScriptSet set = mScriptList.get(position);
        holder.pramText.setText("q1");
        /**
         * 通常レーンの描画・ハンドラ設定
         */
        final ScriptModel scriptDefault = set.scriptDefault;

        /**
         * Blockを押した時
         */
        holder.blockContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "aaa", Toast.LENGTH_SHORT).show();
                if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.START) {
                    clickBlock.onClick(view, -1, 0);
                } else if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.END) {
                    clickBlock.onClick(view, -2, 0);
                } else {
                    clickBlock.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
                }
            }
        });

        /**
         * 追加ボタンを押した時
         */
        holder.conductorAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "bbb", Toast.LENGTH_SHORT).show();
                //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
                if (scriptDefault.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                    //if_startの直後のFalseレーンの場合はTrueレーンのブロック数を数えてから配置する
                    clickConductor.onClick(view, getTrueEndIndex(position), 2);
                } else {
                    clickConductor.onClick(view, scriptDefault.getPos(), scriptDefault.getIfState());
                }
            }
        });



        holder.blockContainerLayout.setOnLongClickListener(new View.OnLongClickListener() {
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
        //holder.mBinding.setScriptOther(scriptSpecial);
//        holder.mBinding.conductorIf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //ここでは追加する場所の前ブロック(タッチされた+ボタンを所持するブロック)のposを送る
//                if (scriptSpecial != null) {
//                    clickConductorIf.onClick(view, scriptSpecial.getPos(), 1);
//                } else {
//                    //if_startの直後のTrueレーンの場合は通常レーンのIF_STARTブロックのposを送る
//                    clickConductorIf.onClick(view, scriptDefault.getPos(), 1);
//                }
//            }
//        });

//        holder.mBinding.blockContainerIf.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                clickBlockIf.onClick(view, scriptSpecial.getPos(), scriptSpecial.getIfState());
//            }
//        });
//        holder.mBinding.blockContainerIf.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                longClickBlock.onLongClick(view, scriptSpecial.getPos());
//                return false;
//            }
//        });

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

    @Override
    public int getItemViewType(int position) {
        //TODO たぶんバグる
        ScriptModel.SpicaBlock block = mScriptList.get(position).getScriptDefault().getBlock();
        // Scriptを受け取る
        // Scriptのenumである、blockを取得する
        // blockによって返す値を変える
        return block.getId();
    }
}

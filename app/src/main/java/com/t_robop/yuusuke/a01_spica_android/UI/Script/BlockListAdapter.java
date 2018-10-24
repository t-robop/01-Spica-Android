//package com.t_robop.yuusuke.a01_spica_android.UI.Script;
//
//import android.content.Context;
//import android.databinding.DataBindingUtil;
//import android.databinding.ViewDataBinding;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.t_robop.yuusuke.a01_spica_android.BR;
//import com.t_robop.yuusuke.a01_spica_android.R;
//
//import java.util.ArrayList;
//
//public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ItemViewHolder> {
//    //private ArrayList<BlockModel> mBlockList;
//    private Context context;
//
//    public static class ItemViewHolder extends RecyclerView.ViewHolder {
//        private ViewDataBinding mBinding;
//
//        public ItemViewHolder(View v) {
//            super(v);
//            mBinding = DataBindingUtil.bind(v);
//        }
//
//        public ViewDataBinding getBinding() {
//            return mBinding;
//        }
//    }
//
////    public BlockListAdapter(Context context, ArrayList<BlockModel> blockList) {
////        mBlockList = blockList;
////        this.context = context;
////    }
//
////    public BlockListAdapter(Context context) {
////        mBlockList = new ArrayList<>();
////        this.context = context;
////    }
//
////    public void add(BlockModel block) {
////        mBlockList.add(block);
////    }
//
//    @Override
//    public BlockListAdapter.ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        // recycler_itemレイアウト
//        View v = null;
//        switch (viewType) {
//            case 1:
//                v = LayoutInflater.from(this.context).inflate(R.layout.block_start, parent, false);
//                break;
//            case 2:
//                v = LayoutInflater.from(this.context).inflate(R.layout.block_end, parent, false);
//                break;
//            case 3:
//                v = LayoutInflater.from(this.context).inflate(R.layout.block_start, parent, false);
//                break;
//            case 4:
//                v = LayoutInflater.from(this.context).inflate(R.layout.block_start, parent, false);
//                break;
//            case 5:
//                v = LayoutInflater.from(this.context).inflate(R.layout.block_start, parent, false);
//                break;
//        }
//
//        return new BlockListAdapter.ItemViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(BlockListAdapter.ItemViewHolder holder, int position) {
//        //BlockModel block = mBlockList.get(position);
//
//        // Userデータをセット。BR.userはxmlのvariableの名前
//        //holder.getBinding().setVariable(BR.block, block);
//        holder.getBinding().executePendingBindings();
//    }
//
//    @Override
//    public int getItemCount() {
//        return mBlockList.size();
//    }
//}

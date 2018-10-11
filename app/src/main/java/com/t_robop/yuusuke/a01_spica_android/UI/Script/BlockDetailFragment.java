package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockDetailFragment extends Fragment {

    public BlockDetailFragment(){}

    View mView;
    Bundle bundle;
    String commandDirection;

    private int pos;

    DetailListener listener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_block_detail, container, false);

        Bundle bundle = getArguments();
        commandDirection = bundle.getString("commandDirection");
        pos=bundle.getInt("pos");

//        ActivityBlockDetailBinding binding = DataBindingUtil.setContentView(this.getActivity(), R.layout.activity_block_detail);
//        binding.detailAddBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ScriptModel script=new ScriptModel();
//                script.setBlock(new BlockModel(BlockModel.SpicaBlock.FRONT));
//                script.setValue(100);//todo ここでedittextの値をいれる
//                listener.onClickadd(pos,script);
//            }
//        });

        Button addBtn=mView.findViewById(R.id.detail_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScriptModel script=new ScriptModel();
                script.setBlock(new BlockModel(BlockModel.SpicaBlock.FRONT));
                script.setValue(100);//todo ここでedittextの値をいれる
                listener.onClickadd(pos,script);
                getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
            }
        });

        popupAnime(mView);

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

//    // FragmentがActivityに追加されたら呼ばれるメソッド
//    @Override
//    public void onAttach(Context context) {
//        // APILevel23からは引数がActivity->Contextになっているので注意する
//
//        // contextクラスがMyListenerを実装しているかをチェックする
//        super.onAttach(context);
//        if (context instanceof BlockDetailFragment.DetailListener) {
//            // リスナーをここでセットするようにします
//            listener = (BlockDetailFragment.DetailListener) context;
//        }
//    }

    private void popupAnime(View view){
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f,1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec


        if(commandDirection == "susumu")
        {
            scaleAnimation.setDuration(200);
        }else if(commandDirection == "magaru")
        {
            scaleAnimation.setDuration(1000);
        }else if(commandDirection == "sagaru"){
            scaleAnimation.setDuration(2000);
        }else {
            scaleAnimation.setDuration(5000);
        }

        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

    public interface DetailListener {
        public void onClickadd(int pos, ScriptModel script);
    }

    public void setAddClickListener(BlockDetailFragment.DetailListener listener) {
        this.listener = listener;
    }


}

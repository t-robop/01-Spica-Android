package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockDetailFragment extends Fragment {

    public BlockDetailFragment() {
    }

    View mView;
    Bundle bundle;
    String commandDirection;

    private int pos;
    private int ifState;

    DetailListener listener;

    private BlockModel.SpicaBlock spicaBlock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_block_detail, container, false);

        Bundle bundle = getArguments();
        commandDirection = bundle.getString("commandDirection");
        pos = bundle.getInt("pos");
        ifState = bundle.getInt("ifState");

        switch (commandDirection) {
            case "susumu":
                spicaBlock = BlockModel.SpicaBlock.FRONT;
                break;
            case "magaru":
                spicaBlock = BlockModel.SpicaBlock.RIGHT;
                break;
            case "sagaru":
                spicaBlock = BlockModel.SpicaBlock.BACK;
                break;
            case "mosimo":
                spicaBlock = BlockModel.SpicaBlock.IF_START;
                break;
            case "kurikaesu":
                spicaBlock = BlockModel.SpicaBlock.FOR_START;
                break;
            case "nukeru":
                spicaBlock = BlockModel.SpicaBlock.BREAK;
                break;
            default:
                spicaBlock = BlockModel.SpicaBlock.FRONT;
        }

        final ImageView blockImage= mView.findViewById(R.id.block_image);
        blockImage.setImageResource(spicaBlock.getIcResource());

        final TextView blockTitle= mView.findViewById(R.id.block_title_text);
        blockTitle.setText(spicaBlock.getName());

        ConstraintLayout containerSwitch=mView.findViewById(R.id.switch_container_detail);
        Switch blockSwitch= mView.findViewById(R.id.switch_detail);
        if(spicaBlock== BlockModel.SpicaBlock.RIGHT){
            containerSwitch.setVisibility(View.VISIBLE);
            blockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b){
                        spicaBlock = BlockModel.SpicaBlock.LEFT;
                    }else{
                        spicaBlock = BlockModel.SpicaBlock.RIGHT;
                    }
                    blockImage.setImageResource(spicaBlock.getIcResource());
                    blockTitle.setText(spicaBlock.getName());
                }
            });
        }else{
            containerSwitch.setVisibility(View.INVISIBLE);
        }

        Button addBtn = mView.findViewById(R.id.detail_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScriptModel script = new ScriptModel();
                script.setBlock(new BlockModel(spicaBlock));
                script.setValue(100);//todo ここでedittextの値をいれる
                script.setIfState(ifState);
                listener.onClickadd(script,pos);
            }
        });

        Button cancelBtn = mView.findViewById(R.id.detail_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
            }
        });

        RelativeLayout bg=mView.findViewById(R.id.bg_detail);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void popupAnime(View view) {
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f, 1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec


        if (commandDirection == "susumu") {
            scaleAnimation.setDuration(200);
        } else if (commandDirection == "magaru") {
            scaleAnimation.setDuration(200);
        } else if (commandDirection == "sagaru") {
            scaleAnimation.setDuration(200);
        } else {
            scaleAnimation.setDuration(200);
        }

        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

    public interface DetailListener {
        public void onClickadd(ScriptModel script, int pos);
    }

    public void setAddClickListener(BlockDetailFragment.DetailListener listener) {
        this.listener = listener;
    }


}

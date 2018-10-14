package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.os.Bundle;
import android.print.PrinterId;
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
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.t_robop.yuusuke.a01_spica_android.R;
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
    private boolean isEdit;
    private int value;

    DetailListener listener;

    private BlockModel.SpicaBlock spicaBlock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
     
        return inflater.inflate(R.layout.activity_block_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final ImageView blockImage = view.findViewById(R.id.block_image);
        final TextView blockTitle = view.findViewById(R.id.block_title_text);
        ConstraintLayout containerSwitch = view.findViewById(R.id.switch_container_detail);
        Switch blockSwitch = view.findViewById(R.id.switch_detail);
        final SeekBar blockSeek=view.findViewById(R.id.seek_value);

        Bundle bundle = getArguments();
        commandDirection = bundle.getString("commandDirection");
        pos = bundle.getInt("pos");
        ifState = bundle.getInt("ifState");
        isEdit = bundle.getBoolean("isEdit", false);
        value=bundle.getInt("value",1);

        switch (commandDirection) {
            case "01":
                spicaBlock = BlockModel.SpicaBlock.FRONT;
                break;
            case "02":
                spicaBlock = BlockModel.SpicaBlock.BACK;
                break;
            case "03":
                spicaBlock = BlockModel.SpicaBlock.LEFT;
                break;
            case "04":
                spicaBlock = BlockModel.SpicaBlock.RIGHT;
                break;
            case "05":
                spicaBlock = BlockModel.SpicaBlock.IF_START;
                break;
            case "06":
                spicaBlock = BlockModel.SpicaBlock.IF_END;
                break;
            case "07":
                spicaBlock = BlockModel.SpicaBlock.FOR_START;
                break;
            case "08":
                spicaBlock = BlockModel.SpicaBlock.FOR_END;
                break;
            case "09":
                spicaBlock = BlockModel.SpicaBlock.BREAK;
                break;
            default:
                spicaBlock = BlockModel.SpicaBlock.FRONT;
        }

        blockImage.setImageResource(spicaBlock.getIcResource());
        blockTitle.setText(spicaBlock.getName());

        if (spicaBlock == BlockModel.SpicaBlock.RIGHT||spicaBlock == BlockModel.SpicaBlock.LEFT) {
            final BlockModel.SpicaBlock defaultBlock=spicaBlock;
            containerSwitch.setVisibility(View.VISIBLE);
            blockSwitch.setChecked(false);
            blockSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        if(defaultBlock==BlockModel.SpicaBlock.LEFT){
                            spicaBlock = BlockModel.SpicaBlock.RIGHT;
                        }else{
                            spicaBlock = BlockModel.SpicaBlock.LEFT;
                        }
                    } else {
                        spicaBlock = defaultBlock;
                    }
                    blockImage.setImageResource(spicaBlock.getIcResource());
                    blockTitle.setText(spicaBlock.getName());
                }
            });
        } else {
            containerSwitch.setVisibility(View.INVISIBLE);
        }

        blockSeek.setProgress(value);

        Button addBtn = view.findViewById(R.id.detail_add_btn);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScriptModel script = new ScriptModel();
                script.setBlock(new BlockModel(spicaBlock));
                script.setValue(blockSeek.getProgress());
                script.setIfState(ifState);
                listener.onClickAdd(script, pos, isEdit);
            }
        });

        Button cancelBtn = view.findViewById(R.id.detail_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
            }
        });

        RelativeLayout bg = view.findViewById(R.id.bg_detail);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
            }
        });

        popupAnime(view);
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
        public void onClickAdd(ScriptModel script, int pos, boolean isEdit);
    }

    public void setAddClickListener(BlockDetailFragment.DetailListener listener) {
        this.listener = listener;
    }


}

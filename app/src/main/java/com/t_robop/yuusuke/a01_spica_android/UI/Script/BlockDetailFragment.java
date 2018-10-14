package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
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
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockDetailFragment extends Fragment implements ScriptContract.DetailView {

    private ScriptContract.Presenter mScriptPresenter;

    ActivityBlockDetailBinding mBinding;

    public BlockDetailFragment() {
    }

    DetailListener listener;

    private BlockModel.SpicaBlock spicaBlock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_block_detail, container, false);
        View root = mBinding.getRoot();
        mBinding.setFragment(this);
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //presenterで保持してるTargetScriptを取得
        ScriptModel targetScript = mScriptPresenter.getTargetScript();
        //ブロック種類を取得
        spicaBlock = targetScript.getBlock().getBlock();
        //描画
        drawScript(spicaBlock);
        //シークバーはここで描画
        mBinding.seekValue.setProgress(targetScript.getValue());
        //アニメーションスタート
        popupAnime(view);
    }

    private void popupAnime(View view) {
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f, 1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec
        scaleAnimation.setDuration(200);
        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

    /**
     * mBindingを通して描画するメソッド
     */
    public void drawScript(BlockModel.SpicaBlock spicaBlock) {
        mBinding.blockImage.setImageResource(spicaBlock.getIcResource());
        mBinding.blockTitleText.setText(spicaBlock.getName());

        if (spicaBlock == BlockModel.SpicaBlock.RIGHT || spicaBlock == BlockModel.SpicaBlock.LEFT) {
            mBinding.switchContainerDetail.setVisibility(View.VISIBLE);
        } else {
            mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
    }

    public interface DetailListener {
        void onClickAdd(ScriptModel script);
    }

    public void setAddClickListener(BlockDetailFragment.DetailListener listener) {
        this.listener = listener;
    }

    /**
     * fragmentとじる
     */
    public void close() {
        //スイッチをoffに
        mBinding.switchDetail.setChecked(false);
        getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
    }

    /**
     * 決定されたら反映したスクリプトをActivityに送る
     */
    public void confirm() {
        ScriptModel script = mScriptPresenter.getTargetScript();
        script.setBlock(new BlockModel(spicaBlock));
        script.setValue(mBinding.seekValue.getProgress());
        //スイッチをoffに
        mBinding.switchDetail.setChecked(false);
        listener.onClickAdd(script);
    }

    /**
     * 回転の逆回転スイッチの処理
     */
    public void onCheckedChanged() {
        boolean b = mBinding.switchDetail.isChecked();
        BlockModel.SpicaBlock defaultBlock = mScriptPresenter.getTargetScript().getBlock().getBlock();
        if (b) {
            if (defaultBlock == BlockModel.SpicaBlock.LEFT) {
                spicaBlock = BlockModel.SpicaBlock.RIGHT;
            } else {
                spicaBlock = BlockModel.SpicaBlock.LEFT;
            }
        } else {
            spicaBlock = defaultBlock;
        }
        drawScript(spicaBlock);
    }

}

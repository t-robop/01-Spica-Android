package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockDetailFragment extends Fragment implements ScriptContract.DetailView {

    private ScriptContract.Presenter mScriptPresenter;

    ActivityBlockDetailBinding mBinding;

    public BlockDetailFragment() {
    }

    DetailListener listener;

    private ScriptModel.SpicaBlock spicaBlock;

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
        spicaBlock = targetScript.getBlock();
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
    public void drawScript(ScriptModel.SpicaBlock spicaBlock) {
//        mBinding.blockImage.setImageResource(spicaBlock.getIcResource());
//        mBinding.blockTitleText.setText(spicaBlock.getName());

        if (spicaBlock == ScriptModel.SpicaBlock.RIGHT || spicaBlock == ScriptModel.SpicaBlock.LEFT) {
            mBinding.switchContainerDetail.setVisibility(View.VISIBLE);
        } else {
            mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
        }

        if (mScriptPresenter.getState() == ScriptPresenter.ViewState.EDIT) {
            mBinding.detailDeleteBtn.setVisibility(View.VISIBLE);
        } else {
            mBinding.detailDeleteBtn.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
    }

    public interface DetailListener {
        void onClickAdd(ScriptModel script);

        void onClickDelete(int pos);
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
        script.setValue(mBinding.seekValue.getProgress());
        //スイッチをoffに
        mBinding.switchDetail.setChecked(false);
        listener.onClickAdd(script);
    }

    /**
     * 削除ボタン
     */
    public void delete() {
        listener.onClickDelete(mScriptPresenter.getTargetScript().getPos());
    }

    /**
     * 回転の逆回転スイッチの処理
     */
    public void onCheckedChanged() {
        boolean b = mBinding.switchDetail.isChecked();
        ScriptModel.SpicaBlock defaultBlock = mScriptPresenter.getTargetScript().getBlock();
        if (b) {
            if (defaultBlock == ScriptModel.SpicaBlock.LEFT) {
                spicaBlock = ScriptModel.SpicaBlock.RIGHT;
            } else {
                spicaBlock = ScriptModel.SpicaBlock.LEFT;
            }
        } else {
            spicaBlock = defaultBlock;
        }
        drawScript(spicaBlock);
    }

}

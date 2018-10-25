package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockDetailFragment extends DialogFragment implements ScriptContract.DetailView {

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

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mBinding.fgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO キーボードが表示されているときは閉じる
            }
        });

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //アニメーションスタート
        popupAnime(view);
    }

    @Override
    public void onStart(){
        super.onStart();
        //presenterで保持してるTargetScriptを取得
        ScriptModel targetScript = mScriptPresenter.getTargetScript();
        //ブロック種類を取得
        spicaBlock = targetScript.getBlock();
        //描画
        drawScript(spicaBlock);
        //シークバー描画
        setSeekValue(spicaBlock, targetScript.getSeekValue());
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
    public void drawScript(ScriptModel.SpicaBlock blockId) {

        switch (blockId) {
            case FRONT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_front);
                mBinding.blockTitleText.setText(R.string.block_front_name);
                mBinding.blockDesText.setText(R.string.block_front_description);
                break;

            case BACK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_back);
                mBinding.blockTitleText.setText(R.string.block_back_name);
                mBinding.blockDesText.setText(R.string.block_back_description);
                break;

            case LEFT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_left);
                mBinding.blockTitleText.setText(R.string.block_left_name);
                mBinding.blockDesText.setText(R.string.block_left_description);
                break;

            case RIGHT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_right);
                mBinding.blockTitleText.setText(R.string.block_right_name);
                mBinding.blockDesText.setText(R.string.block_right_description);
                break;

            case IF_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_if_wall);
                mBinding.blockTitleText.setText(R.string.block_if_start_name);
                mBinding.blockDesText.setText(R.string.block_if_start_description);
                break;

            case FOR_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_for_start);
                mBinding.blockTitleText.setText(R.string.block_for_start_name);
                mBinding.blockDesText.setText(R.string.block_for_start_description);
                break;

            case BREAK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_break);
                mBinding.blockTitleText.setText(R.string.block_break_name);
                mBinding.blockDesText.setText(R.string.block_break_description);
                break;
        }

        if (blockId == ScriptModel.SpicaBlock.RIGHT || blockId == ScriptModel.SpicaBlock.LEFT) {
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
        mBinding.seekValue.setProgress(0);
        //TODO ソフトウェアキーボードが開いてたら閉じる
        getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
    }

    /**
     * 決定されたら反映したスクリプトをActivityに送る
     */
    public void confirm() {
        ScriptModel script = mScriptPresenter.getTargetScript();
        script.setBlock(spicaBlock);
        script.setSeekValue(mBinding.seekValue.getProgress());

        switch (spicaBlock){
            case FRONT:
            case BACK:
            case LEFT:
            case RIGHT:
                script.setValue(getExecTime());
                break;

            case IF_START:
                break;

            case FOR_START:
                break;
        }

        //スイッチをoffに
        mBinding.switchDetail.setChecked(false);
        mBinding.seekValue.setProgress(0);
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

    private void setSeekValue(ScriptModel.SpicaBlock blockId, int seekValue){
        if ((blockId == ScriptModel.SpicaBlock.FRONT || blockId == ScriptModel.SpicaBlock.BACK || blockId == ScriptModel.SpicaBlock.LEFT || blockId == ScriptModel.SpicaBlock.RIGHT)){
            mBinding.seekValue.setMax(2);
            mBinding.seekValue.setVisibility(View.VISIBLE);
            mBinding.seekValue.setProgress(seekValue);
        }else{
            mBinding.seekValue.setVisibility(View.INVISIBLE);
        }
    }

    private float getExecTime(){
        String editValueText = mBinding.editValue.getText().toString();
        return Float.valueOf(editValueText);
    }

    private int getSensorJudgeValue(){
        String editValueText = mBinding.editValue.getText().toString();
        return Integer.valueOf(editValueText);
    }

    private int getLoopCount(){
        String editValueText = mBinding.editValue.getText().toString();
        return Integer.valueOf(editValueText);
    }

}

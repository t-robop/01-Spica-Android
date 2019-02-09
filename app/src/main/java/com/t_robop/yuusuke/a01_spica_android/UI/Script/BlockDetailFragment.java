package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RadioGroup;

import com.t_robop.yuusuke.a01_spica_android.Block;
import com.t_robop.yuusuke.a01_spica_android.Config;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.FragmentBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;

public class BlockDetailFragment extends DialogFragment implements ScriptContract.DetailView {

    private ScriptContract.Presenter mScriptPresenter;

    FragmentBlockDetailBinding mBinding;

    final int STANDARD_BLOCK_MAX_PROGRESS = 500;

    // IF_BLOCK_MAX_PROGRESS > IF_BLOCK_GAP_PROGRESS
    final int IF_BLOCK_MAX_PROGRESS = 30;
    final int IF_BLOCK_GAP_PROGRESS = 10;

    final int FOR_BLOCK_MAX_PROGRESS = 10;

    public BlockDetailFragment() {
    }

    DetailListener listener;

    private String blockId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_block_detail, container, false);
        View root = mBinding.getRoot();
        mBinding.setFragment(this);
        mBinding.fgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // no-op
            }
        });
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //アニメーションスタート
        popupAnimation(mBinding.fgDetail);
        if (mScriptPresenter.getState() == ScriptPresenter.ViewState.EDIT) {
            alphaAnimation(mBinding.bgDetail);
        } else {
            mBinding.bgDetail.setBackgroundResource(R.color.alpha_clear);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //presenterで保持してるTargetScriptを取得
        UIBlockModel targetScript = mScriptPresenter.getTargetScript();
        //ブロック種類を取得
        blockId = targetScript.getId();
        //描画
        drawScript(blockId);

        int res = R.id.speed_middle_radio_button;
        switch (targetScript.getPower()) {
            case Config.LOW_POWER:
                res = R.id.speed_low_radio_button;
                break;
            case Config.MIDDLE_POWER:
                res = R.id.speed_middle_radio_button;
                break;
            case Config.HIGH_POWER:
                res = R.id.speed_high_radio_button;
                break;
        }

        switch (blockId) {
            case Block.FrontBlock.id:
                mBinding.speedRadioGroup.check(res);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                mBinding.seekValue.setProgress((int) (targetScript.getTime() * 100));
                break;

            case Block.BackBlock.id:
                mBinding.speedRadioGroup.check(res);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                mBinding.seekValue.setProgress((int) (targetScript.getTime() * 100));
                break;

            case Block.LeftBlock.id:
                mBinding.speedRadioGroup.check(res);
                mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                mBinding.seekValue.setProgress((int) (targetScript.getTime() * 100));
                break;

            case Block.RightBlock.id:
                mBinding.speedRadioGroup.check(res);
                mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                mBinding.seekValue.setProgress((int) (targetScript.getTime() * 100));
                break;

            case Block.IfStartBlock.id:
                if (targetScript.getIfOperator() == Config.SENSOR_ABOVE) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_above_text);
                } else if (targetScript.getIfOperator() == Config.SENSOR_BELOW) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_below_text);
                } else {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_above_text);
                }
                mBinding.seekValue.setProgress((int) targetScript.getThreshold() - IF_BLOCK_GAP_PROGRESS);
                mBinding.seekValue.setMax(IF_BLOCK_MAX_PROGRESS);
                break;

            case Block.ForStartBlock.id:
                mBinding.seekValue.setProgress(targetScript.getLoopNum());
                mBinding.seekValue.setMax(FOR_BLOCK_MAX_PROGRESS);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_loop_unit_text);
                break;
        }
        //シークバーのテキストに反映
        setSeekValueText();
    }

    private void popupAnimation(View view) {
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f, 1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 ms
        scaleAnimation.setDuration(200);
        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

    private void alphaAnimation(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.5f);
        // animation時間 ms
        alphaAnimation.setDuration(200);
        // 繰り返し回数
        alphaAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        alphaAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(alphaAnimation);
    }

    /**
     * mBindingを通して描画するメソッド
     */
    //FIXME Blockクラスを使ってset~を削減する
    public void drawScript(String blockId) {

        switch (blockId) {
            case Block.FrontBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_front);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_forward_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_forward_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_forward_unit_text);
                break;

            case Block.BackBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_back);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_back_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_back_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_back_unit_text);
                break;

            case Block.LeftBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_left);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_left_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_left_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_left_rotate);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_right_rotate);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_left_unit_text);
                break;

            case Block.RightBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_right);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_right_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_right_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_left_rotate);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_right_rotate);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_right_unit_text);
                break;

            case Block.IfStartBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_if_wall);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_if_start_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_if_start_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_above);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_below);
                mBinding.speedRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_purple);
                break;

            case Block.ForStartBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_for_start);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_for_start_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_for_start_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.speedRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_yellow_2);
                break;

            case Block.BreakBlock.id:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_break);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_break_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_break_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.speedRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_red);
                mBinding.seekValue.setVisibility(View.INVISIBLE);
                break;
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
        void onClickAdd(UIBlockModel uiBlockModel);

        void onClickDelete(int pos);
    }

    public void setAddClickListener(BlockDetailFragment.DetailListener listener) {
        this.listener = listener;
    }

    /**
     * fragmentとじる
     */
    public void close() {
        getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
    }

    /**
     * fragmentの外をタップしたとき
     */
    public void cancel() {
        confirm();
        getFragmentManager().beginTransaction().remove(BlockDetailFragment.this).commit();
    }

    /**
     * 決定されたら反映したスクリプトをActivityに送る
     */
    public void confirm() {
        UIBlockModel uiBlockModel = mScriptPresenter.getTargetScript();
        uiBlockModel.setId(blockId);
        float p = mBinding.seekValue.getProgress();
        // 通常ブロックの時
        if (mBinding.seekValue.getMax() == STANDARD_BLOCK_MAX_PROGRESS) {
            p = p / 100;
        } else if (mBinding.seekValue.getMax() == IF_BLOCK_MAX_PROGRESS) {
            p += IF_BLOCK_GAP_PROGRESS;
        }
        uiBlockModel.setValue(p);

        switch (blockId) {
            case Block.FrontBlock.id:
            case Block.BackBlock.id:
            case Block.LeftBlock.id:
            case Block.RightBlock.id:
                //スピード値の設定
                int speedCheckId = mBinding.speedRadioGroup.getCheckedRadioButtonId();
                if (speedCheckId == R.id.speed_low_radio_button) {
                    uiBlockModel.setLowPower();
                } else if (speedCheckId == R.id.speed_middle_radio_button) {
                    uiBlockModel.setMiddlePower();
                } else {
                    uiBlockModel.setHighPower();
                }
                break;

            case Block.IfStartBlock.id:
                ////ifスタートブロックの条件指定
                int checkId = mBinding.settingRadioGroup.getCheckedRadioButtonId();
                if (checkId == R.id.radiobutton_left) {
                    uiBlockModel.setSensorAbove();
                } else {
                    uiBlockModel.setSensorBelow();
                }
                break;
        }

        listener.onClickAdd(uiBlockModel);
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
    //Binding Method 引数は固定
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        if (blockId.equals(Block.LeftBlock.id) || blockId.equals(Block.RightBlock.id)) {
            if (checkedId == R.id.radiobutton_left) {
                blockId = Block.LeftBlock.id;
            } else if (checkedId == R.id.radiobutton_right) {
                blockId = Block.RightBlock.id;
            }
            drawScript(blockId);
        } else if (blockId.equals(Block.IfStartBlock.id)) {
            if (checkedId == R.id.radiobutton_left) {
                mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_above_text);
            } else if (checkedId == R.id.radiobutton_right) {
                mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_below_text);
            }
        }
    }

    /**
     * 時間のシークバー変更時
     */
    public void onProgressChanged() {
        setSeekValueText();
    }

    private void setSeekValueText() {
        if (mBinding.seekValue.getMax() == STANDARD_BLOCK_MAX_PROGRESS) {
            float p = mBinding.seekValue.getProgress();
            mBinding.textValue.setText(String.valueOf(p / 100));
        } else if (mBinding.seekValue.getMax() == IF_BLOCK_MAX_PROGRESS) {
            int p = mBinding.seekValue.getProgress();
            p += IF_BLOCK_GAP_PROGRESS;
            mBinding.textValue.setText(String.valueOf(p));
        } else if (mBinding.seekValue.getMax() == FOR_BLOCK_MAX_PROGRESS) {
            int p = mBinding.seekValue.getProgress();
            mBinding.textValue.setText(String.valueOf(p));
        }
    }
}

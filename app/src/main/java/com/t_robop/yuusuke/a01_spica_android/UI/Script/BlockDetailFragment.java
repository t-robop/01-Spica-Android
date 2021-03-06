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

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.IF_START;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.LEFT;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.RIGHT;

public class BlockDetailFragment extends DialogFragment implements ScriptContract.DetailView {

    private ScriptContract.Presenter mScriptPresenter;

    ActivityBlockDetailBinding mBinding;

    final int STANDARD_BLOCK_GAP_PROGRESS = 10;
    final int STANDARD_BLOCK_MAX_PROGRESS = 490;

    // IF_BLOCK_GAP_PROGRESS ~ IF_BLOCK_MAX_PROGRESS
    final int IF_BLOCK_MAX_PROGRESS = 20;
    final int IF_BLOCK_GAP_PROGRESS = 10;

    // FOR_BLOCK_GAP_PROGRESS ~ FOR_BLOCK_MAX_PROGRESS
    final int FOR_BLOCK_MAX_PROGRESS = 8;
    final int FOR_BLOCK_GAP_PROGRESS = 2;

    public BlockDetailFragment() {
    }

    DetailListener listener;

    private SpicaBlock spicaBlock;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.activity_block_detail, container, false);
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
        popupAnime(mBinding.fgDetail);
        if (mScriptPresenter.getState() == ScriptPresenter.ViewState.EDIT) {
            alphaAnime(mBinding.bgDetail);
        } else {
            mBinding.bgDetail.setBackgroundResource(R.color.alpha_clear);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //presenterで保持してるTargetScriptを取得
        ScriptModel targetScript = mScriptPresenter.getTargetScript();
        //ブロック種類を取得
        spicaBlock = targetScript.getBlock();
        //シークバー値
        int seekProgress = 0;
        //描画
        drawScript(spicaBlock);
        switch (spicaBlock) {
            case FRONT:
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                seekProgress = (int) (targetScript.getValue() * 10) - STANDARD_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                break;
            case BACK:
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                seekProgress = (int) (targetScript.getValue() * 10) - STANDARD_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                break;
            case LEFT:
                mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                seekProgress = (int) (targetScript.getValue() * 10) - STANDARD_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                break;

            case RIGHT:
                mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                mBinding.seekValue.setMax(STANDARD_BLOCK_MAX_PROGRESS);
                seekProgress = (int) (targetScript.getValue() * 10) - STANDARD_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                break;

            case IF_START:
                if (targetScript.getIfOperator() == targetScript.getSensorAboveNum()) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_above_text);
                } else if (targetScript.getIfOperator() == targetScript.getSensorBelowNum()) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_below_text);
                } else {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.block_detail_fragment_compare_above_text);
                }
                seekProgress = (int) targetScript.getValue() - IF_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                mBinding.seekValue.setMax(IF_BLOCK_MAX_PROGRESS);
                break;
            case FOR_START:
                seekProgress = (int) targetScript.getValue() - FOR_BLOCK_GAP_PROGRESS;
                mBinding.seekValue.setProgress(seekProgress);
                mBinding.seekValue.setMax(FOR_BLOCK_MAX_PROGRESS);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_loop_unit_text);
                break;
        }
        //シークバーのテキストに反映
        syncSeekValue(seekProgress);
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

    private void alphaAnime(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.5f);
        // animation時間 msec
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
    public void drawScript(SpicaBlock blockId) {

        switch (blockId) {
            case FRONT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_front);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_forward_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_forward_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_forward_unit_text);
                break;

            case BACK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_back);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_back_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_back_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_back_unit_text);
                break;

            case LEFT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_left);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_left_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_left_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_left_rotate);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_right_rotate);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_left_unit_text);
                break;

            case RIGHT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_right);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_right_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_right_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_left_rotate);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_right_rotate);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.block_detail_fragment_right_unit_text);
                break;

            case IF_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_if_wall);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_if_start_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_if_start_description);
                mBinding.radiobuttonLeft.setText(R.string.block_detail_fragment_block_state_above);
                mBinding.radiobuttonRight.setText(R.string.block_detail_fragment_block_state_below);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_purple);
                break;

            case FOR_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_for_start);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_for_start_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_for_start_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_yellow_2);
                break;

            case BREAK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_break);
                mBinding.blockTitleText.setText(R.string.block_detail_fragment_block_break_name);
                mBinding.blockDesText.setText(R.string.block_detail_fragment_block_break_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
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
        ScriptModel script = mScriptPresenter.getTargetScript();
        script.setBlock(spicaBlock);
        float p = mBinding.seekValue.getProgress();
        // 通常ブロックの時
        if (mBinding.seekValue.getMax() == STANDARD_BLOCK_MAX_PROGRESS) {
            p += STANDARD_BLOCK_GAP_PROGRESS;
            p = p / 10;
        } else if (mBinding.seekValue.getMax() == IF_BLOCK_MAX_PROGRESS) {
            p += IF_BLOCK_GAP_PROGRESS;
        } else if (mBinding.seekValue.getMax() == FOR_BLOCK_MAX_PROGRESS) {
            p += FOR_BLOCK_GAP_PROGRESS;
        }
        script.setValue(p);

        if (spicaBlock == IF_START) {
            ////ifスタートブロックの条件指定
            int checkId = mBinding.settingRadioGroup.getCheckedRadioButtonId();
            if (checkId == R.id.radiobutton_left) {
                script.setIfOperator(script.getSensorAboveNum());
            } else {
                script.setIfOperator(script.getSensorBelowNum());
            }
        }

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
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (spicaBlock == LEFT || spicaBlock == RIGHT) {
            if (checkedId == R.id.radiobutton_left) {
                spicaBlock = LEFT;
            } else if (checkedId == R.id.radiobutton_right) {
                spicaBlock = RIGHT;
            }
            drawScript(spicaBlock);
        } else if (spicaBlock == IF_START) {
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
    public void onProgressChanged(int progress) {
        syncSeekValue(progress);
    }

    private void syncSeekValue(int progress) {
        if (mBinding.seekValue.getMax() == STANDARD_BLOCK_MAX_PROGRESS) {
            float p = mBinding.seekValue.getProgress();
            p += STANDARD_BLOCK_GAP_PROGRESS;
            mBinding.textValue.setText(String.valueOf(p / 100));
            mBinding.seekValueScreen.setMax(STANDARD_BLOCK_MAX_PROGRESS);
        } else if (mBinding.seekValue.getMax() == IF_BLOCK_MAX_PROGRESS) {
            int p = mBinding.seekValue.getProgress();
            p += IF_BLOCK_GAP_PROGRESS;
            mBinding.textValue.setText(String.valueOf(p));
            mBinding.seekValueScreen.setMax(IF_BLOCK_MAX_PROGRESS);
        } else if (mBinding.seekValue.getMax() == FOR_BLOCK_MAX_PROGRESS) {
            int p = mBinding.seekValue.getProgress();
            p += FOR_BLOCK_GAP_PROGRESS;
            mBinding.textValue.setText(String.valueOf(p));
            mBinding.seekValueScreen.setMax(FOR_BLOCK_MAX_PROGRESS);
        }
        mBinding.seekValueScreen.setProgress(progress);
    }
}

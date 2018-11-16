package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
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
        //描画
        drawScript(spicaBlock);
        int res = R.id.speed_middle_radio_button;
        ;
        if (targetScript.getSpeed() == 1) {
            res = R.id.speed_low_radio_button;
        } else if (targetScript.getSpeed() == 2) {
            res = R.id.speed_middle_radio_button;
        } else if (targetScript.getSpeed() == 3) {
            res = R.id.speed_high_radio_button;
        }
        switch (spicaBlock) {
            case FRONT:
                mBinding.speedRadioGroup.check(res);
                mBinding.seekValue.setMax(300);
                mBinding.seekValue.setProgress((int) (targetScript.getValue() * 100));
                break;
            case BACK:
                mBinding.speedRadioGroup.check(res);
                mBinding.seekValue.setMax(300);
                mBinding.seekValue.setProgress((int) (targetScript.getValue() * 100));
                break;
            case LEFT:
                mBinding.speedRadioGroup.check(res);
                mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                mBinding.seekValue.setMax(300);
                mBinding.seekValue.setProgress((int) (targetScript.getValue() * 100));
                break;

            case RIGHT:
                mBinding.speedRadioGroup.check(res);
                mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                mBinding.seekValue.setMax(300);
                mBinding.seekValue.setProgress((int) (targetScript.getValue() * 100));
                break;

            case IF_START:
                if (targetScript.getIfOperator() == targetScript.getSensorAboveNum()) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.text_value_des_if_fast);
                } else if (targetScript.getIfOperator() == targetScript.getSensorBelowNum()) {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_right);
                    mBinding.textValueDes.setText(R.string.text_value_des_if_near);
                } else {
                    mBinding.settingRadioGroup.check(R.id.radiobutton_left);
                    mBinding.textValueDes.setText(R.string.text_value_des_if_fast);
                }
                mBinding.seekValue.setProgress((int) targetScript.getValue());
                mBinding.seekValue.setMax(10);
                break;
            case FOR_START:
                mBinding.seekValue.setProgress((int) targetScript.getValue());
                mBinding.seekValue.setMax(10);
                mBinding.textValueDes.setText(R.string.text_value_des_for);
                break;
        }
        //シークバーのテキストに反映
        setSeekValueText();
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
                mBinding.blockTitleText.setText(R.string.block_front_name);
                mBinding.blockDesText.setText(R.string.block_front_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.text_value_des_front);
                break;

            case BACK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_back);
                mBinding.blockTitleText.setText(R.string.block_back_name);
                mBinding.blockDesText.setText(R.string.block_back_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.text_value_des_back);
                break;

            case LEFT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_left);
                mBinding.blockTitleText.setText(R.string.block_left_name);
                mBinding.blockDesText.setText(R.string.block_left_description);
                mBinding.radiobuttonLeft.setText(R.string.block_left_name);
                mBinding.radiobuttonRight.setText(R.string.block_right_name);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.text_value_des_left);
                break;

            case RIGHT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_right);
                mBinding.blockTitleText.setText(R.string.block_right_name);
                mBinding.blockDesText.setText(R.string.block_right_description);
                mBinding.radiobuttonLeft.setText(R.string.block_left_name);
                mBinding.radiobuttonRight.setText(R.string.block_right_name);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_blue);
                mBinding.textValueDes.setText(R.string.text_value_des_right);
                break;

            case IF_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_if_wall);
                mBinding.blockTitleText.setText(R.string.block_if_start_name);
                mBinding.blockDesText.setText(R.string.block_if_start_description);
                mBinding.radiobuttonLeft.setText(R.string.dialog_sensor_bigger);
                mBinding.radiobuttonRight.setText(R.string.dialog_sensor_smaller);
                mBinding.speedRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_purple);
                break;

            case FOR_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_for_start);
                mBinding.blockTitleText.setText(R.string.block_for_start_name);
                mBinding.blockDesText.setText(R.string.block_for_start_description);
                mBinding.settingRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.speedRadioGroup.setVisibility(View.INVISIBLE);
                mBinding.bgDetailBlockView.setBackgroundResource(R.color.color_yellow_2);
                break;

            case BREAK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_break);
                mBinding.blockTitleText.setText(R.string.block_break_name);
                mBinding.blockDesText.setText(R.string.block_break_description);
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
        if (mBinding.seekValue.getMax() == 300) {
            p = p / 100;
        }
        script.setValue(p);

        switch (spicaBlock) {
            case FRONT:
            case BACK:
            case LEFT:
            case RIGHT:
                //スピード値の設定
                int speedCheckId = mBinding.speedRadioGroup.getCheckedRadioButtonId();
                if (speedCheckId == R.id.speed_low_radio_button) {
                    script.setSpeed(script.getLowSpeedValue());
                } else if (speedCheckId == R.id.speed_middle_radio_button) {
                    script.setSpeed(script.getMiddleSpeedValue());
                } else {
                    script.setSpeed(script.getHighSpeedValue());
                }
                break;

            case IF_START:
                ////ifスタートブロックの条件指定
                int checkId = mBinding.settingRadioGroup.getCheckedRadioButtonId();
                if (checkId == R.id.radiobutton_left) {
                    script.setIfOperator(script.getSensorAboveNum());
                } else {
                    script.setIfOperator(script.getSensorBelowNum());
                }
                break;
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
                mBinding.textValueDes.setText(R.string.text_value_des_if_fast);
            } else if (checkedId == R.id.radiobutton_right) {
                mBinding.textValueDes.setText(R.string.text_value_des_if_near);
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
        if (mBinding.seekValue.getMax() == 300) {
            float p = mBinding.seekValue.getProgress();
            mBinding.textValue.setText(String.valueOf(p / 100));
        } else if (mBinding.seekValue.getMax() == 10) {
            int p = mBinding.seekValue.getProgress();
            mBinding.textValue.setText(String.valueOf(p));
        }
    }
}

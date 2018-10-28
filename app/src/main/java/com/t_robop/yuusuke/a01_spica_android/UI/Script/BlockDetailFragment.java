package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioGroup;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityBlockDetailBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.IF_START;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.LEFT;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.RIGHT;

public class BlockDetailFragment extends DialogFragment implements ScriptContract.DetailView, TextWatcher {

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
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        });

        mBinding.editValue.addTextChangedListener(this);

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //アニメーションスタート
        popupAnime(view);
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
        //editText初期化
        switch (spicaBlock) {
            //IF_STARTとFOR_STARTは小数
            case IF_START:
                mBinding.editValue.setText(String.valueOf((int)targetScript.getValue()));
                break;
            case FOR_START:
                mBinding.editValue.setText(String.valueOf((int)targetScript.getValue()));
                break;
            default:
                mBinding.editValue.setText(String.valueOf(targetScript.getValue()));
                break;
        }
        //チェックボックス
        switch (spicaBlock) {
            case FRONT:
            case BACK:
                mBinding.speedRadioGroup.check(R.id.speed_middle_radio_button);
                break;
            case LEFT:
                mBinding.speedRadioGroup.check(R.id.speed_middle_radio_button);
                mBinding.radiogroup.check(R.id.radiobutton_left);
                break;

            case RIGHT:
                mBinding.speedRadioGroup.check(R.id.speed_middle_radio_button);
                mBinding.radiogroup.check(R.id.radiobutton_right);
                break;

            case IF_START:
                if (targetScript.getIfOperator() == targetScript.getSensorAboveNum()) {
                    mBinding.radiogroup.check(R.id.radiobutton_left);
                } else if (targetScript.getIfOperator() == targetScript.getSensorBelowNum()) {
                    mBinding.radiogroup.check(R.id.radiobutton_right);
                } else {
                    mBinding.radiogroup.check(R.id.radiobutton_left);
                }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
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
    public void drawScript(SpicaBlock blockId) {

        switch (blockId) {
            case FRONT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_front);
                mBinding.blockTitleText.setText(R.string.block_front_name);
                mBinding.blockDesText.setText(R.string.block_front_description);
                mBinding.editValue.setHint(R.string.dialog_time);
                mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
                break;

            case BACK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_back);
                mBinding.blockTitleText.setText(R.string.block_back_name);
                mBinding.blockDesText.setText(R.string.block_back_description);
                mBinding.editValue.setHint(R.string.dialog_time);
                mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
                break;

            case LEFT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_left);
                mBinding.blockTitleText.setText(R.string.block_left_name);
                mBinding.blockDesText.setText(R.string.block_left_description);
                mBinding.editValue.setHint(R.string.dialog_time);
                mBinding.radiobuttonLeft.setText(R.string.common_left);
                mBinding.radiobuttonRight.setText(R.string.common_right);
                break;

            case RIGHT:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_right);
                mBinding.blockTitleText.setText(R.string.block_right_name);
                mBinding.blockDesText.setText(R.string.block_right_description);
                mBinding.editValue.setHint(R.string.dialog_time);
                mBinding.radiobuttonLeft.setText(R.string.common_left);
                mBinding.radiobuttonRight.setText(R.string.common_right);
                break;

            case IF_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_if_wall);
                mBinding.blockTitleText.setText(R.string.block_if_start_name);
                mBinding.blockDesText.setText(R.string.block_if_start_description);
                mBinding.editValue.setHint(R.string.dialog_sensor_num);
                mBinding.radiobuttonLeft.setText(R.string.dialog_sensor_bigger);
                mBinding.radiobuttonRight.setText(R.string.dialog_sensor_smaller);
                mBinding.speedContainer.setVisibility(View.INVISIBLE);
                break;

            case FOR_START:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_for_start);
                mBinding.blockTitleText.setText(R.string.block_for_start_name);
                mBinding.blockDesText.setText(R.string.block_for_start_description);
                mBinding.editValue.setHint(R.string.dialog_loop_num);
                mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
                mBinding.speedContainer.setVisibility(View.INVISIBLE);
                mBinding.speedContainer.setVisibility(View.INVISIBLE);
                break;

            case BREAK:
                mBinding.blockImage.setImageResource(R.drawable.ic_block_break);
                mBinding.blockTitleText.setText(R.string.block_break_name);
                mBinding.blockDesText.setText(R.string.block_break_description);
                mBinding.editValue.setVisibility(View.INVISIBLE);
                mBinding.switchContainerDetail.setVisibility(View.INVISIBLE);
                mBinding.speedContainer.setVisibility(View.INVISIBLE);
                mBinding.speedContainer.setVisibility(View.INVISIBLE);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
        ScriptModel scriptModel = mScriptPresenter.getTargetScript();
        SpicaBlock blockId = scriptModel.getBlock();
        switch (blockId) {
            case FRONT:
            case BACK:
            case LEFT:
            case RIGHT:
                mBinding.editValue.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                break;

            case IF_START:
            case FOR_START:
                mBinding.editValue.setInputType(InputType.TYPE_CLASS_NUMBER);
                mBinding.editValue.setFilters(new InputFilter[]{new InputFilter.LengthFilter(2)});  //EditText maxLength = "2"
                break;
        }
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        //TODO 整数部: maxLength:2, 小数部: maxLength: 1
        if (charSequence.toString().length() > 3 && charSequence.toString().contains(".")) {
            if (charSequence.toString().length() - charSequence.toString().indexOf(".") > 2) {
                mBinding.editValue.setText(charSequence.toString().substring(0, charSequence.length() - 1));
                mBinding.editValue.setSelection(mBinding.editValue.getText().length());
            }
        }

        if(charSequence.toString().length() > 2){
            if(!charSequence.toString().contains(".")){
                mBinding.editValue.setText(charSequence.toString().substring(0, charSequence.length() - 1));
            }
            mBinding.editValue.setSelection(mBinding.editValue.getText().length());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
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
     * 決定されたら反映したスクリプトをActivityに送る
     */
    public void confirm() {
        ScriptModel script = mScriptPresenter.getTargetScript();
        script.setBlock(spicaBlock);
        script.setValue(getInputText());


        switch (spicaBlock){
            case FRONT:
            case BACK:
            case LEFT:
            case RIGHT:
                //スピード値の設定
                int speedCheckId = mBinding.speedRadioGroup.getCheckedRadioButtonId();
                if (speedCheckId == R.id.speed_low_radio_button){
                    script.setSpeed(script.getLowSpeedValue());
                }else if (speedCheckId == R.id.speed_middle_radio_button){
                    script.setSpeed(script.getMiddleSpeedValue());
                }else{
                    script.setSpeed(script.getHighSpeedValue());
                }
                break;

            case IF_START:
                ////ifスタートブロックの条件指定
                int checkId = mBinding.radiogroup.getCheckedRadioButtonId();
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
        }
    }

    private float getInputText() {
        String editValueText = mBinding.editValue.getText().toString();
        if (editValueText.isEmpty()) editValueText = "0";
        return Float.valueOf(editValueText);
    }

}

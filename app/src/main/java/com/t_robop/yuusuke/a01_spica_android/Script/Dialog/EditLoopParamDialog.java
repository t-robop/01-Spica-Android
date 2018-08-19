package com.t_robop.yuusuke.a01_spica_android.Script.Dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.Script.Model.ItemDataModel;
import com.t_robop.yuusuke.a01_spica_android.Script.ScriptContract;

@SuppressLint("ValidFragment")
public class EditLoopParamDialog extends DialogFragment {

    ScriptContract.Presenter mPresenter;

    @SuppressLint("ValidFragment")
    public EditLoopParamDialog(ScriptContract.Presenter presenter){
        mPresenter=presenter;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_loop_dialog, null);

        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        final EditText editLoopNum = view.findViewById(R.id.edit_loop_num);
        editLoopNum.setInputType(InputType.TYPE_CLASS_NUMBER);


        editLoopNum.setText(Integer.toString(dataModel.getLoopCount()));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editLoopNum.getText().toString().length() != 0 ) {
                            // 数値が入力されてる時
                            dataModel.setLoopCount(Integer.valueOf(editLoopNum.getText().toString()));

                            mPresenter.updateItemParam(listItemPosition, dataModel);
                        }
                    }
                })
                .setNegativeButton("キャンセル", null);
        return builder.create();

    }

}

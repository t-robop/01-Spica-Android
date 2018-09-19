package com.t_robop.yuusuke.a01_spica_android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.t_robop.yuusuke.a01_spica_android.model.ItemDataModel;

public class EditParamDialog extends DialogFragment {

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_dialog, null);

        //ItemDataModelとposition受取
        final int listItemPosition = getArguments().getInt("listItemPosition");
        final ItemDataModel dataModel = (ItemDataModel) getArguments().getSerializable("itemData");

        final EditText editSpeedRight = view.findViewById(R.id.edit_speed_right);
        editSpeedRight.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editSpeedLeft = view.findViewById(R.id.edit_speed_left);
        editSpeedLeft.setInputType(InputType.TYPE_CLASS_NUMBER);
        final EditText editTime = view.findViewById(R.id.edit_time);
        editTime.setInputType(InputType.TYPE_CLASS_NUMBER);

        editSpeedRight.setText(Integer.toString(dataModel.getRightSpeed()));
        editSpeedLeft.setText(Integer.toString(dataModel.getLeftSpeed()));
        editTime.setText(Integer.toString(dataModel.getTime()));

        builder.setView(view)
                .setPositiveButton("決定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        // EditTextの空白判定
                        if (editSpeedRight.getText().toString().length() != 0 && editSpeedLeft.getText().toString().length() != 0 && editTime.getText().toString().length() != 0){
                            // 数値が入力されてる時
                            dataModel.setRightSpeed(Integer.valueOf(editSpeedRight.getText().toString()));
                            dataModel.setLeftSpeed(Integer.valueOf(editSpeedLeft.getText().toString()));
                            dataModel.setTime(Integer.valueOf(editTime.getText().toString()));

                            ScriptActivity scriptActivity = (ScriptActivity) getActivity();
                            scriptActivity.updateItemParam(listItemPosition, dataModel);
                        }
                    }
                })
                .setNegativeButton("キャンセル", null);

        return builder.create();

    }

}

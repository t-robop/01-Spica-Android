package com.t_robop.yuusuke.a01_spica_android.util;

import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.RecyclerAdapter;
import com.t_robop.yuusuke.a01_spica_android.model.ItemDataModel;

import java.util.ArrayList;


public class UtilBlock {
    public enum errorStatus {
        OK,
        NO_BLOCK,
        LOOP_COUNT
    }

    public static String generateUdpStr(RecyclerAdapter recyclerAdapter) {
        StringBuilder sendText = new StringBuilder();
        ArrayList<ItemDataModel> dataArray = recyclerAdapter.getAllItem();

        //構文チェック
        if (compileSuccess(dataArray) == errorStatus.OK) {
            dataArray = evolutionItems(dataArray);
        } else {
            return "";
        }

        for (int i = 0; i < dataArray.size(); i++) {
            sendText.append("&");

            sendText.append(dataArray.get(i).getOrderName());
            sendText.append("&");
            sendText.append(dataArray.get(i).getLeftSpeed());
            sendText.append("&");
            sendText.append(dataArray.get(i).getRightSpeed());
            sendText.append("&");
            sendText.append(dataArray.get(i).getTime() * 1000);

            if (i != dataArray.size() - 1) {
                sendText.append("+");
            }
        }

        return sendText.toString();
    }

    // convertLoopItemの初回呼び出し
    private static ArrayList<ItemDataModel> evolutionItems(ArrayList<ItemDataModel> items) {
        if (items.isEmpty()) {
            return items;
        }
        return convertLoopItem(items, 0, 0);
    }

    //loop文があったら外してリスト化してくれるメソッド
    private static ArrayList<ItemDataModel> convertLoopItem(ArrayList<ItemDataModel> items, int posLoopStart, int cntLoop) {
        int posEnd = -1;
        int i;

        for (i = posLoopStart + 1; i < items.size(); i++) {
            if (items.get(i).getBlockState() == ItemDataModel.BlockState.FOR_START) {
                items = convertLoopItem(items, i, items.get(i).getLoopCount());
            }
            if (items.get(i).getBlockState() == ItemDataModel.BlockState.FOR_END) {
                posEnd = i;
                break;
            }
        }
        if (items.get(posLoopStart).getBlockState() != ItemDataModel.BlockState.FOR_START) {
            return items;
        }

        //loop前の処理を保持
        ArrayList<ItemDataModel> content = new ArrayList<>();
        for (i = 0; i < posLoopStart; i++) {
            content.add(items.get(i));
        }
        //連結
        for (int cnt = 0; cnt < cntLoop; cnt++) {
            for (i = posLoopStart + 1; i < posEnd; i++) {
                content.add(items.get(i));
            }
        }
        //loop外の残りを連結
        for (i = posEnd + 1; i < items.size(); i++) {
            content.add(items.get(i));
        }

        return content;
    }

    //compileの構文チェックメソッド
    public static errorStatus compileSuccess(ArrayList<ItemDataModel> items) {
        int cntLoopStart = 0;
        int cntLoopEnd = 0;
        if (items.size() == 0) {
            return errorStatus.NO_BLOCK;
        }

        for (ItemDataModel item : items) {
            if (item.getBlockState() == ItemDataModel.BlockState.FOR_START) {
                cntLoopStart++;
            } else if (item.getBlockState() == ItemDataModel.BlockState.FOR_END) {
                cntLoopEnd++;
            }
        }

        if (cntLoopStart != cntLoopEnd) {
            return errorStatus.LOOP_COUNT;
        }

        return errorStatus.OK;
    }
}

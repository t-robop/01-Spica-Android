package com.t_robop.yuusuke.a01_spica_android.Script;

import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.AdapterView;

import com.t_robop.yuusuke.a01_spica_android.BasePresenter;
import com.t_robop.yuusuke.a01_spica_android.BaseView;
import com.t_robop.yuusuke.a01_spica_android.Script.Adapter.RecyclerAdapter;
import com.t_robop.yuusuke.a01_spica_android.Script.Model.ItemDataModel;

public interface ScriptContract {
    interface View extends BaseView<Presenter> {

        //RecyclerAdapterをRecyclerViewに反映する
        void setRcyclerAdapter(RecyclerAdapter adapter);

        //editDialogを表示する
        void showEditParamDialog(ItemDataModel data, int pos, boolean isLoop);

        //Toast出す
        void showToast(String message,int length);

        //ふるえる
        void vibrate(int seconds);
    }

    interface Presenter extends BasePresenter {

        //RecyclerView内のItemTouchHelperのコールバック
        ItemTouchHelper.SimpleCallback getRecyclerViewTouchCallBack();

        //blockListのクリックイベント
        AdapterView.OnItemClickListener getBlockListClickCallBack();

        //startButtonのクリックイベント
        android.view.View.OnClickListener getStartBtnClickCallBack();

        //ブロックの内容の一部更新
        void updateItemParam(int listPosition, ItemDataModel dataModel);

        //ブロックの内容の全部更新
        void reloadItem();
    }
}

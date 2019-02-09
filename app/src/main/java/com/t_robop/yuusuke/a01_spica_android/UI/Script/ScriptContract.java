package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;

import java.util.ArrayList;

public interface ScriptContract {

    /**
     * メインのスクリプト
     */
    interface ScriptView extends BaseView<Presenter> {
        void drawScripts(ArrayList<UIBlockModel> scrips);
    }

    /**
     * ブロック選択画面
     */
    interface SelectView extends BaseView<Presenter> {
        /*
         * 配置可能なブロックのみ描画するメソッド
         * todo なにでわたすか決める(リスト or state ?)
         */
    }

    /**
     * ブロック詳細画面
     */
    interface DetailView extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {

        void setScript(UIBlockModel uiBlockModel, int index);

        void insertScript(UIBlockModel uiBlockModel, int beforeIndex);

        void removeScript(int index);

        ArrayList<UIBlockModel> getScripts();

        String getSendableScripts();

        void setState(ScriptPresenter.ViewState state);

        ScriptPresenter.ViewState getState();

        void setTargetScript(UIBlockModel script);

        UIBlockModel getTargetScript();

        void setScripts(ArrayList<UIBlockModel> scripts);
    }
}

interface BaseView<T> {
    void setPresenter(T presenter);
}

interface BasePresenter {
    void start();
}

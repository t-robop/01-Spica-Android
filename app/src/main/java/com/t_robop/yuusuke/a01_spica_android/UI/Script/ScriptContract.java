package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public interface ScriptContract {

    /**
     * メインのスクリプト
     */
    interface ScriptView extends BaseView<Presenter> {
        void drawScripts(ArrayList<ScriptModel> scrips);
    }

    /**
     * ブロック選択画面
     */
    interface SelectView extends BaseView<Presenter> {
        /**
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

        void setScript(ScriptModel script, int index);

        void insertScript(ScriptModel script, int beforeIndex);

        ArrayList<ScriptModel> getScripts();

        String getSendableScripts();

        void setState(ScriptPresenter.ViewState state);

        ScriptPresenter.ViewState getState();

        void setTargetScript(ScriptModel script);

        ScriptModel getTargetScript();
    }
}

interface BaseView<T> {
    void setPresenter(T presenter);
}

interface BasePresenter {
    void start();
}

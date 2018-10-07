package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public interface ScriptContract {
    interface View extends BaseView<Presenter> {
        void drawScripts(ArrayList<ScriptModel> scrips);
    }

    interface Presenter extends BasePresenter {
        void addScript(ScriptModel script);

        void setScript(ScriptModel script,int index);

        ArrayList<ScriptModel> getScripts();
    }
}

interface BaseView<T>{
    void setPresenter(T presenter);
}

interface BasePresenter{
    void start();
}

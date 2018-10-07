package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.View mScriptView;

    private ArrayList<ScriptModel> mScripts;

    public ScriptPresenter(ScriptContract.View scriptView) {
        this.mScriptView=scriptView;
        this.mScriptView.setPresenter(this);
    }

    @Override
    public void start() {
        mScripts=new ArrayList();
    }

    @Override
    public void addScript(ScriptModel script) {
        mScripts.add(script);
    }

    @Override
    public void setScript(ScriptModel script, int index) {
        mScripts.set(index,script);
    }

    @Override
    public ArrayList<ScriptModel> getScripts() {
        return this.mScripts;
    }
}

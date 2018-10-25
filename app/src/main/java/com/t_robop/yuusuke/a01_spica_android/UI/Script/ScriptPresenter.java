package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.annotation.SuppressLint;

import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.ScriptView mScriptView;
    private ScriptContract.SelectView mSelectView;
    private ScriptContract.DetailView mDetailView;

    private ArrayList<ScriptModel> mScripts;

    //今自分がどの画面にいるのかを管理する
    private ViewState mState;
    //画面移動で必要なスクリプト
    private ScriptModel targetScript;

    public ScriptPresenter(
            ScriptContract.ScriptView scriptView,
            ScriptContract.SelectView selectView,
            ScriptContract.DetailView detailView) {
        this.mScriptView = scriptView;
        this.mScriptView.setPresenter(this);
        mScriptView.drawScripts(mScripts);

        this.mSelectView = selectView;
        this.mSelectView.setPresenter(this);

        this.mDetailView = detailView;
        this.mDetailView.setPresenter(this);
    }

    /**
     * 最初の一回だけでok
     */
    @Override
    public void start() {
        mScripts = new ArrayList();
    }

    public void addScript(ScriptModel script) {
        mScripts.add(script);
    }

    /**
     * 指定したindexの次にスクリプトを挿入するメソッド
     */
    public void insert(ScriptModel script, int beforeIndex) {
        mScripts.add(mScripts.get(mScripts.size() - 1));
        for (int i = mScripts.size() - 2; i > beforeIndex; i--) {
            if (i - 1 < 0) {
                break;
            } else {
                mScripts.set(i, mScripts.get(i - 1));
            }
        }
        mScripts.set(beforeIndex + 1, script);
    }

    /**
     *
     */
    public ScriptModel createEmptyBlock(ScriptModel.SpicaBlock spicaBlock, int ifState) {
        ScriptModel scriptOther = new ScriptModel(spicaBlock);
        scriptOther.setIfState(ifState);
        return scriptOther;
    }

    @Override
    public void setScript(ScriptModel script, int index) {
        mScripts.set(index, script);
        mScriptView.drawScripts(mScripts);
    }

    /**
     * スクリプト追加メソッド
     */
    @Override
    public void insertScript(ScriptModel script, int beforeIndex) {
        if (beforeIndex == mScripts.size() - 1) {
            addScript(script);
            if (script.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                addScript(createEmptyBlock(ScriptModel.SpicaBlock.IF_END, script.getIfState()));
            } else if (script.getBlock() == ScriptModel.SpicaBlock.FOR_START) {
                addScript(createEmptyBlock(ScriptModel.SpicaBlock.FOR_END, script.getIfState()));
            }
        } else if (beforeIndex < mScripts.size() - 1) {
            insert(script, beforeIndex);
            if (script.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                insert(createEmptyBlock(ScriptModel.SpicaBlock.IF_END, script.getIfState()),
                        beforeIndex + 1);
            } else if (script.getBlock() == ScriptModel.SpicaBlock.FOR_START) {
                insert(createEmptyBlock(ScriptModel.SpicaBlock.FOR_END, script.getIfState()),
                        beforeIndex + 1);
            }
        }
        mScriptView.drawScripts(mScripts);
    }

    @Override
    public void removeScript(int index) {
        ScriptModel script = mScripts.get(index);
        int size = mScripts.size();
        if (script.getBlock() == ScriptModel.SpicaBlock.IF_START) {
            for (int i = index; i < size; i++) {
                if (mScripts.get(index).getBlock() == ScriptModel.SpicaBlock.IF_END) {
                    break;
                }
                mScripts.remove(index);
            }
        } else if (script.getBlock() == ScriptModel.SpicaBlock.FOR_START) {
            for (int i = index; i < size; i++) {
                if (mScripts.get(index).getBlock() == ScriptModel.SpicaBlock.FOR_END) {
                    break;
                }
                mScripts.remove(index);
            }
        }
        mScripts.remove(index);
        mScriptView.drawScripts(mScripts);
    }

    @Override
    public ArrayList<ScriptModel> getScripts() {
        return this.mScripts;
    }

    /**
     * スクリプト一覧を送信可能データにするメソッド
     */
    @Override
    public String getSendableScripts() {
        String sendStringData = "";
        for (ScriptModel script : mScripts) {
            String ifState = "0" + String.valueOf(script.getIfState());
            @SuppressLint("DefaultLocale") String blockId = String.format("%02d", script.getBlock().getId());
            @SuppressLint("DefaultLocale") String RightSpeed = String.format("%03d", script.getRightSpeed(script.getSeekValue()));
            @SuppressLint("DefaultLocale") String LeftSpeed = String.format("%03d", script.getLeftSpeed(script.getSeekValue()));
            @SuppressLint("DefaultLocale") String value = String.format("%03d", (int)Math.round(script.getValue() * 10.0));

            sendStringData = sendStringData + String.valueOf(ifState) + blockId + String.valueOf(RightSpeed) + String.valueOf(LeftSpeed) + String.valueOf(value);
        }
        return sendStringData;
    }

    @Override
    public void setState(ViewState state) {
        this.mState = state;
    }

    @Override
    public ViewState getState() {
        return this.mState;
    }

    @Override
    public void setTargetScript(ScriptModel script) {
        this.targetScript = script;
    }

    @Override
    public ScriptModel getTargetScript() {
        return this.targetScript;
    }

    public enum ViewState {
        SCRIPT(0),
        SELECT(1),
        ADD(2),
        EDIT(3);

        int id;

        ViewState(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}

package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.util.Log;

import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.ScriptView mScriptView;
    private ScriptContract.SelectView mSelectView;
    private ScriptContract.DetailView mDetailView;

    private ArrayList<ScriptModel> mScripts;

    //今自分がどの画面にいるのかを管理する
    private ViewState mState;

    public ScriptPresenter(ScriptContract.ScriptView scriptView) {
        this.mScriptView = scriptView;
        this.mScriptView.setPresenter(this);
        mScriptView.drawScripts(mScripts);
    }
    public ScriptPresenter(ScriptContract.SelectView selectView) {
        this.mSelectView = selectView;
        this.mSelectView.setPresenter(this);
    }
    public ScriptPresenter(ScriptContract.DetailView detailView) {
        this.mDetailView = detailView;
        this.mDetailView.setPresenter(this);
    }

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
            if(i-1<0){
                break;
            }else{
                mScripts.set(i, mScripts.get(i - 1));
            }
        }
        mScripts.set(beforeIndex + 1, script);
    }

    /**
     *
     */
    public ScriptModel createEmptyBlock(BlockModel.SpicaBlock spicaBlock,int ifState){
        ScriptModel scriptOther = new ScriptModel();
        scriptOther.setBlock(new BlockModel(spicaBlock));
        scriptOther.setIfState(ifState);
        return scriptOther;
    }

    @Override
    public void setScript(ScriptModel script, int index) {
        mScripts.set(index, script);
    }

    /**
     * スクリプト追加メソッド
     */
    @Override
    public void insertScript(ScriptModel script, int beforeIndex) {
        if (beforeIndex == mScripts.size() - 1) {
            addScript(script);
            if (script.getBlock().getBlock() == BlockModel.SpicaBlock.IF_START) {
                addScript(createEmptyBlock(BlockModel.SpicaBlock.IF_END,script.getIfState()));
            } else if (script.getBlock().getBlock() == BlockModel.SpicaBlock.FOR_START) {
                addScript(createEmptyBlock(BlockModel.SpicaBlock.FOR_END,script.getIfState()));
            }
        } else if (beforeIndex < mScripts.size() - 1) {
            insert(script, beforeIndex);
            if (script.getBlock().getBlock() == BlockModel.SpicaBlock.IF_START) {
                insert(createEmptyBlock(BlockModel.SpicaBlock.IF_END,script.getIfState()),
                        beforeIndex + 1);
            } else if (script.getBlock().getBlock() == BlockModel.SpicaBlock.FOR_START) {
                insert(createEmptyBlock(BlockModel.SpicaBlock.FOR_END,script.getIfState()),
                        beforeIndex + 1);
            }
        }
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
            String blockId = script.getBlock().getBlock().getId();
            String RightSpeed = String.valueOf(script.getRightSpeed());
            String LeftSpeed = String.valueOf(script.getLeftSpeed());
            String value = String.valueOf(script.getValue());

            if (RightSpeed.length() == 1) {
                RightSpeed = "00" + RightSpeed;
            } else if (RightSpeed.length() == 2) {
                RightSpeed = "0" + RightSpeed;
            }

            if (LeftSpeed.length() == 1) {
                LeftSpeed = "00" + LeftSpeed;
            } else if (LeftSpeed.length() == 2) {
                LeftSpeed = "0" + LeftSpeed;
            }

            if (value.length() == 1) {
                value = "00" + value;
            } else if (value.length() == 2) {
                value = "0" + value;
            }

            sendStringData = sendStringData + String.valueOf(ifState) + blockId + String.valueOf(RightSpeed) + String.valueOf(LeftSpeed) + String.valueOf(value);
        }
        Log.d("test", sendStringData);
        return sendStringData;
    }

    @Override
    public void setState(ViewState state) {
        this.mState=state;
    }

    public enum ViewState{
        SCRIPT(0),
        SELECT(1),
        ADD(2),
        EDIT(3);

        int id;
        ViewState(int id){
            this.id=id;
        }

        public int getId() {
            return id;
        }
    }
}

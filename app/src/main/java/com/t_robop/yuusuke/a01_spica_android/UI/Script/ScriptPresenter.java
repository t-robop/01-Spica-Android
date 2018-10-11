package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.util.Log;

import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.View mScriptView;

    private ArrayList<ScriptModel> mScripts;

    public ScriptPresenter(ScriptContract.View scriptView) {
        this.mScriptView = scriptView;
        this.mScriptView.setPresenter(this);

        /**
         * テスト
         *
        ScriptModel scriptModel = new ScriptModel();
        BlockModel blockModel = new BlockModel();
        blockModel.setBlock(BlockModel.SpicaBlock.FRONT);
        scriptModel.setBlock(blockModel);
        mScripts.add(scriptModel);
         */

        //todo テストはここに上記ブロックのように記述してください

        mScriptView.drawScripts(mScripts);
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
            mScripts.set(i, mScripts.get(i - 1));
        }
        mScripts.set(beforeIndex + 1, script);
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
                ScriptModel scriptOther = new ScriptModel();
                scriptOther.setBlock(new BlockModel(BlockModel.SpicaBlock.IF_END));
                scriptOther.setIfState(script.getIfState());
                addScript(scriptOther);
            } else if (script.getBlock().getBlock() == BlockModel.SpicaBlock.FOR_START) {
                ScriptModel scriptOther = new ScriptModel();
                scriptOther.setBlock(new BlockModel(BlockModel.SpicaBlock.FOR_END));
                scriptOther.setIfState(script.getIfState());
                addScript(scriptOther);
            }
        } else if (beforeIndex < mScripts.size() - 1) {
            insert(script, beforeIndex);
            if (script.getBlock().getBlock() == BlockModel.SpicaBlock.IF_START) {
                ScriptModel scriptOther = new ScriptModel();
                scriptOther.setBlock(new BlockModel(BlockModel.SpicaBlock.IF_END));
                scriptOther.setIfState(script.getIfState());
                insert(scriptOther, beforeIndex + 1);
            } else if (script.getBlock().getBlock() == BlockModel.SpicaBlock.FOR_START) {
                ScriptModel scriptOther = new ScriptModel();
                scriptOther.setBlock(new BlockModel(BlockModel.SpicaBlock.FOR_END));
                scriptOther.setIfState(script.getIfState());
                insert(scriptOther, beforeIndex + 1);
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
}

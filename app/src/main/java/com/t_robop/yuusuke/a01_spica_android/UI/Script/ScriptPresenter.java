package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.View mScriptView;

    private ArrayList<ScriptModel> mScripts;

    public ScriptPresenter(ScriptContract.View scriptView) {
        this.mScriptView=scriptView;
        this.mScriptView.setPresenter(this);

        for (int i = 0; i < 5; i++) {
            ScriptModel scriptModel = new ScriptModel();
            BlockModel blockModel = new BlockModel();
            blockModel.setBlock(BlockModel.SpicaBlock.FRONT);
            scriptModel.setBlock(blockModel);
            mScripts.add(scriptModel);
        }
        ScriptModel scriptModel = new ScriptModel();
        BlockModel blockModel = new BlockModel();
        blockModel.setBlock(BlockModel.SpicaBlock.IF_START);
        scriptModel.setBlock(blockModel);
        mScripts.add(scriptModel);
        for (int i = 0; i < 5; i++) {
            ScriptModel st = new ScriptModel();
            BlockModel bt = new BlockModel();
            bt.setBlock(BlockModel.SpicaBlock.LEFT);
            st.setBlock(bt);
            st.setIfState(1);
            mScripts.add(st);
        }
        for (int i = 0; i < 3; i++) {
            ScriptModel st = new ScriptModel();
            BlockModel bt = new BlockModel();
            bt.setBlock(BlockModel.SpicaBlock.BACK);
            st.setBlock(bt);
            st.setIfState(2);
            mScripts.add(st);
        }
        ScriptModel st = new ScriptModel();
        BlockModel bt = new BlockModel();
        bt.setBlock(BlockModel.SpicaBlock.IF_END);
        st.setBlock(bt);
        mScripts.add(st);

        mScriptView.drawScripts(mScripts);
    }

    @Override
    public void start() {
        mScripts=new ArrayList();
    }

    @Override
    public void addScript(ScriptModel script) {
        mScripts.add(script);
        mScriptView.drawScripts(mScripts);
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

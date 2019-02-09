package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.annotation.SuppressLint;

import com.t_robop.yuusuke.a01_spica_android.Block;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;

import java.util.ArrayList;

public class ScriptPresenter implements ScriptContract.Presenter {

    private ScriptContract.ScriptView mScriptView;

    private ArrayList<UIBlockModel> uiBlockModels;

    //今自分がどの画面にいるのかを管理する
    private ViewState mState;
    //画面移動で必要なスクリプト
    private UIBlockModel targetScript;

    ScriptPresenter(
            ScriptContract.ScriptView scriptView,
            ScriptContract.SelectView selectView,
            ScriptContract.DetailView detailView) {
        this.mScriptView = scriptView;
        this.mScriptView.setPresenter(this);
        mScriptView.drawScripts(uiBlockModels);

        selectView.setPresenter(this);
        detailView.setPresenter(this);
    }

    /**
     * 最初の一回だけでok
     */
    @Override
    public void start() {
        uiBlockModels = new ArrayList<>();
    }

    private void addScript(UIBlockModel uiBlockModel) {
        uiBlockModels.add(uiBlockModel);
    }

    /**
     * 指定したindexの次にスクリプトを挿入するメソッド
     */
    private void insert(UIBlockModel uiBlockModel, int beforeIndex) {
        uiBlockModels.add(uiBlockModels.get(uiBlockModels.size() - 1));
        for (int i = uiBlockModels.size() - 2; i > beforeIndex; i--) {
            if (i - 1 < 0) {
                break;
            } else {
                uiBlockModels.set(i, uiBlockModels.get(i - 1));
            }
        }
        uiBlockModels.set(beforeIndex + 1, uiBlockModel);
    }

    /**
     *
     */
    private UIBlockModel createEmptyBlock(String id, int ifState) {
        UIBlockModel emptyUiBlockModel = new UIBlockModel();
        emptyUiBlockModel.setId(id);
        emptyUiBlockModel.setIfState(ifState);
        return emptyUiBlockModel;
    }

    @Override
    public void setScript(UIBlockModel uiBlockModel, int index) {
        uiBlockModels.set(index, uiBlockModel);
        mScriptView.drawScripts(uiBlockModels);
    }

    /**
     * スクリプト追加メソッド
     */
    @Override
    public void insertScript(UIBlockModel uiBlockModel, int beforeIndex) {
        //一番最後の位置
        if (beforeIndex == uiBlockModels.size() - 1) {
            addScript(uiBlockModel);
            if (uiBlockModel.getId().equals(Block.IfStartBlock.id)) {
                addScript(createEmptyBlock(Block.IfEndBlock.id, uiBlockModel.getIfState()));
            } else if (uiBlockModel.getId().equals(Block.ForStartBlock.id)) {
                addScript(createEmptyBlock(Block.ForEndBlock.id, uiBlockModel.getIfState()));
            }
        } else if (beforeIndex < uiBlockModels.size() - 1) {
            insert(uiBlockModel, beforeIndex);
            if (uiBlockModel.getId().equals(Block.IfStartBlock.id)) {
                insert(createEmptyBlock(Block.IfEndBlock.id, uiBlockModel.getIfState()),
                        beforeIndex + 1);
            } else if (uiBlockModel.getId().equals(Block.ForStartBlock.id)) {
                insert(createEmptyBlock(Block.ForEndBlock.id, uiBlockModel.getIfState()),
                        beforeIndex + 1);
            }
        }
        mScriptView.drawScripts(uiBlockModels);
    }

    @Override
    public void removeScript(int index) {
        UIBlockModel uiBlockModel = uiBlockModels.get(index);
        int size = uiBlockModels.size();
        if (uiBlockModel.getId().equals(Block.IfStartBlock.id)) {
            for (int i = index; i < size; i++) {
                if (uiBlockModels.get(index).getId().equals(Block.IfEndBlock.id)) {
                    break;
                }
                uiBlockModels.remove(index);
            }
        } else if (uiBlockModel.getId().equals(Block.ForStartBlock.id)) {
            for (int i = index; i < size; i++) {
                if (uiBlockModels.get(index).getId().equals(Block.ForEndBlock.id)) {
                    break;
                }
                uiBlockModels.remove(index);
            }
        }
        uiBlockModels.remove(index);
        mScriptView.drawScripts(uiBlockModels);
    }

    @Override
    public ArrayList<UIBlockModel> getScripts() {
        return this.uiBlockModels;
    }

    @Override
    public void setScripts(ArrayList<UIBlockModel> uiBlockModels) {
        this.uiBlockModels = uiBlockModels;
        mScriptView.drawScripts(uiBlockModels);
    }

    /**
     * スクリプト一覧を送信可能データにするメソッド
     */
    @SuppressLint("DefaultLocale")
    @Override
    public String getSendableScripts() {
        StringBuilder sendStringData = new StringBuilder();
        for (BlockModel blockModel : uiBlockModels) {
            String ifState = String.format("%02d", blockModel.getIfState());
            String blockId = blockModel.getId();

            //速度値は左右同じものを使う
            String option1 = String.format("%03d", blockModel.getOption());
            if (blockId.equals(Block.IfEndBlock.id) || blockId.equals(Block.ForStartBlock.id) || blockId.equals(Block.ForEndBlock.id)) {
                option1 = String.format("%03d", 0);
            }

            String option2 = String.format("%03d", blockModel.getOption());
            if (blockId.equals(Block.IfStartBlock.id) || blockId.equals(Block.IfEndBlock.id) || blockId.equals(Block.ForStartBlock.id) || blockId.equals(Block.ForEndBlock.id)) {
                option2 = String.format("%03d", 0);
            }

            String value = String.format("%03d", (int) Math.round(blockModel.getValue() * 10.0));
            if (blockId.equals(Block.IfStartBlock.id) || blockId.equals(Block.ForStartBlock.id)) {
                value = String.format("%03d", (int) Math.round(blockModel.getValue()));
            } else if (blockId.equals(Block.IfEndBlock.id) || blockId.equals(Block.ForEndBlock.id)) {
                value = String.format("%03d", 0);
            }

            sendStringData.append(ifState).append(blockId).append(option1).append(option2).append(value);
        }
        return sendStringData.toString();
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
    public void setTargetScript(UIBlockModel script) {
        this.targetScript = script;
    }

    @Override
    public UIBlockModel getTargetScript() {
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

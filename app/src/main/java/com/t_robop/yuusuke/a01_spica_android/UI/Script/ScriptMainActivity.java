package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainActivity extends AppCompatActivity implements ScriptContract.View {

    private ScriptContract.Presenter mScriptPresenter;

    private RecyclerView mScriptRecyclerView;
    private ScriptMainAdapter mScriptAdapter;
    private LinearLayoutManager mScriptLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_main);

        mScriptRecyclerView = findViewById(R.id.recycler_script);

        mScriptRecyclerView.setHasFixedSize(true);
        mScriptLayoutManager = new LinearLayoutManager(this);
        mScriptLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mScriptRecyclerView.setLayoutManager(mScriptLayoutManager);

        mScriptAdapter = new ScriptMainAdapter(this);
        mScriptRecyclerView.setAdapter(mScriptAdapter);

        new ScriptPresenter(this);

        for(int i=0;i<20;i++) {
            ScriptModel scriptModel = new ScriptModel();
            BlockModel blockModel = new BlockModel();
            blockModel.setBlock(BlockModel.SpicaBlock.FRONT);
            scriptModel.setBlock(blockModel);
            mScriptPresenter.addScript(scriptModel);
        }
    }

    @Override
    public void drawScripts(ArrayList<ScriptModel> scrips) {
        //引数を使ってUIに反映させる
        for(int i=0;i<scrips.size();i++){
            ScriptModel scriptModel = scrips.get(i);
            mScriptAdapter.addDefault(i,scriptModel);
        }
        mScriptAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter=presenter;
        this.mScriptPresenter.start();
    }
}

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
    }

    @Override
    public void drawScripts(ArrayList<ScriptModel> scripts) {
        //引数を使ってUIに反映させる
        int ifIndex=-1;
        int laneIndex=0;
        for (int i = 0; i < scripts.size(); i++) {
            ScriptModel script = scripts.get(i);
            if(script.getIfState()==0) {
                //通常
                mScriptAdapter.addDefault(laneIndex, script);
                laneIndex++;
            }else if(script.getIfState()==1){
                //true
                mScriptAdapter.addSpecial(laneIndex,script);
                laneIndex++;
            }else if(script.getIfState()==2){
                //false
                mScriptAdapter.addDefault(ifIndex, script);
                ifIndex++;
            }
            if(script.getBlock().getBlock()== BlockModel.SpicaBlock.IF_START){
                ifIndex=i+1;
            }
            if (script.getBlock().getBlock()== BlockModel.SpicaBlock.IF_END){
                ifIndex=-1;
            }
        }
        mScriptAdapter.notifyDataSetChanged();

        mScriptPresenter.getSendableScripts();
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
        this.mScriptPresenter.start();
    }
}

package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

public class ScriptMainActivity extends AppCompatActivity implements ScriptContract.View, BlockSelectFragment.MyListener {

    private ScriptContract.Presenter mScriptPresenter;

    private RecyclerView mScriptRecyclerView;
    private ScriptMainAdapter mScriptAdapter;
    private LinearLayoutManager mScriptLayoutManager;

    private BlockSelectFragment blockSelectFragment;
    private BlockDetailFragment blockDetailFragment;

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

        blockSelectFragment = new BlockSelectFragment();
        blockDetailFragment = new BlockDetailFragment();

        for(int i=0;i<20;i++) {
            ScriptModel scriptModel = new ScriptModel();
            BlockModel blockModel = new BlockModel();
            blockModel.setBlockId(0101+i);
            scriptModel.setBlock(blockModel);
            mScriptAdapter.add(scriptModel);
        }
        mScriptAdapter.notifyDataSetChanged();

        new ScriptPresenter(this);




        /////川口追加
        mScriptAdapter.setActivity(ScriptMainActivity.this);
    }

    public void inflateFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.conductor_fragment, blockSelectFragment);
        fragmentTransaction.commit();
    }




    //BlockSelectFragmentで追加したViewのクリックを検出するリスナー
    @Override
    public void onClickButton( ) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        fragmentTransaction.commit();
    }



    @Override
    public void drawScripts(ArrayList<ScriptModel> scrips) {
        //引数を使ってUIに反映させる
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter=presenter;
        this.mScriptPresenter.start();
    }
}

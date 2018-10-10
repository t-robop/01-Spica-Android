package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.view.View;
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




        /////川口追
        mScriptAdapter.setActivity(ScriptMainActivity.this);
    }

    public void inflateFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.conductor_fragment, blockSelectFragment);
        fragmentTransaction.commit();

        mScriptAdapter.setOnItemClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Toast.makeText(ScriptMainActivity.this,"OK", Toast.LENGTH_SHORT).show();
//                ActivityOptions options = null;
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    options = ActivityOptions.makeScaleUpAnimation(
//                            holder.mBinding.conductorAdd,
//                            (int)holder.mBinding.conductorAdd.getX(),
//                            (int)holder.mBinding.conductorAdd.getY(),
//                            holder.mBinding.conductorAdd.getWidth(),
//                            holder.mBinding.conductorAdd.getHeight());
//                    ScriptMainActivity.this.startActivity(new Intent(ScriptMainActivity.this, BlockSelectActivity.class), options.toBundle());
//                }
            }
        });

        mScriptAdapter.setOnItemLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                Toast.makeText(ScriptMainActivity.this,"OK", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
    }




    //BlockSelectFragmentで追加したViewのクリックを検出するリスナー
    @Override
    public void onClickButton(String buttonName) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

        switch (buttonName){
            case "susumu":
                bundle.putString("commandDirection", "susumu");
                break;
            case "magaru":
                bundle.putString("commandDirection", "magaru");
                break;
            case "sagaru":
                bundle.putString("commandDirection", "sagaru");
                break;
            default:
                bundle.putString("commandDirection", "null");
                break;

        }

        blockDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        fragmentTransaction.commit();
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

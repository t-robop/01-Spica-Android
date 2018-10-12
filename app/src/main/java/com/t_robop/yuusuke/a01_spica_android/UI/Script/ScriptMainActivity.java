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

        blockDetailFragment = new BlockDetailFragment();

        new ScriptPresenter(this);

        /**
         * 追加ボタンクリック時
         */
        mScriptAdapter.setOnConductorClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                inflateFragment(pos, ifState);
            }
        });
        mScriptAdapter.setOnConductorIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                inflateFragment(pos, ifState);
            }
        });

        /**
         * ブロッククリック時
         */
        mScriptAdapter.setOnBlockClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                //スタートボタン時
                if (pos==-1) {
                    //todo スクリプト送信処理
                    String sendData = mScriptPresenter.getSendableScripts();
                    Toast.makeText(ScriptMainActivity.this,"ロボットに送信完了",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mScriptAdapter.setOnBlockIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {

            }
        });

        /**
         * ブロックロングクリック時
         */
        mScriptAdapter.setOnBlockLongClickListener(new ScriptMainAdapter.onItemLongClickListener() {
            @Override
            public void onLongClick(View view, int pos) {
                //実行
            }
        });
        mScriptAdapter.setOnBlockIfLongClickListener(new ScriptMainAdapter.onItemLongClickListener() {
            @Override
            public void onLongClick(View view, int pos) {
                //実行
            }
        });

        /**
         * フラグメントから追加が押された時
         */
        blockDetailFragment.setAddClickListener(new BlockDetailFragment.DetailListener() {
            @Override
            public void onClickadd(ScriptModel script, int pos) {
                mScriptPresenter.insertScript(script, pos);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.remove(blockDetailFragment);
                fragmentTransaction.remove(blockSelectFragment);
                fragmentTransaction.commit();
            }
        });
    }

    public void inflateFragment(int pos, int ifState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        blockSelectFragment = new BlockSelectFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("pos", pos);
        bundle.putInt("ifState", ifState);
        blockSelectFragment.setArguments(bundle);

        fragmentTransaction.add(R.id.conductor_fragment, blockSelectFragment);
        fragmentTransaction.commit();
    }


    //BlockSelectFragmentで追加したViewのクリックを検出するリスナー
    @Override
    public void onClickButton(String buttonName, int pos, int ifState) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle = new Bundle();

        if (buttonName != null) {
            bundle.putString("commandDirection", buttonName);
        } else {
            bundle.putString("commandDirection", "null");
        }
        bundle.putInt("pos", pos);
        bundle.putInt("ifState", ifState);

        blockDetailFragment.setArguments(bundle);
        fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        fragmentTransaction.commit();
    }

    /**
     * スクリプトのリストを投げるとUI構築してくれる神メソッド
     */
    @Override
    public void drawScripts(ArrayList<ScriptModel> scripts) {
        mScriptAdapter.clear();

        //スタートブロック記述
        ScriptModel scriptStart = new ScriptModel();
        BlockModel blockStart = new BlockModel();
        blockStart.setBlock(BlockModel.SpicaBlock.START);
        scriptStart.setBlock(blockStart);
        scriptStart.setPos(-1);
        scriptStart.setIfState(0);
        mScriptAdapter.addDefault(0,scriptStart);

        //引数を使ってUIに反映させる
        int ifIndex = -1;
        int laneIndex = 1;
        for (int i = 0; i < scripts.size(); i++) {
            ScriptModel script = scripts.get(i);
            script.setPos(i);
            if (script.getIfState() == 0) {
                //通常
                mScriptAdapter.addDefault(laneIndex, script);
                laneIndex++;
            } else if (script.getIfState() == 1) {
                //true
                mScriptAdapter.addSpecial(laneIndex, script);
                laneIndex++;
            } else if (script.getIfState() == 2) {
                //false
                mScriptAdapter.addDefault(ifIndex, script);
                if (ifIndex == laneIndex) {
                    laneIndex++;
                }
                ifIndex++;
            }
            if (script.getBlock().getBlock() == BlockModel.SpicaBlock.IF_START) {
                ifIndex = laneIndex;
            }
            if (script.getBlock().getBlock() == BlockModel.SpicaBlock.IF_END) {
                ifIndex = -1;
            }
        }

        //エンドブロック記述
        ScriptModel scriptEnd = new ScriptModel();
        BlockModel blockEnd = new BlockModel();
        blockEnd.setBlock(BlockModel.SpicaBlock.END);
        scriptEnd.setBlock(blockEnd);
        mScriptAdapter.addDefault(mScriptAdapter.getItemCount(),scriptEnd);

        mScriptAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
        this.mScriptPresenter.start();
    }
}

package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.ActivityOptions;
import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;
import android.view.View;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

import java.util.ArrayList;

import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.FOR_END;
import static com.t_robop.yuusuke.a01_spica_android.model.ScriptModel.SpicaBlock.IF_END;

public class ScriptMainActivity extends AppCompatActivity implements ScriptContract.ScriptView, BlockSelectFragment.MyListener {

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

        /**
         * Presenterの初期化
         */
        blockSelectFragment = new BlockSelectFragment();
        blockDetailFragment = new BlockDetailFragment();
        new ScriptPresenter(this, blockSelectFragment, blockDetailFragment);

        /**
         * 追加ボタンクリック時
         */
        mScriptAdapter.setOnConductorClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                mScriptPresenter.setState(ScriptPresenter.ViewState.SELECT);
                ScriptModel scriptModel = new ScriptModel(pos, ifState);
                inflateFragment(scriptModel);
            }
        });
        mScriptAdapter.setOnConductorIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                mScriptPresenter.setState(ScriptPresenter.ViewState.SELECT);
                ScriptModel scriptModel = new ScriptModel(pos, ifState);
                inflateFragment(scriptModel);
            }
        });

        /**
         * ブロッククリック時
         */
        mScriptAdapter.setOnBlockClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                //スタートボタン時
                if (pos == -1) {
                    //todo スクリプト送信処理
                    String sendData = mScriptPresenter.getSendableScripts();
                    Toast.makeText(ScriptMainActivity.this, "ロボットに送信完了", Toast.LENGTH_SHORT).show();
                } else if (pos == -2) {

                } else {
                    /**
                     * ブロック設定へ
                     */
                    mScriptPresenter.setState(ScriptPresenter.ViewState.EDIT);
                    ScriptModel scriptModel = mScriptPresenter.getScripts().get(pos);
                    if(scriptModel.getBlock() == IF_END || scriptModel.getBlock() == FOR_END) return;
                    inflateFragment(scriptModel);
                }
            }
        });
        mScriptAdapter.setOnBlockIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState) {
                /**
                 * ブロック設定へ
                 */
                mScriptPresenter.setState(ScriptPresenter.ViewState.EDIT);
                ScriptModel scriptModel = mScriptPresenter.getScripts().get(pos);
                inflateFragment(scriptModel);
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
         * Detailフラグメントのボタンが押された時
         */
        blockDetailFragment.setAddClickListener(new BlockDetailFragment.DetailListener() {
            @Override
            public void onClickAdd(ScriptModel script) {
                ScriptPresenter.ViewState state = mScriptPresenter.getState();
                if (state == ScriptPresenter.ViewState.ADD) {
                    mScriptPresenter.insertScript(script, script.getPos());
                } else if (state == ScriptPresenter.ViewState.EDIT) {
                    mScriptPresenter.setScript(script, script.getPos());
                } else {
                    Toast.makeText(ScriptMainActivity.this, "ADDorEDITでエラー", Toast.LENGTH_SHORT).show();
                }
                mScriptPresenter.setState(ScriptPresenter.ViewState.SCRIPT);
                clearFragments();
            }

            @Override
            public void onClickDelete(int pos) {
                mScriptPresenter.removeScript(pos);
                mScriptPresenter.setState(ScriptPresenter.ViewState.SCRIPT);
                clearFragments();
            }
        });

        /**
         * fabがクリックされた時
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //todo スクリプト送信処理
                String sendData = mScriptPresenter.getSendableScripts();
                Toast.makeText(ScriptMainActivity.this, "ロボットに送信完了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Fragment生成メソッド
     */
    public void inflateFragment(ScriptModel scriptModel) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ScriptPresenter.ViewState state = mScriptPresenter.getState();
        mScriptPresenter.setTargetScript(scriptModel);
        if (state == ScriptPresenter.ViewState.SELECT) {
            /**
             * ブロック追加用の選択画面へ
             */
            fragmentTransaction.add(R.id.conductor_fragment, blockSelectFragment);
        } else if (state == ScriptPresenter.ViewState.EDIT) {
            /**
             * ブロック設定用の詳細画面へ
             */
            fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        } else if (state == ScriptPresenter.ViewState.ADD) {
            /**
             * ブロック追加用の詳細画面へ
             * todo 上記EDITと同じなのは一旦分かりやすさのため
             */
            fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        }
        fragmentTransaction.commit();
    }

    /**
     * 出てるFragmentを消すメソッド
     */
    public void clearFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(blockDetailFragment);
        fragmentTransaction.remove(blockSelectFragment);
        fragmentTransaction.commit();
    }


    /**
     * 追加時のブロック選択画面で選択されたブロックを元にフラグメント生成
     */
    @Override
    public void onClickButton(ScriptModel.SpicaBlock block) {
        mScriptPresenter.setState(ScriptPresenter.ViewState.ADD);
        ScriptModel scriptModel = mScriptPresenter.getTargetScript();
        scriptModel.setBlock(block);
        inflateFragment(scriptModel);
    }

    /**
     * スクリプトのリストを投げるとUI構築してくれる神メソッド
     */
    @Override
    public void drawScripts(ArrayList<ScriptModel> scripts) {
        mScriptAdapter.clear();

        //スタートブロック記述
        ScriptModel scriptStart = new ScriptModel();
        scriptStart.setBlock(ScriptModel.SpicaBlock.START);
//        blockStart.setBlock(BlockModel.SpicaBlock.START);
//        scriptStart.setBlock(blockStart);
        scriptStart.setPos(-1);
        scriptStart.setIfState(0);
        mScriptAdapter.addDefault(0, scriptStart);

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
            if (script.getBlock() == ScriptModel.SpicaBlock.IF_START) {
                ifIndex = laneIndex;
            }
            if (script.getBlock() == IF_END) {
                ifIndex = -1;
            }
        }

        //エンドブロック記述
        ScriptModel scriptEnd = new ScriptModel();
        scriptEnd.setBlock(ScriptModel.SpicaBlock.END);
        mScriptAdapter.addDefault(mScriptAdapter.getItemCount(), scriptEnd);

        mScriptAdapter.notifyDataSetChanged();
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
        this.mScriptPresenter.start();
    }
}

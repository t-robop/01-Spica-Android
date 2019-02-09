package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.t_robop.yuusuke.a01_spica_android.Block;
import com.t_robop.yuusuke.a01_spica_android.Config;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivityScriptMainBinding;
import com.t_robop.yuusuke.a01_spica_android.model.UIBlockModel;
import com.t_robop.yuusuke.a01_spica_android.util.UdpReceive;
import com.t_robop.yuusuke.a01_spica_android.util.UdpSend;

import java.util.ArrayList;

public class ScriptMainActivity extends AppCompatActivity implements ScriptContract.ScriptView, BlockSelectFragment.BlockClickListener {

    private ScriptContract.Presenter mScriptPresenter;

    ActivityScriptMainBinding mBinding;

    private ScriptMainAdapter mScriptAdapter;

    private BlockSelectFragment blockSelectFragment;
    private BlockDetailFragment blockDetailFragment;

    private UdpReceive udpReceive;

    private float sizeX;

    //FIXME onCreate長すぎなので、切り出しする
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_main);

        hideNavigationBar();
        udpReceive = new UdpReceive(this);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_script_main);

        mBinding.recyclerScript.setHasFixedSize(true);
        LinearLayoutManager mScriptLayoutManager = new LinearLayoutManager(this);
        mScriptLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mBinding.recyclerScript.setLayoutManager(mScriptLayoutManager);
        mScriptAdapter = new ScriptMainAdapter(this);
        mBinding.recyclerScript.setAdapter(mScriptAdapter);

        /*
         * Presenterの初期化
         */
        blockSelectFragment = new BlockSelectFragment();
        blockDetailFragment = new BlockDetailFragment();
        new ScriptPresenter(this, blockSelectFragment, blockDetailFragment);

        /*
         * 追加ボタンクリック時
         */
        mScriptAdapter.setOnConductorClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState, boolean isInLoop) {
                showBlockSelect(pos, ifState, isInLoop);
            }
        });
        mScriptAdapter.setOnConductorIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState, boolean isInLoop) {
                showBlockSelect(pos, ifState, isInLoop);
            }
        });

        /*
         * ブロッククリック時
         */
        mScriptAdapter.setOnBlockClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState, boolean isInLoop) {
                showBlockDetail(pos);
            }
        });
        mScriptAdapter.setOnBlockIfClickListener(new ScriptMainAdapter.onItemClickListener() {
            @Override
            public void onClick(View view, int pos, int ifState, boolean isInLoop) {
                showBlockDetail(pos);
            }
        });

        /*
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

        /*
         * Detailフラグメントのボタンが押された時
         */
        blockDetailFragment.setAddClickListener(new BlockDetailFragment.DetailListener() {
            @Override
            public void onClickAdd(UIBlockModel uiBlockModel) {
                ScriptPresenter.ViewState state = mScriptPresenter.getState();
                if (state == ScriptPresenter.ViewState.ADD) {
                    mScriptPresenter.insertScript(uiBlockModel, uiBlockModel.getPos());
                } else if (state == ScriptPresenter.ViewState.EDIT) {
                    mScriptPresenter.setScript(uiBlockModel, uiBlockModel.getPos());
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

        /*
         * 実行ボタン(fab)がクリックされた時
         */
        mBinding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                objectSave();
                String sendData = mScriptPresenter.getSendableScripts();
                final SharedPreferences pref = getSharedPreferences("udp_config", Context.MODE_PRIVATE);
                final String ip = pref.getString("ip", "");
                if (ip.isEmpty()) {
                    Toast.makeText(ScriptMainActivity.this, R.string.script_main_activity_failed_empty_ip, Toast.LENGTH_SHORT).show();
                } else if (sendData.length() <= 0) {
                    Toast.makeText(ScriptMainActivity.this, R.string.script_main_activity_failed_empty_block, Toast.LENGTH_SHORT).show();
                } else {
                    udpReceive.UdpReceiveStandby();
                    UdpSend udp = new UdpSend();
                    udp.UdpSendText(sendData);
                    Log.d("sendData", sendData);
                    Toast.makeText(ScriptMainActivity.this, R.string.script_main_activity_send_success, Toast.LENGTH_SHORT).show();
                }
            }
        });

        /*
         * fabが1.2秒以上長押しされた時
         */
        final long[] then = {0};
        mBinding.fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    then[0] = System.currentTimeMillis();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if ((System.currentTimeMillis() - then[0]) > 1200) {
                        Intent intent = new Intent(getApplicationContext(), SettingActivity.class);
                        startActivity(intent);
                        return true;
                    }
                }
                return false;
            }
        });


        /*
         *
         * 透明ボタンが押されたときの処理
         */
        Button restoreButton = findViewById(R.id.restore_btn);
        restoreButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                objectLoad();
                return false;
            }
        });

    }

    /**
     * Fragment生成メソッド
     */
    public void inflateFragment(UIBlockModel uiBlockModel) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ScriptPresenter.ViewState state = mScriptPresenter.getState();
        mScriptPresenter.setTargetScript(uiBlockModel);
        if (state == ScriptPresenter.ViewState.SELECT && !blockSelectFragment.isAdded()) {
            /*
             * ブロック追加用の選択画面へ
             */
            fragmentTransaction.add(R.id.conductor_fragment, blockSelectFragment);
        } else if (state == ScriptPresenter.ViewState.EDIT && !blockDetailFragment.isAdded()) {
            /*
             * ブロック設定用の詳細画面へ
             */
            fragmentTransaction.add(R.id.conductor_fragment, blockDetailFragment);
        } else if (state == ScriptPresenter.ViewState.ADD && !blockDetailFragment.isAdded()) {
            /*
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
    public void onClickButton(String blockId) {
        mScriptPresenter.setState(ScriptPresenter.ViewState.ADD);
        UIBlockModel uiBlockModel = mScriptPresenter.getTargetScript();
        uiBlockModel.setId(blockId);
        inflateFragment(uiBlockModel);
    }

    /**
     * スクリプトのリストを投げるとUI構築してくれる神メソッド
     */
    @Override
    public void drawScripts(ArrayList<UIBlockModel> uiBlockModels) {
        mScriptAdapter.clear();

        //スタートブロック記述
        UIBlockModel startBlockModel = new UIBlockModel(-1, Config.OUT_OF_IF_LANE, false);
        startBlockModel.setId(Block.StartBlock.id);
        mScriptAdapter.addDefault(0, startBlockModel);

        //引数を使ってUIに反映させる
        int ifIndex = -1;
        int laneIndex = 1;
        for (int i = 0; i < uiBlockModels.size(); i++) {
            UIBlockModel uiBlockModel = uiBlockModels.get(i);
            uiBlockModel.setPos(i);

            if (uiBlockModel.getId().equals(Block.ForStartBlock.id)) {
                uiBlockModel.setInLoop(true);
            } else if (uiBlockModel.getId().equals(Block.ForEndBlock.id)) {
                uiBlockModel.setInLoop(false);
            }

            switch (uiBlockModel.getIfState()) {
                case Config.OUT_OF_IF_LANE:
                    mScriptAdapter.addDefault(laneIndex, uiBlockModel);
                    laneIndex++;
                    break;

                case Config.IN_TRUE_LANE:
                    mScriptAdapter.addSpecial(laneIndex, uiBlockModel);
                    laneIndex++;
                    break;

                case Config.IN_FALSE_LANE:
                    mScriptAdapter.addDefault(ifIndex, uiBlockModel);
                    if (ifIndex == laneIndex) {
                        laneIndex++;
                    }
                    ifIndex++;
                    break;
            }

            if (uiBlockModel.getId().equals(Block.IfStartBlock.id)) {
                ifIndex = laneIndex;
            }
            if (uiBlockModel.getId().equals(Block.IfEndBlock.id)) {
                ifIndex = -1;
            }
        }

        //エンドブロック記述
        UIBlockModel endBlockModel = new UIBlockModel(uiBlockModels.size() + 1, Config.OUT_OF_IF_LANE, false);
        endBlockModel.setId(Block.EndBlock.id);
        mScriptAdapter.addDefault(mScriptAdapter.getItemCount(), endBlockModel);

        mScriptAdapter.notifyDataSetChanged();

        mBinding.canvasView.setCommandBlocks(mScriptAdapter);
        mBinding.canvasView.windowSizeChange(sizeX, (float) mScriptAdapter.getItemCount());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        sizeX = mBinding.recyclerScript.getWidth();
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
        this.mScriptPresenter.start();
    }

    private void objectSave() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        // objectをjson文字列へ変換
        String jsonInstanceString = gson.toJson(mScriptPresenter.getScripts());
        // 変換後の文字列をputStringで保存
        pref.edit().putString("dataSave", jsonInstanceString).apply();
    }

    public void objectLoad() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Gson gson = new Gson();
        try {
            // 保存されているjson文字列を取得
            String userSettingString = prefs.getString("dataSave", "");
            // json文字列を 「UserSettingクラス」のインスタンスに変換
            ArrayList<UIBlockModel> uiBlockModels = gson.fromJson(userSettingString, new TypeToken<ArrayList<UIBlockModel>>() {
            }.getType());
            mScriptPresenter.setScripts(uiBlockModels);
        } catch (Exception e) {
            Toast.makeText(this, "データがありません", Toast.LENGTH_SHORT).show();
        }

    }

    // Field requires API level 19 (current min is 16): android.view.View#SYSTEM_UI_FLAG_IMMERSIVE
    @SuppressLint("InlinedApi")
    private void hideNavigationBar() {
        View sysView = getWindow().getDecorView();
        sysView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    /**
     * ブロック選択へ
     */
    private void showBlockSelect(int pos, int ifState, boolean isInLoop) {
        hideNavigationBar();
        mScriptPresenter.setState(ScriptPresenter.ViewState.SELECT);
        UIBlockModel uiBlockModel = new UIBlockModel(pos, ifState, isInLoop);
        inflateFragment(uiBlockModel);
    }

    /**
     * ブロック設定へ
     */
    private void showBlockDetail(int pos) {
        hideNavigationBar();
        if (0 <= pos) {  //エンドブロックはリスト外なのでpos比較していない
            mScriptPresenter.setState(ScriptPresenter.ViewState.EDIT);
            UIBlockModel uiBlockModel = mScriptPresenter.getScripts().get(pos);
            if (uiBlockModel.getId().equals(Block.IfEndBlock.id) || uiBlockModel.getId().equals(Block.ForEndBlock.id)) {
                return;
            }
            inflateFragment(uiBlockModel);

        }
    }
}

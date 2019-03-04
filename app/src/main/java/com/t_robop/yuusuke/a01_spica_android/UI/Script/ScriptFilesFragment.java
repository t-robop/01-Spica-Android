package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.FragmentScriptFilesBinding;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;
import com.t_robop.yuusuke.a01_spica_android.repository.ScriptRepository;

import java.util.ArrayList;

public class ScriptFilesFragment extends DialogFragment implements ScriptContract.FilesView {

    private ScriptContract.Presenter mScriptPresenter;
    private FragmentScriptFilesBinding mBinding;
    private ScriptFilesAdapter adapter;
    private ScriptRepository repository;

    public ScriptFilesFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_script_files, container, false);
        View root = mBinding.getRoot();
        mBinding.setFragment(this);
        mBinding.fgDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // no-op
            }
        });
        adapter = new ScriptFilesAdapter(getActivity());
        repository = new ScriptRepository(getActivity());
        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.fileAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // fixme 名前を入力させるかどうか不明なため、一旦数字で保存する
                String title = String.valueOf(repository.getAllScriptSize() + 1);
                repository.writeScript(title, new ArrayList<ScriptModel>());
                loadScript();
            }
        });
        adapter.setOnDeleteBtnClickListener(new ScriptFilesAdapter.OnDeleteBtnClickListener() {
            @Override
            public void onClick(String title) {
                deleteScript(title);
                loadScript();
            }
        });
        adapter.setOnItemClickListener(new ScriptFilesAdapter.OnItemClickListener() {
            @Override
            public void onClick(String title) {
                mScriptPresenter.setScriptTitle(title);
                mScriptPresenter.setScripts(repository.getScript(title));
                loadScript();
                //close();
            }
        });
        loadScript();

        //アニメーションスタート
        popupAnime(mBinding.fgDetail);
        alphaAnime(mBinding.bgDetail);
//        if (mScriptPresenter.getState() == ScriptPresenter.ViewState.EDIT) {
//            alphaAnime(mBinding.bgDetail);
//        } else {
//            mBinding.bgDetail.setBackgroundResource(R.color.alpha_clear);
//        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    private void deleteScript(String title) {
        repository.deleteScript(title);
    }

    private void loadScript() {
        adapter.clear();
        for (String title : repository.getAllScriptTitle()) {
            adapter.titleAdd(title, mScriptPresenter.isScriptTitle(title));
        }
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.listFiles.setLayoutManager(linearLayoutManager);
        mBinding.listFiles.setAdapter(adapter);
    }

    private void popupAnime(View view) {
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f, 1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec
        scaleAnimation.setDuration(200);
        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

    private void alphaAnime(View view) {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 0.5f);
        // animation時間 msec
        alphaAnimation.setDuration(200);
        // 繰り返し回数
        alphaAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        alphaAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(alphaAnimation);
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
    }

    /**
     * fragmentとじる
     */
    public void close() {
        getFragmentManager().beginTransaction().remove(ScriptFilesFragment.this).commit();

    }

    /**
     * fragmentの外をタップしたとき
     */
    public void cancel() {
        getFragmentManager().beginTransaction().remove(ScriptFilesFragment.this).commit();

    }
}

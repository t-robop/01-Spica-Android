package com.t_robop.yuusuke.a01_spica_android.UI.Script;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class BlockSelectFragment extends Fragment implements ScriptContract.SelectView {

    private ScriptContract.Presenter mScriptPresenter;

    public BlockSelectFragment() {
    }

    private MyListener mListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_block_select, container, false);
        popupAnime(mView);

        RelativeLayout bg = mView.findViewById(R.id.bg_select);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(BlockSelectFragment.this).commit();
            }
        });

        LinearLayout bgSelect=mView.findViewById(R.id.select_dialog_bg);
        bgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        mView.findViewById(R.id.susumu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.FRONT);
            }
        });
        mView.findViewById(R.id.magaru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.RIGHT);
            }
        });
        mView.findViewById(R.id.sagaru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.BACK);
            }
        });
        mView.findViewById(R.id.mosimo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.IF_START);
            }
        });
        mView.findViewById(R.id.kurikaesu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.FOR_START);
            }
        });
        mView.findViewById(R.id.nukeru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton(ScriptModel.SpicaBlock.BREAK);
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void drawArrangeableBlocks() {
        //todo
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        this.mScriptPresenter = presenter;
    }


    public interface MyListener {
        void onClickButton(ScriptModel.SpicaBlock block);
    }

    // FragmentがActivityに追加されたら呼ばれるメソッド
    @Override
    public void onAttach(Context context) {
        // APILevel23からは引数がActivity->Contextになっているので注意する

        // contextクラスがMyListenerを実装しているかをチェックする
        super.onAttach(context);
        if (context instanceof MyListener) {
            // リスナーをここでセットするようにします
            mListener = (MyListener) context;
        }
    }

    // FragmentがActivityから離れたら呼ばれるメソッド
    @Override
    public void onDetach() {
        super.onDetach();
        // 画面からFragmentが離れたあとに処理が呼ばれることを避けるためにNullで初期化しておく
        mListener = null;
    }

    @Override
    public void onStart() {
        super.onStart();

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
}

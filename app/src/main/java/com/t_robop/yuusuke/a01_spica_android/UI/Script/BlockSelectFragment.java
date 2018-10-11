package com.t_robop.yuusuke.a01_spica_android.UI.Script;




import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.R;

import java.io.FileReader;

public class BlockSelectFragment extends Fragment {

    private int pos=0;
    private int ifState=0;

    public BlockSelectFragment(){}

    private MyListener mListener;

    View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_block_select, container, false);
        popupAnime(mView);

        Bundle bundle = getArguments();
        pos = bundle.getInt("pos");
        ifState=bundle.getInt("ifState");

        RelativeLayout bg=mView.findViewById(R.id.bg_select);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().remove(BlockSelectFragment.this).commit();
            }
        });

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    public interface MyListener {
        public void onClickButton(String buttonName,int pos,int ifState);
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

        mView.findViewById(R.id.susumu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("susumu",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });
        mView.findViewById(R.id.magaru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("magaru",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });
        mView.findViewById(R.id.sagaru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("sagaru",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });
        mView.findViewById(R.id.mosimo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("mosimo",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });
        mView.findViewById(R.id.kurikaesu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("kurikaesu",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });
        mView.findViewById(R.id.nukeru).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClickButton("nukeru",pos,ifState);
                //Toast.makeText(getActivity(), "hoge!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void popupAnime(View view){
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f,1.0f,
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

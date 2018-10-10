package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.t_robop.yuusuke.a01_spica_android.R;

public class BlockDetailFragment extends Fragment {

    public BlockDetailFragment(){}

    View mView;
    Bundle bundle;
    String commandDirection;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.activity_block_detail, container, false);

        Bundle bundle = getArguments();
        commandDirection = bundle.getString("commandDirection");

        popupAnime(mView);

        return mView;
    }

    ScriptMainActivity scriptMainActivity;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void popupAnime(View view){
        // ScaleAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        ScaleAnimation scaleAnimation = new ScaleAnimation(
                0.01f, 1.0f, 0.01f,1.0f,
                Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        // animation時間 msec


        if(commandDirection == "susumu")
        {
            scaleAnimation.setDuration(200);
        }else if(commandDirection == "magaru")
        {
            scaleAnimation.setDuration(1000);
        }else if(commandDirection == "sagaru"){
            scaleAnimation.setDuration(2000);
        }else {
            scaleAnimation.setDuration(5000);
        }

        // 繰り返し回数
        scaleAnimation.setRepeatCount(0);
        // animationが終わったそのまま表示にする
        scaleAnimation.setFillAfter(true);
        //アニメーションの開始
        view.startAnimation(scaleAnimation);
    }

}

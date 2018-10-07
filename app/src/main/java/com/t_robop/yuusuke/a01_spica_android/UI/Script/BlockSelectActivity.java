package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.t_robop.yuusuke.a01_spica_android.R;

public class BlockSelectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_block_select);
    }

    public void blockClick(View v){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptions options = ActivityOptions.makeScaleUpAnimation(
                    v,
                    (int)v.getX(),
                    (int)v.getY(),
                    v.getWidth(),
                    v.getHeight());
            startActivity(new Intent(this,BlockDetailActivity.class), options.toBundle());
        }
    }
}

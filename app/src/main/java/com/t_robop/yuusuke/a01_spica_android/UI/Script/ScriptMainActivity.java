package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;
import com.t_robop.yuusuke.a01_spica_android.util.CanvasView;

public class ScriptMainActivity extends AppCompatActivity {

    private RecyclerView mScriptRecyclerView;
    private ScriptMainAdapter mScriptAdapter;
    private LinearLayoutManager mScriptLayoutManager;
    private CanvasView mCanvasView;
    private TextView textView;

    private boolean canvasViewScrolling = false;

    int overallXScroll = 0;
    int sizeX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_main);

        mScriptRecyclerView = findViewById(R.id.recycler_script);
        mCanvasView = findViewById(R.id.canvas_view);

        mScriptRecyclerView.setHasFixedSize(true);
        mScriptLayoutManager = new LinearLayoutManager(this);
        mScriptLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mScriptRecyclerView.setLayoutManager(mScriptLayoutManager);
        textView = findViewById(R.id.textView);

        mScriptAdapter = new ScriptMainAdapter(this);
        mScriptRecyclerView.setAdapter(mScriptAdapter);

        for(int i=0;i<10;i++) {
            ScriptModel scriptModel = new ScriptModel();
            BlockModel blockModel = new BlockModel();
            blockModel.setBlockId(0101+i);
            scriptModel.setBlock(blockModel);
            mScriptAdapter.add(scriptModel);

            mCanvasView.setCommandBlockNum(mScriptAdapter.getItemCount() + 2);
            mCanvasView.windowSizeChange(sizeX, (float) mScriptAdapter.getItemCount());
        }

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCanvasView.setCommandBlockNum(mScriptAdapter.getItemCount() + 2);
                mCanvasView.windowSizeChange(sizeX, mScriptRecyclerView.computeHorizontalScrollRange() + 150);
            }

    });

        mScriptAdapter.notifyDataSetChanged();

        final Handler handler = new Handler();
        final Runnable r = new Runnable() {
            int count = 0;
            @Override
            public void run() {

               // mScriptRecyclerView.scrollToPositionWithOffset();
                // mScriptLayoutManager.scrollToPositionWithOffset(0,100);
               // mScriptLayoutManager.getPo

               // mScriptRecyclerView.setScrollX((int)mCanvasView.getPosition());

//                mScriptLayoutManager.scrollVerticallyBy()
//                mScriptRecyclerView.setScrollY(-1000);
//                mScriptRecyclerView.setScrollX(-1000);
              //  mScriptLayoutManager.scrollToPositionWithOffset(4,100);
                handler.postDelayed(this, 100);

            }
        };
        handler.post(r);


        mScriptRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener(){
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if(!canvasViewScrolling) {
                    overallXScroll = overallXScroll + dx;
                    mCanvasView.setPositon(overallXScroll);
                    Log.d("overscroll","" + overallXScroll);
                    Log.d("windowSize","" + sizeX);
                    Log.d("sumNumber","" + (overallXScroll + sizeX));
                    Log.d("maxScroll","" + mScriptRecyclerView.computeHorizontalScrollRange());


                }else {

                }
                //mScriptLayoutManager.scrollToPositionWithOffset(0,-100);
            }
        });

        ScriptMainActivity scriptMainActivity = this;
        mCanvasView.setClass(scriptMainActivity);
    }

    public void setScroll(float pos){
        canvasViewScrolling = true;
        overallXScroll = (int)(pos * (mScriptRecyclerView.computeHorizontalScrollRange() + 150 - sizeX));
        mScriptLayoutManager.scrollToPositionWithOffset(0, -overallXScroll);
        canvasViewScrolling = false;
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        sizeX = mScriptRecyclerView.getWidth();
    }
}

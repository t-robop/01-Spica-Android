package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.BlockModel;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;
import com.t_robop.yuusuke.a01_spica_android.util.CanvasView;

public class ScriptMainActivity extends AppCompatActivity {

    private RecyclerView mScriptRecyclerView;
    private ScriptMainAdapter mScriptAdapter;
    private LinearLayoutManager mScriptLayoutManager;
    private CanvasView mCanvasView;

    int overallXScroll = 0;

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

        mScriptAdapter = new ScriptMainAdapter(this);
        mScriptRecyclerView.setAdapter(mScriptAdapter);

        for(int i=0;i<20;i++) {
            ScriptModel scriptModel = new ScriptModel();
            BlockModel blockModel = new BlockModel();
            blockModel.setBlockId(0101+i);
            scriptModel.setBlock(blockModel);
            mScriptAdapter.add(scriptModel);

            mCanvasView.setCommandBlockNum(mScriptAdapter.getItemCount() + 2);
            mCanvasView.windowSizeChange(7, (float) mScriptAdapter.getItemCount());
        }
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

                overallXScroll = overallXScroll + dx;
                mCanvasView.setPositon(overallXScroll);
            }
        });

        ScriptMainActivity scriptMainActivity = this;
        mCanvasView.setClass(scriptMainActivity);
    }

//    public void setScroll(float pos){
//        float itemPos =  (mScriptAdapter.getItemCount() * pos);
//        itemPos = itemPos;
//         mScriptLayoutManager.computeHorizontalScrollOffset();
//
//        mScriptLayoutManager.scrollToPositionWithOffset((int)itemPos,100);
//    }



    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
//        int num  = mScriptRecyclerView.computeVerticalScrollRange();
//        num = mScriptRecyclerView.computeHorizontalScrollRange();
//        mCanvasView.windowSizeChange(mScriptRecyclerView.getWidth(),mScriptRecyclerView.computeHorizontalScrollRange(), false);
//        temp = true;
    }
}

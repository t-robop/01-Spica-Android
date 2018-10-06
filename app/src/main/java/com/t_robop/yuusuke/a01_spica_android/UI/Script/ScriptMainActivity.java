package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.model.ScriptModel;

public class ScriptMainActivity extends AppCompatActivity {

    ScriptMainAdapter adapter;
    private LinearLayoutManager mLayoutManager;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script_main);

        adapter = new ScriptMainAdapter(this);
        adapter.add(new ScriptModel(12));
        adapter.add(new ScriptModel(3));

        recyclerView = findViewById(R.id.recycler_script);
        recyclerView.setAdapter(adapter);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // ここで横方向に設定
        recyclerView.setLayoutManager(mLayoutManager);
    }
}

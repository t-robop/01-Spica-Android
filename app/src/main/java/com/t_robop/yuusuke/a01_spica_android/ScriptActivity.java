package com.t_robop.yuusuke.a01_spica_android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.model.ItemDataModel;
import com.t_robop.yuusuke.a01_spica_android.model.MenuItemModel;
import com.t_robop.yuusuke.a01_spica_android.util.SimpleItemTouchHelperCallback;
import com.t_robop.yuusuke.a01_spica_android.util.UdpSend;
import com.t_robop.yuusuke.a01_spica_android.util.UtilBlock;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ScriptActivity extends AppCompatActivity implements RecyclerAdapter.OnRecyclerListener, AdapterView.OnItemClickListener, View.OnClickListener {

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        //RecyclerView処理
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ItemDataModel> ItemDataArray = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(ItemDataArray, this);
        recyclerView.setAdapter(recyclerAdapter);

        //ItemTouchHelper RecyclerView内のドラッグなどの検知
        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(recyclerAdapter, getApplicationContext());
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //画面左のListView内の初期化
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(this);
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "前進", "パワーと時間を設定して、ロボットを前に動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "後退", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "左回転", "パワーと時間を設定して、ロボットを左に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "右回転", "パワーと時間を設定して、ロボットを右に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, "ループ開始", "ループの始まり"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, "ループ終了", "ループの終わり"));

        ListView brockList = findViewById(R.id.brock_list);
        brockList.setAdapter(menuItemAdapter);
        brockList.setOnItemClickListener(this);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

    }

    @Override
    public void onRecyclerClicked(View view, int position) {

        //ループのとき
        if (recyclerAdapter.getItem(position).getBlockState() == ItemDataModel.BlockState.FOR_START) {
            EditLoopParamDialog editLoopParamDialog = new EditLoopParamDialog();
            Bundle data = new Bundle();
            ItemDataModel itemDataModel = new ItemDataModel(
                    recyclerAdapter.getItem(position).getOrderName(),
                    recyclerAdapter.getItem(position).getRightSpeed(),
                    recyclerAdapter.getItem(position).getLeftSpeed(),
                    recyclerAdapter.getItem(position).getTime(),
                    recyclerAdapter.getItem(position).getBlockState(),
                    recyclerAdapter.getItem(position).getLoopCount());

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editLoopParamDialog.setArguments(data);
            editLoopParamDialog.show(getFragmentManager(), null);

            //方向指定コマンドのとき
        } else {
            // ダイアログの表示
            EditParamDialog editParamDialog = new EditParamDialog();
            Bundle data = new Bundle();

            ItemDataModel itemDataModel = new ItemDataModel(
                    recyclerAdapter.getItem(position).getOrderName(),
                    recyclerAdapter.getItem(position).getRightSpeed(),
                    recyclerAdapter.getItem(position).getLeftSpeed(),
                    recyclerAdapter.getItem(position).getTime(),
                    recyclerAdapter.getItem(position).getBlockState(),
                    recyclerAdapter.getItem(position).getLoopCount());

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editParamDialog.setArguments(data);
            editParamDialog.show(getFragmentManager(), null);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        int DEFAULT_SPEED_R = 100;
        int DEFAULT_SPEED_L = 100;
        int DEFAULT_TIME = 2;

        switch (position) {
            case 0: //前進
                recyclerAdapter.addItem(new ItemDataModel("forward", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, ItemDataModel.BlockState.FORWARD, 0));
                break;
            case 1: //後退
                recyclerAdapter.addItem(new ItemDataModel("back", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, ItemDataModel.BlockState.BACK, 0));
                break;
            case 2: //左回転
                recyclerAdapter.addItem(new ItemDataModel("left", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, ItemDataModel.BlockState.LEFT, 0));
                break;
            case 3: //右回転
                recyclerAdapter.addItem(new ItemDataModel("right", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, ItemDataModel.BlockState.RIGHT, 0));
                break;
            case 4: //ループ開始
                recyclerAdapter.addItem(new ItemDataModel("loopStart", ItemDataModel.BlockState.FOR_START, 2));

                break;
            case 5: //ループ終了
                recyclerAdapter.addItem(new ItemDataModel("loopEnd", ItemDataModel.BlockState.FOR_END, 0));
                break;

        }
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (UtilBlock.compileSuccess(recyclerAdapter.getAllItem()) == UtilBlock.errorStatus.NO_BLOCK) {
            Toast.makeText(this, "Blockがありません", Toast.LENGTH_SHORT).show();
            return;
        }

        if (UtilBlock.compileSuccess(recyclerAdapter.getAllItem()) == UtilBlock.errorStatus.LOOP_COUNT) {
            Toast.makeText(this, "ループの数が間違ってます", Toast.LENGTH_SHORT).show();
            return;
        }
        UdpSend udp = new UdpSend();

        String ip = "";
        String sendData = UtilBlock.generateUdpStr(recyclerAdapter);
        udp.setIpAddress(ip);
        udp.setPort(10000);
        try {
            udp.setSendText(sendData);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        Log.d("udpText", sendData);
        udp.send();
    }

    //View更新
    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        recyclerAdapter.setItem(listPosition, dataModel);
        recyclerAdapter.notifyDataSetChanged();
    }

}

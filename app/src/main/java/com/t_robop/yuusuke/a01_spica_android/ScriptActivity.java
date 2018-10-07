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
import com.t_robop.yuusuke.a01_spica_android.util.UdpRecieve;
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
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_front, getString(R.string.common_forward), getString(R.string.forward_description)));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_back, getString(R.string.common_back), getString(R.string.back_description)));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_left, getString(R.string.common_left), getString(R.string.left_description)));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_right, getString(R.string.common_right), getString(R.string.right_description)));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, getString(R.string.common_loop_start), getString(R.string.loop_start_description)));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, getString(R.string.common_loop_end), getString(R.string.loop_end_description)));

        ListView brockList = findViewById(R.id.brock_list);
        brockList.setAdapter(menuItemAdapter);
        brockList.setOnItemClickListener(this);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(this);

        //UDPパケット待受
        new UdpRecieve(this).UdpRecieveStandby();
    }

    @Override
    public void onRecyclerClicked(View view, int position) {

        //ループのとき
        if (recyclerAdapter.getItem(position).getBlockState() == ItemDataModel.BlockState.FOR_START) {
            EditLoopParamDialog editLoopParamDialog = new EditLoopParamDialog();
            Bundle data = new Bundle();
            ItemDataModel itemDataModel = recyclerAdapter.getItem(position);

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editLoopParamDialog.setArguments(data);
            editLoopParamDialog.show(getFragmentManager(), null);

            //方向指定コマンドのとき
        } else {
            // ダイアログの表示
            EditParamDialog editParamDialog = new EditParamDialog();
            Bundle data = new Bundle();
            ItemDataModel itemDataModel = recyclerAdapter.getItem(position);

            data.putSerializable("itemData", itemDataModel);
            data.putInt("listItemPosition", position);
            editParamDialog.setArguments(data);
            editParamDialog.show(getFragmentManager(), null);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

        ItemDataModel.BlockState state = ItemDataModel.BlockState.intToEnum(position);
        recyclerAdapter.addItem(new ItemDataModel(state));
        recyclerView.setAdapter(recyclerAdapter);
    }

    @Override
    public void onClick(View view) {
        if (UtilBlock.compileSuccess(recyclerAdapter.getAllItem()) == UtilBlock.errorStatus.NO_BLOCK) {
            Toast.makeText(this, getString(R.string.error_no_brock), Toast.LENGTH_SHORT).show();
            return;
        }

        if (UtilBlock.compileSuccess(recyclerAdapter.getAllItem()) == UtilBlock.errorStatus.LOOP_COUNT) {
            Toast.makeText(this, getString(R.string.error_invalid_loop_count), Toast.LENGTH_SHORT).show();
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

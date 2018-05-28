package com.t_robop.yuusuke.a01_spica_android;

import android.content.Context;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class ScriptActivity extends AppCompatActivity implements RecyclerAdapter.OnRecyclerListener {

    //RecyclerView
    private RecyclerView recyclerView;
    private RecyclerAdapter recyclerAdapter;

    private ArrayList<ItemDataModel> fullGenerateDataArray = new ArrayList<>();

    // コマンドのデフォルトパラメータ
    private int DEFAULT_SPEED_R = 100;
    private int DEFAULT_SPEED_L = 100;
    private int DEFAULT_TIME = 2;
    private int DEFAULT_BLOCK_STATE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_script);

        //RecyclerView処理
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ArrayList<ItemDataModel> ItemDataArray = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(ItemDataArray, this);
        recyclerView.setAdapter(recyclerAdapter);


        //ItemTouchHelper RecyclerView内のドラッグなどの検知
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        recyclerAdapter.itemMoved(fromPos, toPos);
                        recyclerAdapter.notifyItemMoved(fromPos, toPos);
                        return true;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        recyclerAdapter.removeItem(fromPos);
                        recyclerAdapter.notifyItemRemoved(fromPos);
                        Log.d("","");
                    }

                    @Override
                    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                        super.onSelectedChanged(viewHolder, actionState);

                        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG ) {
                            ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
                        }
                    }
                });
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Drawer内の初期化
        MenuItemAdapter drawerMenuItemAdapter = new MenuItemAdapter(this);
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "前進", "パワーと時間を設定して、ロボットを前に動かします。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "後退", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "左回転", "パワーと時間を設定して、ロボットを左に回転させます。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "右回転", "パワーと時間を設定して、ロボットを右に回転させます。"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, "ループ開始", "ループの始まり"));
        drawerMenuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, "ループ終了", "ループの終わり"));

        ListView drawerMenuList = findViewById(R.id.drawer_list);
        drawerMenuList.setAdapter(drawerMenuItemAdapter);

        drawerMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // positionが1から始まるため
                int orderId = i + 1;
                switch (orderId) {
                    case 1: //前進
                    case 2: //後退
                    case 3: //左回転
                    case 4: //右回転
                        recyclerAdapter.addItem(new ItemDataModel(orderId, DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, 0));
                        break;
                    case 5: //ループ開始
                        recyclerAdapter.addItem(new ItemDataModel(orderId, DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, 1, 2));
                        break;
                    case 6: //ループ終了
                        recyclerAdapter.addItem(new ItemDataModel(orderId, DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, 2, 0));
                        break;

                }
                recyclerView.setAdapter(recyclerAdapter);

            }
        });

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO UDP通信

            }
        });

    }

    @Override
    public void onRecyclerClicked(View view, int position) {

        //ループのとき
        if (recyclerAdapter.getItem(position).getBlockState() == 1) {
            EditLoopParamDialog editLoopParamDialog = new EditLoopParamDialog();
            Bundle data = new Bundle();
            ItemDataModel itemDataModel = new ItemDataModel(
                    recyclerAdapter.getItem(position).getOrderId(),
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
                    recyclerAdapter.getItem(position).getOrderId(),
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

    //View更新
    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        recyclerAdapter.setItem(listPosition,dataModel);
        recyclerAdapter.notifyDataSetChanged();
    }

    // TODO なんかループの処理書く
    private void convertLoopCommand(int start,int end, int depth) {

        for (int i = start; i <= end; i++) {
            if (recyclerAdapter.getItem(i).getBlockState() == 1) {
                convertLoopCommand(i + 1, end, depth + 1);
                for (;i < end; i++){
                    if (recyclerAdapter.getItem(i).getBlockState() == 2) {
                        break;
                    }
                }

            }else if (recyclerAdapter.getItem(i).getBlockState() == 2) {
                if (depth != 0) {
                    ArrayList<ItemDataModel> tmpDataArray = new ArrayList<>();
                    ArrayList<ItemDataModel> tmpDataArray2 = new ArrayList<>();
                    for (int j = start; j < i; j++) {
                        tmpDataArray.add(recyclerAdapter.getItem(j));
                    }
                    for (int j = 0; j < recyclerAdapter.getItem(start - 1).getLoopCount(); j++) {
                        tmpDataArray2.addAll(tmpDataArray);
                    }
                    fullGenerateDataArray.addAll(tmpDataArray2);
                    return;
                }
            } else if (depth == 0) {
                fullGenerateDataArray.add(recyclerAdapter.getItem(i));
            }
        }
    }

    boolean loopErrorCheck() {
        int loopStart = 0,loopEnd = 0;
        for (int i = 0; i < recyclerAdapter.getItemCount(); i++) {
            if (recyclerAdapter.getItem(i).getBlockState() == 1) {
                loopStart++;
            } else if (recyclerAdapter.getItem(i).getBlockState() == 2) {
                loopEnd++;
                if (loopStart < loopEnd) {
                    Toast.makeText(this,"forブロックの始まりより前に終わりが来ています",Toast.LENGTH_SHORT).show();
                    return false;
                }

            }
        }
        if ((loopStart < loopEnd)) {
            Toast.makeText(this,"forブロックの終わりの数が多すぎます",Toast.LENGTH_SHORT).show();
            return false;
        } else if (loopStart > loopEnd){
            Toast.makeText(this,"forブロックの始まりの数が多すぎます",Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}

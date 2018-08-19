package com.t_robop.yuusuke.a01_spica_android.Script;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.Script.Adapter.RecyclerAdapter;
import com.t_robop.yuusuke.a01_spica_android.Script.Model.ItemDataModel;
import com.t_robop.yuusuke.a01_spica_android.UdpSend;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ScriptPresenter implements RecyclerAdapter.OnRecyclerListener,ScriptContract.Presenter {

    private ScriptContract.View mScriptView;

    private RecyclerAdapter recyclerAdapter;

    private UdpSend udp = new UdpSend();

    // コマンドのデフォルトパラメータ
    private int DEFAULT_SPEED_R = 100;
    private int DEFAULT_SPEED_L = 100;
    private int DEFAULT_TIME = 2;
    private int DEFAULT_BLOCK_STATE = 0;

    public ScriptPresenter(ScriptContract.View scriptView){
        //mvpのviewの初期化
        mScriptView=scriptView;
        mScriptView.setPresenter(this);

        ArrayList<ItemDataModel> itemDataArray = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(itemDataArray, this);
    }

    int[] loopStartEndPosition(ArrayList<ItemDataModel> dataArray) {
        int loopPos[] = new int[dataArray.size()];
        for (int i = 0; i < dataArray.size(); i++) {
            ItemDataModel model = dataArray.get(i);
            if (model.getBlockState() == 1) {
                loopPos[i] = 1;
            } else if (model.getBlockState() == 2) {
                loopPos[i] = 2;
            }
        }
        return loopPos;
    }

    boolean forCheck(ArrayList<ItemDataModel> dataArray){
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < dataArray.size(); i++) {
            s.append(dataArray.get(i).getBlockState());
        }
        if (s.toString().contains("1")) {
            return true;
        }
        return false;
    }

    private String generateUdpStr() {
        StringBuilder sendText = new StringBuilder();
        ArrayList<ItemDataModel> dataArray = recyclerAdapter.getAllItem();

        //構文チェック
        if(compileSuccess(dataArray)) {
            dataArray = evolutionItems(dataArray);
        }else{
            return "";
        }


        for (int i = 0; i < dataArray.size(); i++) {
            sendText.append("&");

            sendText.append(dataArray.get(i).getOrderName());
            sendText.append("&");
            sendText.append(dataArray.get(i).getLeftSpeed());
            sendText.append("&");
            sendText.append(dataArray.get(i).getRightSpeed());
            sendText.append("&");
            sendText.append(dataArray.get(i).getTime() * 1000);

            if (i != dataArray.size() - 1) {
                sendText.append("+");
            }
        }

        return sendText.toString();
    }

    /** こいつに送信前のリストデータを与えれば二重loop処理が動くはず **/
    //完全体に進化するメソッド(結果にCommitします)
    public ArrayList<ItemDataModel> evolutionItems(ArrayList<ItemDataModel> items){
        if(items.isEmpty()){
            return items;
        }
        return convertLoopItem(items,0,0);
    }

    //loop文があったら外してリスト化してくれるメソッド
    public ArrayList<ItemDataModel> convertLoopItem(ArrayList<ItemDataModel> items,int posLoopStart,int cntLoop){
        int posEnd=-1;
        int i;

        for(i=posLoopStart+1;i<items.size();i++){
            if(items.get(i).getBlockState()==1){
                items=convertLoopItem(items,i,items.get(i).getLoopCount());
            }
            if(items.size()>i&&items.get(i).getBlockState()==2){
                posEnd=i;
                break;
            }
        }
        if(items.get(posLoopStart).getBlockState()!=1){
            return items;
        }

        //loop前の処理を保持
        ArrayList<ItemDataModel> content=new ArrayList<>();
        for(i=0;i<posLoopStart;i++){
            content.add(items.get(i));
        }
        //連結
        for(int cnt=0;cnt<cntLoop;cnt++) {
            for (i = posLoopStart + 1; i < posEnd; i++) {
                content.add(items.get(i));
            }
        }
        //loop外の残りを連結
        for (i=posEnd+1;i<items.size();i++){
            content.add(items.get(i));
        }

        return content;
    }

    //compileの構文チェックメソッド
    public boolean compileSuccess(ArrayList<ItemDataModel> items){
        int cntLoopStart=0;
        int cntLoopEnd=0;

        for(ItemDataModel item :items){
            if(item.getBlockState()==1){
                cntLoopStart++;
            }
            else if(item.getBlockState()==2){
                cntLoopEnd++;
            }
        }

        if (cntLoopStart!=cntLoopEnd){
            mScriptView.showToast("ループの数が間違ってます",Toast.LENGTH_SHORT);
            return false;
        }

        return true;
    }

    @Override
    public ItemTouchHelper.SimpleCallback getRecyclerViewTouchCallBack() {
        return new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.RIGHT) {

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
            }

            @Override
            public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
                super.onSelectedChanged(viewHolder, actionState);

                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    mScriptView.vibrate(50);
                }
            }
        };
    }

    @Override
    public AdapterView.OnItemClickListener getBlockListClickCallBack() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // positionが1から始まるため
                int orderId = i + 1;
                switch (orderId) {
                    case 1: //前進
                        recyclerAdapter.addItem(new ItemDataModel("forward", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, 0));
                        break;
                    case 2: //後退
                        recyclerAdapter.addItem(new ItemDataModel("back", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, 0));
                        break;
                    case 3: //左回転
                        recyclerAdapter.addItem(new ItemDataModel("left", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, 0));
                        break;
                    case 4: //右回転
                        recyclerAdapter.addItem(new ItemDataModel("right", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, DEFAULT_BLOCK_STATE, 0));
                        break;
                    case 5: //ループ開始
                        //recyclerAdapter.addItem(new ItemDataModel("loopStart", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, 1, 2));

                        recyclerAdapter.addItem(new ItemDataModel("loopStart", 1, 2));

                        break;
                    case 6: //ループ終了
                        //recyclerAdapter.addItem(new ItemDataModel("loopEnd", DEFAULT_SPEED_R, DEFAULT_SPEED_L, DEFAULT_TIME, 2, 0));
                        recyclerAdapter.addItem(new ItemDataModel("loopEnd", 2, 0));
                        break;

                }
                mScriptView.setRcyclerAdapter(recyclerAdapter);
            }
        };
    }

    @Override
    public View.OnClickListener getStartBtnClickCallBack() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ip = "";
                String sendData = generateUdpStr();
                if(!sendData.isEmpty()) {
                    udp.setIpAddress(ip);
                    udp.setPort(10000);
                    try {
                        udp.setSendText(sendData);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    //Log.d("udpText", sendData);
                    udp.send();
                }
            }
        };
    }

    @Override
    public void updateItemParam(int listPosition, ItemDataModel dataModel) {
        recyclerAdapter.setItem(listPosition, dataModel);
        mScriptView.setRcyclerAdapter(recyclerAdapter);
    }

    @Override
    public void reloadItem() {
        mScriptView.setRcyclerAdapter(recyclerAdapter);
    }

    @Override
    public void start() {

    }

    @Override
    public void onRecyclerClicked(View view, int position) {

        ItemDataModel itemDataModel = new ItemDataModel(
                recyclerAdapter.getItem(position).getOrderName(),
                recyclerAdapter.getItem(position).getRightSpeed(),
                recyclerAdapter.getItem(position).getLeftSpeed(),
                recyclerAdapter.getItem(position).getTime(),
                recyclerAdapter.getItem(position).getBlockState(),
                recyclerAdapter.getItem(position).getLoopCount());

        //ループのとき
        if (recyclerAdapter.getItem(position).getBlockState() == 1) {
            mScriptView.showEditParamDialog(itemDataModel,position,true);
        }
        //方向指定コマンドのとき
        else {
            mScriptView.showEditParamDialog(itemDataModel,position,false);
        }
    }
}

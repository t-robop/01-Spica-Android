package com.t_robop.yuusuke.a01_spica_android.Script;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.Script.Adapter.RecyclerAdapter;
import com.t_robop.yuusuke.a01_spica_android.Script.Adapter.MenuItemAdapter;
import com.t_robop.yuusuke.a01_spica_android.Script.Model.ItemDataModel;
import com.t_robop.yuusuke.a01_spica_android.Script.Model.MenuItemModel;

import java.lang.reflect.InvocationTargetException;

public class ScriptFragment extends Fragment implements ScriptContract.View {

    private ScriptContract.Presenter mPresenter;
    private Context mContext;

    //RecyclerView
    private RecyclerView recyclerView;

    public ScriptFragment(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_script, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mContext=view.getContext();

        //RecyclerView処理
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        mPresenter.reloadItem();

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(view.getContext());
        recyclerView.setLayoutManager(layoutManager);

        //ItemTouchHelper RecyclerView内のドラッグなどの検知
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mPresenter.getRecyclerViewTouchCallBack());
        itemTouchHelper.attachToRecyclerView(recyclerView);

        //Drawer内の初期化
        MenuItemAdapter menuItemAdapter = new MenuItemAdapter(view.getContext());
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_front, "前進", "パワーと時間を設定して、ロボットを前に動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_back, "後退", "パワーと時間を設定して、ロボットを後ろに動かします。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_left, "左回転", "パワーと時間を設定して、ロボットを左に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.move_right, "右回転", "パワーと時間を設定して、ロボットを右に回転させます。"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_start, "ループ開始", "ループの始まり"));
        menuItemAdapter.add(new MenuItemModel(R.drawable.loop_end, "ループ終了", "ループの終わり"));

        ListView brockList = view.findViewById(R.id.brock_list);
        brockList.setAdapter(menuItemAdapter);

        brockList.setOnItemClickListener(mPresenter.getBlockListClickCallBack());

        Button startButton = view.findViewById(R.id.start_button);
        startButton.setOnClickListener(mPresenter.getStartBtnClickCallBack());
    }

    @Override
    public void setRcyclerAdapter(RecyclerAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showEditParamDialog(ItemDataModel data, int pos, boolean isLoop) {
        Class<? extends DialogFragment> c=null;
        DialogFragment dialog;

        if(isLoop) {
            try {
                c= (Class<? extends DialogFragment>) Class.forName("com.t_robop.yuusuke.a01_spica_android.Script.Dialog.EditLoopParamDialog");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            try {
                c= (Class<? extends DialogFragment>) Class.forName("com.t_robop.yuusuke.a01_spica_android.Script.Dialog.EditParamDialog");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        try {
            assert c != null;
            dialog= c.getDeclaredConstructor(ScriptContract.Presenter.class).newInstance(mPresenter);
            Bundle bundleData = new Bundle();
            bundleData.putSerializable("itemData", data);
            bundleData.putInt("listItemPosition", pos);
            dialog.setArguments(bundleData);
            //todo FragmentManagerに依存してる謎
            //dialog.show(getFragmentManager(), null);
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showToast(String message, int length) {
        Toast.makeText(mContext,message,length).show();
    }

    @Override
    public void vibrate(int seconds) {
        ((Vibrator) mContext.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(seconds);
    }

    @Override
    public void setPresenter(ScriptContract.Presenter presenter) {
        mPresenter=presenter;
    }
}

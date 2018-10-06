package com.t_robop.yuusuke.a01_spica_android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ScriptModel extends BaseObservable {
    //ブロックid
    private int id;

    public ScriptModel(){}
    public ScriptModel(int id){
        this.id = id;
    }

    // ゲッターに@Bindableをつける。変数につけてゲッター書かなくてもよい？
    @Bindable
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

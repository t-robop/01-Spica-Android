package com.t_robop.yuusuke.a01_spica_android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ScriptModel extends BaseObservable {
    //ブロック
    private BlockModel block;

    public ScriptModel() {
    }

    public ScriptModel(BlockModel block) {
        this.block = block;
    }

    @Bindable
    public BlockModel getBlock() {
        return this.block;
    }

    public void setBlock(BlockModel block) {
        this.block = block;
    }

}

package com.t_robop.yuusuke.a01_spica_android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ScriptModel extends BaseObservable {
    //if
    private int ifState;
    //ブロック
    private BlockModel block;
    //右パワー
    private int rightSpeed;
    //左パワー
    private int leftSpeed;
    //ブロック毎の値
    private int value;

    public ScriptModel() {
    }

    public ScriptModel(BlockModel block) {
        this.block = block;
    }

    @Bindable
    public int getIfState() {
        return ifState;
    }
    public void setIfState(int ifState) {
        this.ifState = ifState;
    }

    @Bindable
    public BlockModel getBlock() {
        return this.block;
    }

    public void setBlock(BlockModel block) {
        this.block = block;
    }

    @Bindable
    public int getRightSpeed() {
        return this.rightSpeed;
    }

    public void setRightSpeed(int rightSpeed) {
        this.rightSpeed = rightSpeed;
    }

    @Bindable
    public int getLeftSpeed() {
        return this.leftSpeed;
    }

    public void setLeftSpeed(int leftSpeed) {
        this.leftSpeed = leftSpeed;
    }

    @Bindable
    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}

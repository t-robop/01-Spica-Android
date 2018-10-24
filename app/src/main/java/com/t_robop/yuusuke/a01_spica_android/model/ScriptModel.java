package com.t_robop.yuusuke.a01_spica_android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ScriptModel extends BaseObservable {


    private String id;
    private int pos;
    //if
    private int ifState = 0;
    //ブロック
    private SpicaBlock block;
    //右パワー
    private int rightSpeed = 100;
    //左パワー
    private int leftSpeed = 100;
    //ブロック毎の値
    private int value = 0;


    public ScriptModel() {
    }

    public ScriptModel(SpicaBlock block) {
        this.block = block;
    }

    public ScriptModel(int pos, int ifState) {
        this.pos = pos;
        this.ifState = ifState;
    }

    @Bindable
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Bindable
    public int getIfState() {
        return ifState;
    }

    public void setIfState(int ifState) {
        this.ifState = ifState;
    }

    @Bindable
    public SpicaBlock getBlock() {
        return this.block;
    }

    public void setBlock(SpicaBlock block) {
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

    public enum SpicaBlock {
        FRONT(1),
        BACK(2),
        LEFT(3),
        RIGHT(4),
        IF_START(5),
        IF_END(6),
        FOR_START(7),
        FOR_END(8),
        BREAK(9),
        START(10),
        END(11);

        private final int id;
        private SpicaBlock(final int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }


    }
}

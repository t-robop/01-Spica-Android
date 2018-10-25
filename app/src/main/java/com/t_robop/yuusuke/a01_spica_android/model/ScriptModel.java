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
    //右パワー  //FIXME 百分率で渡すのか、直接値を渡すか
    private int rightSlowSpeed = 50;
    private int rightStandardSpeed = 100;
    private int rightFastSpeed = 150;
    //左パワー  //FIXME 百分率で渡すのか、直接値を渡すか
    private int leftSlowSpeed = 50;
    private int leftStandardSpeed = 100;
    private int leftFastSpeed = 150;
    //シークバーの値 3段階(0~2)
    private int seekValue = 0;
    //ブロック毎の値 (基本ブロックなら実行時間 ifブロックならセンサーを判断する距離 forブロックならループ回数)
    private float value = 0;  //TODO valueにrename

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

    public int getRightSpeed(int seekValue) {
        switch (seekValue){
            case 0:  //シークバー 小
                return rightSlowSpeed;
            case 1:  //シークバー 中
                return rightStandardSpeed;
            case 2:  //シークバー 大
                return rightFastSpeed;
        }

        return rightStandardSpeed;
    }

    public void setRightSlowSpeed(int rightSlowSpeed) {
        this.rightSlowSpeed = rightSlowSpeed;
    }

    public void setRightStandardSpeed(int rightStandardSpeed) {
        this.rightStandardSpeed = rightStandardSpeed;
    }

    public void setRightFastSpeed(int rightFastSpeed) {
        this.rightFastSpeed = rightFastSpeed;
    }

    public int getLeftSpeed(int seekValue) {
        switch (seekValue){
            case 0:  //シークバー 小
                return leftSlowSpeed;
            case 1:  //シークバー 中
                return leftStandardSpeed;
            case 2:  //シークバー 大
                return leftFastSpeed;
        }

        return leftStandardSpeed;
    }

    public void setLeftSlowSpeed(int leftSlowSpeed) {
        this.leftSlowSpeed = leftSlowSpeed;
    }

    public void setLeftStandardSpeed(int leftStandardSpeed) {
        this.leftStandardSpeed = leftStandardSpeed;
    }

    public void setLeftFastSpeed(int leftFastSpeed) {
        this.leftFastSpeed = leftFastSpeed;
    }

    @Bindable
    public int getSeekValue() {
        return this.seekValue;
    }

    public void setSeekValue(int value) {
        this.seekValue = value;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
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
        SpicaBlock(final int id) {
            this.id = id;
        }

        public int getId() {
            return this.id;
        }
    }
}

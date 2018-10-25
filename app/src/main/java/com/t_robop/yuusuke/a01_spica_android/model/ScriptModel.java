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
    private int speed = 0;
    //ブロック毎の値
    private float value = 1;
    private boolean isInLoop=false;

    public ScriptModel() {
    }

    public ScriptModel(SpicaBlock block) {
        this.block = block;
    }

    public ScriptModel(int pos, int ifState,boolean isInLoop) {
        this.pos = pos;
        this.ifState = ifState;
        this.isInLoop=isInLoop;
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
    public int getSpeed() {
        return this.speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Bindable
    public int getSeekValue() {  //シークバーの3段階(0~2)の取得
        return this.speed;
    }

    public void setSeekValue(int speed) {  //シークバーの3段階(0~2)のセット
        this.speed = speed;
    }

    @Bindable
    public int getIfOperator() {  //ifブロックの比較演算子(1: センサー値より大きい, 2: センサー値より小さい)の取得
        return this.speed;
    }

    public void setIfOperator(int speed) {
        this.speed = speed;
    }

    public Float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Bindable
    public boolean isInLoop(){
        return this.isInLoop;
    }
    public void setInLoop(boolean isInLoop){
        this.isInLoop=isInLoop;
    }

    public int getIfUpperNum() {
        return 1;  //センサー値より大きい を表す
    }

    public int getIfLowerNum() {
        return 2;  //センサー値より小さい を表す
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

package com.t_robop.yuusuke.a01_spica_android.model;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class ScriptModel extends BaseObservable {

    private String id="";
    private int pos=0;
    //if
    private int ifState = 0;
    //ブロック
    private SpicaBlock block=SpicaBlock.FRONT;
    //スピードの三段階値
    private int speed = 1;
    //ブロック毎の値
    private float value = 0;
    private boolean isInLoop = false;

    public ScriptModel() {
    }

    public ScriptModel(SpicaBlock block) {
        this.block = block;
    }

    public ScriptModel(int pos, int ifState, boolean isInLoop) {
        this.pos = pos;
        this.ifState = ifState;
        this.isInLoop = isInLoop;
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
    public int getIfOperator() {  //ifブロックの比較演算子(1: センサー値より大きい, 2: センサー値より小さい)の取得
        return this.speed;
    }

    public void setIfOperator(int speed) {
        this.speed = speed;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @Bindable
    public boolean isInLoop() {
        return this.isInLoop;
    }

    public void setInLoop(boolean isInLoop) {
        this.isInLoop = isInLoop;
    }

    //FIXME 定数なのでenumを使う
    public int getSensorAboveNum() {
        return 1;  //センサー値より大きい を表す
    }

    public int getSensorBelowNum() {
        return 2;  //センサー値より小さい を表す
    }

    public int getLowSpeedValue() {
        return 1;
    }

    public int getMiddleSpeedValue() {
        return 2;
    }

    public int getHighSpeedValue() {
        return 3;
    }

    public enum SpicaBlock {
        FRONT(1),
        BACK(2),
        LEFT(3),
        RIGHT(4),
        FOR_START(5),
        FOR_END(6),
        IF_START(7),
        IF_END(8),
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

        public static SpicaBlock getScriptBlock(final int id) {
            SpicaBlock[] blocks = SpicaBlock.values();
            for (SpicaBlock block : blocks) {
                if (block.getId() == id) {
                    return block;
                }
            }
            return null;
        }
    }
}

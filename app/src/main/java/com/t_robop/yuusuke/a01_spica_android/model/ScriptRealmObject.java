package com.t_robop.yuusuke.a01_spica_android.model;

import io.realm.RealmObject;

public class ScriptRealmObject extends RealmObject {

    public void from(ScriptModel script) {
        this.id = String.valueOf(script.getBlock().getId());
        this.pos = script.getPos();
        this.ifState = script.getIfState();
        this.value = script.getValue();
        this.isInLoop = script.isInLoop();
    }

    private String id;
    private int pos;
    //if
    private int ifState = 0;
    //スピードの三段階値
    private int speed = 1;
    //ブロック毎の値
    private float value = 0;
    private boolean isInLoop = false;

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

    public int getIfState() {
        return ifState;
    }

    public void setIfState(int ifState) {
        this.ifState = ifState;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public boolean isInLoop() {
        return isInLoop;
    }

    public void setInLoop(boolean inLoop) {
        isInLoop = inLoop;
    }
}

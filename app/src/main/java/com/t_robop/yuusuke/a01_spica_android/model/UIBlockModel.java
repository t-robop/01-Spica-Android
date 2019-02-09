package com.t_robop.yuusuke.a01_spica_android.model;

import com.t_robop.yuusuke.a01_spica_android.Config;

public class UIBlockModel extends BlockModel {
    private int pos;
    private boolean isInLoop;

    public UIBlockModel() {
    }

    public UIBlockModel(int pos, int ifState, boolean isInLoop) {
        this.pos = pos;
        super.setIfState(ifState);
        this.isInLoop = isInLoop;
    }

    public int getPos() {
        return pos;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    public boolean isInLoop() {
        return isInLoop;
    }

    public void setInLoop(boolean inLoop) {
        isInLoop = inLoop;
    }

    // 走行秒数
    public float getTime() {
        return getValue();
    }

    public void setTime(float time) {
        setValue(time);
    }

    // ループ回数
    public int getLoopNum() {
        return (int) getValue();
    }

    public void setLoopNum(int num) {
        setValue(num);
    }

    // しきい値
    public float getThreshold() {
        return getValue();
    }

    public void setThreshold(float value) {
        setValue(value);
    }

    // power
    public int getPower() {
        return getOption();
    }

    // おそい
    public void setLowPower() {
        setOption(Config.LOW_POWER);
    }

    // ふつう
    public void setMiddlePower() {
        setOption(Config.MIDDLE_POWER);
    }

    // はやい
    public void setHighPower() {
        setOption(Config.HIGH_POWER);
    }

    // センサー値
    public int getIfOperator() {
        return getOption();
    }

    // センサー値より大きい
    public void setSensorAbove() {
        setOption(Config.SENSOR_ABOVE);
    }

    // センサー値より小さい
    public void setSensorBelow() {
        setOption(Config.SENSOR_BELOW);
    }

    @Override
    public void setValue(float value) {
        super.setValue(value);
    }

    @Override
    public int getOption() {
        return super.getOption();
    }

    @Override
    public void setOption(int option) {
        super.setOption(option);
    }
}

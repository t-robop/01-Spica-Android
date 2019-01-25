package com.t_robop.yuusuke.a01_spica_android.model;

import com.t_robop.yuusuke.a01_spica_android.Config;

public class UIBlockModel extends BlockModel {
    int pos;
    boolean isInLoop;

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
    public void setTime(float time) {
        setValue(time);
    }

    // ループ回数
    public void setLoopNum(int num) {
        setValue(num);
    }

    // しきい値
    public void setThreshold(float value) {
        setValue(value);
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
    public void setOption(int option) {
        super.setOption(option);
    }
}

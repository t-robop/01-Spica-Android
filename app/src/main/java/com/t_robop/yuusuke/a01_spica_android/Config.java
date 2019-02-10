package com.t_robop.yuusuke.a01_spica_android;

public final class Config {
    //FIXME 実際の速度値(0~255)にする
    public static final int LOW_POWER = 1;
    public static final int MIDDLE_POWER = 2;
    public static final int HIGH_POWER = 3;

    public static final int SENSOR_ABOVE = 1; //センサー計測値より大きい場合
    public static final int SENSOR_BELOW = 2; //センサー計測値より小さい場合

    public static final int OUT_OF_IF_LANE = 0; //ブロックが通常レーンにある
    public static final int IN_TRUE_LANE = 1; //ブロックがifブロック内のtrueレーンにある場合
    public static final int IN_FALSE_LANE = 2;  //ブロックがifブロック内のfalseレーンにある場合
}

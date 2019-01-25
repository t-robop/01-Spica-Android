package com.t_robop.yuusuke.a01_spica_android.model;

public class BlockModel {
    // ブロック毎の固有ID(ここでブロックを判別する)
    private String id;
    // ブロック毎の値(秒数・for回数・ifしきい値)
    private float value;
    // 必要なオプション値がある場合はここで(power・ifOpeなど)
    private int option;
    // ブロックがifに関係するどの場所にいるか
    // 0 : 通常
    // 1 : if内のTrue
    // 2 : if内のFalse
    // FIXME 名前変えたい
    private int ifState;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public int getOption() {
        return option;
    }

    public void setOption(int option) {
        this.option = option;
    }

    public int getIfState() {
        return ifState;
    }

    public void setIfState(int ifState) {
        this.ifState = ifState;
    }
}

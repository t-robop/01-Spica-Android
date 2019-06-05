package com.t_robop.yuusuke.a01_spica_android.model;

import io.realm.RealmObject;

public class ScriptShell extends RealmObject {
    private int allBookNum = 0; // 削除したもの含めて今まで作成した全ファイル数

    public int getAllBookNum() {
        return allBookNum;
    }

    public void setAllBookNum(int allBookNum) {
        this.allBookNum = allBookNum;
    }
}

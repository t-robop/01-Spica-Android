package com.t_robop.yuusuke.a01_spica_android.repository;

import android.content.Context;

import com.t_robop.yuusuke.a01_spica_android.manager.FirebaseAnalyticsManager;

public class LogRepository {
    FirebaseAnalyticsManager fbAManager;

    public LogRepository(Context context) {
        fbAManager = new FirebaseAnalyticsManager(context);
    }

    public void onClickRun(int blockNum, String state) {
        fbAManager.sendClickRun(blockNum, state);
    }

    public void onClickRecovery() {
        fbAManager.sendClickRecovery();
    }

    public void onToSettingView() {
        fbAManager.sendToSettingView();
    }
}

package com.t_robop.yuusuke.a01_spica_android.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.firebase.analytics.FirebaseAnalytics;

public class FirebaseAnalyticsManager {
    private FirebaseAnalytics mFirebaseAnalytics;
    private SharedPreferences pref;

    public FirebaseAnalyticsManager(Context context) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);
        pref = context.getSharedPreferences("udp_config", Context.MODE_PRIVATE);
    }

    /**
     * Runボタンが押された
     */
    public void sendClickRun(int blockNum, String state) {
        Bundle bundle = new Bundle();
        bundle.putInt("block_num", blockNum);
        bundle.putString("run_state", state);
        sendLog("click_run", bundle);
    }

    /**
     * 設定画面に行こうとした
     */
    public void sendToSettingView() {
        Bundle bundle = new Bundle();
        sendLog("to_setting_view", bundle);
    }

    /**
     * 復元ボタンが押された
     */
    public void sendClickRecovery() {
        Bundle bundle = new Bundle();
        sendLog("click_recovery", bundle);
    }

    private void sendLog(String event, Bundle bundle) {
        // ipアドレスだけは毎回付与する
        bundle.putString("ip", getIp());
        mFirebaseAnalytics.logEvent(event, bundle);
    }

    private String getIp() {
        return pref.getString("ip", "");
    }
}

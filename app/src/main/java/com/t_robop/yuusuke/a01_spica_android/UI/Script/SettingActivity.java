package com.t_robop.yuusuke.a01_spica_android.UI.Script;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.t_robop.yuusuke.a01_spica_android.R;
import com.t_robop.yuusuke.a01_spica_android.databinding.ActivitySettingBinding;

public class SettingActivity extends AppCompatActivity {

    private ActivitySettingBinding binding;
    private SharedPreferences preferences;

    private final String preferenceIpKey = "ip";
    private final String preferenceCheckLoopKey = "loopState";
    private final String preferenceCheckIfKey = "ifState";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setting);

        final String preferenceKey = "udp_config";
        preferences = getSharedPreferences(preferenceKey, Context.MODE_PRIVATE);

        setSupportActionBar(binding.settingToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
        }

        String ip = preferences.getString(preferenceIpKey, "192.168.1.101");
        binding.settingIpTextEdit.setText(ip);

        binding.checkboxLoop.setChecked(preferences.getBoolean(preferenceCheckLoopKey, true));
        binding.checkboxIf.setChecked(preferences.getBoolean(preferenceCheckIfKey, false));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                saveSettings();
                finish();
                return true;

            case R.id.menu_setting_detail:
                Intent intent = new Intent(getApplicationContext(), SettingDetailActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveSettings();
    }

    public void onClickReadQr(View view) {
        new IntentIntegrator(SettingActivity.this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            String resultData = result.getContents();
            String[] parsedData = resultData.split(";", 0);
            String ip = parsedData[0].split(":")[1];
            binding.settingIpTextEdit.setText(ip);
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(preferenceIpKey, binding.settingIpTextEdit.getText().toString());
        editor.putBoolean(preferenceCheckLoopKey, binding.checkboxLoop.isChecked());
        editor.putBoolean(preferenceCheckIfKey, binding.checkboxIf.isChecked());
        editor.apply();
    }
}
